package Server.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class ContactListUpdate implements Serializable {

    private ArrayList<User> list;

    public ContactListUpdate(ArrayList<User> list) {
        this.list = list;
    }

    public ArrayList<User> getList() {
        return list;
    }
}
