package Controller;



import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;


public class Controller  {
    private final int port = 10000;
    private NewClientConnection newClientConnection;



    public MainServerController() {
        this.connectedClients = new ConnectedClients();
        this.unsendMessages = new UnsendMessages();
        this.newClientConnection = new NewClientConnection(10000);
        this.contactList = new ContactList();


        try{
            LogReaderWriter.getInstance();
        }
        catch (IOException e){
            System.out.println("Something went wrong when getting the instance for LogReaderWriter in MSC constructor.");
        }
        new ServerViewController();
        newClientConnection.start();
        //System.out.println("ServerController startad"); //Minimera utskrifter i terminal eftersom den används av ServerView
    }


    /**
     * Metoden tar emot ett inkomande Message från en klient. Sätter tiden som objektet mottagits.
     * Loggar objektet. Loppar igenom mottagarna och skickar det om motagaren är online. Är mottagaren inte online
     * sparas meddelandet undan.
     * @param msg Meddelandet
     * @param user Användaren som skickat meddelandet.
     * @throws IOException
     */
    public synchronized void newIncomingMessage(Message msg, User user) throws IOException {
        msg.setTimeSent(new Date());
        LogReaderWriter.getInstance().addToLogList(new ModelLog("New message received by server"));
        for (int  i = 0; i < msg.getRecipients().size(); i++){

            User userToSend;
            userToSend = msg.getRecipients().get(i);
            if (connectedClients.isUserConnected(userToSend)){
                connectedClients.getConnectionForUser(userToSend).sendMessage(msg);
            }
            else {
                unsendMessages.put(userToSend, msg);
            }

        }
        notifyAll();
    }


    /**
     * Metoden kontrollerar efter en ny anslutning om det finns några sparade meddelanden och levererar dem
     * till användaren
     * @param user Användaren som anslutit sig
     */
    public void checkForOldMessages(User user) {
        if (unsendMessages.userHasOldMessages(user)) {
            ArrayList<Message> toSend = unsendMessages.getMessagesForUser(user);
            for (int i = 0; i < toSend.size(); i++) {
                connectedClients.getConnectionForUser(user).sendMessage(toSend.get(i));
            }
        }


    }

    /**
     * Metoden skickar ut en lista på alla anslutna användare till alla användare. Skickar även ut vilken
     * användare som senast anslutit sig.
     * @param user Senaste anslutna användaren
     */
    public synchronized void notifyOfNewOnlineUser(User user) {
        Update update = new Update(user, connectedClients.getListOfConnected());
        ArrayList<Connection> recivers = connectedClients.getListOfAllConnection();
        for (Connection connection : recivers){
            connection.sendUpdate(update);

        }
        notifyAll();

    }

    /**
     * Metoden skickar ut en lista på alla anslutna användare till alla användare. Metoden körs eftter att en
     * användare har kopplat ner
     */
    public synchronized void updateAfterDisconnect(){
        Update update = new Update(null, connectedClients.getListOfConnected());
        ArrayList<Connection> recivers = connectedClients.getListOfAllConnection();
        for (Connection connection : recivers){
            connection.sendUpdate(update);

        }

        notifyAll();
    }

    /**
     * Metoden körs när en ny användare ansluter sig. Loggar det. Metoden kontrollerar så att det inte rdan
     * finns en anslutning med samma användare. Om det finns så kopplas båda klienterna ner. Användaren och hens connectionobjekt
     * läggs till i en hashmap. Sen körs metoderna checkForOldMessages och notifyOfNewOnlineUser.
     * @param user användaren som anslutit sig
     * @param connection användarens connectionobjekt
     * @throws IOException
     */
    public void newConnetction(User user, Connection connection) throws IOException {
        LogReaderWriter.getInstance().addToLogList(new ModelLog("User: " + user.getUserName() + " connected to the server"));
        if (connectedClients.isUserConnected(user)){
            try {
                connectedClients.getConnectionForUser(user).getSocket().close();
                connectedClients.removeUser(user);
                connection.getSocket().close();
                updateAfterDisconnect();
            }
            catch (IOException e){
                System.out.println("IOException, newConnection(), MainServerController  ");
            }

        }
        connectedClients.put(user, connection);


        connection.sendContactList(new ContactUpdateObject(user, contactList.getContactlist(user)));
        checkForOldMessages(user);
        notifyOfNewOnlineUser(user);
    }


    /**
     * Metoden uppdaterar en användares kontaktlista
     * @param ob ett ContactUpdateObjekt
     * @throws IOException
     */
    public void clientContactListUpdate(ContactUpdateObject ob) throws IOException {
        LogReaderWriter.getInstance().addToLogList(new ModelLog("New ClientContactListUpdate received by server"));
        contactList.put(ob.getUser(),ob.getUserArrayList());
    }

    /**
     * Inre klass som lyssnar efter nya anslutningar och skapar nya connection objekt när en klient ansluter sig.
     * Klassen körs i en egen tråd.
     */
    private class NewClientConnection extends Thread {
        private ServerSocket serverSocket;

        public NewClientConnection(int port){
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
}
