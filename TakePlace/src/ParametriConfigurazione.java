 import com.thoughtworks.xstream.*;
import java.io.*;
import java.nio.file.*;

public class ParametriConfigurazione {
    public String font;
    public double dimensioneFont;
    public String coloreBackground;
    public String colorePulsanti;
    public String IPClient;
    public String IPServer;
    public int portaServer;
    public String oraInizioPartenza;
    public String oraFinePartenza;
    public int numeroGiorni;
    public int numeroRigheTabella;
    public String IPDB;
    public int portaDB;
    public String usernameDB;
    public String passwordDB;
    public int numeroSecondiAttesa;
    public int numeroGiorniMax;
    public int distanzaMax;
    public int numeroOreMin;
    
    public ParametriConfigurazione(String fileXML){
        try {
            System.out.println("Inizializzo i parametri di configurazione..");
            String fileXSD = "myfiles/parametriConfigurazione.xsd";
            boolean result = ValidazioneXML.valida(fileXML, fileXSD);
            if(result)
                   System.out.println("Validazione andata a buon fine!");
            ParametriConfigurazione p = (ParametriConfigurazione)(new XStream()).fromXML(new String(Files.readAllBytes(Paths.get(fileXML))));
            font = p.font;
            dimensioneFont = p.dimensioneFont;
            coloreBackground = p.coloreBackground;
            colorePulsanti = p.colorePulsanti;
            IPClient = p.IPClient;
            IPServer = p.IPServer;
            portaServer = p.portaServer;
            oraInizioPartenza = p.oraInizioPartenza;
            oraFinePartenza = p.oraFinePartenza;
            numeroGiorni = p.numeroGiorni;
            numeroRigheTabella = p.numeroRigheTabella;
            IPDB = p.IPDB;
            portaDB = p.portaDB;
            usernameDB = p.usernameDB;
            passwordDB = p.passwordDB;
            numeroSecondiAttesa = p.numeroSecondiAttesa;
            numeroGiorniMax = p.numeroGiorniMax;
            numeroOreMin = p.numeroOreMin;
            distanzaMax = p.distanzaMax;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
