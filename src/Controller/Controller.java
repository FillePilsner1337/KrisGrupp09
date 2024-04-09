package Controller;

import Boundary.*;
import Model.*;

public class Controller  {
    private final int port = 10000;
    private NewClientConnection newClientConnection;

    private ConnectedClients connectedClients;
    
    private ServerInputHandler serverInputHandler;




    public Controller() {
        this.connectedClients = new ConnectedClients();
        this.serverInputHandler = new ServerInputHandler(this);
        this.newClientConnection = new NewClientConnection(20000, this, serverInputHandler);
        newClientConnection.start();
        System.out.println("Controller startad");
        
        


        
    }


    
    public void userDisconnect(User user) {
        connectedClients.removeUser(user);
    }

    public void newLogIn(User user, Connection connection) {
        connectedClients.put(user, connection);
    }
}
