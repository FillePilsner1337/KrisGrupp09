package SharedModel;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 9;

    private String userName;
    private InUtStatus inUtStatus;
    private String password;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", inUtStatus=" + inUtStatus.toString() +
                '}';
    }
}
