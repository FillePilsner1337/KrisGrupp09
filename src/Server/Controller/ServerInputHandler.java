package Server.Controller;

import SharedModel.*;


public class ServerInputHandler {
    private ControllerServer controllerServer;

    public ServerInputHandler(ControllerServer controllerServer) {
        this.controllerServer = controllerServer;

    }
    public void newObjectReceived(Object o, User user){
        if (o instanceof InUtStatus){
            InUtStatus status = (InUtStatus) o;
            controllerServer.changeStatus(status, user);
                    }
        if (o instanceof OkFollowReg){
            OkFollowReg okFollowReg = (OkFollowReg) o;
            controllerServer.okToFollow(okFollowReg);
        }
        if (o instanceof FollowReq){
            FollowReq req = (FollowReq)o;
            controllerServer.reqToFollow(req, user);
        }
    }
}
