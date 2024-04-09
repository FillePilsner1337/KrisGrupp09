import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;

public class KrisWayPoint extends DefaultWaypoint {
    private String s;
    public KrisWayPoint(GeoPosition geoPosition, String s){
        super(geoPosition);
        this.s = s;
    }

    public GeoPosition getGeo(){
        return super.getPosition();
    }

    public String getS(){
        return s;
    }
}
