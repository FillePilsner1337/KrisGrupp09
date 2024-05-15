package Client.Model;

import org.jxmapviewer.viewer.GeoPosition;

import java.io.Serializable;

public class CityObject implements Serializable {
    private String cityName;
    private String kommunName;
    private String regionName;
    private GeoPosition geoPosition;

    public CityObject(String cityName, String kommunName, String regionName, GeoPosition geoPosition) {
        this.cityName = cityName;
        this.kommunName = kommunName;
        this.regionName = regionName;
        this.geoPosition = geoPosition;
    }

    public String getCityName() {
        return cityName;
    }

    public String getKommunName() {
        return kommunName;
    }

    public String getRegionName() {
        return regionName;
    }

    public GeoPosition getGeoPosition() {
        return geoPosition;
    }

    @Override
    public String toString() {
        return cityName + ", " + kommunName + " kommun, " + regionName + " län" ;

    }
}


