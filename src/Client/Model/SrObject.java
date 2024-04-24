package Client.Model;

import org.jxmapviewer.viewer.GeoPosition;

import java.io.Serializable;

public class SrObject implements Serializable {

    private GeoPosition pos;
    private String adress;
    private String idNummer;
    private String kapacitet;


    public SrObject(GeoPosition pos, String adress, String idNummer, String kapacitet) {
        this.pos = pos;
        this.adress = adress;
        this.idNummer = idNummer;
        this.kapacitet = kapacitet;
    }

    public GeoPosition getPos() {
        return pos;
    }

    public String getAdress() {
        return adress;
    }

    public String getIdNummer() {
        return idNummer;
    }

    public String getKapacitet() {
        return kapacitet;
    }


}


