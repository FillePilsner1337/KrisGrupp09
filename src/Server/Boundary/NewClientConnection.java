package Server.Boundary;

import Server.Controller.ServerController;
import Server.Controller.ServerInputHandler;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Objekt av klassen lyssnar efter nya anslutningar från klienter. När en ny anslutning etableras
 * skapas ett ClientConnectionobjekt
 *
 * @author Ola Persson och Jonatan Tempel
 */
public class NewClientConnection extends Thread {

    private ServerController serverController;
    private ServerSocket serverSocket;
    private ServerInputHandler serverInputHandler;

    public NewClientConnection(int port, ServerController serverController, ServerInputHandler serverInputHandler)  {
        this.serverController = serverController;
        this.serverInputHandler = serverInputHandler;
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("IOException i NewClientConnection konstruktor");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                Socket socket = serverSocket.accept();
                new ClientConnection(socket, serverController, serverInputHandler);
            } catch (IOException e) {
                System.out.println("IOException i NewClientConnection run metod");
                System.out.println(e.getMessage());
            }
        }
    }
}