package SharedModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Objektet skickas till User från server och innehåller en uppdaterad lista med userns vänner
 * och des status
 * @author Ola Persson och Jonatan Tempel
 */
public class ContactListUpdate implements Serializable {
    private static final long serialVersionUID = 3;
    private ArrayList<User> list;

    public ContactListUpdate(ArrayList<User> list) {
        this.list = list;
    }

    public ArrayList<User> getList() {
        return list;
    }
}
