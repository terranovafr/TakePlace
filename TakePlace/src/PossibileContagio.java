import com.thoughtworks.xstream.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class PossibileContagio {
    private final int matricolaContagiato;
    private final List<StudenteARischio> studentiARischio;
        
    public PossibileContagio(int matricolaContagiato, List<StudenteARischio> studentiARischio){
        this.matricolaContagiato = matricolaContagiato;
        this.studentiARischio = studentiARischio;
    }
    
    private String getXML(){
        XStream xs = new XStream();
        return xs.toXML(this);
    }
    
    public void salvaDati(){
        String xml = getXML();
        System.out.println(xml);
        try {
            Files.write(Paths.get("myfiles/listaUtentiARischio.xml"), xml.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException ex) {
            System.out.println("Eccezione: " + ex.getMessage());
        }
    }
}
    
