package Server.Model;

import Server.Controller.ControllerServer;
import SharedModel.User;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class SavedOutgoingObj {
    private ControllerServer controllerServer;

    private ConcurrentHashMap <User, ArrayList<Object>> objMap;
    boolean fileLoaded = false;
    public SavedOutgoingObj(ControllerServer controllerServer) {
        this.controllerServer = controllerServer;
        this.objMap = new ConcurrentHashMap<>();
        saveLoad();

    }
    public void saveLoad(){
        new loadSave().start();
    }

    public void saveObj(User user, Object o) {
        if (!objMap.containsKey(user)){
            objMap.put(user, new ArrayList<>());
        }
        objMap.get(user).add(o);
    }

    public ArrayList<Object> getObjToSend(User user){
        ArrayList<Object> list = objMap.get(user);
        objMap.get(user).clear();
        return list;
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
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("files/savedObjToSend.dat"))) {
                oos.writeObject(objMap);
                oos.flush();
                System.out.println("fil sparad");

            } catch (IOException e) {
                System.out.println("Kunde inte spara fil");
            }
        }

        private void loadFile() {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("files/savedObjToSend.dat"))) {
                objMap = (ConcurrentHashMap<User, ArrayList<Object>>) ois.readObject();
                fileLoaded = true;
                System.out.println("ObToSend fil laddad");

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


}
