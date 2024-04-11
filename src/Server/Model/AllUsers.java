package Server.Model;

import Server.Controller.ControllerServer;

import java.util.ArrayList;

public class AllUsers {

    private ArrayList<User> allUsers;
    private ControllerServer controllerServer;

    public AllUsers(ControllerServer controllerServer) {
        this.allUsers = new ArrayList<>();
        this.controllerServer = controllerServer;
    }

    public synchronized void put(User user){
        if (!allUsers.contains(user)){
            allUsers.add(user);
        }
        else{
            for (int i = 0; i < allUsers.size(); i++){
                if (user.equals(allUsers.get(i))){
                    user = allUsers.get(i);
                    break;
                }

            }

            controllerServer.sendSelfUpdate(user);
        }
    }

    public void updateStatus(InUtStatus status, User user) {
        for (User allUser : allUsers) {
            if (user.equals(allUser)) {
                allUser.setInUtStatus(status);
                System.out.println("uppdaterat status ");
            }

        }
        controllerServer.allContactUpdatesToAll();

    }
}
