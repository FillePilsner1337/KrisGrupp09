package Server.Controller;

import SharedModel.*;


public class ServerInputHandler {
    private ServerController serverController;

    public ServerInputHandler(ServerController serverController) {
        this.serverController = serverController;

    }
    public void newObjectReceived(Object o, User user){
        if (o instanceof UserStatus){
            UserStatus status = (UserStatus) o;
            serverController.changeStatus(status, user);
                    }
        if (o instanceof OkFollowRequest){
            OkFollowRequest okFollowRequest = (OkFollowRequest) o;
            serverController.okToFollow(okFollowRequest);
        }
        if (o instanceof FollowRequest){
            FollowRequest req = (FollowRequest)o;
            serverController.requestToFollow(req, user);
        }
    }
}
