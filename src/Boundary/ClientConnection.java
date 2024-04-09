package Boundary;

import Controller.Controller;
import Controller.MainServerController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientConnection extends Thread {

    private Controller controller;
    private ServerSocket serverSocket;

    public ClientConnection(int port, Controller controller) {
        this.controller = controller;

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
                new Connection(socket, MainServerController.this, connectedClients);


            } catch (IOException e) {
                System.out.println("IOException i NewClientConnection run metod");
                System.out.println(e.getMessage());

            }


        }

    }

}