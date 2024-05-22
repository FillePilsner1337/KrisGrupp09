package Server.Model;

import Server.Controller.ServerController;
import SharedModel.User;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Klassen sparar ner objekt som ska skickas till enUser nästa gång den kopplar upp sig till server.
 */

public class SavedOutgoingObject {
    private ServerController serverController;
    private ConcurrentHashMap <User, ArrayList<Object>> objMap;
    boolean fileLoaded = false;

    public SavedOutgoingObject(ServerController serverController) {
        this.serverController = serverController;
        this.objMap = new ConcurrentHashMap<>();
        saveOrLoad();
       //saveFile();
    }
    private void saveFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("files/savedObjToSend.dat"))) {
            oos.writeObject(objMap);
            oos.flush();
        } catch (IOException e) {
            System.out.println("Kunde inte spara fil");
        }
    }

    public void saveOrLoad(){
        new loadOrSave().start();
    }

    public void saveObj(User user, Object object) {
        if (!objMap.containsKey(user)){
            objMap.put(user, new ArrayList<>());
        }
        objMap.get(user).add(object);

    }

    public ArrayList<Object> getObjToSend(User user){
        if (!objMap.containsKey(user)){
            objMap.put(user, new ArrayList<>());
        }
        ArrayList<Object> list = objMap.get(user);
        return list;
    }

    public void clearUserObjectList(User user) {
        if (!objMap.containsKey(user)){
            objMap.put(user, new ArrayList<>());
        }
        objMap.get(user).clear();
    }

    private class loadOrSave extends Thread {
        @Override
        public void run() {
            if (!fileLoaded) {
                loadFile();
            } else {
                saveFile();
            }
        }

        private void saveFile() {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("files/savedObjToSend.dat"))) {
                oos.writeObject(objMap);
                oos.flush();
            } catch (IOException e) {
                System.out.println("Kunde inte spara fil");
            }
        }

        private void loadFile() {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("files/savedObjToSend.dat"))) {
                objMap = (ConcurrentHashMap<User, ArrayList<Object>>) ois.readObject();
                fileLoaded = true;
                serverController.log("Fil inläst savedObjToSend.dat");
            } catch (IOException e) {
                if (e instanceof EOFException) {
                    System.out.println("ObToSend: Ingen mer fil att läsa");
                } else {
                    System.out.println("ObToSend: Kunde inte ladda fil IOException");
                    e.printStackTrace();
                }
            } catch (ClassNotFoundException e) {
                System.out.println("Kunde inte ladda fil ClassNotFoundException ObToSend");
            }
        }
    }

    public boolean isFileLoaded() {
        return fileLoaded;
    }
}
