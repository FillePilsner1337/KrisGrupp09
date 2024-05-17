package Server.Controller;

import Server.Boundary.*;

import Server.Model.*;
import Server.Boundary.ServerMainFrame;
import SharedModel.*;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class ServerController {
    private final int port = 10000;
    private NewClientConnection newClientConnection;
    private ConnectedClients connectedClients;
    private ServerInputHandler serverInputHandler;
    private AllUsers allUsers;
    private ContactList contactList;
    private SavedOutgoingObject savedOutgoingObject;
    private TimerTask timer;
    private ServerMainFrame serverMainFrame;

    public ServerController() {
        this.serverMainFrame = new ServerMainFrame(this);
        this.connectedClients = new ConnectedClients(this);
        this.serverInputHandler = new ServerInputHandler(this);
        this.newClientConnection = new NewClientConnection(20000, this, serverInputHandler);
        this.allUsers = new AllUsers(this);
        this.contactList = new ContactList(this);
        this.savedOutgoingObject = new SavedOutgoingObject(this);
        newClientConnection.start();
        loadServerGuiData();

        //setTimer();
    }

    private void loadServerGuiData() {
        // Det här är inte bra. För testning
        boolean filesNotLoaded = true;
        while (filesNotLoaded){
              if (contactList.isFileLoaded() && savedOutgoingObject.isFileLoaded() && allUsers.isFileLoaded()){
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
            UserStatus status = getRealStatus(listToReadFrom.get(i));
            listToSend.get(i).setUserStatus(status);
        }
        return listToSend;
    }

    private UserStatus getRealStatus(User user) {
        return allUsers.getStatusForUser(user);
    }

    public void setTimer(){
        Timer timer = new Timer();
        TimerToResetLogin timerToResetLogin = new TimerToResetLogin();
        timer.scheduleAtFixedRate(timerToResetLogin, 0, 10000);
    }
    public boolean registrationRequest(RegistrationRequest r){
        return allUsers.checkIfExists(r);
    }
    public void userDisconnect(User user) {
        connectedClients.removeUser(user);
    }

    public void newLogIn(User user, ClientConnection clientConnection) {
        connectedClients.put(user, clientConnection);
        connectedClients.getConnectionForUser(user).sendObject(new ContactListUpdate(createContactList(user)));
        checkForOldObjToSend(user);
    }

    public void allContactUpdatesToAll(){
        for (int i = 0; i < connectedClients.getListOfConnected().size(); i++){
            User user = connectedClients.getListOfConnected().get(i);
            ClientConnection c = connectedClients.getConnectionForUser(user);
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

    public void changeStatus(UserStatus status, User user) {
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

    public UserStatus getStatusForUser(User user) {
        return allUsers.getStatusForUser(user);
    }

    public User getRealUser(User u) {
        return allUsers.getRealUser(u);
    }

    public void registerNewUser(User user) {
        User u = user;
        u.setUserStatus(new UserStatus(false,null,null));
        allUsers.put(u);
    }

    public void requestToFollow(FollowRequest req, User user) {
       if (!checkUserExists(req.getUserToBeFollowd())){
           connectedClients.getConnectionForUser(user).sendObject(new Message("Användaren finns ej"));
       }
       if (checkUserExists(req.getUserToBeFollowd())){
           User toBeFollowd = getRealUser(req.getUserToBeFollowd());
           req.setUserToBeFollowd(toBeFollowd);
           if (connectedClients.isUserConnected(req.getUserToBeFollowd())){
               connectedClients.getConnectionForUser(req.getUserToBeFollowd()).sendObject(req);
           }
           else {
                savedOutgoingObject.saveObj(req.getUserToBeFollowd(), req);
            }
           log("Följförfrågan skickad: Från: " + req.getWantsToFollow().getUsername() + " till " + req.getUserToBeFollowd().getUsername());
       }
    }

    public void checkForOldObjToSend(User user){
        if (!savedOutgoingObject.getObjToSend(user).isEmpty()){
            ArrayList<Object> list = savedOutgoingObject.getObjToSend(user);
            for (int i = 0; i < list.size(); i++) {
                connectedClients.getConnectionForUser(user).sendObject(list.get(i));
            }
        }
        savedOutgoingObject.clearUserObjectList(user);
        log("Eventuella sparade meddelanden skickat till: " + user.getUsername());
    }
    public void okToFollow(OkFollowRequest ok){
        User follower = getRealUser(ok.getFollowRequest().getWantsToFollow());
        User toBeFollowd = getRealUser(ok.getFollowRequest().getUserToBeFollowd());
        contactList.addContact(follower,toBeFollowd);
        if (connectedClients.isUserConnected(follower)) {
            connectedClients.getConnectionForUser(follower).sendObject(new Message(toBeFollowd.getUsername() + " har godkänt din följförfrågning"));
        }
        else {
            savedOutgoingObject.saveObj(follower, new  Message(toBeFollowd.getUsername() + " har godkänt din följförfrågning" ));
        }
        log("Godkänd följförfrågan: " + toBeFollowd.getUsername() + " har godkänt " + follower.getUsername() + "s förfrågan");
        allContactUpdatesToAll();
    }

    public boolean okLengthUsernameAndPassword(RegistrationRequest o) {
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
