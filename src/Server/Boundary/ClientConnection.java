package Server.Boundary;



import Server.Controller.ServerController;
import Server.Controller.ServerInputHandler;
import Server.Model.Buffer;
import Server.Model.ConnectedClients;
import SharedModel.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class ClientConnection {

    private InputHandler inputHandler;
    private Socket socket;
    private Buffer<Object> outputBuffer;
    private ServerController serverController;
    private OutputHandler outputHandler;
    private ConnectedClients connectedClients;
    private ServerInputHandler serverInputHandler;
    private User user;
    private boolean loggingOn = true;

    public ClientConnection(Socket socket, ServerController serverController, ServerInputHandler serverInputHandler)  {
        this.serverController = serverController;
        this.socket = socket;
        this.serverInputHandler = serverInputHandler;
        this.inputHandler = new InputHandler(socket);
        inputHandler.start();
        this.outputBuffer = new Buffer<Object>();
        this.outputHandler = new OutputHandler(socket);
        outputHandler.start();
        serverController.log("Ny anslutning etablerad");

    }

    public Socket getSocket() {
        return socket;
    }

    public void newConnection(){
        user.setUserStatus(serverController.getStatusForUser(user));
        sendObject(user);
        sendObject(new ConfirmLogin());
        serverController.newLogIn(user, this);
        serverController.log("Ny inloggning av " + user.getUsername());

    }

    public void sendObject(Object object){
        outputBuffer.put(object);
    }

    private void checkUserNamneAndPassword(User u) {
        boolean exists = serverController.checkUserExists(u);
         if (!exists){
            sendObject(new Message("Felaktigt användarnamn"));
             serverController.log("Felaktigt användarnamn: " + u.getUsername());

         }
         boolean password = serverController.checkPassword(u);
         if (exists && password){

             user = serverController.getRealUser(u);
             newConnection();
             loggingOn = false;
             serverController.log("Rätt användarnamn och lösenord: " + u.getUsername());

         }
         if (exists && !password) {
             sendObject(new Message("Felaktigt lösenord"));
             serverController.log("Felaktigt lösenord: " + u.getUsername());

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
                    oos.writeObject(obj);
                    oos.flush();
                    oos.reset();
                }
            } catch (IOException e) {
                System.out.println("FEL Run metoden i InputHandler IOException");
                System.out.println(e.getMessage());
            } catch (InterruptedException e) {
                System.out.println("FEL Run metoden i InputHandler InterruptedException");
                System.out.println(e.getMessage());
            }
            finally {
                serverController.userDisconnect(user);
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
                    if (o instanceof RegistrationRequest){
                        registrationRequest((RegistrationRequest)o);

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
              serverController.userDisconnect(user);

            }
        }
    }

    private void registrationRequest(RegistrationRequest o) {
       boolean allredyregisterd = serverController.registrationRequest(o);
       if (allredyregisterd){
           sendObject(new Message("Användarnamnet används redan"));

       }
       boolean okUserNameAndPassword = serverController.okLengthUsernameAndPassword(o);
       if (!okUserNameAndPassword){
           sendObject(new Message("Användarnamn och lösenord måste innehålla minst tre tecken"));
       }
       if (!allredyregisterd && okUserNameAndPassword){
           serverController.registerNewUser(new User(o.getUserName(), o.getPassword()));
           sendObject(new Message("Ditt konto är registrerat"));
           sendObject(new ConfirmRegistration());
           serverController.log("Ny registrerad klient: " + o.getUserName());

       }
    }
    public User getUser(){
        return user;
    }
}










