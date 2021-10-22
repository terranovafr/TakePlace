public class StudenteARischio {
    private final int matricola;
    private final String dataPossibileContagio;
    private final String nomeAula;
    private final int posizione;
    
    public StudenteARischio(int matricola, String data, int posizione, String nomeAula){
        this.matricola = matricola;
        this.dataPossibileContagio = data;
        this.nomeAula = nomeAula;
        this.posizione = posizione;
    }
}
