import java.io.*;
import java.net.*;

public class GestoreEventiLog {
    private final String IPServer;
    private final int portaServer;
    private final String IPClient;
    
    public GestoreEventiLog(ParametriConfigurazione parametriConf){
        IPServer = parametriConf.IPServer;
        portaServer = parametriConf.portaServer;
        IPClient = parametriConf.IPClient;
    }
    
    public void creaEventoLog(String etichettaEvento){
        EventoLog log = new EventoLog(IPClient, etichettaEvento);
        invia(log);
    }
    
    private void invia(EventoLog log){ //(00)
        try {
            DataOutputStream dout = new DataOutputStream((new Socket(IPServer, portaServer).getOutputStream()));
            dout.writeUTF(log.getXML());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}

/*
(00)
    Invia al server remoto in formato XML l'evento di log
*/