package Server.Controller;

import Server.Boundary.*;

import Server.Model.*;
import SharedModel.*;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class ControllerServer {
    private final int port = 10000;
    private NewClientConnection newClientConnection;

    private ConnectedClients connectedClients;
    
    private ServerInputHandler serverInputHandler;

    private AllUsers allUsers;
    private ContactList contactList;
    private SavedOutgoingObj savedOutgoingObj;

    private TimerTask timer;

    public ControllerServer() {
        this.connectedClients = new ConnectedClients();
        this.serverInputHandler = new ServerInputHandler(this);
        this.newClientConnection = new NewClientConnection(20000, this, serverInputHandler);
        this.allUsers = new AllUsers(this);
        this.contactList = new ContactList(this);
        this.savedOutgoingObj = new SavedOutgoingObj(this);
        newClientConnection.start();
        setTimer();
        System.out.println("Controller startad");

    }
    public void setTimer(){
        Timer timer = new Timer();
        TimerToResetLogin timerToResetLogin = new TimerToResetLogin();
        timer.scheduleAtFixedRate(timerToResetLogin, 0, 10000);

    }
    public boolean registrationRequest(RegReq r){
        return allUsers.checkIfExists(r);

    }
    public void userDisconnect(User user) {
        connectedClients.removeUser(user);
    }

    public void newLogIn(User user, Connection connection) {
        connectedClients.put(user, connection);
        connectedClients.getConnectionForUser(user).sendObject(new ContactListUpdate(contactList.getContactlist(user)));
        System.out.println("new LogIn metoden");
        checkForOldObjToSend(user);
    }

    public void allContactUpdatesToAll(){
        System.out.println("allContactUpdatesToAll");

        for (int i = 0; i < connectedClients.getListOfConnected().size(); i++){
            User user = connectedClients.getListOfConnected().get(i);
            System.out.println(user.getInUtStatus().isIncheckad());

            Connection c = connectedClients.getConnectionForUser(user);

            c.sendObject(new ContactListUpdate(contactList.getContactlist(user)));

        }
    }
    public void sendSelfUpdate(User user) {
        connectedClients.getConnectionForUser(user).sendObject(user);
    }

    public void sendMessageToUser(User user, String msg){
        connectedClients.getConnectionForUser(user).sendObject(new Message(msg));
    }

    public void changeStatus(InUtStatus status, User user) {
        allUsers.updateStatus(status, user);




    }

    public void updateContactlistFromUser(ContactListUpdate update, User user) {
      //  contactList.put(user, update.getList());
        //Metoden ska int användas
    }

    public boolean checkUserExists(User user) {
        ArrayList<User> list = allUsers.getAllUsers();
        for (int i = 0; i< list.size(); i++){
            if (list.get(i).equals(user))
            {
                return true;
            }
        }
        return false;
    }

    public boolean checkPassword(User user) {
        ArrayList<User> list = allUsers.getAllUsers();
        for (int i = 0; i< list.size(); i++){
            if (list.get(i).equals(user) && list.get(i).getPassword().equals(user.getPassword())){
                return true;
            }
        }
        return false;
    }

    public InUtStatus getStatusForUser(User user) {
        return allUsers.getStatusForUser(user);
    }

    public User getRealUser(User u) {
        return allUsers.getRealUser(u);
    }

    public void registerNewUser(User user) {
        User u = user;
        u.setInUtStatus(new InUtStatus(false,null,null));
        allUsers.put(u);
    }

    public void reqToFollow(FollowReq req, User user) {
       if (!checkUserExists(req.getPersonToBeFollowd())){
           connectedClients.getConnectionForUser(user).sendObject(new Message("Användaren finns ej"));
       }
       if (checkUserExists(req.getPersonToBeFollowd())){

           User toBeFollowd = getRealUser(req.getPersonToBeFollowd());
           req.setPersonToBeFollowd(toBeFollowd);

           if (connectedClients.isUserConnected(req.getPersonToBeFollowd())){
               connectedClients.getConnectionForUser(req.getPersonToBeFollowd()).sendObject(req);

           }
           else {
                savedOutgoingObj.saveObj(req.getPersonToBeFollowd(), req);

            }

       }


    }

    public void checkForOldObjToSend(User user){

        if (!savedOutgoingObj.getObjToSend(user).isEmpty()){
            System.out.println("inne if sats checkmetod");
            ArrayList<Object> list = savedOutgoingObj.getObjToSend(user);
            for (int i = 0; i < list.size(); i++) {
                connectedClients.getConnectionForUser(user).sendObject(list.get(i));
            }
        }
        savedOutgoingObj.clearUserObjectList(user);


    }
    public void okToFollow(OkFollowReg ok){
        User follower = getRealUser(ok.getFollowReq().getWantsToFollow());
        User toBeFollowd = getRealUser(ok.getFollowReq().getPersonToBeFollowd());
        contactList.addContact(follower,toBeFollowd);
        if (connectedClients.isUserConnected(follower)) {
            connectedClients.getConnectionForUser(follower).sendObject(new Message(toBeFollowd.getUserName() + " har godkänt din följförfrågning"));
            connectedClients.getConnectionForUser(follower).sendObject(new ContactListUpdate(contactList.getContactlist(follower)));
        }
        else {
            savedOutgoingObj.saveObj(follower, new  Message(toBeFollowd.getUserName() + " har godkänt din följförfrågning" ));
        }

    }

    public boolean okLengthUsernameAndPassword(RegReq o) {
        if (o.getUserName().length() >= 3 && o.getPassword().length() >= 3){
            return true;
        }
        else{
            return false;
        }
    }

    public class TimerToResetLogin extends TimerTask {




        @Override
        public void run() {
            allUsers.autoCheckout();

        }
    }
}
