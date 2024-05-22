package SharedModel;

import java.io.Serializable;
/**
 * Objektet skickas från klient till servern. Det är en förfrågan om att registrera ett nytt konto
 */
public class RegistrationRequest implements Serializable {
    private static final long serialVersionUID = 8;
    private String userName;
    private String password;

    public RegistrationRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    @Override
    public int hashCode() {
        return userName.hashCode();
    }
    @Override
    public boolean equals(Object obj) {
        if(obj!=null && obj instanceof RegistrationRequest)
            return userName.equals(((RegistrationRequest)obj).getUserName());
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
}




