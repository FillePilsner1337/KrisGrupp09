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
            this.user = (User)o;
            System.out.println(user.getInUtStatus().toString());
        }
        if (o instanceof ConfirmLogon){
            guiController.userAndPassOk();
        }
         if (o instanceof ContactListUpdate){
             System.out.println("Tog emot ContactListUpdate ");
            this.allFriends = ((ContactListUpdate) o).getList();
             System.out.println(allFriends.get(10).getInUtStatus().isIncheckad());
            if (!allFriends.isEmpty()) {
                System.out.println(allFriends.get(0).getUserName());
                updateLists();
            }
        }
        if (o instanceof Message){
            String msg = ((Message) o).getMsg();
            guiController.displayMessage(msg);
        }
    }

    private void updateLists() {
        String[] friendList = new String[allFriends.size()];
        for (int i = 0; i < allFriends.size(); i++){
            if (!allFriends.get(i).equals(user) ){
            friendList[i] = allFriends.get(i).getUserName();
            }
        }
        displayer.displayFriends(friendList);

        String[] friendsInShelter = new String[allFriends.size()];
        for (int i = 0; i < allFriends.size(); i++) {
            if (!allFriends.get(i).equals(user)) {
                if (allFriends.get(i).getInUtStatus().isIncheckad() == true) {
                    friendsInShelter[i] = allFriends.get(i).getUserName();
                }
            }
        }
        displayer.displayFriendsInShelter(friendsInShelter);
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
        u.setInUtStatus(new InUtStatus(false,null,null));
        serverConnection.sendObject(u);
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

