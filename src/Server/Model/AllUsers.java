package Server.Model;

import Server.Controller.ControllerServer;

import java.util.ArrayList;
import java.util.Date;

public class AllUsers {

    private ArrayList<User> allUsers;
    private ControllerServer controllerServer;

    public AllUsers(ControllerServer controllerServer) {
        this.allUsers = new ArrayList<>();
        this.controllerServer = controllerServer;
        fillWithDummyData();
    }

    private void fillWithDummyData() {
        for (int i = 0; i < 10; i++){
            User user = new User("Test" + i);
            user.setInUtStatus(new InUtStatus(false,null,null));
            allUsers.add(user);
        }
        for (int i = 5; i < allUsers.size(); i++){
            allUsers.get(i).getInUtStatus().setIncheckad(true);
            allUsers.get(i).getInUtStatus().setId("175439-5");
            allUsers.get(i).getInUtStatus().setTid(new Date());
        }
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
        for (int i = 0; i < allUsers.size(); i++) {
            if (user.equals(allUsers.get(i))) {
                allUsers.get(i).setInUtStatus(status);
                System.out.println("uppdaterat status Update Status i All users update status  ");
            }
        }
        controllerServer.allContactUpdatesToAll();
    }

    public ArrayList<User> getAllUsers() {
        return allUsers;
    }
}
