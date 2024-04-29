package Client.Controller;
import Client.Model.KrisWayPoint;
import Client.Model.VMAobject;
import Client.View.*;
import org.jxmapviewer.JXMapViewer;

import javax.swing.*;
import java.util.ArrayList;

public class GUIcontroller implements ImapDisplay, IinfoFriends, Ivma{

    private KartaController kartaController;

    private ControllerKlient controllerKlient;

    private VmaController vmaController;

    private MainFrame mainFrame;

    private LogInFrame logInFrame;

    public GUIcontroller(KartaController kartaController, ControllerKlient controllerKlient, VmaController vmaController) {
        this.kartaController = kartaController;
        this.controllerKlient = controllerKlient;
        this.vmaController = vmaController;
        logInFrame = new LogInFrame(this, controllerKlient);

    }

    public void startMainFrame(){


    }
    @Override
    public void displayVMA(ArrayList<VMAobject> vmaObjects) {
        if (mainFrame != null) {
            mainFrame.getVmaPanel().addVma(vmaObjects);
        }
    }

    public void displayMap(JXMapViewer mapViewer){
        mainFrame.setKartPanel(mapViewer);
    }

    public void displayFriends(String[] friendList){
        mainFrame.getCheckInPanel().getListAllaVanner().setListData(friendList);
    }
    public void displayFriendsInShelter(String[] friendsInShelter){
        mainFrame.getCheckInPanel().getListIncheckadeVanner().setListData(friendsInShelter);
    }

    public void enableCheckOutButton(){
        mainFrame.getCheckInPanel().getCheckaUt().setEnabled(true);
    }

    public void checkout(){
        controllerKlient.checkout();
        mainFrame.getCheckInPanel().getIncheckad().setText(controllerKlient.getUser().getUserName() + ", Du är inte incheckad");
    }

    public void setCheckinText(String shelter){
        mainFrame.getCheckInPanel().getIncheckad().setText(controllerKlient.getUser().getUserName() + ", Du är nu incheckad i " + shelter);
    }
    
    public int displayShelterInfo(ArrayList<KrisWayPoint> foundShelters, int i,ImageIcon img2){
        int choice = JOptionPane.showConfirmDialog(mainFrame, " ID: " + foundShelters.get(i).getId() +
                "\n Fastighet: " + foundShelters.get(i).getAddress() +
                "\n Platser: " + foundShelters.get(i).getCapacaty() +
                "\n Vill du checka in? ", foundShelters.get(i).getId(), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,img2);
        return choice;
    }

    public String chooseLocationDialog(String[] cities){
        Object start1 = JOptionPane.showInputDialog(mainFrame, "Välj stad. Cancel för manuel inmatning", "Ange plats", JOptionPane.QUESTION_MESSAGE, null, cities, null);
        String returnString = null;
        try {
             returnString = start1.toString();
        }
        catch (Exception e){}
            return returnString;
    }
    public String enterManually(){
        String manualInput = JOptionPane.showInputDialog(mainFrame, "Ange koordinater (latitude, longitude): \n På detta sätt: " +
                "55.6088535, 12.9941134");
        return manualInput;
    }

    public void repaintGUI(){
        mainFrame.getCheckInPanel().repaint();
        mainFrame.getCheckInPanel().revalidate();
    }


    public KartaController getKartaController() {
        return kartaController;
    }

    public void setKartaController(KartaController kartaController){
        this.kartaController=kartaController;
    }

    public void userAndPassOk() {
        logInFrame.setVisible(false);
        logInFrame = null;
        this.mainFrame = new MainFrame(this);

    }
}
