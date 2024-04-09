import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;

public class KrisWayPoint extends DefaultWaypoint {
    private String address;
    private String id;
    private String capacaty;

    public KrisWayPoint(GeoPosition geoPosition, String address, String id, String capacaty){
        super(geoPosition);
        this.address = address;
        this.id = id;
        this.capacaty = capacaty;
    }

    public GeoPosition getGeo(){
        return super.getPosition();
    }

    public String getAddress(){
        return address;
    }

    public String getId() {
        return id;
    }

    public String getCapacaty() {
        return capacaty;
    }
}
