
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.event.MouseInputListener;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.cache.FileBasedLocalCache;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.WaypointPainter;


public class Karta {
    HashSet<SwingWaypoint> waypoints = new HashSet<SwingWaypoint>();
    ArrayList<SrObject> srObjects;


    public void start() {

        try {

            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        tileFactory.setThreadPoolSize(16);


        File cacheDir = new File(System.getProperty("user.home") + File.separator + ".jxmapviewer2");
        tileFactory.setLocalCache(new FileBasedLocalCache(cacheDir, false));


        JXMapViewer mapViewer = new JXMapViewer();
        mapViewer.setTileFactory(tileFactory);


        GeoPosition start = new GeoPosition(55.610348059975394, 12.994770622696002);


        mapViewer.setZoom(5);
        mapViewer.setAddressLocation(start);
        System.out.println(new Date());
        for (int i = 0; i < srObjects.size(); i++){
            waypoints.add(new SwingWaypoint(srObjects.get(i).adress, srObjects.get(i).idNummer, srObjects.get(i).kapacitet, srObjects.get(i).pos));

        }
        System.out.println(new Date());


        /*
        try {
            BufferedReader br = new BufferedReader( new FileReader( "files/SR.csv" ) );
            String[] parts;
            GeoPosition position;
            String str = br.readLine();
            while( str != null ) {
                parts = str.split( "," );
                double x = Double.parseDouble(parts[0]);
                double y = Double.parseDouble(parts[1]);
                String adress = parts[2];
                String idNummer = parts[3];
                String kapacitet = parts[4];
                position = new GeoPosition( x,y);
                srObjects.add(new SrObject(position, adress, idNummer, kapacitet));

                str = br.readLine();
            }
            br.close();
        } catch( IOException e ) {
            System.out.println( "readPersons: " + e );
        }
        saveFile();

         waypoints = new HashSet<SwingWaypoint>();

        try {
            BufferedReader br = new BufferedReader( new FileReader( "files/SR.csv" ) );
            String[] parts;
            GeoPosition position;
            String str = br.readLine();
            while( str != null ) {
                parts = str.split( "," );
                double x = Double.parseDouble(parts[0]);
                double y = Double.parseDouble(parts[1]);
                String adress = parts[2];
                String idNummer = parts[3];
                String kapacitet = parts[4];
                position = new GeoPosition( x,y);
                waypoints.add(new SwingWaypoint(adress, idNummer, kapacitet, position));

                str = br.readLine();
            }
            br.close();
        } catch( IOException e ) {
            System.out.println( "readPersons: " + e );
        }

*/

        System.out.println(new Date());
        MouseInputListener mia = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mia);
        mapViewer.addMouseMotionListener(mia);
        mapViewer.addMouseListener(new CenterMapListener(mapViewer));
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCenter(mapViewer));
        mapViewer.addKeyListener(new PanKeyListener(mapViewer));
        System.out.println(new Date());



        WaypointPainter<SwingWaypoint> swingWaypointPainter = new SwingWaypointOverlayPainter();
        swingWaypointPainter.setWaypoints(waypoints);
        mapViewer.setOverlayPainter(swingWaypointPainter);
        System.out.println(new Date());

        for (SwingWaypoint w : waypoints) {
            mapViewer.add(w.getButton());
        }

        System.out.println(new Date());

        JFrame frame = new JFrame("Skyddsrum");
       
        System.out.println("innan getContent " + new Date());
        frame.getContentPane().add(mapViewer);
        System.out.println("efter getContent " + new Date());
        frame.setSize(800, 600);
        System.out.println("efter setsize " + new Date());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        System.out.println("efter defult close " + new Date());

        frame.setVisible(true);
        System.out.println("efter setvisble " + new Date());

    }

    public void saveFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("files/srOb.dat"))) {
            oos.writeObject(srObjects);
            oos.flush();

        } catch (IOException e){
            System.out.println("Kunde inte spara fil");
        }


    }

    public void loadFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("files/srOB.dat"))) {
           Object o = ois.readObject();
           System.out.println("Lästfil");
            srObjects = (ArrayList<SrObject>) o;
            System.out.println("Loaded");

        } catch (IOException e){
            if (e instanceof EOFException) {
                System.out.println("EOF fel");
            } else {
                System.out.println("unde inte ladda fil IOException");
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Kunde inte ladda fil ClassNotFoundException");
        }
    }
}
