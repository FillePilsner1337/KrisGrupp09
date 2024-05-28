package Server.Model;

import Server.Controller.ServerController;
import SharedModel.User;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Klassen håller varje Users individuella kontaktlista. Sparar och laddar till fil via inre klass loadSave
 * @author Ola Persson och Jonatan Tempel
 */

public class ContactList {
        private ConcurrentHashMap<User, ArrayList<User>> contactList;
        private ServerController serverController;
        private boolean fileLoaded = false;

        public ContactList(ServerController controller) {
            this.serverController = controller;
            this.contactList = new ConcurrentHashMap<>();
            saveLoad();
            //saveFile(); Behövs för att nolla datfiler
        }

    private void saveFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("files/savedContactLists.dat"))) {
            oos.writeObject(contactList);
            oos.flush();
            System.out.println("fil sparad");
        } catch (IOException e) {
            System.out.println("Kunde inte spara fil");
        }
    }

        public synchronized void addContact(User userKey, User userToAdd) {
            if (!contactList.containsKey(userKey)) {
                contactList.put(userKey, new ArrayList<>());
                serverController.log("Ny person på kontaktlista: " + userKey.getUsername() + " la till " + userToAdd.getUsername());
            }
                contactList.get(userKey).add(userToAdd);
                saveLoad();
                serverController.allContactUpdatesToAll();
        }

    private void saveLoad() {
            new loadSave().start();
    }

    public ArrayList<User> getContactlist(User user) {
            if (!contactList.containsKey(user)){
                contactList.put(user, new ArrayList<>());
            }
            return contactList.get(user);
        }

    public void removeFriend(User userToRemove, User user) {
            contactList.get(user).remove(userToRemove);
            serverController.allContactUpdatesToAll();
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
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("files/savedContactLists.dat"))) {
                oos.writeObject(contactList);
                oos.flush();
            } catch (IOException e) {
                System.out.println("Kunde inte spara fil");
            }
        }

        private void loadFile() {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("files/savedContactLists.dat"))) {
                contactList = (ConcurrentHashMap<User, ArrayList<User>>) ois.readObject();
                fileLoaded = true;
                serverController.log("Fil inläst savedContactLists.dat ");
            } catch (IOException e) {
                if (e instanceof EOFException) {
                    System.out.println("contactList: Ingen mer fil att läsa");
                } else {
                    System.out.println("contactList: Kunde inte ladda fil IOException");
                    e.printStackTrace();
                }
            } catch (ClassNotFoundException e) {
                System.out.println("Kunde inte ladda fil ClassNotFoundException");
            }
        }
    }

    public boolean isFileLoaded() {
        return fileLoaded;
    }
}
