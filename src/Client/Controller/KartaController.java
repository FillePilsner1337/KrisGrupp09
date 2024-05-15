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
            "Göteborg, 57.7078558, 11.9732139", "Ronneby, 56.210434, 15.276022900000043"};

    public KartaController(ControllerKlient ck) {
        this.controllerKlient = ck;
        loadFile();
        /*
        Dessa används inte för tillfället men de kommer ligga kvar då de kan behövas första gången man startar applikationen från
        en ny dator.
         */
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
        JButton search = new JButton("Sök plats");
        search.setVisible(true);

        mapViewer.add(search);
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayer.openCloseSearch();
            }
        });
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
                           displayer.displayErrorMessage("Felaktig inmatning");
                           startPos(null); //tar dig till niagara om exception inträffar.
                       }
                   }
                   else{
                       displayer.displayErrorMessage("Felaktig inmatning");
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
                for (int i = 0; i < controllerKlient.getAllFriends().size(); i++) {
                    if (krisWayPoint.getId().equals(controllerKlient.getAllFriends().get(i).getInUtStatus().getId()) && !controllerKlient.getAllFriends().get(i).getUserName().equals(controllerKlient.getUser().getUserName())) {
                        graphics2D.setColor(Color.GREEN);
                        height = 8;
                        width = 8;
                        graphics2D.fillRect((int) point.getX() - width / 2, (int) point.getY() - height / 2, width, height);
                    }
                }
                try {
                    if (krisWayPoint.getId().equals(controllerKlient.getUser().getInUtStatus().getId())) {
                        graphics2D.setColor(Color.BLUE);
                        height = 8;
                        width = 8;
                        graphics2D.fillRect((int) point.getX() - width / 2, (int) point.getY() - height / 2, width, height);
                    }
                }catch (Exception e){}
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
                    int[] howClose = clickDependingOnZoom(mapViewer.getZoom());
                    String waypointString = String.valueOf(waypoint.getGeo());
                    String[] waypointStringSplit = waypointString.split(",", 2);
                    /*
                    Om man är på zoomnivå 5 eller högre kommer det inte gå att klicka på ett skyddsrum som koden är implementerad
                    i detta läget. Men det är lättare att klicka på ett skyddsrum om man befinner sig i zoomnivå 3-4.
                     */
                    try {
                        if ((clickedpointStringSplit[0].substring(1, 15).regionMatches(0, waypointStringSplit[0], 1, howClose[0])) && (clickedpointStringSplit[1].substring(0, 15).regionMatches(0, waypointStringSplit[1], 0, howClose[1]))) {
                            foundShelters.add(waypoint);
                        }
                    }catch (Exception exceptionClickedWaypoint) {
                    }
                }
                if (foundShelters.size() > 1) {
                    int numberOfShelters = foundShelters.size();
                    for (int i = 0; i < foundShelters.size(); i++) {
                        if (numberOfShelters > 1) {
                            int choice = displayer.displayShelterInfo(foundShelters,i); //Kan kommat att ändras i framtiden
                            numberOfShelters--;
                            if (choice == 0) {
                                checkIn(foundShelters, i);
                                foundShelters.clear();
                                break;
                            }
                        } else {
                            int choice = displayer.displayShelterInfo(foundShelters, i); //Kan kommat att ändras i framtiden
                            numberOfShelters = 0;
                            if (choice == 0) {
                                checkIn(foundShelters,i);
                            }
                        }
                    }
                } else if (foundShelters.size() == 1) {
                    int i = foundShelters.size()-1;
                    int choice = displayer.displayShelterInfo(foundShelters, i); //Kan kommat att ändras i framtiden
                    if (choice == 0) {
                        checkIn(foundShelters, i);
                    }
                }
            }
        });
        displayer.displayMap(mapViewer);
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

    public int[] clickDependingOnZoom(int mapZoom){
        int[] howClose = new int[2];
        int howCloseLong = 0;
        int howCloseLat = 0;
        if (mapZoom == 0 || mapZoom <= 2){
            howCloseLong = 8;
            howCloseLat = 8;
            howClose[0] = howCloseLong;
            howClose[1] = howCloseLat;
        }
        else if (mapZoom >= 3 && mapZoom < 5){
            howCloseLong = 6;
            howCloseLat = 7;
            howClose[0] = howCloseLong;
            howClose[1] = howCloseLat;
        }
        else {
            howClose = null;
        }
        return howClose;
    }

    public void loadFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("files/srOB.dat"))) {
            Object o = ois.readObject();
            srObjects = (ArrayList<SrObject>) o;
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

    public JXMapViewer getMapViewer(){return mapViewer;}

}

