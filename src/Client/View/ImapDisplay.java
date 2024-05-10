package Client.View;

import Client.Model.KrisWayPoint;
import org.jxmapviewer.JXMapViewer;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public interface ImapDisplay {

    public void displayMap(JXMapViewer mapViewer);
    public void setCheckinText(String shelter);
    public int displayShelterInfo(ArrayList<KrisWayPoint> foundShelters, int i);
    public String chooseLocationDialog(String[] cities);
    public String enterManually();
    public void displayErrorMessage(String msg);
}
