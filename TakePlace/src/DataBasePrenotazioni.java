import java.sql.*;
import java.util.*;

public class DataBasePrenotazioni {
    private Connection connessioneDB;
    private PreparedStatement selezionaAuleDisponibili; 
    private PreparedStatement selezionaPostiAula;
    private PreparedStatement selezionaPostiAulaDefault;
    private PreparedStatement selezionaDatiAccesso;
    private PreparedStatement selezionaDatiPrenotazione;
    private PreparedStatement inserisciUtente;
    private PreparedStatement inserisciPrenotazione;
    private PreparedStatement cancellaPrenotazione;
    private PreparedStatement registraAccesso;
    private PreparedStatement selezionaUtentiARischio;
    
    public DataBasePrenotazioni(ParametriConfigurazione parametriConf){
        try {
            System.out.println("Inizializzo il gestore del DB..");
            connessioneDB = DriverManager.getConnection("jdbc:mysql://"+parametriConf.IPDB+":"+parametriConf.portaDB+"/takeplace", parametriConf.usernameDB, parametriConf.passwordDB);
            selezionaAuleDisponibili = connessioneDB.prepareStatement("SELECT AULA, COUNT(*) AS NUMEROPOSTIDISPONIBILI\n" +
                    "FROM ( SELECT P.AULA, P.ID\n" +
                    "    FROM POSIZIONE P\n" +
                    "    WHERE P.DISPONIBILITà = 1 AND NOT EXISTS (\n" +
                    "        SELECT *\n" +
                    "        FROM PRENOTAZIONE PR\n" +
                    "        WHERE PR.AULA = P.AULA AND PR.POSIZIONE = P.ID AND PR.DATA = ?\n" +
                    "        AND ( (? BETWEEN PR.ORAINIZIO AND PR.ORAFINE OR ? BETWEEN PR.ORAINIZIO AND PR.ORAFINE) OR ( PR.ORAINIZIO BETWEEN ? AND ? OR PR.ORAFINE BETWEEN ? AND ? ))\n" +
                    "    )\n" +
                    ") AS D GROUP BY AULA;"); //(00)
            selezionaPostiAula = connessioneDB.prepareStatement("SELECT ID, AULA,\n" +
                    "CASE WHEN OCCUPATO > 0 THEN 'OCCUPATO' WHEN PRENOTATO > 0 THEN 'PRENOTATO' WHEN DISPONIBILE > 0 THEN 'DISPONIBILE' ELSE 'NON DISPONIBILE' END AS STATO\n" +
                    "FROM ("
                    + "SELECT P.ID, P.AULA, P.Disponibilità AS DISPONIBILE, (\n" +
                    "    SELECT COUNT(*)\n" +
                    "    FROM PRENOTAZIONE PR\n" +
                    "    WHERE PR.POSIZIONE = P.ID\n" +
                    "          AND PR.AULA = P.AULA AND PR.DATA = ?\n" +
                    "          AND ( (? BETWEEN PR.ORAINIZIO AND PR.ORAFINE OR ? BETWEEN PR.ORAINIZIO AND PR.ORAFINE) OR ( PR.ORAINIZIO BETWEEN ? AND ? OR PR.ORAFINE BETWEEN ? AND ? ))\n" +                    "          AND PR.ORAACCESSO IS NULL\n" +
                    "    ) AS PRENOTATO,\n" +
                    "    ( SELECT COUNT(*)\n" +
                    "      FROM PRENOTAZIONE PR\n" +
                    "      WHERE PR.POSIZIONE = P.ID AND PR.AULA = P.AULA AND PR.DATA = ?\n" +
                    "           AND ( (? BETWEEN PR.ORAINIZIO AND PR.ORAFINE OR ? BETWEEN PR.ORAINIZIO AND PR.ORAFINE) OR ( PR.ORAINIZIO BETWEEN ? AND ? OR PR.ORAFINE BETWEEN ? AND ? ))\n" +                   "            AND PR.ORAACCESSO IS NOT NULL\n" +
                    "    ) AS OCCUPATO\n" +
                    "FROM POSIZIONE P WHERE P.AULA = ?) AS D;\n"); //(01)
            inserisciUtente = connessioneDB.prepareStatement("INSERT INTO UTENTE(MATRICOLA)\n" +
                    "SELECT DISTINCT ?\n" +
                    "FROM UTENTE\n" +
                    "WHERE NOT EXISTS(\n" +
                    "    SELECT DISTINCT MATRICOLA FROM UTENTE WHERE MATRICOLA=?);\n"); //(02)
            inserisciPrenotazione = connessioneDB.prepareStatement("INSERT INTO PRENOTAZIONE(UTENTE,"+
                    " POSIZIONE, AULA, DATA, ORAINIZIO, ORAFINE, ORAACCESSO, DATAPRENOTAZIONE, ORAPRENOTAZIONE)\n" +
                    " VALUES (?,?,?,?,?,?,NULL,CURRENT_DATE,CURRENT_TIME);");
            cancellaPrenotazione = connessioneDB.prepareStatement("DELETE FROM PRENOTAZIONE\n" +
                    "WHERE UTENTE = ? AND POSIZIONE = ? AND AULA = ? AND DATA = ? AND ORAINIZIO = ? AND ORAFINE = ?;");
            registraAccesso = connessioneDB.prepareStatement("UPDATE PRENOTAZIONE\n" +
                    "SET ORAACCESSO = CURRENT_TIME\n" +
                    "WHERE UTENTE = ? AND DATA = CURRENT_DATE AND CURRENT_TIME BETWEEN ORAINIZIO AND ORAFINE;");
            selezionaPostiAulaDefault = connessioneDB.prepareStatement("SELECT ID, AULA, 'NON DISPONIBILE' AS STATO"
                    + " FROM POSIZIONE WHERE AULA = 'DEFAULT'");
            selezionaDatiAccesso = connessioneDB.prepareStatement("SELECT AULA, ORAINIZIO, ORAFINE\n" +
                        "FROM PRENOTAZIONE\n" +
                        "WHERE DATA = CURRENT_DATE AND UTENTE = ? AND CURRENT_TIME BETWEEN ORAINIZIO AND ORAFINE;"); //(03)
            selezionaDatiPrenotazione = connessioneDB.prepareStatement("SELECT AULA, POSIZIONE\n" +
                        "FROM PRENOTAZIONE\n" +
                        "WHERE UTENTE = ?\n" +
                        "     AND ( (? BETWEEN ORAINIZIO AND ORAFINE OR ? BETWEEN ORAINIZIO AND ORAFINE) OR ( ORAINIZIO BETWEEN ? AND ? OR ORAFINE BETWEEN ? AND ? ))\n" +
                        "     AND DATA = ?;"); //(04)
            selezionaUtentiARischio = connessioneDB.prepareStatement("SELECT P.UTENTE AS MATRICOLA, P.DATA, P.POSIZIONE, P.AULA\n" +
                        "FROM PRENOTAZIONE P,\n" +
                        "    (\n" +
                        "    SELECT DATA, ORAINIZIO, ORAFINE, AULA, POSIZIONE\n" +
                        "    FROM PRENOTAZIONE \n" +
                        "    WHERE DATA >= ( CURRENT_DATE - ?) AND UTENTE = ? AND ORAACCESSO IS NOT NULL\n" +
                        "        ) AS D\n" +
                        "WHERE UTENTE != ? AND P.AULA = D.AULA AND P.DATA = D.DATA\n" +
                        "    AND ((D.ORAINIZIO BETWEEN P.ORAINIZIO AND P.ORAFINE OR D.ORAFINE BETWEEN P.ORAINIZIO AND P.ORAFINE) OR ( P.ORAINIZIO BETWEEN D.ORAINIZIO AND D.ORAFINE OR P.ORAFINE BETWEEN D.ORAINIZIO AND D.ORAFINE))\n" +
                        "    AND TIMEDIFF(IF(P.ORAFINE>D.ORAFINE, D.ORAFINE, P.ORAFINE),IF(P.ORAINIZIO>D.ORAINIZIO, P.ORAINIZIO, D.ORAINIZIO)) >= IF(?<10,CONCAT(\"0\", ?, \":59:59\"), CONCAT(?,\":59:59\"))\n" +
                        "    AND P.ORAACCESSO IS NOT NULL\n" +
                        " AND (((FLOOR((P.POSIZIONE-1) / 4) = FLOOR((D.POSIZIONE-1) / 4)) AND (ABS((D.POSIZIONE-1) - (P.POSIZIONE-1)) <= ?)) \n" +
                        " OR ((ABS(FLOOR((P.POSIZIONE-1) / 4) - FLOOR((D.POSIZIONE-1) / 4)) <= 1) AND (ABS(((P.POSIZIONE-1) % 4) - ((D.POSIZIONE-1) % 4)) <= ?))\n" +
                        " OR ((FLOOR((P.POSIZIONE-1)/16) = FLOOR((D.POSIZIONE-1)/16)) AND (((P.POSIZIONE-1) % 4) = ((D.POSIZIONE-1) % 4))));");
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
    }

    public List<Aula> ottieniAuleDisponibili(String oraInizio, String oraFine, String data){
        List<Aula> lista = new ArrayList<>();
        System.out.println("Cerco le aule disponibili nel DB..");
        try {        
            selezionaAuleDisponibili.setString(1, data);
            selezionaAuleDisponibili.setString(2, oraInizio);
            selezionaAuleDisponibili.setString(3, oraFine);
            selezionaAuleDisponibili.setString(4, oraInizio);
            selezionaAuleDisponibili.setString(5, oraFine);
            selezionaAuleDisponibili.setString(6, oraInizio);
            selezionaAuleDisponibili.setString(7, oraFine);
            ResultSet rs = selezionaAuleDisponibili.executeQuery();
            while(rs.next()){
                lista.add(new Aula(rs.getString("AULA"), rs.getInt("NUMEROPOSTIDISPONIBILI")));
            }
        } catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return lista;
    }
    
    public List<Posto> ottieniPostiAulaDefault(){
        List<Posto> lista = new ArrayList<>();
        try {        
            ResultSet rs = selezionaPostiAulaDefault.executeQuery();
        while(rs.next())
            lista.add(new Posto(rs.getInt("ID"), rs.getString("AULA"), rs.getString("STATO")));
        } catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return lista;
    }
    
    public String ottieniDatiPrenotazione(String oraInizio, String oraFine, String data, int matricola){
        try {        
            System.out.println("Ottengo le informazioni sulla prenotazione dal database..");
            selezionaDatiPrenotazione.setInt(1, matricola);
            selezionaDatiPrenotazione.setString(2, oraInizio);
            selezionaDatiPrenotazione.setString(3, oraFine);
            selezionaDatiPrenotazione.setString(4, oraInizio);
            selezionaDatiPrenotazione.setString(5, oraFine);
            selezionaDatiPrenotazione.setString(6, oraInizio);
            selezionaDatiPrenotazione.setString(7, oraFine);
            selezionaDatiPrenotazione.setString(8, data);
            ResultSet rs = selezionaDatiPrenotazione.executeQuery();
            if(rs.next())
                return rs.getString("AULA") + " " + rs.getInt("POSIZIONE");
            } catch(SQLException e){
                System.err.println(e.getMessage());
            }
        return null;
    }
    
     public String ottieniDatiAccesso(int matricola){
        try {        
            selezionaDatiAccesso.setInt(1, matricola);
            ResultSet rs = selezionaDatiAccesso.executeQuery();
        while(rs.next())
            return rs.getString("AULA") + " " + rs.getString("ORAINIZIO") + " " + rs.getString("ORAFINE");
        } catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return null;
    }
    
    public List<Posto> ottieniPostiAula(String aula, String oraInizio, String oraFine, String data){
        List<Posto> lista = new ArrayList<>();
        try {        
            selezionaPostiAula.setString(1, data);
            selezionaPostiAula.setString(2, oraInizio);
            selezionaPostiAula.setString(3, oraFine);
            selezionaPostiAula.setString(4, oraInizio);
            selezionaPostiAula.setString(5, oraFine);
            selezionaPostiAula.setString(6, oraInizio);
            selezionaPostiAula.setString(7, oraFine);
            selezionaPostiAula.setString(8, data);
            selezionaPostiAula.setString(9, oraInizio);
            selezionaPostiAula.setString(10, oraFine);
            selezionaPostiAula.setString(11, oraInizio);
            selezionaPostiAula.setString(12, oraFine);
            selezionaPostiAula.setString(13, oraInizio);
            selezionaPostiAula.setString(14, oraFine);
            selezionaPostiAula.setString(15, aula);
            ResultSet rs = selezionaPostiAula.executeQuery();
            while(rs.next())
                lista.add(new Posto(rs.getInt("ID"), rs.getString("AULA"), rs.getString("STATO")));
        } catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return lista;
    }

    public boolean inserisciPrenotazione(String oraInizio, String oraFine, String aula, String data, int posto, int matricola){
        int affected;
        try {
            inserisciUtente.setInt(1, matricola);
            inserisciUtente.setInt(2, matricola);
            affected = inserisciUtente.executeUpdate();
            if(affected == 1)
                   System.out.println("Utente inserito nel database!");
            inserisciPrenotazione.setInt(1, matricola);
            inserisciPrenotazione.setInt(2, posto);
            inserisciPrenotazione.setString(3, aula);
            inserisciPrenotazione.setString(4, data);
            inserisciPrenotazione.setString(5, oraInizio);
            inserisciPrenotazione.setString(6, oraFine);
            affected = inserisciPrenotazione.executeUpdate();
            return (affected == 1);
        } catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return false;
    }
  
    public boolean cancellaPrenotazione(String oraInizio, String oraFine, String aula, String data, int posto, int matricola){
        int affected;
        try {
            cancellaPrenotazione.setInt(1, matricola);
            cancellaPrenotazione.setInt(2, posto);
            cancellaPrenotazione.setString(3, aula);
            cancellaPrenotazione.setString(4, data);
            cancellaPrenotazione.setString(5, oraInizio);
            cancellaPrenotazione.setString(6, oraFine);
            affected = cancellaPrenotazione.executeUpdate();
            return (affected == 1);
        } catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return false;
    }
    
    public boolean registraAccesso(int matricola){
        int affected;
        try {
            registraAccesso.setInt(1, matricola);
            affected = registraAccesso.executeUpdate();
            return (affected == 1);
        } catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return false;
    }
    
    public List<StudenteARischio> ottieniUtentiARischio(int matricola, int numeroGiorni, int numeroOreMin, int distanzaMax){
        List<StudenteARischio> lista = new ArrayList<>();
        System.out.println("Cerco gli utenti a rischio...");
        try {        
            selezionaUtentiARischio.setInt(1, numeroGiorni);
            selezionaUtentiARischio.setInt(2, matricola);
            selezionaUtentiARischio.setInt(3, matricola);
            selezionaUtentiARischio.setInt(4, numeroOreMin);
            selezionaUtentiARischio.setInt(5, numeroOreMin);
            selezionaUtentiARischio.setInt(6, numeroOreMin);
            selezionaUtentiARischio.setInt(7, distanzaMax);
            selezionaUtentiARischio.setInt(8, distanzaMax/2);
            ResultSet rs = selezionaUtentiARischio.executeQuery();
            while(rs.next())
                lista.add(new StudenteARischio(rs.getInt("MATRICOLA"),rs.getString("DATA"), rs.getInt("POSIZIONE"), rs.getString("AULA")));
        } catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return lista;
    }
}

/*
(00)
    Seleziona le aule e il numero di posti disponibili per ogni aula. Un posto si considera disponibile se risulta essere accessibile e se non prevede prenotazioni ad esso
    associate un dato giorno e in un dato intervallo orario
(01)
     Per ogni posto a sedere di una data aula ne mostra il corrispondente stato, che può essere :
        - occupato, se vi è una prenotazione in quella data e in quell'intervallo orario per cui un utente ha inoltre fatto l'accesso
        - prenotato, se vi è una prenotazione in quella data e in quell'intervallo orario
        - non disponibile, se non accessibile così da mantenere le distanze
        - disponibile, altrimenti
(02)
    Effettua l'inserimento dell'utente nel database qualora le sue informazioni non siano già presenti
(03)
    Controlla che vi sia una prenotazione oggi, in questa fascia oraria, così da valutare se permettere o meno l'accesso
(04)
    Cerca i dati relativi a una prenotazione così da valutare se sia lecito cancellare la prenotazione
*/