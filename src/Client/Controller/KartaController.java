package Client.Controller;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.io.*;
import java.util.*;
import Client.Model.*;
import Client.View.*;

import javax.swing.*;
import javax.swing.event.MouseInputListener;

import Client.Controller.ControllerKlient;
import Client.Model.SrObject;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.cache.FileBasedLocalCache;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.viewer.*;


public class KartaController {
    HashSet<KrisWayPoint> waypoints = new HashSet<KrisWayPoint>();
    ArrayList<SrObject> srObjects;
    MainFrame mainFrame;
    ControllerKlient controllerKlient;

    public KartaController(MainFrame m, ControllerKlient c) {
        this.mainFrame = m;
        this.controllerKlient = c;
        loadFile();
        //srObjects = readSrObject();
       // saveFile();
        start();
    }

    public void start() {
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        tileFactory.setThreadPoolSize(16);


        File cacheDir = new File(System.getProperty("user.home") + File.separator + ".jxmapviewer2");
        tileFactory.setLocalCache(new FileBasedLocalCache(cacheDir, false));


        final JXMapViewer mapViewer = new JXMapViewer();
        mapViewer.setTileFactory(tileFactory);
        JButton angePlats = new JButton("Ange plats");
        mapViewer.add(angePlats);
        angePlats.setVisible(true);
        angePlats.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String malmö = "Malmö, 55.6088535, 12.9941134";
                String lund = "Lund, 55.704551, 13.192441";
                String stockholm = "Stockholm, 59.325587, 18.0552665";
                String göteborg = "Göteborg, 57.7078558, 11.9732139";
                String[] menyval = new String[4];
                menyval[0] = malmö;
                menyval[1] = lund;
                menyval[2] = stockholm;
                menyval[3] = göteborg;

                /*String start = JOptionPane.showInputDialog(mapViewer, "Ange koordinater (latitude, longitude): \n På detta sätt: " +
                        "55.6088535, 12.9941134" +
                        "\n Lund : 55.704551, 13.192441 \n Stockholm: 59.325587, 18.0552665 \n Göteborg: 57.7078558,11.9732139");

                 */
                Object start1 = JOptionPane.showInputDialog(mapViewer, "Välj stad. Cancel för manuel inmatning", "Ange plats", JOptionPane.QUESTION_MESSAGE, null, menyval, null);
                if (start1 == null){
                    String start = JOptionPane.showInputDialog(mapViewer, "Ange koordinater (latitude, longitude): \n På detta sätt: " +
                            "55.6088535, 12.9941134" +
                            "\n Lund : 55.704551, 13.192441 \n Stockholm: 59.325587, 18.0552665 \n Göteborg: 57.7078558,11.9732139");
                    String[] parts = start.split(",", 2);
                    GeoPosition home = new GeoPosition(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]));
                    mapViewer.setAddressLocation(home);
                    mapViewer.setZoom(5);
                }
                else if (start1!=null) {
                    String start = start1.toString();
                    String[] parts = start.split(",", 3);
                    GeoPosition home = new GeoPosition(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]));
                    mapViewer.setAddressLocation(home);
                    mapViewer.setZoom(5);
                }
            }
        });

        GeoPosition start = new GeoPosition(55.610348059975394, 12.994770622696002);

        mapViewer.setZoom(5);
        mapViewer.setAddressLocation(start);
        System.out.println(new Date());
        for (int i = 0; i < srObjects.size(); i++){
            waypoints.add(new KrisWayPoint(srObjects.get(i).getPos(), srObjects.get(i).getAdress(), srObjects.get(i).getIdNummer(), srObjects.get(i).getKapacitet()));

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

                Point2D point = jxMapViewer.getTileFactory().geoToPixel(new org.jxmapviewer.viewer.GeoPosition(latitude, longitude), jxMapViewer.getZoom());

                graphics2D.setColor(Color.RED);
                int height = 8;
                int width = 8;
                graphics2D.fillRect((int) point.getX() - width/2, (int) point.getY() - height/2, width, height);
                if (krisWayPoint.getId().equals(controllerKlient.getUser().getInUtStatus().getId())){
                    graphics2D.setColor(Color.BLUE);
                     height = 8;
                     width = 8;
                    graphics2D.fillRect((int) point.getX() - width/2, (int) point.getY() - height/2, width, height);
                }
                for (int i = 0; i < controllerKlient.getAllFriends().size(); i++){
                    if(krisWayPoint.getId().equals(controllerKlient.getAllFriends().get(i).getInUtStatus().getId())){
                        graphics2D.setColor(Color.GREEN);
                        height = 8;
                        width = 8;
                        graphics2D.fillRect((int) point.getX() - width/2, (int) point.getY() - height/2, width, height);
                    }
                }

            }
        });
        waypointPainter.setWaypoints(waypoints);
        mapViewer.setOverlayPainter(waypointPainter);

        mapViewer.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent me) {
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

                ImageIcon img = new ImageIcon("files/1.png");
                Image img1 = img.getImage().getScaledInstance(80,80, Image.SCALE_SMOOTH);
                ImageIcon img2 = new ImageIcon(img1);
                 
                if (foundShelters.size() > 1){
                    int numberOfShelters= foundShelters.size();
                    System.out.println(numberOfShelters);
                    for (int i = 0; i < foundShelters.size(); i++) {
                        if (numberOfShelters > 1) {
                            int choice = JOptionPane.showConfirmDialog(mapViewer, " ID: " + foundShelters.get(i).getId() +
                                    "\n Fastighet: " + foundShelters.get(i).getAddress() +
                                    "\n Platser: " + foundShelters.get(i).getCapacaty() +
                                    "\n Vill du checka in? ", foundShelters.get(i).getId(), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,img2);
                            numberOfShelters--;
                            if (choice == JOptionPane.YES_OPTION){
                                mainFrame.getCheckInPanel().getIncheckad().setText(controllerKlient.getUser().getUserName()+   ", Du är incheckad i: " + foundShelters.get(i).getId());
                                System.out.println("Du checkas in i " + foundShelters.get(i).getId() + " - sout bara för att se att det fungerar");
                                controllerKlient.checkIn(foundShelters.get(i).getId());
                                foundShelters.clear();
                                mapViewer.repaint();
                                break;
                            }
                        } else{
                           int choice = JOptionPane.showConfirmDialog(mapViewer, " ID: " + foundShelters.get(i).getId() +
                                    "\n Fastighet: " + foundShelters.get(i).getAddress() +
                                    "\n Kapacitet: " + foundShelters.get(i).getCapacaty() +
                                    "\n Vill du checka in? ", foundShelters.get(i).getId(), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,img2);
                            numberOfShelters= 0;
                            if (choice == JOptionPane.YES_OPTION){
                                mainFrame.getCheckInPanel().getIncheckad().setText(controllerKlient.getUser().getUserName()+   ", Du är incheckad i: " + foundShelters.get(i).getId());
                                controllerKlient.checkIn(foundShelters.get(i).getId());
                                mapViewer.repaint();
                            }
                        }
                    }
                }
                else if (foundShelters.size()==1){
                    int choice = JOptionPane.showConfirmDialog(mapViewer, " ID: " + foundShelters.get(0).getId() +
                            "\n Fastighet: " + foundShelters.get(0).getAddress() +
                            "\n Kapacitet: " + foundShelters.get(0).getCapacaty() +
                            "\n Vill du checka in? ", foundShelters.get(0).getId(), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,img2);
                    if (choice == JOptionPane.YES_OPTION) {
                        mainFrame.getCheckInPanel().getIncheckad().setText(controllerKlient.getUser().getUserName()+   ", Du är incheckad i: " + foundShelters.get(0).getId());
                        System.out.println("Du checkas in i " + foundShelters.get(0).getId() + " - sout bara för att se att det fungerar");
                        controllerKlient.checkIn(foundShelters.get(0).getId());
                        mainFrame.getCheckInPanel().repaint();
                        mainFrame.getCheckInPanel().revalidate();
                        mapViewer.repaint();
                    }
                }
                }
        });
        mainFrame.setKartPanel(mapViewer);
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
    public static ArrayList<SrObject> readSrObject() {
        ArrayList<SrObject> srObjects1 = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader( new FileReader("files/SR.csv"));
            String[] parts;
            SrObject srObject;
            String str = br.readLine();
            while( str != null ) {
                parts = str.split( "," );
                srObject = new SrObject(new GeoPosition(Double.parseDouble(parts[0]),Double.parseDouble(parts[1])),parts[2],parts[3],parts[4]);
                srObjects1.add(srObject);
                str = br.readLine();
            }
            br.close();
        } catch( IOException e ) {
            System.out.println( "readPersons: " + e );
        }
        return srObjects1;
    }

    public void saveFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("files/srOb.dat"))) {
            oos.writeObject(srObjects);
            oos.flush();

        } catch (IOException e){
            System.out.println("Kunde inte spara fil");
        }
    }
}
