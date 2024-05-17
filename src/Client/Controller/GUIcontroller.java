package Client.Controller;
import Client.Model.CityObject;
import Client.Model.ShelterWaypoint;
import Client.Model.VMAobject;
import Client.View.*;
import org.jxmapviewer.JXMapViewer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GUIcontroller implements Imap, IinfoFriends, Ivma{

    private MapController mapController;

    private ClientController clientController;

    private VMAcontroller vmaController;
    private SearchCityController searchCityController;

    private MainFrame mainFrame;

    private LogInFrame logInFrame;

    private RegisterFrame registerFrame;
    private SearchFrame searchFrame;

    public GUIcontroller(ClientController clientController){
        this.mapController = mapController;
        this.clientController = clientController;
        this.vmaController = vmaController;
        logInFrame = new LogInFrame(this, clientController);
    }

    public void startMainFrame(){}
    @Override
    public void displayVMA(ArrayList<VMAobject> vmaObjects) {
        if (mainFrame != null) {
            mainFrame.getVmaPanel().addVma(vmaObjects);
        }
    }

    public void displayMap(JXMapViewer mapViewer){
        mainFrame.setMapPanel(mapViewer);
    }

    public void displayFriends(ArrayList<String> friendList){
        mainFrame.getCheckInPanel().updateAllFriends(friendList);
        mainFrame.getCheckInPanel().getListAllFriends().revalidate();
        mainFrame.getCheckInPanel().getListAllFriends().repaint();
    }
    public void displayFriendsInShelter(ArrayList<String> friendsInShelter){
        mainFrame.getCheckInPanel().updateFriendsInShelter(friendsInShelter);
        mainFrame.getCheckInPanel().getListFriendsInShelter().revalidate();
        mainFrame.getCheckInPanel().getListFriendsInShelter().repaint();

    }

    public void enableCheckOutButton(){
        mainFrame.getCheckInPanel().getCheckOutButton().setEnabled(true);
    }

    public void checkout(){
        clientController.checkout();
        setCheckoutText();
    }

    public void setCheckinText(String shelter){
        if (mainFrame != null) {
            if (shelter == null) {
                setCheckoutText();
            }
            else {
                mainFrame.getCheckInPanel().getCheckedIn().setText(clientController.getUser().getUsername() + ", Du är nu incheckad i " + shelter);
            }
        }
    }

    public void setCheckoutText(){
        mainFrame.getCheckInPanel().getCheckedIn().setText(clientController.getUser().getUsername() + ", Du är inte incheckad.");
    }
    
    public int displayShelterInfo(ArrayList<ShelterWaypoint> foundShelters, int i){
        ImageIcon img = new ImageIcon("files/1.png");
        Image imgRescale = img.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        ImageIcon img2 = new ImageIcon(imgRescale);

        StringBuilder friendsInShelterBuilder= new StringBuilder();
        for (int j = 0; j < clientController.getAllFriends().size(); j++){
            if (clientController.getAllFriends().get(j).getUserStatus().isCheckedIn()) {

                if (clientController.getAllFriends().get(j).getUserStatus().getId().equals(foundShelters.get(i).getIdNumber())) {
                    friendsInShelterBuilder.append(clientController.getAllFriends().get(j).getUsername()).append("\n ");
                }
            }
        };
        String friendsInShelter = friendsInShelterBuilder.toString();


        int choice;
        if (!friendsInShelter.isEmpty()) {
             choice = JOptionPane.showConfirmDialog(mainFrame, " ID: " + foundShelters.get(i).getIdNumber() +
                    "\n Fastighet: " + foundShelters.get(i).getAddress() +
                    "\n Platser: " + foundShelters.get(i).getCapacity() + "\n" +
                    "\n Incheckade vänner: " +
                    "\n " + friendsInShelter +
                    "\n Vill du checka in? ", foundShelters.get(i).getIdNumber(), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, img2);
        }
        else {
            choice = JOptionPane.showConfirmDialog(mainFrame, " ID: " + foundShelters.get(i).getIdNumber() +
                    "\n Fastighet: " + foundShelters.get(i).getAddress() +
                    "\n Platser: " + foundShelters.get(i).getCapacity() +
                    "\n Vill du checka in? ", foundShelters.get(i).getIdNumber(), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, img2);
        }
        return choice;
    }

    public Object chooseLocationDialog(Object[] cities) {
        String[] options = {"Ok", "Manuell inmatning"};
        JComboBox menuBar = new JComboBox(cities);
        int choice = JOptionPane.showOptionDialog(
                mainFrame,
                menuBar,
                "Välj stad",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);
        if (choice == 0) {
            //String pickedCity = cities[menuBar.getSelectedIndex()];
            Object pickedCity = cities[menuBar.getSelectedIndex()];
            return pickedCity;
        } else {
            return null;
        }
    }

    public String enterManually(){
        String manualInput = JOptionPane.showInputDialog(mainFrame, "Ange koordinater (latitude, longitude): \n På detta sätt: " +
                "55.6088535, 12.9941134");
        return manualInput;
    }

    public void repaintMap(){
        mapController.getMapViewer().repaint();
        mapController.getMapViewer().revalidate();
    }


    public void displayMessage(Component comp, String msg){
        JOptionPane.showMessageDialog(comp,msg);
    }


    public MapController getMapController() {
        return mapController;
    }

    public void setMapController(MapController mapController){
        this.mapController = mapController;
    }
    public void setVMAcontroller(VMAcontroller vmaController){this.vmaController = vmaController;}

    public void usernameAndPasswordOk() {
        logInFrame.setVisible(false);
        logInFrame = null;
        this.mainFrame = new MainFrame(this);
        /*
        Flyttade detta till OpenClose Search.
        Om det inte finns en frame skapas en ny annars sätts visable till true.
        //this.searchFrame = new SearchFrame(this);
        //searchFrame.setVisible(false);
         */
        clientController.setUp();
    }

    public void setSearchCityController(SearchCityController searchCityController) {
        this.searchCityController = searchCityController;
    }

    public void closeRegistartionFrame() {
        registerFrame.setVisible(false);
        registerFrame = null;
        logInFrame.setVisible(true);
    }

    public void startRegistrationFrame() {
        logInFrame.setVisible(false);
        registerFrame = new RegisterFrame(this, clientController);

    }

    public void requestToFollow() {
        String namn = JOptionPane.showInputDialog(mainFrame, "Ange användarnamn på den du vill följa");
        clientController.sendFollowRequest(namn);
    }

    public boolean receivedFollowRequest(String userName) {
        int svar = JOptionPane.showConfirmDialog(mainFrame, "Får " + userName + " vill följa dig?", "Följförfrågan", JOptionPane.YES_NO_OPTION);
        return svar == 0;
    }

    @Override
    public void displayErrorMessage(String msg){
        JOptionPane.showMessageDialog(mainFrame, msg);
    }

    @Override
    public void displaySearchResult(ArrayList<CityObject> list) {
        searchFrame.updateSearchResult(list);

    }

    @Override
    public void openSearchFrame() {
        if (searchFrame == null){
            searchFrame = new SearchFrame(this);
        }
        else {
            /*
            Tar mainframes position och lägger till lite för att lägga searchfönstret mer i mitten av mainframe.
             */
            searchFrame.setLocation(getMainFrame().getX()+250, getMainFrame().getY()+100);
            searchFrame.setVisible(true);
        }
    }

    public LogInFrame getLogInFrame(){
        return logInFrame;
    }
    public RegisterFrame getRegisterFrame(){return registerFrame;};
    public MainFrame getMainFrame(){return mainFrame;};

    public void searchCity(String s) {
        searchCityController.search(s);
    }
}
