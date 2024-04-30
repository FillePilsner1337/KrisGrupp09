package Server.Controller;

import SharedModel.*;


public class ServerInputHandler {

    private ControllerServer controllerServer;

    public ServerInputHandler(ControllerServer controllerServer) {
        this.controllerServer = controllerServer;
        System.out.println("ServerInputhandler Startad");
    }

    public void newObjectReceived(Object o, User user){
        if (o instanceof InUtStatus){
            InUtStatus status = (InUtStatus) o;
            System.out.println(status.toString());
            controllerServer.changeStatus(status, user);
            System.out.println("newObjectReceived i inputhandler");
        }
        if (o instanceof ContactListUpdate) {
            ContactListUpdate update = (ContactListUpdate) o;
            controllerServer.updateContactlistFromUser(update, user);
        }
        if (o instanceof FollowReq){
            FollowReq req = (FollowReq)o;
            controllerServer.reqToFollow(req, user);
        }
         else  {
            System.out.println("instance of funkar inte");

        }
    }
}
