package Client.Controller;

import SharedModel.*;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import Client.View.*;

/**
 * Klass som sköter den övergripande logiken i klienten. När klienten startas skapas ett nytt objekt av denna klassen som sen i sin tur
 * skapar alla andra controllers.
 * @Author Ola Persson, Jonatan Tempel
 */
public class ClientController {

    private MapController mapController;
    private ServerConnection serverConnection;
    private ArrayList<User> allFriends = new ArrayList<>();
    private VMAcontroller vmaController;
    private GUIcontroller guiController;
    private SearchCityController searchCityController;
    private IinfoFriends displayer;
    private User user;

    public ClientController(){
        this.guiController = new GUIcontroller(this);
        this.displayer = guiController;
        this.serverConnection = new ServerConnection( this);
    }

    public void setUp(){
        this.vmaController = new VMAcontroller(this);
        this.mapController = new MapController(this);
        this.searchCityController = new SearchCityController(this, guiController);
        guiController.setSearchCityController(searchCityController);
        guiController.setMapController(mapController);
        guiController.setVMAcontroller(vmaController);
        mapController.setDisplayer(guiController);
        vmaController.setDisplayer(guiController);
        vmaController.fetchAndDisplayVmaData();
        mapController.start();
        if (user.getUserStatus().isCheckedIn()) {
            guiController.setCheckinText(user.getUserStatus().getId());
            checkIn(user.getUserStatus().getId());
        }
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Metod som beroende på vilken typ av objekt som tas emot av InputHandlern gör olika saker som tillexempel uppdaterar GUI eller
     * visar ett meddelande.
     * @param obj Det objekt som tagits emot av InputHandler
     */
    public void receivedObject(Object obj){
        if (obj instanceof User){
            this.user = (User) obj;
            guiController.setCheckinText(user.getUserStatus().getId());
        }
        if (obj instanceof ConfirmRegistration){
            guiController.closeRegistartionFrame();
        }
        if (obj instanceof ConfirmLogin){
            guiController.usernameAndPasswordOk();
        }
         if (obj instanceof ContactListUpdate){
             this.allFriends = ((ContactListUpdate) obj).getList();
             updateLists();
        }
        if (obj instanceof Message) {
            String msg = ((Message) obj).getMsg();
            try {
                if (guiController.getLogInFrame() != null && guiController.getLogInFrame().isVisible()) {
                    guiController.displayMessage(guiController.getLogInFrame(), msg);
                } else if (guiController.getRegisterFrame() != null && guiController.getRegisterFrame().isVisible()) {
                    guiController.displayMessage(guiController.getRegisterFrame(), msg);
                }
                else if (guiController.getMainFrame().isVisible()){
                    guiController.displayMessage(guiController.getMainFrame(), msg);
                }
            }
            catch (Exception e){}
        }
         if (obj instanceof FollowRequest){
             FollowRequest request = (FollowRequest) obj;
             followRequest(request);
         }
    }

    private void followRequest(FollowRequest request){
        boolean ok = guiController.receivedFollowRequest(request.getWantsToFollow().getUsername());
        if (ok){
            serverConnection.sendObject(new OkFollowRequest(request, true));
        }
    }

    /**
     * Metod som uppdaterar de arraylistor som används för att visa information i GUI så som vänlistan.
     */
    private void updateLists() {
        ArrayList<String> friendList = new ArrayList<>();
        for (int i = 0; i < allFriends.size(); i++){
            friendList.add(allFriends.get(i).getUsername());

            }
        displayer.displayFriends(friendList);

        ArrayList<String> friendsInShelter = new ArrayList<>();
        for (int i = 0; i < allFriends.size(); i++) {
                if (allFriends.get(i).getUserStatus().isCheckedIn()) {
                    friendsInShelter.add(allFriends.get(i).getUsername() + ", " + allFriends.get(i).getUserStatus().getId());
                }
        }

        displayer.displayFriendsInShelter(friendsInShelter);
        guiController.repaintMap();
    }

    /**
     * Metod som används för att registrera en ny användare.
     * @param username Det användarnamn som användaren vill registrera sig med.
     * @param password Det lösenord användaren vill använda till sin inloggning.
     */
    public void register(String username, String password){
        serverConnection.sendObject(new RegistrationRequest(username,password));

    }

    /**
     * Metod som används när en användare checkar in i ett skyddsrum.
     * GUI uppdateras och ett nytt UserStatus objekt skapas där de sätts som incheckad, vilket skyddsrum och vilken tid de checkade in.
     * Objektet skickas till servern.
     * @param shelterID Det ID som skyddsrummet som användaren har checkat in i har.
     */
    public void checkIn(String shelterID) {
        guiController.enableCheckOutButton();
        UserStatus status = new UserStatus(true, shelterID, new Date());
        user.setUserStatus(status);
        serverConnection.sendObject(status);
    }

    /**
     * Fungerar på liknande sätt som checkIn-metoden. Ett nytt UserStatus objekt skapas där checkedIn sätts till false.
     * Detta objektet sätts som användarens UserStatus och skickas till servern.
     */
    public void checkout(){
        UserStatus checkoutStatus = new UserStatus(false,null,null);
        user.setUserStatus(checkoutStatus);
        serverConnection.sendObject(checkoutStatus);
    }

    public User getUser(){
        return user;
    }

    public ArrayList<User> getAllFriends() {
        return allFriends;
    }

    /**
     * Metod som används när en användare loggar in på klienten. Metoden skapar ett nytt user-objekt och detta skickas till servern.
     * @param userName Det användarnamn som angivits.
     * @param password Det lösenord som angivits.
     */
    public void login(String userName, String password) {
        User u = new User(userName, password);
        u.setUserStatus(new UserStatus(false,null,null));
        serverConnection.sendObject(u);
    }

    public void sendFollowRequest(String name) {
        serverConnection.sendObject(new FollowRequest(user, name));
    }

    public SearchCityController getSearchCityController(){
        return searchCityController;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new ClientController();
    }

    public void removeFriend(String namn) {
        for (int i = 0; i < allFriends.size(); i++) {
            if (namn.equals(allFriends.get(i).getUsername())){
            serverConnection.sendObject(new RemoveFriend(allFriends.get(i)));
            break;
            }
        }
    }
}

