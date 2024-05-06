package SharedModel;

import java.io.Serializable;
import java.util.ArrayList;

public class ContactListUpdate implements Serializable {
    private static final long serialVersionUID = 604569492464863070L;

    private ArrayList<User> list;

    public ContactListUpdate(ArrayList<User> list) {
        this.list = list;
    }

    public ArrayList<User> getList() {
        return list;
    }
}
