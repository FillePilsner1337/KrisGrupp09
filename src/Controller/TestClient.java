package Controller;

import Model.User;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TestClient {

    public static void main(String[] args) throws IOException {
        User u = new User("username2");
        Socket socket = new Socket("127.0.0.1", 20000);
        try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()))) {
            oos.writeObject(u);
            oos.flush();

            while (!Thread.interrupted()) {

            }
        } catch (IOException e) {
            System.err.println("I/O exception at Output: " + e);
            e.printStackTrace();

        }
    }
}
