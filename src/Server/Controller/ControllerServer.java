package Server.Controller;

import Server.Boundary.*;

import Server.Model.*;
import SharedModel.*;

import java.util.ArrayList;


public class ControllerServer {
    private final int port = 10000;
    private NewClientConnection newClientConnection;

    private ConnectedClients connectedClients;
    
    private ServerInputHandler serverInputHandler;

    private AllUsers allUsers;
    private ContactList contactList;

    public ControllerServer() {
        this.connectedClients = new ConnectedClients();
        this.serverInputHandler = new ServerInputHandler(this);
        this.newClientConnection = new NewClientConnection(20000, this, serverInputHandler);
        this.allUsers = new AllUsers(this);
        this.contactList = new ContactList(this);
        newClientConnection.start();
        System.out.println("Controller startad");
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
           //Byt denna koden mot att skicka ut till användare. Nu kan alla lägga till alla
           contactList.addContact(user, getRealUser(req.getPersonToBeFollowd()));
          connectedClients.getConnectionForUser(user).sendObject(new Message("Användare tillagd"));
           connectedClients.getConnectionForUser(user).sendObject(new ContactListUpdate(contactList.getContactlist(user)));
       }


    }
}
