package Server.Controller;

import Server.Boundary.*;

import Server.Model.*;
import Server.Boundary.ServerMainFrame;
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
    private ServerMainFrame serverMainFrame;

    public ControllerServer() {
        this.serverMainFrame = new ServerMainFrame(this);
        this.connectedClients = new ConnectedClients(this);
        this.serverInputHandler = new ServerInputHandler(this);
        this.newClientConnection = new NewClientConnection(20000, this, serverInputHandler);
        this.allUsers = new AllUsers(this);
        this.contactList = new ContactList(this);
        this.savedOutgoingObj = new SavedOutgoingObj(this);
        newClientConnection.start();
        loadServerGuiData();

        //setTimer();
    }

    private void loadServerGuiData() {
        // Det här är inte bra. För testning
        boolean filesNotLoaded = true;
        while (filesNotLoaded){
              if (contactList.isFileLoaded() && savedOutgoingObj.isFileLoaded() && allUsers.isFileLoaded()){
                  serverMainFrame.loadGUIdata();
                  filesNotLoaded = false;
              }

        }


        serverMainFrame.loadGUIdata();
    }

    public ArrayList<User> createContactList(User user){
        ArrayList <User> listToSend = new ArrayList<>();
        ArrayList <User> listToReadFrom = contactList.getContactlist(user);
        for (int i = 0; i < listToReadFrom.size(); i++){
            listToSend.add(getRealUser(listToReadFrom.get(i)));
            InUtStatus status = getRealStatus(listToReadFrom.get(i));
            listToSend.get(i).setInUtStatus(status);
        }
        return listToSend;
    }

    private InUtStatus getRealStatus(User user) {
        return allUsers.getStatusForUser(user);
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
        connectedClients.getConnectionForUser(user).sendObject(new ContactListUpdate(createContactList(user)));
        checkForOldObjToSend(user);
    }

    public void allContactUpdatesToAll(){
        for (int i = 0; i < connectedClients.getListOfConnected().size(); i++){
            User user = connectedClients.getListOfConnected().get(i);
            Connection c = connectedClients.getConnectionForUser(user);
            c.sendObject(new ContactListUpdate(createContactList(user)));
        }
        log("Uppdatering skickad till alla användare ");
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

    public boolean checkUserExists(User user) {
        ArrayList<User> list = allUsers.getAllUsers();
        for (int i = 0; i< list.size(); i++){
            if (list.get(i).equals(user)){
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
           log("Följförfrågan skickad: Från: " + req.getWantsToFollow().getUserName() + " till " + req.getPersonToBeFollowd().getUserName());
       }
    }

    public void checkForOldObjToSend(User user){
        if (!savedOutgoingObj.getObjToSend(user).isEmpty()){
            ArrayList<Object> list = savedOutgoingObj.getObjToSend(user);
            for (int i = 0; i < list.size(); i++) {
                connectedClients.getConnectionForUser(user).sendObject(list.get(i));
            }
        }
        savedOutgoingObj.clearUserObjectList(user);
        log("Eventuella sparade meddelanden skickat till: " + user.getUserName());
    }
    public void okToFollow(OkFollowReg ok){
        User follower = getRealUser(ok.getFollowReq().getWantsToFollow());
        User toBeFollowd = getRealUser(ok.getFollowReq().getPersonToBeFollowd());
        contactList.addContact(follower,toBeFollowd);
        if (connectedClients.isUserConnected(follower)) {
            connectedClients.getConnectionForUser(follower).sendObject(new Message(toBeFollowd.getUserName() + " har godkänt din följförfrågning"));
        }
        else {
            savedOutgoingObj.saveObj(follower, new  Message(toBeFollowd.getUserName() + " har godkänt din följförfrågning" ));
        }
        log("Godkänd följförfrågan: " + toBeFollowd.getUserName() + " har godkänt " + follower.getUserName() + "s förfrågan");
        allContactUpdatesToAll();
    }

    public boolean okLengthUsernameAndPassword(RegReq o) {
        if (o.getUserName().length() >= 3 && o.getPassword().length() >= 3){
            return true;
        }
        else{
            return false;
        }
    }

    public void printAllUsers() {
        allUsers.printAllUsers();
    }

    public ArrayList<User> getAllUsers() {
        return allUsers.getAllUsers();
    }

    public ArrayList<User> getConnectedUsers() {
        return connectedClients.getListOfConnected();
    }

    public class TimerToResetLogin extends TimerTask {
        @Override
        public void run() {
            allUsers.autoCheckout();
        }
    }

    public ConnectedClients getConnectedClients() {
        return connectedClients;
    }

  public void updateGuiConnectedUsers(ArrayList<User> connectedUsers){
        serverMainFrame.updateGuiConnectedUsers(connectedUsers);
    }
    public void updateGuiAllUsers(ArrayList<User> allUsers){
        serverMainFrame.updateGuiAllUsers(allUsers);
    }

    public void log(String string){
        serverMainFrame.log(string);
    }

}
