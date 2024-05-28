package SharedModel;

import java.io.Serializable;

public class RemoveFriend implements Serializable {
    private User userToRemove;
    boolean ok = true;

    public RemoveFriend(User user){
        this.userToRemove = user;
    }

    public User getUserToRemove() {
        return userToRemove;
    }
}
