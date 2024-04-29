package Server.Boundary;


import Server.Controller.ControllerServer;
import Server.Controller.ServerInputHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class NewClientConnection extends Thread {

    private ControllerServer controllerServer;
    private ServerSocket serverSocket;
    private ServerInputHandler serverInputHandler;

    public NewClientConnection(int port, ControllerServer controllerServer, ServerInputHandler serverInputHandler)  {
        this.controllerServer = controllerServer;
        this.serverInputHandler = serverInputHandler;
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("IOException i NewClientConnection konstruktor");
            System.out.println(e.getMessage());
        }
        System.out.println("New client connection startad");
    }

    @Override
    public void run() {
        System.out.println("run metod new client ");
        while (!Thread.interrupted()) {
            try {
                Socket socket = serverSocket.accept();
                new Connection(socket, controllerServer, serverInputHandler);
            } catch (IOException e) {
                System.out.println("IOException i NewClientConnection run metod");
                System.out.println(e.getMessage());
            }
        }
    }
}