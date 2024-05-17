package Client.Controller;

import SharedModel.*;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import Client.View.*;

public class ClientController {

    private MapController kc;
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
        this.kc = new MapController(this);
        this.searchCityController = new SearchCityController(this, guiController);
        guiController.setSearchCityController(searchCityController);
        guiController.setMapController(kc);
        guiController.setVMAcontroller(vmaController);
        kc.setDisplayer(guiController);
        vmaController.setDisplayer(guiController);
        vmaController.fetchAndDisplayVmaData();
        kc.start();
        if (user.getUserStatus().isCheckedIn()) {
            guiController.setCheckinText(user.getUserStatus().getId());
            checkIn(user.getUserStatus().getId());
        }
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void receivedObject(Object o){
        if (o instanceof User){
            this.user = (User) o;
            guiController.setCheckinText(user.getUserStatus().getId());
        }
        if (o instanceof ConfirmRegistration){
            guiController.closeRegistartionFrame();
        }
        if (o instanceof ConfirmLogin){
            guiController.usernameAndPasswordOk();
        }
         if (o instanceof ContactListUpdate){
             this.allFriends = ((ContactListUpdate) o).getList();
             updateLists();
        }
        if (o instanceof Message) {
            String msg = ((Message) o).getMsg();
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
         if (o instanceof FollowRequest){
             FollowRequest req = (FollowRequest) o;
             followRequest(req);
         }
    }

    private void followRequest(FollowRequest req){
        boolean ok = guiController.receivedFollowRequest(req.getWantsToFollow().getUsername());
        if (ok){
            serverConnection.sendObject(new OkFollowRequest(req, true));
        }
    }

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
    public void register(String userName, String password){
        serverConnection.sendObject(new RegistrationRequest(userName,password));

    }
    public void checkIn(String id) {
        guiController.enableCheckOutButton();
        UserStatus status = new UserStatus(true, id, new Date());
        user.setUserStatus(status);
        serverConnection.sendObject(status);
    }

    public User getUser(){
        return user;
    }

    public ServerConnection getServerConnection(){
        return serverConnection;
    }

    public ArrayList<User> getAllFriends() {
        return allFriends;
    }

    public void checkout(){
        UserStatus checkoutStatus = new UserStatus(false,null,null);
        user.setUserStatus(checkoutStatus);
        serverConnection.sendObject(checkoutStatus);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new ClientController();
    }

    public void login(String userName, String password) {
        User u = new User(userName, password);
        u.setUserStatus(new UserStatus(false,null,null)); //Kolla metoden. Varför ny status?
        serverConnection.sendObject(u);
    }

    public void closeRegisterFrame() {
        guiController.closeRegistartionFrame();
    }

    public void sendFollowRequest(String namn) {
        serverConnection.sendObject(new FollowRequest(user, namn));
    }

    public SearchCityController getSearchCityController(){
        return searchCityController;
    }

    /*public static void logInDialog() {
        String username = JOptionPane.showInputDialog(null, "Ange användarnamn");

        if (username == null || username.isEmpty()){
            JOptionPane.showMessageDialog(null,"Felaktigt användarnamn");
            logInDialog();
        }
        else {
            new ClientController(username);
        }
    }

     */
}

