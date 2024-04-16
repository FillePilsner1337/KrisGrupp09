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



    //samma här, med stor bokstav
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
            User user = connectedClients.getListOfConnected().get(i);
            System.out.println("1");
           Connection c = connectedClients.getConnectionForUser(user); //denna rad ska ett mellanslag in
            System.out.println("2");
            c.sendObject(new ContactListUpdate(contactList.getContactlist(user)));
            //c.sendObject(contactList.getContactlist(user));

            System.out.println(user.getUserName());

        }

    }
    public void sendSelfUpdate(User user) {
        connectedClients.getConnectionForUser(user).sendObject(user);
                                                            //vissa metoder som denna har en blankrad och vissa inte i slutet, förslagsvis bör vi ha ingen?
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
