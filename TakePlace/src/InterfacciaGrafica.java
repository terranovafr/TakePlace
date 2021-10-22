import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;

public class InterfacciaGrafica {
    public final Button pulsantePrenotazione;
    public final Button pulsanteCancellazione;
    public final Button pulsanteAccesso;
    public final Label etichettaPrenotazione;
    public final Label etichettaAccesso;
    public final Label etichettaMatricolaPrenotazione;
    public final Label etichettaMatricolaAccesso;
    public final Label etichettaAula;
    public final Label etichettaOraInizio;
    public final Label etichettaOraFine;
    public final Label etichettaData;
    public final ImageView legenda;
    public final TextField nomeAula;
    public final TextField messaggioStato;
    public final Label etichettaRintracciamento;
    public final Button pulsanteRintraccia;
    public final Label etichettaMatricolaContagi;
    public VBox VBoxRintracciamento;
    
    public InterfacciaGrafica(ParametriConfigurazione parametriConf){
        System.out.println("Inizializzo l'interfaccia grafica...");
        pulsantePrenotazione = new Button("Prenota");
        pulsanteCancellazione = new Button("Cancella");
        pulsanteAccesso = new Button("Accedi");
        pulsantePrenotazione.setStyle("-fx-background-color: " + parametriConf.colorePulsanti); //(00)
        pulsanteCancellazione.setStyle("-fx-background-color: " + parametriConf.colorePulsanti); //(00)
        pulsanteAccesso.setStyle("-fx-background-color: " + parametriConf.colorePulsanti);// (00)
        etichettaPrenotazione = new Label("Prenotazione");
        etichettaAccesso = new Label("Accesso");
        etichettaPrenotazione.getStyleClass().add("title"); //(01)
        etichettaAccesso.getStyleClass().add("title"); //(01)
        etichettaMatricolaPrenotazione = new Label("Matricola");
        etichettaMatricolaAccesso = new Label("Matricola");
        etichettaAula = new Label("Aula");
        etichettaOraInizio = new Label("Ora Inizio");
        etichettaOraFine = new Label("Ora Fine");
        etichettaData = new Label("Data");
        etichettaData.setStyle("-fx-font-size: " + parametriConf.dimensioneFont +"em; -fx-font-family: " + parametriConf.font + ";"); //(00)
        etichettaAula.setStyle("-fx-font-size: " + parametriConf.dimensioneFont +"em; -fx-font-family: " + parametriConf.font + ";"); //(00)
        etichettaMatricolaAccesso.setStyle("-fx-font-size: " + parametriConf.dimensioneFont +"em; -fx-font-family: " + parametriConf.font + ";"); //(00)
        etichettaMatricolaPrenotazione.setStyle("-fx-font-size: " + parametriConf.dimensioneFont +"em; -fx-font-family: " + parametriConf.font + ";"); //(00)
        etichettaOraInizio.setStyle("-fx-font-size: " + parametriConf.dimensioneFont +"em; -fx-font-family: " + parametriConf.font + ";"); //(00)
        etichettaOraFine.setStyle("-fx-font-size: " + parametriConf.dimensioneFont +"em; -fx-font-family: " + parametriConf.font + ";"); //(00)
        legenda = new ImageView("file:myfiles/images/legenda.png");
        legenda.setFitHeight(120);
        legenda.setFitWidth(480);
        nomeAula = new TextField("");
        nomeAula.setDisable(true);
        nomeAula.setAlignment(Pos.CENTER);
        nomeAula.setMaxWidth(300);
        nomeAula.setStyle("-fx-opacity: 1.0;");
        messaggioStato = new TextField("");
        messaggioStato.setDisable(true);
        messaggioStato.setAlignment(Pos.CENTER);
        messaggioStato.setMaxWidth(600);
        messaggioStato.setStyle("-fx-opacity: 1.0; -fx-background-color: linear-gradient(from 0% 93% to 0% 100%, #0000cc 0%, #0000ff 100%),radial-gradient(center 50% 50%, radius 100%, #f0f0f5, #f0f0f5);");
        etichettaMatricolaContagi = new Label("Matricola");
        etichettaMatricolaContagi.setStyle("-fx-font-size: " + parametriConf.dimensioneFont +"em; -fx-font-family: " + parametriConf.font + ";"); //(00)
        etichettaRintracciamento = new Label("Rintraccia utenti");
        etichettaRintracciamento.getStyleClass().add("title"); //(01)
        pulsanteRintraccia = new Button("Rintraccia");
        pulsanteRintraccia.setStyle("-fx-background-color: " + parametriConf.colorePulsanti);// (00)
    }
}

/*
(00)
    Specifico lo stile dipendente da parametri di configurazione mediante metodo setStyle, il resto
    dello stile (la parte statica) è espresso nel file 'style.css'
(01)
    Utilizzo uno stile diverso per i titoli 'Prenotazione' e 'Accesso'
*/