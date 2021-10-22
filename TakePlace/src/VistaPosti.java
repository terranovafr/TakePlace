import java.util.*;
import javafx.geometry.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;

public class VistaPosti{
    public List<Posto> ListaPosti;
    public ImageView[] PostiInterfaccia;
    public GridPane grigliaSx;
    public GridPane grigliaDx;

    public VistaPosti(List<Posto> ListaPosti){
        System.out.println("Inizializzo vista dei posti..");
        int numeroPosti = 32;
        PostiInterfaccia = new ImageView[numeroPosti];
        this.ListaPosti = ListaPosti;
        grigliaSx = new GridPane();
        grigliaSx.setHgap(5);
        grigliaSx.setPadding(new Insets(20, 0, 0, 0));
        grigliaSx.setVgap(5); 
        for(int i = 0; i < 4; ++i)
            for(int j = 0; j < 4; ++j){ //(00)
                PostiInterfaccia[(i*4)+j] = new ImageView("file:myfiles/images/grey.png");
                PostiInterfaccia[(i*4)+j].setFitHeight(86);
                PostiInterfaccia[(i*4)+j].setFitWidth(69);
                grigliaSx.add(PostiInterfaccia[(i*4)+j], j, i);
            }
        grigliaDx = new GridPane();
        grigliaDx.setHgap(5);
        grigliaDx.setVgap(5); 
        for(int i = 4; i < 8; ++i)
            for(int j = 0; j < 4; ++j){ //(00)
                PostiInterfaccia[(i*4)+j] = new ImageView("file:myfiles/images/grey.png");
                PostiInterfaccia[(i*4)+j].setFitHeight(86);
                PostiInterfaccia[(i*4)+j].setFitWidth(69);
                grigliaDx.add(PostiInterfaccia[(i*4)+j], j, i);
            }
        aggiornaVistaPosti();
    }

    public void aggiornaPosti(List<Posto> ListaPosti){
        this.ListaPosti = ListaPosti;
        aggiornaVistaPosti();
    }

    private void aggiornaVistaPosti(){ 
        for(int i = 0; i < 8; ++i){
            for(int j = 0; j < 4; ++j){
                switch(ListaPosti.get((i*4)+j).getStato()){ //(01)
                    case "NON DISPONIBILE": 
                        PostiInterfaccia[(i*4)+j].setImage(new Image("file:myfiles/images/grey.png"));
                        break; 
                    case "PRENOTATO": 
                        PostiInterfaccia[(i*4)+j].setImage(new Image("file:myfiles/images/blue.png"));
                        break; 
                    case "OCCUPATO": 
                        PostiInterfaccia[(i*4)+j].setImage(new Image("file:myfiles/images/red.png")); 
                        break; 
                    case "DISPONIBILE": 
                        PostiInterfaccia[(i*4)+j].setImage(new Image("file:myfiles/images/green.png")); 
                        break;  
                    default: 
                        PostiInterfaccia[(i*4)+j].setImage(new Image("file:myfiles/images/yellow.png"));
                }
            }
        }
    }
    
    public void selezionaPosto(int index){
        switch (ListaPosti.get(index).getStato()) { //(02)
            case "SELEZIONATO DISPONIBILE":
                ListaPosti.get(index).setStato("DISPONIBILE");
                aggiornaVistaPosti();
                return;
            case "SELEZIONATO PRENOTATO":
                ListaPosti.get(index).setStato("PRENOTATO");
                aggiornaVistaPosti();
                return;
        }
        for(int i = 0; i < 32; ++i){ //(03)
            switch (ListaPosti.get(i).getStato()){
                case "SELEZIONATO DISPONIBILE":
                    ListaPosti.get(i).setStato("DISPONIBILE");
                    break;
                case "SELEZIONATO PRENOTATO":
                    ListaPosti.get(i).setStato("PRENOTATO");
                    break;
            }
        }   
        switch (ListaPosti.get(index).getStato()){//(04)
            case "DISPONIBILE":
                ListaPosti.get(index).setStato("SELEZIONATO DISPONIBILE");
                break;
            case "PRENOTATO":
                ListaPosti.get(index).setStato("SELEZIONATO PRENOTATO");
                break;
        }   
        aggiornaVistaPosti();
    }
}

/*
(00)
    All'inizio i posti a sedere sono tutti non disponibili (non è stata ancora selezionata un'aula).
(01)
    Associo ad ogni immagine la giusta URL, predisponendo per ogni elemento che rappresenta il posto a sedere
    un'immagine di colore diverso a seconda del suo stato
(02)
    Se viene selezionato un posto già selezionato allora lo deseleziono
(03)
    Deseleziono altri posti già selezionati, così da avere al più un solo posto selezionato ad ogni istante
(04)
    Seleziono il posto a sedere portandolo rispettivamente nello stato 'Selezionato Prenotato' o
    'Selezionato Disponibile' a seconda del suo stato precedente. Questa distinzione sarà utile
    nell'operazione di prenotazione/cancellazione.
    Si può infatti prenotare solo un posto disponibile, ma non prenotato, e cancellare la prenotazione
    solo di un posto prenotato, ma non disponibile.
*/