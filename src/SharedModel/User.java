package SharedModel;

import java.io.Serializable;

public class User implements Serializable {

    private String userName;
    private InUtStatus inUtStatus;

    public User(String userName) {
        this.userName = userName;



    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public InUtStatus getInUtStatus() {
        return inUtStatus;
    }

    public void setInUtStatus(InUtStatus inUtStatus) {
        this.inUtStatus = inUtStatus;
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

    public String getUserName() {
        return userName;
    }


}
