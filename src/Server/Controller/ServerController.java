package Server.Controller;

import Server.Boundary.*;
import Server.Model.*;
import Server.Boundary.ServerMainFrame;
import SharedModel.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Huvudsakliga controllern till servern.
 *
 * @author Ola Persson och Jonatan Tempel
 */
public class ServerController {
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
        setTimer();
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
        timer.scheduleAtFixedRate(timerToResetLogin, 0, 60000);
    }

    public boolean registrationRequest(RegistrationRequest registrationRequest){
        return allUsers.checkIfExists(registrationRequest);
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
            ClientConnection connectionForUser = connectedClients.getConnectionForUser(user);
            connectionForUser.sendObject(new ContactListUpdate(createContactList(user)));
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

    public User getRealUser(User user) {
        return allUsers.getRealUser(user);
    }

    public void registerNewUser(User user) {
        user.setUserStatus(new UserStatus(false,null,null));
        allUsers.put(user);
    }

    public void requestToFollow(FollowRequest followRequest, User user) {
        if (!checkUserExists(followRequest.getUserToBeFollowd())){
            connectedClients.getConnectionForUser(user).sendObject(new Message("Användaren finns ej"));
        }
        if (checkUserExists(followRequest.getUserToBeFollowd())){
            User toBeFollowd = getRealUser(followRequest.getUserToBeFollowd());
            followRequest.setUserToBeFollowd(toBeFollowd);
            if (connectedClients.isUserConnected(followRequest.getUserToBeFollowd())){
                connectedClients.getConnectionForUser(followRequest.getUserToBeFollowd()).sendObject(followRequest);
            }
            else {
                savedOutgoingObject.saveObj(followRequest.getUserToBeFollowd(), followRequest);
            }
            log("Följförfrågan skickad: Från: " + followRequest.getWantsToFollow().getUsername() + " till " + followRequest.getUserToBeFollowd().getUsername());
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
    public void okToFollow(OkFollowRequest okFollowRequest){
        User follower = getRealUser(okFollowRequest.getFollowRequest().getWantsToFollow());
        User toBeFollowd = getRealUser(okFollowRequest.getFollowRequest().getUserToBeFollowd());
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

    public boolean okLengthUsernameAndPassword(RegistrationRequest registrationRequest) {
        if (registrationRequest.getUserName().length() >= 3 && registrationRequest.getPassword().length() >= 3){
            return true;
        }
        else{
            return false;
        }
    }

    public ArrayList<User> getAllUsers() {
        return allUsers.getAllUsers();
    }

    public ArrayList<User> getConnectedUsers() {
        return connectedClients.getListOfConnected();
    }

    public void removeFriend(RemoveFriend removeObj, User user) {
        User userToRemove = removeObj.getUserToRemove();
        contactList.removeFriend(userToRemove, user);
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
