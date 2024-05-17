package Client.View;

import Client.Model.CityObject;
import Client.Model.ShelterWaypoint;
import org.jxmapviewer.JXMapViewer;

import java.util.ArrayList;

public interface Imap {

    public void displayMap(JXMapViewer mapViewer);
    public void setCheckinText(String shelter);
    public int displayShelterInfo(ArrayList<ShelterWaypoint> foundShelters, int i);
    public Object chooseLocationDialog(Object[] cities);
    public String enterManually();
    public void displayErrorMessage(String msg);
    public void displaySearchResult(ArrayList<CityObject> list);

    void openSearchFrame();
}
