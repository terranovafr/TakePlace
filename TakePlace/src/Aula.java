import javafx.beans.property.*;

public class Aula { //(00)
    private final SimpleStringProperty nome;
    private final SimpleIntegerProperty numeroPostiDisponibili;

    public Aula(String nome, int numeroPosti){
        this.nome = new SimpleStringProperty(nome);
        this.numeroPostiDisponibili = new SimpleIntegerProperty(numeroPosti);
    }

    public String getNome(){
        return nome.get();
    }

    public int getNumeroPostiDisponibili(){
        return numeroPostiDisponibili.get();
    }
}
/*
(00)
    Classe bean dell'aula utilizzata per tenere sincronizzata l'interfaccia con i dati presenti nel database
*/