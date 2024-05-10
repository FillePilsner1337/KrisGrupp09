package Server.Boundary;



import Server.Controller.ControllerServer;
import Server.Controller.ServerInputHandler;
import Server.Model.Buffer;
import Server.Model.ConnectedClients;
import SharedModel.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;


public class Connection {

    private InputHandler inputHandler;
    private Socket socket;
    private Buffer<Object> outputBuffer;
    private ControllerServer controllerServer;
    private OutputHandler outputHandler;
    private ConnectedClients connectedClients;
    private ServerInputHandler serverInputHandler;
    private User user;
    private boolean loggingOn = true;

    public Connection(Socket socket, ControllerServer controllerServer, ServerInputHandler serverInputHandler)  {
        System.out.println("Connection konstruktor rad 1");
        this.controllerServer = controllerServer;
        this.socket = socket;
        this.serverInputHandler = serverInputHandler;
        this.inputHandler = new InputHandler(socket);
        inputHandler.start();
        this.outputBuffer = new Buffer<Object>();
        this.outputHandler = new OutputHandler(socket);
        outputHandler.start();
        System.out.println("new connection objekt");
    }

    public Socket getSocket() {
        return socket;
    }

    public void newConnection(){
        user.setInUtStatus(controllerServer.getStatusForUser(user));
        sendObject(user);
        sendObject(new ConfirmLogon());
        controllerServer.newLogIn(user, this);

    }

    public void sendObject(Object object){
        outputBuffer.put(object);
    }

    private void checkUserNamneAndPassword(User u) {
        boolean exists = controllerServer.checkUserExists(u);
         if (!exists){
            sendObject(new Message("Felaktigt användarnamn"));
             System.out.println("Användare finns ej");
         }
         boolean password = controllerServer.checkPassword(u);
         if (exists && password){

             user = controllerServer.getRealUser(u);
             newConnection();
             loggingOn = false;
             System.out.println("Rätt användarnamn och lösenord");
         }
         if (exists && !password) {
             sendObject(new Message("Felaktigt lösenord"));
             System.out.println("Fel lösenord");
         }
    }


    private class OutputHandler extends Thread {
        private Socket socket;

        public OutputHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try (ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());) {
                while (!Thread.interrupted()) {
                    Object obj = outputBuffer.get();
                    if (obj instanceof ContactListUpdate){
                        ContactListUpdate clu = ((ContactListUpdate)obj);
                        for (int i = 0; i < ((ContactListUpdate) obj).getList().size(); i++){
                            System.out.println(((ContactListUpdate) obj).getList().get(i) + " " + new Date());
                        }
                    }
                    oos.writeObject(obj);
                    oos.flush();
                }
            } catch (IOException e) {
                System.out.println("FEL Run metoden i InputHandler IOException");
                System.out.println(e.getMessage());
            } catch (InterruptedException e) {
                System.out.println("FEL Run metoden i InputHandler InterruptedException");
                System.out.println(e.getMessage());
            }
            finally {
               // controllerServer.userDisconnect(user);
                try{
                    this.socket.close();
                }
                catch (IOException e){
                    System.out.println(e.getMessage());
                    System.out.println("Kunde ej stänga socket");
                }
            }
        }
    }

    private class InputHandler extends Thread {
        private Socket socket;

        public InputHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try (ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());) {
                while (loggingOn){
                    Object o = ois.readObject();
                    if (o instanceof User){
                        checkUserNamneAndPassword((User)o);
                    }
                    if (o instanceof RegReq){
                        registrationRequest((RegReq)o);
                        System.out.println("Tagit emot RegReq");
                    }
                }
                while (!Thread.interrupted()) {
                    Object o = ois.readObject();
                        serverInputHandler.newObjectReceived(o, user);
                    }
            } catch (Exception e) {
                System.out.println("FEL Run metoden i InputHandler IOException");
                e.printStackTrace();
            }
             finally {
              controllerServer.userDisconnect(user);

            }
        }
    }

    private void registrationRequest(RegReq o) {
       boolean allredyregisterd = controllerServer.registrationRequest(o);
       if (allredyregisterd){
           sendObject(new Message("Användarnamnet används redan"));
           System.out.println("registrationRequest redan reggas");
       }
       boolean okUserNameAndPassword = controllerServer.okLengthUsernameAndPassword(o);
       if (!okUserNameAndPassword){
           sendObject(new Message("Användarnamn och lösenord måste innehålla minst tre tecken"));
       }
       if (!allredyregisterd && okUserNameAndPassword){
           controllerServer.registerNewUser(new User(o.getUserName(), o.getPassword()));
           sendObject(new Message("Ditt konto är registrerat"));
           sendObject(new ConfirmReg());
           System.out.println("registrationRequest redan reggas");
       }
    }
    public User getUser(){
        return user;
    }
}










