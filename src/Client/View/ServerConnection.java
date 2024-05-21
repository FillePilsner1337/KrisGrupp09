package Client.View;

import Client.Controller.ClientController;
import Server.Model.Buffer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Klass som sköter uppkopplingen till servern.
 * @Author Ola Persson
 */
public class ServerConnection {
    private Socket socket;
    private InputHandler inputHandler;
    private OutputHandler outputHandler;
    private Buffer<Object> outputBuffer;
    private ClientController clientController;

    public ServerConnection(ClientController clientController)  {
        this.clientController = clientController;

        try {
            this.socket = new Socket("127.0.0.1", 20000);
        } catch (IOException e) {
            System.out.println("Kunde inte skapa socket");
            throw new RuntimeException(e);
        }
        this.outputBuffer = new Buffer<>();
        this.inputHandler = new InputHandler(socket);
        this.outputHandler = new OutputHandler(socket);
        inputHandler.start();
        outputHandler.start();
    }

    public void sendObject(Object o){
        outputBuffer.put(o);
    }
    public void receivedObject(Object o){
        clientController.receivedObject(o);
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
                System.out.println("FEL Run metoden i OutputHandler IOException");
                System.out.println(e.getMessage());
            } catch (InterruptedException e) {
                System.out.println("FEL Run metoden i OutputHandler InterruptedException");
                System.out.println(e.getMessage());
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
                    receivedObject(o);
                }
            } catch (IOException e) {
                System.out.println("FEL Run metoden i InputHandler IOException");
                System.out.println(e.getMessage());

            } catch (ClassNotFoundException e) {
                System.out.println("FEL Run metoden i InputHandler InterruptedException");
                System.out.println(e.getMessage());
            }
        }
    }
}
