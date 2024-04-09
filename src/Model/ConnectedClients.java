package Model;

import Boundary.Connection;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;


public class ConnectedClients {
    private ConcurrentHashMap<User, Connection> connectedList;

    public ConnectedClients() {
        this.connectedList = new ConcurrentHashMap<>();
    }


    public synchronized void put(User user, Connection connection) {
        if (!connectedList.containsKey(user)) {
            connectedList.put(user, connection);
            System.out.println("Användare tillagd i hashMap ConnectedClients");
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


    }

}
