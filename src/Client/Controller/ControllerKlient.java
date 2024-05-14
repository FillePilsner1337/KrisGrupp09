package Client.Controller;

import SharedModel.*;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import Client.View.*;

public class ControllerKlient {

    private KartaController kc;
    private ServerConnection serverConnection;
    private ArrayList<User> allFriends = new ArrayList<>();
    private VmaController vmaController;
    private GUIcontroller guiController;
    private IinfoFriends displayer;
    private User user;

    public ControllerKlient(){
        this.guiController = new GUIcontroller(this);
        this.displayer = guiController;
        this.serverConnection = new ServerConnection( this);
    }

    public void setUp(){
        this.vmaController = new VmaController(this);
        this.kc = new KartaController(this);
        guiController.setKartaController(kc);
        guiController.setVmaController(vmaController);
        kc.setDisplayer(guiController);
        vmaController.setDisplayer(guiController);
        vmaController.fetchAndDisplayVmaData();
        kc.start();
        if (user.getInUtStatus().isIncheckad()) {
            guiController.setCheckinText(user.getInUtStatus().getId());
            checkIn(user.getInUtStatus().getId());
        }
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void recivedObject(Object o){
        if (o instanceof User){
            this.user = (User) o;
            guiController.setCheckinText(user.getInUtStatus().getId());
        }
        if (o instanceof ConfirmReg){
            guiController.closeRegFrame();
        }
        if (o instanceof ConfirmLogon){
            guiController.userAndPassOk();
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
         if (o instanceof FollowReq){
             FollowReq req = (FollowReq) o;
             followReq(req);
         }
    }

    private void followReq(FollowReq req){
        boolean ok = guiController.recivedFollowReq(req.getWantsToFollow().getUserName());
        if (ok){
            serverConnection.sendObject(new OkFollowReg(req, true));
        }
    }

    private void updateLists() {
        ArrayList<String> friendList = new ArrayList<>();
        for (int i = 0; i < allFriends.size(); i++){
            friendList.add(allFriends.get(i).getUserName());

            }
        displayer.displayFriends(friendList);

        ArrayList<String> friendsInShelter = new ArrayList<>();
        for (int i = 0; i < allFriends.size(); i++) {
                if (allFriends.get(i).getInUtStatus().isIncheckad()) {
                    friendsInShelter.add(allFriends.get(i).getUserName());
                }
        }

        displayer.displayFriendsInShelter(friendsInShelter);
        guiController.repaintGUI();
    }
    public void register(String userName, String password){
        serverConnection.sendObject(new RegReq(userName,password));

    }
    public void checkIn(String id) {
        guiController.enableCheckOutButton();
        InUtStatus status = new InUtStatus(true, id, new Date());
        user.setInUtStatus(status);
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
        InUtStatus checkoutStatus = new InUtStatus(false,null,null);
        user.setInUtStatus(checkoutStatus);
        serverConnection.sendObject(checkoutStatus);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new ControllerKlient();
    }

    public void logon(String userName, String password) {
        User u = new User(userName, password);
        u.setInUtStatus(new InUtStatus(false,null,null)); //Kolla metoden. Varför ny status?
        serverConnection.sendObject(u);
    }

    public void closeRegisterFrame() {
        guiController.closeRegFrame();
    }

    public void sendFollowReq(String namn) {
        serverConnection.sendObject(new FollowReq(user, namn));
    }

    /*public static void logInDialog() {
        String username = JOptionPane.showInputDialog(null, "Ange användarnamn");

        if (username == null || username.isEmpty()){
            JOptionPane.showMessageDialog(null,"Felaktigt användarnamn");
            logInDialog();
        }
        else {
            new ControllerKlient(username);
        }
    }

     */
}

