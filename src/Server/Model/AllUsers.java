package Server.Model;

import Server.Controller.ServerController;
import SharedModel.UserStatus;
import SharedModel.RegistrationRequest;
import SharedModel.User;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * Klassen håller alla användares User objekt. Laddar fil från HD när servern startas.
 * Sparar varje gång en ny användare registrerar sig.
 *
 * @author Ola Persson och Jonatan Tempel
 */
public class AllUsers {
    private ArrayList<User> allUsers;
    private ServerController serverController;
    private boolean fileLoaded = false;
    public AllUsers (ServerController serverController) {
        this.allUsers = new ArrayList<>();
        this.serverController = serverController;
        //saveOrLoad();
        saveFile();
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
    public void saveOrLoad(){
        new loadOrSave().start();
    }
    public synchronized void put(User user){
        if (!allUsers.contains(user)){
            allUsers.add(user);
            saveOrLoad();
        }
        else{
            for (int i = 0; i < allUsers.size(); i++){
                if (user.equals(allUsers.get(i))){
                    user = allUsers.get(i);
                    break;
                }
            }
            serverController.sendSelfUpdate(user);
        }
    }

    public void updateStatus(UserStatus status, User user) {
        for (int i = 0; i < allUsers.size(); i++) {
            if (user.equals(allUsers.get(i))) {
                allUsers.get(i).setUserStatus(status);
                break;
            }
        }
        serverController.allContactUpdatesToAll();
        serverController.updateGuiAllUsers(allUsers);
        serverController.log("Ny status: " + user.getUsername() + " " + user.getUserStatus().toString());
    }

    public ArrayList<User> getAllUsers() {
        return allUsers;
    }

    public UserStatus getStatusForUser(User user) {
        for (int i = 0; i < allUsers.size() ; i++) {
            if (user.equals(allUsers.get(i))){
                return allUsers.get(i).getUserStatus();
            }
        }
        return null;
    }

    public User getRealUser(User user) {
        for (int i = 0; i< allUsers.size(); i++){
            if (user.equals(allUsers.get(i))){
                return allUsers.get(i);
            }
        }
        return null;
    }

    public boolean isFileLoaded() {
        return fileLoaded;
    }

    public boolean checkIfExists(RegistrationRequest registrationRequest) {
        for (int i = 0; i < allUsers.size(); i++) {
            if (allUsers.get(i).getUsername().equals(registrationRequest.getUserName())){
                return true;
            }
        }
        return false;
    }

    public void autoCheckout() {
        for (int i = 0; i < allUsers.size(); i++) {
            Date checkInTime = new Date();
            Date currentTime = new Date();
            if (allUsers.get(i).getUserStatus().getTime() != null) {
                checkInTime = allUsers.get(i).getUserStatus().getTime();
            }
            if (allUsers.get(i).getUserStatus().getTime() != null && checkInTime.getTime() + (1*300*1000) > currentTime.getTime()){
                allUsers.get(i).setUserStatus(new UserStatus(false, null,null));
                serverController.sendMessageToUser(allUsers.get(i), "Du har blivit automatiskt utloggad från skyddsrummet");
                serverController.getConnectedClients().getConnectionForUser(allUsers.get(i)).sendObject(allUsers.get(i));
                serverController.log("Användare automatiskt utcheckad: " + allUsers.get(i).getUsername());
            }
        }
        serverController.allContactUpdatesToAll();
        serverController.updateGuiAllUsers(allUsers);
    }

    public class loadOrSave extends Thread {
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
                serverController.log("Fil inläst savedAllUsers.dat: ");
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
