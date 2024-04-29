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
import com.sun.tools.javac.Main;
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
    private ImapDisplay displayer;
    final JXMapViewer mapViewer = new JXMapViewer();
    ControllerKlient controllerKlient;
    private String[] cities = new String[]{"Malmö, 55.6088535, 12.9941134", "Lund, 55.704551, 13.192441", "Stockholm, 59.325587, 18.0552665",
            "Göteborg, 57.7078558, 11.9732139"};

    public KartaController(ControllerKlient ck) {
        this.controllerKlient = ck;
        loadFile();
        //srObjects = readSrObject();
        // saveFile();
        //start();
    }

    public void start() {
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        tileFactory.setThreadPoolSize(16);

        File cacheDir = new File(System.getProperty("user.home") + File.separator + ".jxmapviewer2");
        tileFactory.setLocalCache(new FileBasedLocalCache(cacheDir, false));

        mapViewer.setTileFactory(tileFactory);
        JButton angePlats = new JButton("Ange plats");
        mapViewer.add(angePlats);
        angePlats.setVisible(true);
        angePlats.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String start1 = displayer.chooseLocationDialog(cities);
                if (start1 != null){
                    String[] parts = start1.split(",", 3);
                    GeoPosition home = new GeoPosition(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]));
                    startPos(home);
                }
                else {
                   String start = displayer.enterManually();
                   if (start != null){
                       String[] parts = start.split(",", 2);
                       try { //felhantering om man skulle skriva input på fel sätt.
                           GeoPosition home = new GeoPosition(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]));
                           startPos(home);
                       }catch (Exception ex){
                           startPos(null); //tar dig till niagara om exception inträffar.
                       }
                   }
                   else{
                       GeoPosition home = null;
                       startPos(home);
                   }
                }
            }
        });

        startPos(null);
        for (int i = 0; i < srObjects.size(); i++) {
            waypoints.add(new KrisWayPoint(srObjects.get(i).getPos(), srObjects.get(i).getAdress(), srObjects.get(i).getIdNummer(), srObjects.get(i).getKapacitet()));
        }
        MouseInputListener mia = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mia);
        mapViewer.addMouseMotionListener(mia);
        mapViewer.addMouseListener(new CenterMapListener(mapViewer));
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCenter(mapViewer));
        mapViewer.addKeyListener(new PanKeyListener(mapViewer));

        WaypointPainter<KrisWayPoint> waypointPainter = new WaypointPainter<>();
        waypointPainter.setRenderer(new WaypointRenderer<KrisWayPoint>() {
            @Override
            public void paintWaypoint(Graphics2D graphics2D, JXMapViewer jxMapViewer, KrisWayPoint krisWayPoint) {
                double latitude = krisWayPoint.getGeo().getLatitude();
                double longitude = krisWayPoint.getGeo().getLongitude();

                Point2D point = jxMapViewer.getTileFactory().geoToPixel(new org.jxmapviewer.viewer.GeoPosition(latitude, longitude), jxMapViewer.getZoom());

                graphics2D.setColor(Color.RED);
                int height = 8;
                int width = 8;
                graphics2D.fillRect((int) point.getX() - width / 2, (int) point.getY() - height / 2, width, height);
                if (krisWayPoint.getId().equals(controllerKlient.getUser().getInUtStatus().getId())) {
                    graphics2D.setColor(Color.BLUE);
                    height = 8;
                    width = 8;
                    graphics2D.fillRect((int) point.getX() - width / 2, (int) point.getY() - height / 2, width, height);
                }
                for (int i = 0; i < controllerKlient.getAllFriends().size(); i++) {
                    if (krisWayPoint.getId().equals(controllerKlient.getAllFriends().get(i).getInUtStatus().getId())) {
                        graphics2D.setColor(Color.GREEN);
                        height = 8;
                        width = 8;
                        graphics2D.fillRect((int) point.getX() - width / 2, (int) point.getY() - height / 2, width, height);
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
                GeoPosition clickedPointasGeo = mapViewer.convertPointToGeoPosition(clickedPoint);
                String clickedpointString = String.valueOf(clickedPointasGeo);
                String[] clickedpointStringSplit = clickedpointString.split(",", 2);
                int numberOfResults = 0;
                ArrayList<KrisWayPoint> foundShelters = new ArrayList<>();
                for (KrisWayPoint waypoint : waypoints) {
                    String waypointString = String.valueOf(waypoint.getGeo());
                    String[] waypointStringSplit = waypointString.split(",", 2);
                    if ((clickedpointStringSplit[0].substring(0, 8).equals(waypointStringSplit[0].substring(0, 8))) && (clickedpointStringSplit[1].substring(0, 8).equals(waypointStringSplit[1].substring(0, 8)))) {
                        foundShelters.add(waypoint); //Bryt ut denna delen till en egen metod och gör den beroende på getZoomLevel
                    }
                }
                /*
                Ska mest troligt ska denna delen flyttas till det nya framefönstret.
                 */
                ImageIcon img = new ImageIcon("files/1.png");
                Image imgRescale = img.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                ImageIcon img2 = new ImageIcon(imgRescale);

                if (foundShelters.size() > 1) {
                    int numberOfShelters = foundShelters.size();
                    for (int i = 0; i < foundShelters.size(); i++) {
                        if (numberOfShelters > 1) {
                            int choice = displayer.displayShelterInfo(foundShelters, i, img2); //Kan kommat att ändras i framtiden
                            numberOfShelters--;
                            if (choice == 0) {
                                checkIn(foundShelters, i);
                                foundShelters.clear();
                                break;
                            }
                        } else {
                            int choice = displayer.displayShelterInfo(foundShelters, i, img2); //Kan kommat att ändras i framtiden
                            numberOfShelters = 0;
                            if (choice == 0) {
                                checkIn(foundShelters,i);
                            }
                        }
                    }
                } else if (foundShelters.size() == 1) {
                    int i = foundShelters.size()-1;
                    int choice = displayer.displayShelterInfo(foundShelters, i, img2); //Kan kommat att ändras i framtiden
                    if (choice == 0) {
                        checkIn(foundShelters, i);
                    }
                }
            }
        });
        //displayer.displayMap(mapViewer);
    }

    public void takeMeHere(GeoPosition geoPosition){
        mapViewer.setAddressLocation(geoPosition);
        mapViewer.setZoom(7);
    }

    public void startPos(GeoPosition geoPosition){
        if (geoPosition == null) {
             geoPosition = new GeoPosition(55.610348059975394, 12.994770622696002);
        }
        mapViewer.setZoom(5);
        mapViewer.setAddressLocation(geoPosition);
    }

    public void checkIn(ArrayList<KrisWayPoint> foundShelters, int i){
        displayer.setCheckinText(foundShelters.get(i).getId());
        controllerKlient.checkIn(foundShelters.get(i).getId());
        mapViewer.repaint();
    }

    public void loadFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("files/srOB.dat"))) {
            Object o = ois.readObject();
            System.out.println("Lästfil");
            srObjects = (ArrayList<SrObject>) o;
            System.out.println("Loaded");

        } catch (IOException e) {
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
            BufferedReader br = new BufferedReader(new FileReader("files/SR.csv"));
            String[] parts;
            SrObject srObject;
            String str = br.readLine();
            while (str != null) {
                parts = str.split(",");
                srObject = new SrObject(new GeoPosition(Double.parseDouble(parts[0]), Double.parseDouble(parts[1])), parts[2], parts[3], parts[4]);
                srObjects1.add(srObject);
                str = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            System.out.println("readPersons: " + e);
        }
        return srObjects1;
    }

    public void saveFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("files/srOb.dat"))) {
            oos.writeObject(srObjects);
            oos.flush();

        } catch (IOException e) {
            System.out.println("Kunde inte spara fil");
        }
    }

    public void setDisplayer(ImapDisplay displayer) {
        this.displayer = displayer;
    }

    public void displayMapViewer(){
        displayer.displayMap(mapViewer);
    }

}

