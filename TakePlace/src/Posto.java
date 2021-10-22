public class Posto { 
    private final int id;
    private final String aula;
    private String stato;

    public Posto(int id, String aula, String stato){
        this.id = id;
        this.aula = aula;
        this.stato = stato;
    }

    public int getId(){
        return id;
    }

    public String getAula(){
        return aula;
    }

    public String getStato(){
        return stato;
    }
    
    public void setStato(String nuovoStato){
        stato = nuovoStato;
    }
}
