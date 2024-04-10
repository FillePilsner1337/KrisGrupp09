package Server.Boundary;



import Server.Controller.Controller;
import Server.Controller.ServerInputHandler;
import Server.Controller.*;
import Server.Model.Buffer;
import Server.Model.ConnectedClients;
import Server.Model.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;




public class Connection {


    private InputHandler inputHandler;
    private Socket socket;

    private Buffer<Object> outputBuffer;

    private Controller controller;
    private OutputHandler outputHandler;

    private ConnectedClients connectedClients;
    private ServerInputHandler serverInputHandler;

    private User user;

    public Connection(Socket socket, Controller controller, ServerInputHandler serverInputHandler)  {
        System.out.println("Connection konstruktor rad 1");
        this.controller = controller;
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
        controller.newLogIn(user, this);
    }

    public void sendObject(Object object){
        outputBuffer.put(object);


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
                controller.userDisconnect(user);

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


                while (!Thread.interrupted()) {
                    Object o = ois.readObject();
                    if (o instanceof User){
                        user = (User)o;
                        newConnection();
                        System.out.println("User objekt mottagit");
                    }
                    else {
                        serverInputHandler.newObjectReceived(o, user);
                    }



                }
            } catch (IOException e) {
                System.out.println("FEL Run metoden i InputHandler IOException");
                System.out.println(e.getMessage());

            } catch (ClassNotFoundException e) {
                System.out.println("FEL Run metoden i InputHandler InterruptedException");
                System.out.println(e.getMessage());

            } finally {
                controller.userDisconnect(user);
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










