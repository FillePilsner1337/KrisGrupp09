package Server.Model;

import Server.Controller.ControllerServer;
import SharedModel.User;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ContactList {

        private ConcurrentHashMap<User, ArrayList<User>> contactList;
        private ControllerServer controllerServer;

        private AllUsers allUsers;

        public ContactList(ControllerServer controller, AllUsers al) {
            this.contactList = new ConcurrentHashMap<>();
            this.controllerServer = controller;
            this.allUsers = al;
        }

        public synchronized void put(User userKey, ArrayList<User> array) {
            contactList.put(userKey, array);
            notifyAll();
        }

        public ArrayList<User> getContactlist(User user) {
            if (!contactList.containsKey(user)){
               // contactList.put(user, new ArrayList<User>());
               //fullösning för dummydata
                contactList.put(user,allUsers.getAllUsers() );
            }
            return contactList.get(user);
        }
}
