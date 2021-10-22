import java.time.*;
import javafx.collections.*;
import javafx.scene.control.*;

public class FormPrenotazione {
    public final TextField matricola;
    public final DatePicker data;
    public final ComboBox<String> oraInizio;
    public final ComboBox<String> oraFine;
    private final ParametriConfigurazione parametriConf; //(00)
    
    public FormPrenotazione(ParametriConfigurazione parametriConf){
        System.out.println("Inizializzo la form relativa a una nuova prenotazione..");
        LocalDate now = LocalDate.now();
        matricola = new TextField("");
        data = new DatePicker(now);
        ObservableList<String> hours;
        String x;
        hours = FXCollections.observableArrayList();
        for(int i = 8; i <= 20; ++i){ //(01)
            if(i < 10){
                x = "0" + i + ":00";
            } else x = i + ":00"; 
            hours.add(x);
        }
        oraInizio = new ComboBox<>(hours);
        oraFine = new ComboBox<>(hours);
        oraInizio.getSelectionModel().select(parametriConf.oraInizioPartenza);
        oraFine.getSelectionModel().select(parametriConf.oraFinePartenza);
        this.parametriConf = parametriConf;
    }
    
    public void svuotaCampi(){ //(02)
        LocalDate now = LocalDate.now();
        matricola.setText("");
        data.setValue(now);
        oraInizio.getSelectionModel().select(parametriConf.oraInizioPartenza);
        oraFine.getSelectionModel().select(parametriConf.oraFinePartenza);
    }
    
    public boolean matricolaValida(){ //(03)
        try {
            Integer.parseInt(matricola.getText());  
        } catch(NumberFormatException e){  
            matricola.setStyle("-fx-border-color: #FF0000;");
            return false;
        }
        if(matricola.getText().length() != 6){
            matricola.setStyle("-fx-border-color: #FF0000;");
            return false;
        }  
        return true;
    }
    
    public boolean dataValida(){//(04)
        LocalDate date = data.getValue();
        LocalDate max = LocalDate.now().plusDays(parametriConf.numeroGiorni);
        LocalDate min = LocalDate.now();
         if(date.isAfter(max) || date.isBefore(min)){
            data.setStyle("-fx-border-color: #FF0000;");
            data.setValue(LocalDate.now());
            return false;
        }
        return true;
    }
    
    public boolean oreValide(){ //(05)
        if(Integer.parseInt(oraInizio.getValue().substring(0,2)) >= Integer.parseInt(oraFine.getValue().substring(0,2))){
            oraInizio.setStyle("-fx-border-color: #FF0000;");
            oraFine.setStyle("-fx-border-color: #FF0000;");
            return false;
        }
        return true;
    }
}

/*
(00)
    Memorizzo il riferimento come campo così da riportare gli orari ai loro valori di partenza
    quando i campi vengono svuotati
(01)
    Creo le fasce orarie per le ComboBox di ora inizio e ora fine
(02)
    Svuoto le TextField, ripristino la data a quella corrente e gli orari a quelli di default
(03)
    Per essere valida, la matricola deve essere un intero e deve essere composto da 6 cifre
(04)
    Per essere valida, la data deve essere almeno pari a quella odierna (non è possibile prenotare un posto nel passato)
    e inoltre non deve andare oltre il valore impostato da parametri di configurazione
(05)
    Un intervallo orario è valido se l'ora finale è "strettamente magggiore" all'ora iniziale
*/
