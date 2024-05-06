package SharedModel;

import java.io.Serializable;

public class OkFollowReg implements Serializable {
    private FollowReq followReq;
    private boolean ok;

    public OkFollowReg(FollowReq followReq, boolean ok) {
        this.followReq = followReq;
        this.ok = ok;
    }

    public FollowReq getFollowReq() {
        return followReq;
    }
}
