package Server.Model;

import Server.Controller.ControllerServer;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ContactList {


        private ConcurrentHashMap<User, ArrayList<User>> contactList;
        private ControllerServer controllerServer;



        public ContactList(ControllerServer controller) {
            this.contactList = new ConcurrentHashMap<>();
            this.controllerServer = controller;

        }


        public synchronized void put(User userKey, ArrayList<User> array) {
            contactList.put(userKey, array);


            notifyAll();
        }


        public ArrayList<User> getContactlist(User user) {
            if (!contactList.containsKey(user)){
                contactList.put(user, new ArrayList<User>());
            }

            return contactList.get(user);
        }
}
