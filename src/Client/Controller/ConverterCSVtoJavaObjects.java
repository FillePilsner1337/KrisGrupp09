package Client.Controller;

import Client.Model.CityObject;
import org.jxmapviewer.viewer.GeoPosition;

import java.io.*;
import java.util.ArrayList;

/**
 * Klass som används för att läsa en .CSV fil och göra om det till javaobjekt. Använts för att konvertera SR.csv och stader.txt.
 * Klassen finns kvar om någon av filerna skulle bli korrupta i framtiden.
 * @Author Ola Persson
 */
public class ConverterCSVtoJavaObjects {
    ArrayList<CityObject> cityObjects = new ArrayList<CityObject>();

    public static void main(String[] args) {
        new ConverterCSVtoJavaObjects();
    }

    public ConverterCSVtoJavaObjects(){
        loadFile();
        savefile();
    }

    private void savefile() {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("files/cityObjects.dat"))) {
                oos.writeObject(cityObjects);
                oos.flush();
            } catch (IOException e) {
                System.out.println("Kunde inte spara fil");
            }
    }

    public void loadFile(){
        try {
            BufferedReader br = new BufferedReader( new FileReader("files/stader.txt" ));
            String[] parts;
            CityObject city;
            String str = br.readLine();
            while( str != null ) {
                parts = str.split( "," );
                city = new CityObject( parts[0], parts[1], parts[2], new GeoPosition(Double.parseDouble(parts[3]), Double.parseDouble(parts[4])));
                cityObjects.add(city);
                str = br.readLine();
            }
            br.close();
        } catch( IOException e ) {
        }
    }
}
