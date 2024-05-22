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

/**
 * Servern håller ett ClientConnectionobjekt för varje klient som är uppkopplad till servern.
 * Klassen innehåller en inputhandler och en outputhandler som inre klasser. Både inputhandler
 * och outputhandler körs si egna trådar. Hanterar även objekt som kommer från klient fram tills att
 * klienten är inloggad.
 *
 * @author Ola Persson och Jonatan Tempel
 */

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
        this.outputBuffer = new Buffer<>();
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

    private void checkUserNamneAndPassword(User user) {
        boolean exists = serverController.checkUserExists(user);
        if (!exists){
            sendObject(new Message("Felaktigt användarnamn"));
            serverController.log("Felaktigt användarnamn: " + user.getUsername());
        }
        boolean password = serverController.checkPassword(user);
        if (exists && password){
            this.user = serverController.getRealUser(user);
            newConnection();
            loggingOn = false;
            serverController.log("Rätt användarnamn och lösenord: " + user.getUsername());
        }
        if (exists && !password) {
            sendObject(new Message("Felaktigt lösenord"));
            serverController.log("Felaktigt lösenord: " + user.getUsername());
        }
    }

    private class OutputHandler extends Thread {
        private Socket socket;

        public OutputHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try (ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {
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
                } catch (IOException e){
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
                    Object obj = ois.readObject();
                    if (obj instanceof User){
                        checkUserNamneAndPassword((User)obj);
                    }
                    if (obj instanceof RegistrationRequest){
                        registrationRequest((RegistrationRequest)obj);
                    }
                }
                while (!Thread.interrupted()) {
                    Object obj = ois.readObject();
                    serverInputHandler.newObjectReceived(obj, user);
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

    private void registrationRequest(RegistrationRequest registrationRequest) {
        boolean allredyregisterd = serverController.registrationRequest(registrationRequest);
        if (allredyregisterd){
            sendObject(new Message("Användarnamnet används redan"));
        }
        boolean okUserNameAndPassword = serverController.okLengthUsernameAndPassword(registrationRequest);
        if (!okUserNameAndPassword){
            sendObject(new Message("Användarnamn och lösenord måste innehålla minst tre tecken"));
        }
        if (!allredyregisterd && okUserNameAndPassword){
            serverController.registerNewUser(new User(registrationRequest.getUserName(), registrationRequest.getPassword()));
            sendObject(new Message("Ditt konto är registrerat"));
            sendObject(new ConfirmRegistration());
            serverController.log("Ny registrerad klient: " + registrationRequest.getUserName());
        }
    }

    public User getUser(){
        return user;
    }
}










