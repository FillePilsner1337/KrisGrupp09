package Client.Controller;
import Client.Model.KrisWayPoint;
import Client.Model.VMAobject;
import Client.View.*;
import SharedModel.User;
import org.jxmapviewer.JXMapViewer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GUIcontroller implements ImapDisplay, IinfoFriends, Ivma{

    private KartaController kartaController;

    private ControllerKlient controllerKlient;

    private VmaController vmaController;

    private MainFrame mainFrame;

    public GUIcontroller(KartaController kartaController, ControllerKlient controllerKlient, VmaController vmaController) {
        this.kartaController = kartaController;
        this.controllerKlient = controllerKlient;
        this.vmaController = vmaController;
        this.mainFrame = new MainFrame(this);
    }

    @Override
    public void displayVMA(ArrayList<VMAobject> vmaObjects) {


            mainFrame.getVmaPanel().addVma(vmaObjects);





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
    
    public int displayShelterInfo(ArrayList<KrisWayPoint> foundShelters, int i){
        ImageIcon img = new ImageIcon("files/1.png");
        Image imgRescale = img.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        ImageIcon img2 = new ImageIcon(imgRescale);

        StringBuilder friendsInShelterBuilder= new StringBuilder();
        for (int j = 0; j < controllerKlient.getAllFriends().size(); j++){
            if (controllerKlient.getAllFriends().get(j).getInUtStatus().isIncheckad()) {

                if (controllerKlient.getAllFriends().get(j).getInUtStatus().getId().equals(foundShelters.get(i).getId())) {
                    friendsInShelterBuilder.append(controllerKlient.getAllFriends().get(j).getUserName()).append("\n ");
                }
            }
        };
        String friendsInShelter = friendsInShelterBuilder.toString();
        System.out.println(friendsInShelter);

        int choice;
        if (!friendsInShelter.isEmpty()) {
             choice = JOptionPane.showConfirmDialog(mainFrame, " ID: " + foundShelters.get(i).getId() +
                    "\n Fastighet: " + foundShelters.get(i).getAddress() +
                    "\n Platser: " + foundShelters.get(i).getCapacaty() + "\n" +
                    "\n Incheckade vänner: " +
                    "\n " + friendsInShelter +
                    "\n Vill du checka in? ", foundShelters.get(i).getId(), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, img2);
        }
        else {
            choice = JOptionPane.showConfirmDialog(mainFrame, " ID: " + foundShelters.get(i).getId() +
                    "\n Fastighet: " + foundShelters.get(i).getAddress() +
                    "\n Platser: " + foundShelters.get(i).getCapacaty() +
                    "\n Vill du checka in? ", foundShelters.get(i).getId(), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, img2);
        }
        return choice;
    }

    public String chooseLocationDialog(String[] cities){
        String[] options = {"Ok", "Manuell inmatning"};
        JComboBox menuBar = new JComboBox(cities);

        //Object start1 = JOptionPane.showInputDialog(mainFrame, "Välj stad.", "Ange plats", JOptionPane.QUESTION_MESSAGE, null, cities, null);
        int choice = JOptionPane.showOptionDialog(
                mainFrame,
                menuBar,
                "Välj stad",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);
        if (choice == 0){
            String pickedCity = cities[menuBar.getSelectedIndex()];
            return pickedCity;
        }
        else if (choice == 1){
            String nullValue = null;
        }
        return null;
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
}
