package Boundary;



import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 Klassen är en anslutning till varje klient på server. De två inre klasser hanterar input respektive outout.

 */

public class Connection {


    private InputHandler inputHandler;
    private Socket socket;

    private Buffer<Object> outputBuffer;

    private MainServerController msc;
    private OutputHandler outputHandler;

    private ConnectedClients connectedClients;

    private User client;

    public Connection(Socket socket, MainServerController msc, ConnectedClients connectedClients)  {
        this.msc = msc;
        this.connectedClients = connectedClients;
        this.socket = socket;

        this.inputHandler = new InputHandler(socket);
        inputHandler.start();
        this.outputBuffer = new Buffer<Object>();
        this.outputHandler = new OutputHandler(socket);
        outputHandler.start();



    }

    public Socket getSocket() {
        return socket;
    }

    /**
     * Metoden tar emot ett Message objekt loggar det och lägger till i outputbuffert
     * @param msg Messageobjekt som ska skickas
     */
    public void sendMessage(Message msg){
        msg.setTimeDelivered(new Date());

        try {
            LogReaderWriter.getInstance().addToLogList(new ModelLog("New outgoing message: " + msg.toString()));
        }
        catch (IOException e){
            System.out.println("fel i sendMessageMetoden");
        }
        finally {
            outputBuffer.put(msg);
        }
    }
    /**
     * Metoden tar emot ett Update objekt loggar det och lägger till i outputbuffert
     * @param update updateobjekt som ska skickas
     */
    public void sendUpdate(Update update) {
        try {

            LogReaderWriter.getInstance().addToLogList(new ModelLog("New outgoing Update: " + update.toString()));
        }
        catch (IOException e){
            System.out.println("fel i sendMessageMetoden");
        }
        finally {
            outputBuffer.put(update);
        }


        outputBuffer.put(update);
    }

    /**
     * Metoden tar emot ett ContactListUpdate, loggar det och lägger till i outputbuffert
     * @param ob ContactjUpdateObjekt som ska skickas
     */
    public void sendContactList(ContactUpdateObject ob){
        try {

            LogReaderWriter.getInstance().addToLogList(new ModelLog("New outgoing ContactUpdateObject: " + ob.toString()));
        }
        catch (IOException e){
            System.out.println("fel i sendMessageMetoden");
        }
        finally {
            outputBuffer.put(ob);
        }


    }

    /**
     * Inre klass i egen tråd som hämtar från outputbuffert och skickar det till klient
     */

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
                connectedClients.removeUser(client);
                msc.updateAfterDisconnect();
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

    /**
     * Inre klass som körs i egen tråd. Tar emot objekt från klient kontrolerar vilken typ av objekt det är
     * och skickar vidare till rätt metod.
     */
    private class InputHandler extends Thread {
        private Socket socket;

        public InputHandler(Socket socket) {

            this.socket = socket;
        }

        public void run() {
            try (ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());) {


                while (!Thread.interrupted()) {
                    Object ob = ois.readObject();
                    if (ob instanceof Message){
                        msc.newIncomingMessage((Message) ob, client);
                    }
                    if (ob instanceof ContactUpdateObject){
                        msc.clientContactListUpdate((ContactUpdateObject) ob);
                    }
                    if (ob instanceof User){
                        client = (User)ob;
                        msc.newConnetction((User) ob, Connection.this);
                    }



                }
            } catch (IOException e) {
                if (e instanceof EOFException) {
                    System.out.println("end of file");
                }
                System.out.println("FEL Run metoden i InputHandler IOException");
                System.out.println(e.getMessage());

            } catch (ClassNotFoundException e) {
                System.out.println("FEL Run metoden i InputHandler InterruptedException");
                System.out.println(e.getMessage());

            } finally {
                connectedClients.removeUser(client);
                msc.updateAfterDisconnect();
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










