import javafx.scene.control.*;

public class FormAccesso {
    public final TextField matricola;
    public final TextField matricolaRintracciamento;
    
    public FormAccesso(){
        System.out.println("Inizializzo la form relativa a un nuovo accesso..");
        matricola = new TextField("");
        matricolaRintracciamento = new TextField("");
    }
    
    public void svuotaCampi(){
        matricola.setText("");
    }
    
    public boolean matricolaValida(){ //(00)
        if(matricola.getText().equals("admin"))
            return true;
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
}

/*
(00)
    Per essere valida, la matricola deve essere un intero e deve essere composto da 6 cifre
*/