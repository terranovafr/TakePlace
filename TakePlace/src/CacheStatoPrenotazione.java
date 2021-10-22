import java.io.*;
import java.time.*;
import java.time.format.*;

public class CacheStatoPrenotazione {
    private final FormPrenotazione formPrenotazione; //(00)
    private final FormAccesso formAccesso; //(00)
    private final String fileCache;
    private String matricolaPrenotazione; //(01)
    private String matricolaAccesso; //(01)
    private String data; //(01)
    private String oraInizio; //(01)
    private String oraFine; //(01)
    
    public CacheStatoPrenotazione(FormPrenotazione formPrenotazione, FormAccesso formAccesso){
           System.out.println("Inizializzo la cache..");
           this.formPrenotazione = formPrenotazione;
           this.formAccesso = formAccesso;
           fileCache = "myfiles/cache.bin";
    }
    
    public void conservaDatiBin(){
        System.out.println("Conservo i dati nel file binario..");
        prelevaDatiForm();
        try (FileOutputStream fout = new FileOutputStream(fileCache);
             ObjectOutputStream oout = new ObjectOutputStream(fout);){
            oout.writeObject(matricolaPrenotazione);
            oout.writeObject(data);
            oout.writeObject(oraInizio);
            oout.writeObject(oraFine);
            oout.writeObject(matricolaAccesso);
        } catch(IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    private void prelevaDatiForm(){
        matricolaPrenotazione = formPrenotazione.matricola.getText();
        data = formPrenotazione.data.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        oraInizio = formPrenotazione.oraInizio.getValue();
        oraFine = formPrenotazione.oraFine.getValue();
        matricolaAccesso = formAccesso.matricola.getText();
    }
    
    public void prelevaDatiBin(){
        System.out.println("Ripristino i dati dal file binario..");
        try (FileInputStream fin = new FileInputStream(fileCache);
             ObjectInputStream oin = new ObjectInputStream(fin)){
            matricolaPrenotazione = (String)oin.readObject();
            data = (String)oin.readObject();
            oraInizio = (String)oin.readObject();
            oraFine = (String)oin.readObject();
            matricolaAccesso = (String)oin.readObject();
        } catch(Exception ex){
            System.out.println("Nessun dato in cache!");
        }
        ripristinaDatiForm();
    }     
    
    private void ripristinaDatiForm(){
        if(matricolaPrenotazione != null)
            formPrenotazione.matricola.setText(matricolaPrenotazione);
        if(data != null)
            formPrenotazione.data.setValue(LocalDate.parse(data));
        if(oraInizio != null)
            formPrenotazione.oraInizio.setValue(oraInizio);
        if(oraFine != null)
            formPrenotazione.oraFine.setValue(oraFine);
        if(matricolaAccesso != null)
            formAccesso.matricola.setText(matricolaAccesso);           
    }
}

/*
(00)
    Riferimenti alle form così da permettere alla classe Cache di recuperare i dati dai campi.
(01)
    Campi utilizzati per permettere lo scambio di informazioni fra i metodi prelevaDatiBin e ripristinaDatiForm
    e conservaDatiBin e prelevaDatiForm
*/
