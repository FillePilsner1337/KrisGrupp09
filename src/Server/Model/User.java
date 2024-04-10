package Server.Model;

import java.io.Serializable;

public class User implements Serializable {

    private String userName;

    public User(String userName) {
        this.userName = userName;
    }

    @Override
    public int hashCode() {
        return userName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj!=null && obj instanceof User)
            return userName.equals(((User)obj).getUserName());
        return false;
    }

    private String getUserName() {
        return userName;
    }


}
