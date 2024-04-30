package Server.Model;

import Server.Controller.ControllerServer;
import SharedModel.InUtStatus;
import SharedModel.RegReq;
import SharedModel.User;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class AllUsers {

    private ArrayList<User> allUsers;
    private ControllerServer controllerServer;

    private boolean fileLoaded = false;
    public AllUsers (ControllerServer controllerServer) {
        this.allUsers = new ArrayList<>();
        this.controllerServer = controllerServer;
        saveLoad();

    }
    public void saveLoad(){
        new loadSave().start();
    }


    public synchronized void put(User user){
        if (!allUsers.contains(user)){
            allUsers.add(user);
            saveLoad();
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

    public InUtStatus getStatusForUser(User user) {
        for (int i = 0; i < allUsers.size() ; i++) {
            if (user.equals(allUsers.get(i))){
                return allUsers.get(i).getInUtStatus();
            }
        }
        return new InUtStatus(false, null,null);
    }

    public User getRealUser(User u) {
        for (int i = 0; i< allUsers.size(); i++){
            if (u.equals(allUsers.get(i))){
                return allUsers.get(i);
            }
        }
        return null;
    }

    public boolean checkIfExists(RegReq r) {
        for (int i = 0; i < allUsers.size(); i++) {
            if (allUsers.get(i).getUserName().equals(r.getUserName())){
                System.out.println("Return True i checkIfExists ");
                return true;

            }

        }
        System.out.println("Return false i checkIfExists ");
        return false;
    }
    public class loadSave extends Thread {
        @Override
        public void run() {
            if (!fileLoaded) {
                loadFile();
            } else {
                saveFile();
            }
        }

        private void saveFile() {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("files/savedAllUsers.dat"))) {
                oos.writeObject(allUsers);
                oos.flush();

            } catch (IOException e) {
                System.out.println("Kunde inte spara fil");
            }
        }

        private void loadFile() {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("files/savedAllUsers.dat"))) {
                allUsers = (ArrayList<User>) ois.readObject();
                fileLoaded = true;

            } catch (IOException e) {
                if (e instanceof EOFException) {
                    System.out.println("AllUsers: Ingen mer fil att läsa");
                } else {
                    System.out.println("AllUsers: Kunde inte ladda fil IOException");
                    e.printStackTrace();
                }
            } catch (ClassNotFoundException e) {
                System.out.println("Kunde inte ladda fil ClassNotFoundException");
            }
        }
    }
}
