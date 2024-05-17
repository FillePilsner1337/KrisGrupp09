package SharedModel;

import java.io.Serializable;

public class FollowRequest implements Serializable {
    private static final long serialVersionUID = 4;
    private User wantsToFollow;
    private User UserToBeFollowd;

    public FollowRequest(User wantsToFollow, String UserToBeFollowd) {
        this.wantsToFollow = wantsToFollow;
        this.UserToBeFollowd = new User(UserToBeFollowd, null);
    }

    public User getWantsToFollow() {
        return wantsToFollow;
    }

    public void setWantsToFollow(User wantsToFollow) {
        this.wantsToFollow = wantsToFollow;
    }

    public User getUserToBeFollowd() {
        return UserToBeFollowd;
    }

    public void setUserToBeFollowd(User userToBeFollowd) {
        this.UserToBeFollowd = userToBeFollowd;
    }
}
