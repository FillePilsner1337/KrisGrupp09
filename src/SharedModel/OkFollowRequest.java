package SharedModel;

import java.io.Serializable;

public class OkFollowRequest implements Serializable {
    private static final long serialVersionUID = 7;
    private FollowRequest followRequest;
    private boolean ok;

    public OkFollowRequest(FollowRequest followRequest, boolean ok) {
        this.followRequest = followRequest;
        this.ok = ok;
    }

    public FollowRequest getFollowRequest() {
        return followRequest;
    }
}
