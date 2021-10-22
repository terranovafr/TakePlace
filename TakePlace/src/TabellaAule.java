import java.util.*;
import javafx.collections.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;

@SuppressWarnings("unchecked")
public class TabellaAule extends TableView<Aula> {
    private final ObservableList<Aula> listaOsservabileAule;

    public TabellaAule(ParametriConfigurazione parametriConf){
        System.out.println("Inizializzo la tabella delle aule..");
        TableColumn aulaColumn = new TableColumn("Aula");
        aulaColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        TableColumn numeroPostiColumn = new TableColumn("Numero Posti Disponibili");
        numeroPostiColumn.setCellValueFactory(new PropertyValueFactory<>("numeroPostiDisponibili"));
        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //(00)
        aulaColumn.setMaxWidth(1f*Integer.MAX_VALUE*30); //(00)
        numeroPostiColumn.setMaxWidth(1f*Integer.MAX_VALUE*70); //(00)
        setFixedCellSize(35); //(01)
        prefHeightProperty().bind(this.fixedCellSizeProperty().multiply(parametriConf.numeroRigheTabella+1).add(5)); //(01)
        listaOsservabileAule = FXCollections.observableArrayList();
        setItems(listaOsservabileAule);
        getColumns().addAll(aulaColumn, numeroPostiColumn);
    }

    public void aggiornaListaAule(List<Aula> listaAule){ //(02)
        System.out.println("Aggiorno la tabella delle aule..");
        listaOsservabileAule.clear();
        listaOsservabileAule.addAll(listaAule);
    }
}

/*
(00)
    Prevedo per le due colonne una larghezza pari, rispettivamente, al 30% e al 70% della
    larghezza della tabella
(01)
    Mostro un numero di righe della tabella pari a quello specificato da parametri di configurazione
(02)
    Metodo utilizzato per l'aggiornamento della tabella dopo ogni nuova query di selezione delle aule
*/