package Server.Model;

import Server.Boundary.Connection;
import Server.Controller.ControllerServer;
import SharedModel.User;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;


public class ConnectedClients {
    private ConcurrentHashMap<User, Connection> connectedList;
    private ControllerServer controllerServer;

    public ConnectedClients(ControllerServer controllerServer) {
        this.connectedList = new ConcurrentHashMap<>();
        this.controllerServer = controllerServer;

    }

    public synchronized void put(User user, Connection connection) {
        if (!connectedList.containsKey(user)) {
            connectedList.put(user, connection);
            controllerServer.updateGuiConnectedUsers(getListOfConnected());

        }
    }

    public boolean isUserConnected(User user) {

        return connectedList.containsKey(user);
    }

    public Connection getConnectionForUser(User user) {
        return connectedList.get(user);
    }

    public ArrayList<User> getListOfConnected() {
        ArrayList<User> listConnectedClients = new ArrayList<>();
        for (User user : connectedList.keySet()){
            listConnectedClients.add(user);
        }
        return listConnectedClients;
    }

    public ArrayList<Connection> getListOfAllConnection() {
        ArrayList<Connection> allC = new ArrayList<>();

        for (Connection connection : connectedList.values()){
            allC.add(connection);
        }
        return allC;
    }


    public void removeUser(User user){
        connectedList.remove(user);
        controllerServer.updateGuiConnectedUsers(getListOfConnected());
        controllerServer.log("Klient nerkopplad: " + user.getUserName());

    }
}
