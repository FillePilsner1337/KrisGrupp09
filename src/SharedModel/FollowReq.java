package SharedModel;

import java.io.Serializable;

public class FollowReq implements Serializable {
    private static final long serialVersionUID = 604569492464863070L;
    private User wantsToFollow;
    private User personToBeFollowd;

    public FollowReq(User wantsToFollow, String personToBeFollowd) {
        this.wantsToFollow = wantsToFollow;
        this.personToBeFollowd = new User(personToBeFollowd, null);
    }

    public User getWantsToFollow() {
        return wantsToFollow;
    }

    public void setWantsToFollow(User wantsToFollow) {
        this.wantsToFollow = wantsToFollow;
    }

    public User getPersonToBeFollowd() {
        return personToBeFollowd;
    }

    public void setPersonToBeFollowd(User personToBeFollowd) {
        this.personToBeFollowd = personToBeFollowd;
    }
}
