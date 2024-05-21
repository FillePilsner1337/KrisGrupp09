package Client.Model;

import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;

/**
 * Klass som används för att rita ut skyddsrummen på kartan.
 * @Author Ola Persson, Jonatan Tempel
 */
public class ShelterWaypoint extends DefaultWaypoint {
    private String address;
    private String idNumber;
    private String capacity;

    public ShelterWaypoint(GeoPosition geoPosition, String address, String idNumber, String capacity){
        super(geoPosition);
        this.address = address;
        this.idNumber = idNumber;
        this.capacity = capacity;
    }

    public GeoPosition getGeoPosition(){
        return super.getPosition();
    }

    public String getAddress(){
        return address;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public String getCapacity() {
        return capacity;
    }
}
