
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
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
import org.jxmapviewer.viewer.*;


public class Karta {
    HashSet<KrisWayPoint> waypoints = new HashSet<KrisWayPoint>();
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
            waypoints.add(new KrisWayPoint(srObjects.get(i).pos, srObjects.get(i).adress, srObjects.get(i).idNummer, srObjects.get(i).kapacitet));

        }
        System.out.println(new Date());

        System.out.println(new Date());
        MouseInputListener mia = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mia);
        mapViewer.addMouseMotionListener(mia);
        mapViewer.addMouseListener(new CenterMapListener(mapViewer));
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCenter(mapViewer));
        mapViewer.addKeyListener(new PanKeyListener(mapViewer));
        System.out.println(new Date());

        WaypointPainter<KrisWayPoint> waypointPainter = new WaypointPainter<>();
        waypointPainter.setRenderer(new WaypointRenderer<KrisWayPoint>() {
            @Override
            public void paintWaypoint(Graphics2D graphics2D, JXMapViewer jxMapViewer, KrisWayPoint krisWayPoint){
                // Extract waypoint coordinates
                double latitude = krisWayPoint.getGeo().getLatitude();
                double longitude = krisWayPoint.getGeo().getLongitude();

                // Convert latitude and longitude to screen coordinates
                Point2D point = jxMapViewer.getTileFactory().geoToPixel(new org.jxmapviewer.viewer.GeoPosition(latitude, longitude), jxMapViewer.getZoom());

                // Assuming KrisWayPoint has a method to get its appearance
                // For example, krisWayPoint.getColor(), krisWayPoint.getShape(), etc.
                // You would then use this information to paint the waypoint

                // Example: Paint a simple circle at the waypoint's location
                graphics2D.setColor(Color.RED); // Example color
                int height = 8;
                int width = 8;
                graphics2D.fillRect((int) point.getX() - width/2, (int) point.getY() - height/2, width, height);
            }
        });
        waypointPainter.setWaypoints(waypoints);
        mapViewer.setOverlayPainter(waypointPainter);

        mapViewer.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent me) {

                    //convert to screen
                    //check if near the mouse
                    Point clickedPoint = me.getPoint();
                    //System.out.println(clickedPoint);
                    GeoPosition clickedPointasGeo = mapViewer.convertPointToGeoPosition(clickedPoint);
                    String clickedpointString = String.valueOf(clickedPointasGeo);
                    String[] clickedpointStringSplit = clickedpointString.split(",", 2);
                    System.out.println("\n ----- " + clickedpointStringSplit[0] + " " + clickedpointStringSplit[1]);
                int numberOfResults = 0;
                ArrayList<KrisWayPoint> foundShelters = new ArrayList<>();
                for (KrisWayPoint waypoint : waypoints) {
                    String waypointString = String.valueOf(waypoint.getGeo());
                    //System.out.println(waypointString);
                    String[] waypointStringSplit = waypointString.split(",", 2);
                    System.out.println(waypointStringSplit[0] + " " + waypointStringSplit[1]);
                    // System.out.println(waypoint.getGeo());
                    // System.out.println(clickedpointString);
                    // System.out.println("\n " + clickedPointasGeo);
                    if ((clickedpointStringSplit[0].substring(0,8).equals(waypointStringSplit[0].substring(0,8))) && (clickedpointStringSplit[1].substring(0,8).equals(waypointStringSplit[1].substring(0,8)))) {
                          foundShelters.add(waypoint);
                        }
                    }
                if (foundShelters.size() > 1){
                    int numberOfShelters= foundShelters.size();
                    System.out.println(numberOfShelters);
                    for (int i = 0; i < foundShelters.size(); i++) {
                        if (foundShelters.size() >= 2 && numberOfShelters > 1) {
                            JOptionPane.showMessageDialog(mapViewer, "There is more than one result. Click ok to show the next one. \n" +
                                    "Result 1: \n " +
                                    "ID: " + foundShelters.get(i).getId() +
                                    "\n Address: " + foundShelters.get(i).getAddress() +
                                    "\n Capacaty: " + foundShelters.get(i).getCapacaty());
                            numberOfShelters--;

                        } else {
                            JOptionPane.showMessageDialog(mapViewer, "Result 2: \n" +
                                    " ID: " + foundShelters.get(i).getId() +
                                    "\n Address: " + foundShelters.get(i).getAddress() +
                                    "\n Capacaty: " + foundShelters.get(i).getCapacaty());
                            numberOfShelters= 0;
                        }
                    }

                }
                else if (foundShelters.size()==1){
                    JOptionPane.showMessageDialog(mapViewer, " ID: " + foundShelters.get(0).getId() +
                            "\n Address: " + foundShelters.get(0).getAddress() +
                            "\n Capacaty: " + foundShelters.get(0).getCapacaty());
                }


                }
        });



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
