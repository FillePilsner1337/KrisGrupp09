package Client.View;

import Client.Model.CityObject;
import Client.Model.KrisWayPoint;
import org.jxmapviewer.JXMapViewer;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public interface ImapDisplay {

    public void displayMap(JXMapViewer mapViewer);
    public void setCheckinText(String shelter);
    public int displayShelterInfo(ArrayList<KrisWayPoint> foundShelters, int i);
    public Object chooseLocationDialog(Object[] cities);
    public String enterManually();
    public void displayErrorMessage(String msg);
    public void diplaySearchResult(ArrayList<CityObject> list);

    void openCloseSearch();
}
