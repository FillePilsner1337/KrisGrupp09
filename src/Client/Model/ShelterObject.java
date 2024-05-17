package Client.Model;

import org.jxmapviewer.viewer.GeoPosition;

import java.io.Serializable;

public class ShelterObject implements Serializable {

    private GeoPosition position;
    private String address;
    private String idNumber;
    private String capacity;


    public ShelterObject(GeoPosition position, String address, String idNumber, String capacity) {
        this.position = position;
        this.address = address;
        this.idNumber = idNumber;
        this.capacity = capacity;
    }

    public GeoPosition getPosition() {
        return position;
    }

    public String getAddress() {
        return address;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public String getCapacity() {
        return capacity;
    }


}


