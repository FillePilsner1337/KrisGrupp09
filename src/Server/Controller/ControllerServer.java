package Server.Controller;

import Server.Boundary.*;

import Server.Model.*;

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
        this.contactList = new ContactList(this, allUsers);

        newClientConnection.start();
        System.out.println("Controller startad");
        
        


        
    }


    
    public void userDisconnect(User user) {
        connectedClients.removeUser(user);
    }

    public void newLogIn(User user, Connection connection) {
        connectedClients.put(user, connection);
        allUsers.put(user);
        connectedClients.getConnectionForUser(user).sendObject(new ContactListUpdate(contactList.getContactlist(user)));
    }

    public void allContactUpdatesToAll(){

        for (int i = 0; i < connectedClients.getListOfConnected().size(); i++){
            connectedClients.getConnectionForUser(connectedClients.getListOfConnected().get(i)).sendObject
                    (new ContactListUpdate(contactList.getContactlist(connectedClients.getListOfConnected().get(i))));

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
        System.out.println("changeStatus i serverController");
    }

    public void updateContactlistFromUser(ContactListUpdate update, User user) {
        contactList.put(user, update.getList());
    }
}
