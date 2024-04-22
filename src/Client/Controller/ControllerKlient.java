package Client.Controller;

import Server.Model.ContactListUpdate;
import Server.Model.InUtStatus;
import Server.Model.Message;
import Server.Model.User;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import Client.View.*;

public class ControllerKlient {

    private MainFrame mainFrame;
    private KartaController kc;
    private ServerConnection serverConnection;
    private ArrayList<User> allFriends = new ArrayList<>();


    private User user;
    public ControllerKlient(String username){
        this.user = new User(username);
        user.setInUtStatus(new InUtStatus(false,null,null));
        this.mainFrame = new MainFrame();
        this.serverConnection = new ServerConnection(user, this);
        this.kc = new KartaController(mainFrame, this);
    }

    public void recivedObject(Object o){
        if (o instanceof User){
            user = (User)o;
            System.out.println(user.getInUtStatus().toString());
        }
         if (o instanceof ContactListUpdate){
             System.out.println("Tog emot ContactListUpdate ");
            this.allFriends = ((ContactListUpdate) o).getList();
             System.out.println(allFriends.get(10).getInUtStatus().isIncheckad());

            if (!allFriends.isEmpty()) {
                System.out.println(allFriends.get(0).getUserName());
            }
            updateLists();
        }
        if (o instanceof Message){
            String msg = ((Message) o).getMsg();
            JOptionPane.showMessageDialog(null,msg);
        }
    }

    private void updateLists() {

        String[] allaSomArray = new String[allFriends.size()];
        for (int i = 0; i < allFriends.size(); i++){
            if (!allFriends.get(i).equals(user) ){
            allaSomArray[i] = allFriends.get(i).getUserName();
            }
        }
        mainFrame.getCheckInPanel().getListAllaVanner().setListData(allaSomArray);


        String[] allaincheckade = new String[allFriends.size()];
        for (int i = 0; i < allFriends.size(); i++){
            if (!allFriends.get(i).equals(user)) {
                if (allFriends.get(i).getInUtStatus().isIncheckad() == true) {
            allaincheckade[i] = allFriends.get(i).getUserName();
                }
            }
        }
        mainFrame.getCheckInPanel().getListIncheckadeVanner().setListData(allaincheckade);
        mainFrame.getCheckInPanel().repaint();
        mainFrame.getCheckInPanel().revalidate();
       // mainFrame.getCheckInPanel().getListIncheckadeVanner().setListData(allaincheckade);
    }



    public void checkIn(String id) {
        mainFrame.getCheckInPanel().getCheckaUt().setEnabled(true);
        InUtStatus status = new InUtStatus(true, id, new Date());
        user.setInUtStatus(status);
        serverConnection.sendObject(status);
        mainFrame.getCheckInPanel().getCheckaUt().addActionListener(new CheckoutActionListener(this));
    }

    public MainFrame getMainFrame(){
        return mainFrame;
    }

    public User getUser(){
        return user;
    }

    public ServerConnection getServerConnection(){
        return serverConnection;
    }

    public ArrayList<User> getAllFriends(){
        return allFriends;
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

