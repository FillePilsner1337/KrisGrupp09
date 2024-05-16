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
        //saveFile();
    }
    private void saveFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("files/savedAllUsers.dat"))) {
            oos.writeObject(allUsers);
            oos.flush();
        } catch (IOException e) {
            System.out.println("Kunde inte spara fil");
        }
    }
    public void printAllUsers(){
           for (int i = 0; i < allUsers.size(); i++){
            System.out.println(allUsers.get(i).toString());
        }
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
                 break;
            }
        }
        controllerServer.allContactUpdatesToAll();
        controllerServer.updateGuiAllUsers(allUsers);
        controllerServer.log("Ny status: " + user.getUserName() + " " + user.getInUtStatus().toString());
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
        return null;
    }

    public User getRealUser(User u) {
        for (int i = 0; i< allUsers.size(); i++){
            if (u.equals(allUsers.get(i))){
                return allUsers.get(i);
            }
        }
        return null;
    }

    public boolean isFileLoaded() {
        return fileLoaded;
    }

    public boolean checkIfExists(RegReq r) {
        for (int i = 0; i < allUsers.size(); i++) {
            if (allUsers.get(i).getUserName().equals(r.getUserName())){
                return true;
            }
        }
        return false;
    }

    public void autoCheckout() {
        for (int i = 0; i < allUsers.size(); i++) {
            Date checkInTime = new Date();
            Date currentTime = new Date();
            if (allUsers.get(i).getInUtStatus().getTid() != null) {
                checkInTime = allUsers.get(i).getInUtStatus().getTid();
            }
            if (allUsers.get(i).getInUtStatus().getTid() != null && checkInTime.getTime() + (1*300*1000) > currentTime.getTime()){
                allUsers.get(i).setInUtStatus(new InUtStatus(false, null,null));
                controllerServer.sendMessageToUser(allUsers.get(i), "Du har blivit automatiskt utloggad från skyddsrummet");
                controllerServer.getConnectedClients().getConnectionForUser(allUsers.get(i)).sendObject(allUsers.get(i));
                controllerServer.log("Användare automatiskt utcheckad: " + allUsers.get(i).getUserName());
            }
        }
        controllerServer.allContactUpdatesToAll();
        controllerServer.updateGuiAllUsers(allUsers);
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
                controllerServer.log("Fil inläst savedAllUsers.dat: ");
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
