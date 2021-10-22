import com.thoughtworks.xstream.*;

public class EventoLog {
    public final String nomeApplicazione;
    public final String indirizzoIP;
    public final String timestamp;
    public final String etichettaEvento;
    
    public EventoLog(String indirizzoIP, String etichettaEvento){
        this.nomeApplicazione = "TakePlace";
        this.indirizzoIP = indirizzoIP;
        this.etichettaEvento = etichettaEvento;
        this.timestamp = new java.util.Date().toString(); 
    }
    
    public String getXML(){ //(00)
        XStream xs = new XStream();
        return xs.toXML(this);
    }
}

/*
(00)
    Serializza l'oggetto implicito, metodo utilizzato dal Gestore degli eventi di log.
*/