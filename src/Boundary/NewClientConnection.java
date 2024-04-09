package Boundary;


import Controller.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class NewClientConnection extends Thread {

    private Controller controller;
    private ServerSocket serverSocket;

    private ServerInputHandler serverInputHandler;


    public NewClientConnection(int port, Controller controller, ServerInputHandler serverInputHandler)  {
        this.controller = controller;
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
                new Connection(socket, controller, serverInputHandler);



            } catch (IOException e) {
                System.out.println("IOException i NewClientConnection run metod");
                System.out.println(e.getMessage());

            }


        }

    }

}