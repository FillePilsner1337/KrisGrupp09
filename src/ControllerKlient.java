import Server.Model.ContactListUpdate;
import Server.Model.InUtStatus;
import Server.Model.Message;
import Server.Model.User;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;

public class ControllerKlient {

    private MainFrame mainFrame;
    private KartaController kc;
    private ServerConnection serverConnection;
    private ArrayList<User> allFriends = new ArrayList<>();


    private User user;
    public ControllerKlient(String username){
        this.user = new User(username);
        this.serverConnection = new ServerConnection(user, this);



        this.mainFrame = new MainFrame();
        this.kc = new KartaController(mainFrame, this);


    }

    public void recivedObject(Object o){
        if (o instanceof User){
            user = (User)o;
            System.out.println(user.getInUtStatus().toString());
        }
         if (o instanceof ContactListUpdate){
            this.allFriends = ((ContactListUpdate) o).getList();
            if (!allFriends.isEmpty()) {
                System.out.println(allFriends.get(0).getUserName());
            }
        }
        if (o instanceof Message){
            String msg = ((Message) o).getMsg();
            JOptionPane.showMessageDialog(null,msg);

        }




    }

    public void checkIn(String id) {
        InUtStatus status = new InUtStatus(true, id, new Date());
        user.setInUtStatus(status);
        serverConnection.sendObject(status);
        allFriends.add(new User("TestKalle")); // TEST flytta till vettigt ställe
        serverConnection.sendObject(new ContactListUpdate(allFriends)); //TEST flytta till vettigt ställe
    }


















    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        logInDialog();



    }

    public static void logInDialog() {
        String username = JOptionPane.showInputDialog(null, "Ange användarnamn");

        if (username == null || username.isEmpty()){
            JOptionPane.showMessageDialog(null,"Felaktigt användarnamn");
            logInDialog();

        }
        else {
            new ControllerKlient(username);
        }
    }





}

