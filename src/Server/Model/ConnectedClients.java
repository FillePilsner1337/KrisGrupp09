package Server.Model;

import Server.Boundary.ClientConnection;
import Server.Controller.ServerController;
import SharedModel.User;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;


public class ConnectedClients {
    private ConcurrentHashMap<User, ClientConnection> connectedList;
    private ServerController serverController;

    public ConnectedClients(ServerController serverController) {
        this.connectedList = new ConcurrentHashMap<>();
        this.serverController = serverController;

    }

    public synchronized void put(User user, ClientConnection clientConnection) {
        if (!connectedList.containsKey(user)) {
            connectedList.put(user, clientConnection);
            serverController.updateGuiConnectedUsers(getListOfConnected());

        }
    }

    public boolean isUserConnected(User user) {

        return connectedList.containsKey(user);
    }

    public ClientConnection getConnectionForUser(User user) {
        return connectedList.get(user);
    }

    public ArrayList<User> getListOfConnected() {
        ArrayList<User> listConnectedClients = new ArrayList<>();
        for (User user : connectedList.keySet()){
            listConnectedClients.add(user);
        }
        return listConnectedClients;
    }

    public ArrayList<ClientConnection> getListOfAllConnections() {
        ArrayList<ClientConnection> allC = new ArrayList<>();

        for (ClientConnection clientConnection : connectedList.values()){
            allC.add(clientConnection);
        }
        return allC;
    }


    public void removeUser(User user){
        connectedList.remove(user);
        serverController.updateGuiConnectedUsers(getListOfConnected());
        serverController.log("Klient nerkopplad: " + user.getUsername());

    }
}
