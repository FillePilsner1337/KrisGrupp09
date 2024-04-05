import org.jxmapviewer.viewer.GeoPosition;

import java.io.Serializable;

public class SrObject implements Serializable {

    public GeoPosition pos;
    public String adress;
    public String idNummer;
    public String kapacitet;


    public SrObject(GeoPosition pos, String adress, String idNummer, String kapacitet) {
        this.pos = pos;
        this.adress = adress;
        this.idNummer = idNummer;
        this.kapacitet = kapacitet;
    }
}


