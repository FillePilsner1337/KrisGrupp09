package Client.Controller;
import Client.Model.CityObject;
import Client.Model.ShelterWaypoint;
import Client.Model.VMAobject;
import Client.View.*;
import org.jxmapviewer.JXMapViewer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Klass som sköter uppdateringen av klientens GUI. Skickar vidar information till de olika GUI delarna. På detta sätt kan GUI delarna
 * bytas ut på ett effektivt sätt.
 * @Author Ola Persson, Jonatan Tempel
 */
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
        this.clientController = clientController;
        logInFrame = new LogInFrame(this, clientController);
    }

   /*
   Metoder som tillhör Ivma
    */
    @Override
    public void displayVMA(ArrayList<VMAobject> vmaObjects) {
        if (mainFrame != null) {
            mainFrame.getVmaPanel().addVma(vmaObjects);
        }
    }

    /*
    Metoder som tillhör Imap
     */

    public void displayMap(JXMapViewer mapViewer){
        mainFrame.setMapPanel(mapViewer);
    }

    /**
     * Metod som uppdaterar GUI om du checkar in i ett skyddsrum. Om Stringen som används som inparameter är null så kommer metoden setCheckoutText istället att köras.
     * @param shelterID ID på det skyddsrum användaren har valt att checka in i. Används vid utskrift i GUI.
     */
    public void setCheckinText(String shelterID){
        if (mainFrame != null) {
            if (shelterID == null) {
                setCheckoutText();
            }
            else {
                mainFrame.getCheckInPanel().getCheckedIn().setText(clientController.getUser().getUsername() + ", Du är nu incheckad i " + shelterID);
            }
        }
    }

    /**
     * Metod som visar en inforuta om det skyddsrum som användaren har klickat på i kartan. I vissa fall finns där två skyddsrum
     * på samma plats och då visas först det ena skyddsrummet och beroende på vad användaren väljer att göra så visas det andra skyddsrummet.
     * @param foundShelters En arraylista som innehåller de skyddsrum som ligger på den plats användaren klickat på
     * @param i Visar informationen från det objektet som ligger på index i i arraylistan.
     * @return Lämnar tillbaka en int som används för att antingen checka in eller inte göra något alls.
     */
    public int displayShelterInfo(ArrayList<ShelterWaypoint> foundShelters, int i){
        /*
        Tar en bild som finns bland filerna, skalar ner den och använder den i inforutan.
         */
        ImageIcon img = new ImageIcon("files/1.png");
        Image imgRescale = img.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        ImageIcon img2 = new ImageIcon(imgRescale);

        /*
        En StringBuilder som loopar igenom kontaktlistan och kollar om någon användare på listan är incheckad i ett skyddsrum.
        Om en användare på kontaklistan är incheckad kollar metoden om skyddsrumsID på användarens UserStatus matchar med det skyddsrumsID som visas.
        Om det matchar används StringBuilder för att skapa en sträng med alla de användare och den Stringen används vid visning på GUI.
         */
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
        /*
        Om string friendsInShelter inte är tom visas en dialogruta. Om den skulle vara tom visas en annan dialogruta.
         */
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

    /**
     * Metoden används inte för tillfället men har används i tidigare versioner. Användaren väljer en stad ur en array och kartans
     * position sätts till de koordinater som tillhör staden användaren valde.
     * @param cities en array som innehåller de städer som man vill visa i dialogrutan.
     * @return Det objekt som motsvarar den staden man vill visa på kartan.
     */
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
            Object pickedCity = cities[menuBar.getSelectedIndex()];
            return pickedCity;
        } else {
            return null;
        }
    }

    /**
     * Metod där användaren matar in koordinater på angivet sätt.
     * @return En String som sedan hanteras av MapController för att visa angiven plats på kartan.
     */
    public String enterManually(){
        String manualInput = JOptionPane.showInputDialog(mainFrame, "Ange koordinater (latitude, longitude): \n På detta sätt: " +
                "55.6088535, 12.9941134");
        return manualInput;
    }

    @Override
    public void displayErrorMessage(String msg){
        JOptionPane.showMessageDialog(mainFrame, msg);
    }

    @Override
    public void displaySearchResult(ArrayList<CityObject> list) {
        searchFrame.updateSearchResult(list);
    }

    /**
    Metod för att visa searchFrame. Om det inte finns någon searchframe så skapas ett nytt objekt. Annars sätts visible till true.
     */
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

    /*
    Metoder som tillhör IinfoFriends
     */

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

    /*
    Metoder som inte tillhör något interface.
     */

    public void enableCheckOutButton(){
        mainFrame.getCheckInPanel().getCheckOutButton().setEnabled(true);
    }

    /**
     * Metod som används när en användare klickar på knappen Checka ut. Då körs metoden checkout som finns i ClientController-klassen och metoden
     * setCheckoutText.
     */
    public void checkout(){
        clientController.checkout();
        setCheckoutText();
    }

    public void setCheckoutText(){
        mainFrame.getCheckInPanel().getCheckedIn().setText(clientController.getUser().getUsername() + ", Du är inte incheckad.");
    }

    public void repaintMap(){
        mapController.getMapViewer().repaint();
        mapController.getMapViewer().revalidate();
    }

    /**
     * Metod för att visa ett meddelande till användaren.
     * @param comp Det fönster som meddelandet ska dyka upp i.
     * @param msg Det meddelande som ska visas.
     */
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

    /**
     * Metod som används för att kontrollera att användaren har angivit rätt användarnamn och lösenord vid inloggning. Om rätt information
     * har matats in skapas ett nytt mainFrame objekt och metoden setUp körs.
     */
    public void usernameAndPasswordOk() {
        logInFrame.setVisible(false);
        logInFrame = null;
        this.mainFrame = new MainFrame(this);
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

    public LogInFrame getLogInFrame(){
        return logInFrame;
    }
    public RegisterFrame getRegisterFrame(){return registerFrame;};
    public MainFrame getMainFrame(){return mainFrame;};

    public void searchCity(String search) {
        searchCityController.search(search);
    }

    public void removeFrind(String namn) {
        clientController.removeFriend(namn);
    }
}
