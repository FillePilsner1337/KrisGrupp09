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

import Client.Model.ShelterObject;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.cache.FileBasedLocalCache;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.viewer.*;

/**
 * Klass som sköter logiken för kartfunktionen i applikationen. Klassen målar ut de skyddsrum som den läser in från fil på kartan.
 * @Author Ola Persson, Jonatan Tempel
 */
public class MapController {
    HashSet<ShelterWaypoint> waypoints = new HashSet<ShelterWaypoint>();
    ArrayList<ShelterObject> shelterObjects;
    private Imap displayer;
    final JXMapViewer mapViewer = new JXMapViewer();
    ClientController clientController;

    /*
    private String[] cities = new String[]{"Malmö, 55.6088535, 12.9941134", "Lund, 55.704551, 13.192441", "Stockholm, 59.325587, 18.0552665",
            "Göteborg, 57.7078558, 11.9732139", "Ronneby, 56.210434, 15.276022900000043"};
     */
    private Object[] cities;

    public MapController(ClientController ck) {
        this.clientController = ck;
        loadFile();
        /*
        Dessa används inte för tillfället men de kommer ligga kvar då de kan behövas första gången man startar applikationen från
        en ny dator eller om filerna skulle bli korrupta.
         */
        // shelterObjects = readSrObject();
        // saveFile();
        //start();
    }

    /**
     * Metod som startar kartan. Körs inte denna metoden kommer en tom panel att visas.
     */
    public void start() {
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        tileFactory.setThreadPoolSize(16);

        File cacheDir = new File(System.getProperty("user.home") + File.separator + ".jxmapviewer2");
        tileFactory.setLocalCache(new FileBasedLocalCache(cacheDir, false));
        mapViewer.setTileFactory(tileFactory);

        JButton angePlats = new JButton("Sök");
        mapViewer.add(angePlats);
        angePlats.setVisible(true);

        //cities = clientController.getSearchCityController().getCities().toArray(); Används inte för tillfället men har används i en tidigare version
        angePlats.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayer.openSearchFrame();
                /*
                Detta har används i en tidigare version när sökfunktionen inte fanns. Kan plockas bort när sökfunktionen har visats för resten av gruppen och de har godkänt
                att sökfunktionen används istället.

                String start1 = displayer.chooseLocationDialog(cities);
                if (start1 != null){
                    String[] parts = start1.split(",", 3);
                    GeoPosition home = new GeoPosition(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]));
                    startPos(home);
                }
                 */
                /*
                CityObject start1 = ((CityObject) displayer.chooseLocationDialog(cities));
                if (start1 != null){
                    GeoPosition home = start1.getGeoPosition();
                    startPos(home);
                }
                else {
                    displayer.openCloseSearch();
                }
                 */
                //Vilken else sats som används beror på vilken implementering man vill använda.
                /*
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
                   else {
                       displayer.displayErrorMessage("Felaktig inmatning");
                       GeoPosition home = null;
                       startPos(home);
                   }
                }
                 */
            }
        });

        startPosition(null);
        /*
        Loopar igenom arraylistan som innehåller skyddsrummen och lägger till dem i ett Hashset. De används för att rita ut skyddsrummen på kartan.
         */
        for (int i = 0; i < shelterObjects.size(); i++) {
            waypoints.add(new ShelterWaypoint(shelterObjects.get(i).getPosition(), shelterObjects.get(i).getAddress(), shelterObjects.get(i).getIdNumber(), shelterObjects.get(i).getCapacity()));
        }
        MouseInputListener mouseInputListener = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mouseInputListener);
        mapViewer.addMouseMotionListener(mouseInputListener);
        mapViewer.addMouseListener(new CenterMapListener(mapViewer));
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCenter(mapViewer));
        mapViewer.addKeyListener(new PanKeyListener(mapViewer));

        WaypointPainter<ShelterWaypoint> waypointPainter = new WaypointPainter<>();

        /**
         * Metod som ritar ut skyddsrummen på kartan. Den tar en GeoPosistion och gör om den till pixlar på kartan.
         * Beroende på om skyddsrummet är tomt, om vänner är incheckade eller om användaren själv är incheckad målas skyddsrummet i en
         * speciell färg.
         */
        waypointPainter.setRenderer(new WaypointRenderer<ShelterWaypoint>() {
            @Override
            public void paintWaypoint(Graphics2D graphics2D, JXMapViewer jxMapViewer, ShelterWaypoint shelterWaypoint) {
                double latitude = shelterWaypoint.getGeoPosition().getLatitude();
                double longitude = shelterWaypoint.getGeoPosition().getLongitude();

                Point2D point = jxMapViewer.getTileFactory().geoToPixel(new org.jxmapviewer.viewer.GeoPosition(latitude, longitude), jxMapViewer.getZoom());

                graphics2D.setColor(Color.RED);
                int height = 8;
                int width = 8;
                graphics2D.fillRect((int) point.getX() - width / 2, (int) point.getY() - height / 2, width, height);
                for (int i = 0; i < clientController.getAllFriends().size(); i++) {
                    if (shelterWaypoint.getIdNumber().equals(clientController.getAllFriends().get(i).getUserStatus().getId()) && !clientController.getAllFriends().get(i).getUsername().equals(clientController.getUser().getUsername())) {
                        graphics2D.setColor(Color.GREEN);
                        height = 8;
                        width = 8;
                        graphics2D.fillRect((int) point.getX() - width / 2, (int) point.getY() - height / 2, width, height);
                    }
                }
                try {
                    if (shelterWaypoint.getIdNumber().equals(clientController.getUser().getUserStatus().getId())) {
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
            /**
             * Metod som lyssnar på var användaren klickar på kartan. Beroende på inzoomningsnivå och hur nära ett skyddsrum användaren klickar kommer
             * information om skyddsrummet att visas.
             * Gör om den punkt användaren klickade på till en geoposition. Geopositionen görs sedan om till en sträng och jämförs
             * med ett skyddsrums geoposition som också gjort om till en string för att se om de matchar.
             * En högre inzoomning kräver mer noggrannhet kräver att användaren klickar närmare ett skyddsrum.
             * @param me the event to be processed
             */
            @Override
            public void mouseClicked(MouseEvent me) {
                Point clickedPoint = me.getPoint();
                GeoPosition clickedPointasGeo = mapViewer.convertPointToGeoPosition(clickedPoint);
                String clickedpointString = String.valueOf(clickedPointasGeo);
                String[] clickedpointStringSplit = clickedpointString.split(",", 2);
                int numberOfResults = 0;
                ArrayList<ShelterWaypoint> foundShelters = new ArrayList<>();
                for (ShelterWaypoint waypoint : waypoints) {
                    int[] howClose = clickDependingOnZoom(mapViewer.getZoom());
                    String waypointString = String.valueOf(waypoint.getGeoPosition());
                    String[] waypointStringSplit = waypointString.split(",", 2);
                    /*
                    Om man är på zoomnivå 5 eller högre kommer det inte gå att klicka på ett skyddsrum som koden är implementerad
                    i detta läget. Men det är lättare att klicka på ett skyddsrum om man befinner sig i zoomnivå 3-4.
                     */
                    try {
                        /*
                        Om koordinaterna en användare klickat på stämmer överens med de koordinater ett skyddsrum ligger på kommer det skyddsrummet
                        att läggas till i en arraylista. Det kan finnas mer än ett skyddsrum på en koordinat.
                         */
                        if ((clickedpointStringSplit[0].substring(1, 15).regionMatches(0, waypointStringSplit[0], 1, howClose[0])) && (clickedpointStringSplit[1].substring(0, 15).regionMatches(0, waypointStringSplit[1], 0, howClose[1]))) {
                            foundShelters.add(waypoint);
                        }
                    }catch (Exception exceptionClickedWaypoint) {
                    }
                }
                /*
                Om det finns mer än ett skyddsrum visas först information om det första skyddsrummet i listan. Om användaren väljer att inte checka
                in i det första skyddsrummet kommer det andra skyddsrummet i listan att visas. Finns det bara ett skyddsrum visas informationen för det skyddsrummet.
                 */
                if (foundShelters.size() > 1) {
                    int numberOfShelters = foundShelters.size();
                    for (int i = 0; i < foundShelters.size(); i++) {
                        if (numberOfShelters > 1) {
                            int choice = displayer.displayShelterInfo(foundShelters,i);
                            numberOfShelters--;
                            if (choice == 0) {
                                checkIn(foundShelters, i);
                                foundShelters.clear();
                                break;
                            }
                        } else {
                            int choice = displayer.displayShelterInfo(foundShelters, i);
                            numberOfShelters = 0;
                            if (choice == 0) {
                                checkIn(foundShelters,i);
                            }
                        }
                    }
                } else if (foundShelters.size() == 1) {
                    int i = foundShelters.size()-1;
                    int choice = displayer.displayShelterInfo(foundShelters, i);
                    if (choice == 0) {
                        checkIn(foundShelters, i);
                    }
                }
            }
        });
        displayer.displayMap(mapViewer);
    }

    /**
     * Metod som används för att visa en inzoomad nivå av en plats på kartan. Används för att visa var ett VMA meddelande har inträffat.
     * @param geoPosition Den position som man vill visa på kartan.
     */
    public void takeMeHere(GeoPosition geoPosition){
        mapViewer.setAddressLocation(geoPosition);
        mapViewer.setZoom(7);
    }

    /**
     * Metod som används för att visa en mindre inzoomad nivå av en plats på martan. Används för att visa vilket skyddsrum ens anhöriga sitter i.
     * @param geoPosition Den position som man vill visa på kartan.
     */
    public void takeMeHereFriendLocation(GeoPosition geoPosition){
        mapViewer.setAddressLocation(geoPosition);
        mapViewer.setZoom(4);
    }

    /**
     * Metod som visar en plats på kartan. Används när användaren anger en plats att visa på kartan. Om användaren skulle ange en felaktig
     * input kommer ett standardvärde att användas istället.
     * @param geoPosition Den plats användaren vill visa på kartan.
     */
    public void startPosition(GeoPosition geoPosition){
        if (geoPosition == null) {
             geoPosition = new GeoPosition(55.610348059975394, 12.994770622696002);
        }
        mapViewer.setZoom(5);
        mapViewer.setAddressLocation(geoPosition);
    }

    public void checkIn(ArrayList<ShelterWaypoint> foundShelters, int i){
        displayer.setCheckinText(foundShelters.get(i).getIdNumber());
        clientController.checkIn(foundShelters.get(i).getIdNumber());
        mapViewer.repaint();
    }

    /**
     * Metod som används för att avgöra hur nära ett skyddsrum en användare behöver klicka beroende på vilken zoomnivå som används.
     * @param mapZoom Den zoomnivå som används när användaren klickar på kartan.
     * @return En int array som innehåller två värde. Dessa värde avgör hur mycket av longitud och latitud som behöver stämma överrens
     * med den position användaren klickade på och den position skyddsrummet ligger på.
     */
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

    /**
     * Metod som läser in den fil som innehåller alla skyddsrum.
     */
    public void loadFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("files/srOB.dat"))) {
            Object o = ois.readObject();
            shelterObjects = (ArrayList<ShelterObject>) o;
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

    /**
     * Metod som används för att läsa in skyddsrummen från en fil och lägger till de som javaObejkt i en arraylista. Liknande funktion som klassen ConverterCSVtoJavaObjects.
     * @return en arraylista som innehåller alla skyddsrum.
     */
    public static ArrayList<ShelterObject> readSrObject() {
        ArrayList<ShelterObject> shelterObjects1 = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("files/SR.csv"));
            String[] parts;
            ShelterObject shelterObject;
            String str = br.readLine();
            while (str != null) {
                parts = str.split(",");
                shelterObject = new ShelterObject(new GeoPosition(Double.parseDouble(parts[0]), Double.parseDouble(parts[1])), parts[2], parts[3], parts[4]);
                shelterObjects1.add(shelterObject);
                str = br.readLine();
            }
            br.close();
        } catch (IOException e) {}
        return shelterObjects1;
    }

    /**
     * Metod för att spara arraylistan som innehåller skyddsrummen till en fil så att denna kan användas vid programstart.
     */
    public void saveFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("files/srOb.dat"))) {
            oos.writeObject(shelterObjects);
            oos.flush();
        } catch (IOException e) {
            System.out.println("Kunde inte spara fil");
        }
    }

    public void setDisplayer(Imap displayer) {
        this.displayer = displayer;
    }

    public JXMapViewer getMapViewer(){return mapViewer;}

    public HashSet<ShelterWaypoint> getWaypoints(){
        return waypoints;
    }
}

