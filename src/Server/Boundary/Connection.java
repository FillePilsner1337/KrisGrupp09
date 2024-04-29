package Server.Boundary;



import Server.Controller.ControllerServer;
import Server.Controller.ServerInputHandler;
import Server.Model.Buffer;
import Server.Model.ConnectedClients;
import SharedModel.ConfirmLogon;
import SharedModel.Message;
import SharedModel.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;




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
        controllerServer.newLogIn(user, this);
        sendObject(new ConfirmLogon());
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
             user = u;
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
                controllerServer.userDisconnect(user);

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

                }


                while (!Thread.interrupted()) {
                    Object o = ois.readObject();

                        serverInputHandler.newObjectReceived(o, user);
                    }




            } catch (Exception e) {
                System.out.println("FEL Run metoden i InputHandler IOException");
                System.out.println(e.getMessage());

            }

             finally {
                //controllerServer.userDisconnect(user);
                try{
                    this.socket.close();
                }

                catch (IOException e){
                    System.out.println(e.getMessage());
                    System.out.println("Kunde inte stänga socket");
                }
            }

        }


    }


}










