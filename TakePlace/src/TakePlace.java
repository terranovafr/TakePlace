import java.time.*;
import java.util.*;
import javafx.application.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.stage.*;

public class TakePlace extends Application {
       private CacheStatoPrenotazione cache;
       private VistaPosti vistaPosti;
       private DataBasePrenotazioni databasePrenotazioni;
       private TabellaAule tabellaAule;
       private FormPrenotazione formPrenotazione;
       private FormAccesso formAccesso;
       private ParametriConfigurazione parametriConf;
       private InterfacciaGrafica interfacciaGrafica;
       private GestoreEventiLog gestoreEventiLog;
       private Stage stage;
       
       @Override
       public void start(Stage stage){
           parametriConf = new ParametriConfigurazione("myfiles/parametriConfigurazione.xml");
           gestoreEventiLog = new GestoreEventiLog(parametriConf);
           gestoreEventiLog.creaEventoLog("AVVIO");
           interfacciaGrafica = new InterfacciaGrafica(parametriConf);
           databasePrenotazioni = new DataBasePrenotazioni(parametriConf);
           formPrenotazione = new FormPrenotazione(parametriConf);
           formAccesso = new FormAccesso();
           cache = new CacheStatoPrenotazione(formPrenotazione, formAccesso);
           cache.prelevaDatiBin();
           tabellaAule = new TabellaAule(parametriConf);
           List<Aula> listaAule = databasePrenotazioni.ottieniAuleDisponibili(formPrenotazione.oraInizio.getValue(),formPrenotazione.oraFine.getValue(), formPrenotazione.data.getValue().toString()); //(00)
           tabellaAule.aggiornaListaAule(listaAule);
           List<Posto> listaPosti = databasePrenotazioni.ottieniPostiAulaDefault();
           vistaPosti = new VistaPosti(listaPosti);
           this.stage = stage;
           inizializzaInterfaccia();
           creaEventi();
       }

       private void inizializzaInterfaccia(){
           VBox VBoxPrenotazione = realizzaVBoxPrenotazione();
           VBox VBoxAccesso = realizzaVBoxAccesso();
           realizzaVBoxRintracciamento();
           VBox menu = new VBox(20);
           menu.getChildren().addAll(VBoxPrenotazione, VBoxAccesso, interfacciaGrafica.VBoxRintracciamento);
           menu.setPadding(new Insets(20, 50, 0, 50));
           HBox FilePosti = new HBox(20);
           FilePosti.getChildren().addAll(vistaPosti.grigliaSx, vistaPosti.grigliaDx);
           FilePosti.setAlignment(Pos.CENTER);
           FilePosti.setPadding(new Insets(0, 50, 50, 50));
           VBox legenda = new VBox(5);
           legenda.getChildren().addAll(interfacciaGrafica.legenda);
           legenda.getStyleClass().add("VBox");
           legenda.setAlignment(Pos.BASELINE_RIGHT);
           legenda.setMaxWidth(300);
           VBox PlaceTaker = new VBox(10);
           PlaceTaker.getChildren().addAll(interfacciaGrafica.nomeAula, FilePosti);
           PlaceTaker.setAlignment(Pos.CENTER);
           PlaceTaker.getStyleClass().add("VBox");
           PlaceTaker.setMaxHeight(700);
           PlaceTaker.setPadding(new Insets(35, 0, 0, 0));
           VBox PlaceView = new VBox(15);
           PlaceView.getChildren().addAll(interfacciaGrafica.messaggioStato, PlaceTaker, legenda);
           PlaceView.setAlignment(Pos.CENTER);
           PlaceView.setPadding(new Insets(-140, 0, 0, 0));
           HBox layout = new HBox(20);
           layout.getChildren().addAll(menu, PlaceView);
           Group group = new Group(layout);
           Scene scene = new Scene(group, 1400, 750, Color.web(parametriConf.coloreBackground));
           stage.setTitle("TakePlace");
           stage.getIcons().add(new Image("file:myfiles/images/distance.png"));
           scene.getStylesheets().add("style.css");
           stage.setScene(scene);
           stage.show();
       }
       
       private void realizzaVBoxRintracciamento(){
           interfacciaGrafica.VBoxRintracciamento = new VBox(20);
           HBox HBoxRintracciamento = new HBox(6);
           HBoxRintracciamento.getStyleClass().add("HBox");
           HBoxRintracciamento.getChildren().addAll(interfacciaGrafica.etichettaMatricolaContagi,formAccesso.matricolaRintracciamento);
           interfacciaGrafica.VBoxRintracciamento.getStyleClass().add("VBox");
           interfacciaGrafica.VBoxRintracciamento.getChildren().addAll(interfacciaGrafica.etichettaRintracciamento, HBoxRintracciamento, interfacciaGrafica.pulsanteRintraccia);
           interfacciaGrafica.VBoxRintracciamento.setPadding(new Insets(20, 20, 20, 20));
           interfacciaGrafica.VBoxRintracciamento.setVisible(false);
       }
       
       private VBox realizzaVBoxPrenotazione(){
           HBox HBoxData = new HBox(8); 
           HBox HBoxOre = new HBox(6); 
           HBox HBoxMatricolaP = new HBox(5); 
           HBox HBoxPulsantiP = new HBox(12);
           HBoxData.getChildren().addAll(interfacciaGrafica.etichettaData, formPrenotazione.data);
           HBoxData.getStyleClass().add("HBox");
           HBoxOre.getChildren().addAll(interfacciaGrafica.etichettaOraInizio, formPrenotazione.oraInizio, interfacciaGrafica.etichettaOraFine, formPrenotazione.oraFine);
           HBoxOre.getStyleClass().add("HBox");
           HBoxMatricolaP.getChildren().addAll(interfacciaGrafica.etichettaMatricolaPrenotazione, formPrenotazione.matricola);
           HBoxMatricolaP.getStyleClass().add("HBox");
           HBoxPulsantiP.getChildren().addAll(interfacciaGrafica.pulsantePrenotazione,interfacciaGrafica.pulsanteCancellazione);
           HBoxPulsantiP.getStyleClass().add("HBox");
           VBox VBoxPrenotazione = new VBox(15);
           VBoxPrenotazione.getStyleClass().add("VBox");
           VBoxPrenotazione.setPadding(new Insets(20, 20, 20, 20));
           VBoxPrenotazione.getChildren().addAll(interfacciaGrafica.etichettaPrenotazione, HBoxData, HBoxOre, tabellaAule, HBoxMatricolaP, HBoxPulsantiP);     
           return VBoxPrenotazione;
       }
       
       private VBox realizzaVBoxAccesso(){
           HBox HBoxAccesso = new HBox(6);
           HBoxAccesso.getChildren().addAll(interfacciaGrafica.etichettaMatricolaAccesso,formAccesso.matricola);
           HBoxAccesso.getStyleClass().add("HBox");
           VBox VBoxAccesso = new VBox(15);
           VBoxAccesso.getChildren().addAll(interfacciaGrafica.etichettaAccesso, HBoxAccesso, interfacciaGrafica.pulsanteAccesso);     
           VBoxAccesso.setPadding(new Insets(20, 20, 20, 20));
           VBoxAccesso.getStyleClass().add("VBox");
           return VBoxAccesso;
       }
       
        private void creaEventi(){
           stage.setOnCloseRequest((WindowEvent e) -> {
              cache.conservaDatiBin();
              gestoreEventiLog.creaEventoLog("TERMINAZIONE");
           });
           formPrenotazione.oraFine.setOnAction((ActionEvent ev) -> {
               gestoreEventiLog.creaEventoLog("SELEZIONE INTERVALLO ORARIO"); //(10)
               aggiornaAule();
           });
           formPrenotazione.oraInizio.setOnAction((ActionEvent ev) -> {aggiornaAule();});
           formPrenotazione.data.setOnAction((ActionEvent ev) -> {
               gestoreEventiLog.creaEventoLog("SELEZIONE DATA"); //(10)
               aggiornaAule();
           });
           interfacciaGrafica.pulsanteRintraccia.setOnAction((ActionEvent ev) -> { rintracciaUtentiARischio();});
           interfacciaGrafica.pulsantePrenotazione.setOnAction((ActionEvent ev) -> { prenotaPosto(); });
           interfacciaGrafica.pulsanteCancellazione.setOnAction((ActionEvent ev) -> { cancellaPrenotazionePosto(); });
           interfacciaGrafica.pulsanteAccesso.setOnAction((ActionEvent ev) -> { accediPosto(); });
           tabellaAule.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> { selezionaRigaTabellaAule(newSelection);});
           for(int i = 0; i < 32; ++i){
               final int index = i;
               vistaPosti.PostiInterfaccia[i].setOnMouseClicked((MouseEvent e) -> { selezionaPosto(index);}); //(11)
           }
        }
        
        private void aggiornaAule(){
            if(!formPrenotazione.oreValide()){
                   interfacciaGrafica.messaggioStato.setText("Hai inserito un intervallo orario non valido!");
                   return;
            }
            if(!formPrenotazione.dataValida()){
                   interfacciaGrafica.messaggioStato.setText("Hai inserito una data non valida!");
                   return;
            }
            formPrenotazione.data.setStyle(null); //(01)
            formPrenotazione.oraInizio.setStyle(null); //(01)
            formPrenotazione.oraFine.setStyle(null); //(01)
            String oraInizio = formPrenotazione.oraInizio.getValue();
            String oraFine = LocalTime.parse(formPrenotazione.oraFine.getValue()).minusSeconds(1).toString(); //(02)
            String data = formPrenotazione.data.getValue().toString();
            List<Aula> listaAule = databasePrenotazioni.ottieniAuleDisponibili(oraInizio, oraFine, data);
            tabellaAule.aggiornaListaAule(listaAule);
            List<Posto> listaPosti = databasePrenotazioni.ottieniPostiAulaDefault();
            vistaPosti.aggiornaPosti(listaPosti);
       }
       
        private void prenotaPosto(){
            gestoreEventiLog.creaEventoLog("PRENOTAZIONE");
            if(!formPrenotazione.dataValida() || !formPrenotazione.oreValide())
                return;
            if(!formPrenotazione.matricolaValida()){
                interfacciaGrafica.messaggioStato.setText("Hai inserito una matricola non valida!");
                return;
            }
            formPrenotazione.matricola.setStyle(null); //(01)
            String oraInizio = formPrenotazione.oraInizio.getValue();
            String oraFine = LocalTime.parse(formPrenotazione.oraFine.getValue()).minusSeconds(1).toString(); //(02)
            String data = formPrenotazione.data.getValue().toString();
            int matricola = Integer.parseInt(formPrenotazione.matricola.getText());
            int posto = -1;
            Aula aula = tabellaAule.getSelectionModel().getSelectedItem();
            if(aula == null){
                tabellaAule.setStyle("-fx-border-color: #FF0000;");
                interfacciaGrafica.messaggioStato.setText("Devi prima selezionare un'aula!");
                return;
            }
            for(int i = 0; i < 32; ++i){ //(03)
                if(vistaPosti.ListaPosti.get(i).getStato().equals("SELEZIONATO DISPONIBILE"))
                    posto = vistaPosti.ListaPosti.get(i).getId();
            }
            if(posto == -1){
                interfacciaGrafica.messaggioStato.setText("Non hai selezionato alcun posto!");
                return;
            }
            String vecchiaPrenotazione = databasePrenotazioni.ottieniDatiPrenotazione(oraInizio, oraFine, data, matricola); //(04)
            if(vecchiaPrenotazione != null)
                interfacciaGrafica.messaggioStato.setText("Hai già prenotato un posto quel giorno in quella fascia oraria!");
            else
                if(databasePrenotazioni.inserisciPrenotazione(oraInizio, oraFine, aula.getNome(), data, posto, matricola))
                    interfacciaGrafica.messaggioStato.setText("Inserimento andato a buon fine!");
            List<Posto> listaPosti = databasePrenotazioni.ottieniPostiAula(aula.getNome(), oraInizio, oraFine, data);
            vistaPosti.aggiornaPosti(listaPosti);
            List<Aula> listaAule = databasePrenotazioni.ottieniAuleDisponibili(oraInizio, oraFine, data);
            tabellaAule.aggiornaListaAule(listaAule);
            tabellaAule.getSelectionModel().clearSelection();
            formPrenotazione.svuotaCampi();
        }
       
        private void cancellaPrenotazionePosto(){
            if(!formPrenotazione.dataValida() || !formPrenotazione.oreValide())
                return;
            if(!formPrenotazione.matricolaValida()){
                interfacciaGrafica.messaggioStato.setText("Hai inserito una matricola non valida!");
                return;
            }
            gestoreEventiLog.creaEventoLog("CANCELLAZIONE PRENOTAZIONE");
            formPrenotazione.matricola.setStyle(null); //(01)
            String oraInizio = formPrenotazione.oraInizio.getValue();
            String oraFine = LocalTime.parse(formPrenotazione.oraFine.getValue()).minusSeconds(1).toString(); //(02)
            String data = formPrenotazione.data.getValue().toString();
            int matricola = Integer.parseInt(formPrenotazione.matricola.getText());
            int posto = -1;
            Aula aula = tabellaAule.getSelectionModel().getSelectedItem();
            if(aula == null){
                tabellaAule.setStyle("-fx-border-color: #FF0000;");
                interfacciaGrafica.messaggioStato.setText("Devi prima selezionare un'aula!");
                return;
            }
            for(int i = 0; i < 32; ++i){ //(03)
                if(vistaPosti.ListaPosti.get(i).getStato().equals("SELEZIONATO PRENOTATO"))
                    posto = vistaPosti.ListaPosti.get(i).getId();
            }
            if(posto == -1){
                interfacciaGrafica.messaggioStato.setText("Non è stato selezionato alcun posto!");
                return;
            }
            if(databasePrenotazioni.cancellaPrenotazione(oraInizio, oraFine, aula.getNome(), data, posto, matricola))
                interfacciaGrafica.messaggioStato.setText("Cancellazione andata a buon fine!");
            else
                interfacciaGrafica.messaggioStato.setText("I dati da te inseriti non sono corretti!");
            List<Posto> listaPosti = databasePrenotazioni.ottieniPostiAula(aula.getNome(), oraInizio, oraFine, data);
            vistaPosti.aggiornaPosti(listaPosti);
            formPrenotazione.svuotaCampi();
            List<Aula> listaAule = databasePrenotazioni.ottieniAuleDisponibili(oraInizio, oraFine, data);
            tabellaAule.aggiornaListaAule(listaAule);
            tabellaAule.getSelectionModel().clearSelection();
       }
       
        private void accediPosto(){
            gestoreEventiLog.creaEventoLog("ACCESSO");
            if(!formAccesso.matricolaValida()){
                interfacciaGrafica.messaggioStato.setText("Hai inserito una matricola non valida!");
                return;
            }
            if(formAccesso.matricola.getText().equals("admin")){ //(05)
                stage.setHeight(950);
                interfacciaGrafica.VBoxRintracciamento.setVisible(true);
                return;
            }
            stage.setHeight(806);
            interfacciaGrafica.VBoxRintracciamento.setVisible(false); //(05)
            formAccesso.matricola.setStyle(null); //(01)
            int matricola = Integer.parseInt(formAccesso.matricola.getText());
            String dati = databasePrenotazioni.ottieniDatiAccesso(matricola);
            if(dati == null){ //(06)
                interfacciaGrafica.messaggioStato.setText("Non hai una prenotazione valida associata!");
                return;
            }
            String[] splitted = dati.split("\\s+");
            List<Posto> listaPostiOld = databasePrenotazioni.ottieniPostiAula(splitted[0], splitted[1], splitted[2], LocalDate.now().toString());
            vistaPosti.aggiornaPosti(listaPostiOld);
            if(databasePrenotazioni.registraAccesso(matricola)){
                interfacciaGrafica.messaggioStato.setText("Accesso andato a buon fine!");
                interfacciaGrafica.nomeAula.setText("Aula " + splitted[0]);
                List<Posto> listaPostiNew = databasePrenotazioni.ottieniPostiAula(splitted[0], splitted[1], splitted[2], LocalDate.now().toString());
                new Thread(() -> { //(07)
                    try {
                        Thread.sleep(1500);
                        Platform.runLater(() -> { vistaPosti.aggiornaPosti(listaPostiNew);});
                    } catch (InterruptedException ex) { System.out.println(ex.getMessage()); } 
                }).start();
                formAccesso.svuotaCampi();
                formAccesso.matricola.setDisable(true); //(08)
                interfacciaGrafica.pulsanteAccesso.setDisable(true); //(08)
                new Thread(() -> { //(08)
                    try {
                        Thread.sleep(parametriConf.numeroSecondiAttesa * 1000);
                        Platform.runLater(() -> {
                            formAccesso.matricola.setDisable(false);
                            interfacciaGrafica.pulsanteAccesso.setDisable(false);
                        });
                    } catch (InterruptedException ex) { System.out.println(ex.getMessage()); }
                }).start();
            }
       }
       
       private void selezionaPosto(int index){
            gestoreEventiLog.creaEventoLog("SELEZIONE DI UN POSTO A SEDERE");
            if(tabellaAule.getSelectionModel().getSelectedItem() != null)
                vistaPosti.selezionaPosto(index);
            else {
                interfacciaGrafica.messaggioStato.setText("Devi prima selezionare un'aula dalla tabella!");
                tabellaAule.setStyle("-fx-border-color: #FF0000;");
            }
       }
       
       private void selezionaRigaTabellaAule(Aula selezione){
            if (selezione != null){
                tabellaAule.setStyle(null);
                gestoreEventiLog.creaEventoLog("SELEZIONE AULA");
                interfacciaGrafica.nomeAula.setText("Aula " + selezione.getNome()); //(09)
                String oraInizio = formPrenotazione.oraInizio.getValue();
                String oraFine = LocalTime.parse(formPrenotazione.oraFine.getValue()).minusSeconds(1).toString(); //(01)
                String data = formPrenotazione.data.getValue().toString();
                List<Posto> listaPosti = databasePrenotazioni.ottieniPostiAula(selezione.getNome(), oraInizio, oraFine, data);
                vistaPosti.aggiornaPosti(listaPosti);
            }
       }
       
       private void rintracciaUtentiARischio(){
            gestoreEventiLog.creaEventoLog("RINTRACCIAMENTO UTENTI");
            List<StudenteARischio> lista = databasePrenotazioni.ottieniUtentiARischio(Integer.parseInt(formAccesso.matricolaRintracciamento.getText()), parametriConf.numeroGiorniMax, parametriConf.numeroOreMin, parametriConf.distanzaMax);
            new PossibileContagio(Integer.parseInt(formAccesso.matricolaRintracciamento.getText()),lista).salvaDati();
            interfacciaGrafica.messaggioStato.setText("File XML generato!");
       }
       
    }

/*
(00)
    Ottengo inizialmente la lista delle aule disponibili in funzione dei valori di default (da parametri di configurazione)
    o dei valori ripristinati dalla cache qualora siano diversi da quelli di default
(01)
    Superati i controlli di validità precedentemente lo stile potrà essere stato modificato (di fronte a controlli di validità
    non superati, prevedendo dei bordi di color rosso. Ripristino in ogni caso il bordo al suo colore precedente.
(02)
    Sottraggo un seconda dall'ora di fine, così da far prenotare intervalli del tipo: '08:00:00' - '08:59:59', necessario
    per il corretto funzionamento delle query SQL
(03) 
    Cerco il posto selezionato e mi assicuro che sia stato selezionato un posto disponibile e non un posto prenotato
(04)
    Controllo che l'utente non abbia già prenotato un posto in questa o in un'altra aula in quella fascia oraria e in quella data
(05)
    Qualora a effettuare l'accesso sia l'amministratore mostro la box del rintracciamento e la nascondo appena mi trovo
    davanti ad un altro accesso che non sia quello dell'amministratore
(06)
    Controllo che vi sia una prenotazione valida associata a quell'accesso. In particolare controllo che vi sia una prenotazione
    relativa alla data odierna e in questa fascia oraria.
(07)
    Dopo aver portato l'interfaccia sull'aula in cui si fa ora accesso, dopo 1,5 secondi porto il posto dallo stato prenotato
    allo stato acceduto, così da aumentare l'interattività
(08)
    Disabilito la funzionalità di accesso per un certo numero di secondi (avendo in mente il distanziamento sociale),
    operazione non svolta quando ad effettuare l'accesso è l'amministratore
(09) 
    Mostro il nome dell'aula sull'interfaccia grafica una volta che questa viene selezionata
(10)
    Invoco lo stesso metodo, a cambiare è solo l'evento generato, effettuo l'operazione di segnalazione dell'evento quindi
    fuori dal metodo
(11)
    Associo un gestore ai click su ogni posto a sedere
*/