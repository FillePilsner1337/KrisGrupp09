package Server.Controller;

import SharedModel.*;

/**
 * Klassen hanterar alla objekt som kommer från klienter efter det att en klient har loggat in.
 *
 * @author Ola Persson och Jonatan Tempel
 */
public class ServerInputHandler {
    private ServerController serverController;

    public ServerInputHandler(ServerController serverController) {
        this.serverController = serverController;
    }
    public void newObjectReceived(Object obj, User user){
        if (obj instanceof UserStatus){
            UserStatus status = (UserStatus) obj;
            serverController.changeStatus(status, user);
        }
        if (obj instanceof OkFollowRequest){
            OkFollowRequest okFollowRequest = (OkFollowRequest) obj;
            serverController.okToFollow(okFollowRequest);
        }
        if (obj instanceof FollowRequest){
            FollowRequest req = (FollowRequest)obj;
            serverController.requestToFollow(req, user);
        }
        if (obj instanceof RemoveFriend){
            RemoveFriend removeObj = (RemoveFriend) obj;
            serverController.removeFriend(removeObj, user);
        }

    }
}
