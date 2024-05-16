package Server.Boundary;

import Server.Controller.ControllerServer;
import SharedModel.User;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;

public class ServerMainFrame extends JFrame {

    private JList<User> userList;
    private DefaultListModel<User> modelUserList;
    private JList<User> connectedUserList;
    private DefaultListModel<User> modelConnectedUserList;

    private JList<String> logList;
    private DefaultListModel<String> modelLogList;

    private ControllerServer controllerServer;

    public ServerMainFrame(ControllerServer controllerServer) {
        this.controllerServer = controllerServer;
        setUpFrame();
    }

    public ServerMainFrame() {
        setUpFrame();
    }

    private void setUpFrame() {
setLayout(null);
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Server");
        setVisible(true);
        JLabel userListLabel = new JLabel("All users:");
        userListLabel.setSize(100, 20);
        userListLabel.setLocation(20, 0);
        add(userListLabel);


        userList = new JList<>();
        modelUserList = new DefaultListModel<>();
        userList.setModel(modelUserList);
        JScrollPane userScrollPane = new JScrollPane(userList);
        userScrollPane.setSize(200, 300);
        userScrollPane.setLocation(20, 20);
        add(userScrollPane);

        JLabel connectedUserListLabel = new JLabel("Connected users:");
        connectedUserListLabel.setSize(150, 20);
        connectedUserListLabel.setLocation(20, 330);
        connectedUserListLabel.setVisible(true);
        add(connectedUserListLabel);

        connectedUserList = new JList<>();
        modelConnectedUserList = new DefaultListModel<>();
        connectedUserList.setModel(modelConnectedUserList);
        JScrollPane connectedUserScrollPane = new JScrollPane(connectedUserList);
        connectedUserScrollPane.setSize(200, 200);
        connectedUserScrollPane.setLocation(20, 350);
        add(connectedUserScrollPane);

        JLabel logListLabel = new JLabel("Log:");
        logListLabel.setSize(100, 20);
        logListLabel.setLocation(250, 0);
        add(logListLabel);

        logList = new JList<>();
        modelLogList = new DefaultListModel<>();
        logList.setModel(modelLogList);
        JScrollPane logScrollPane = new JScrollPane(logList);
        logScrollPane.setSize(500, 530);
        logScrollPane.setLocation(250, 20);
        add(logScrollPane);


        repaint();
        revalidate();

    }

    public void log(String string){
        modelLogList.addElement(string + " " + new Date().toString());
        repaint();
        revalidate();
    }

    public void loadGUIdata() {

        ArrayList<User> allUsers = controllerServer.getAllUsers();
        modelUserList.clear();
        modelUserList.addAll(allUsers);
        ArrayList<User> connectedUsers = controllerServer.getConnectedUsers();
        modelConnectedUserList.clear();
        modelConnectedUserList.addAll(connectedUsers);
    }

    public void updateGuiConnectedUsers(ArrayList<User> connectedUsers){
        modelConnectedUserList.clear();
        modelConnectedUserList.addAll(connectedUsers);
        repaint();
        revalidate();
    }
    public void updateGuiAllUsers(ArrayList<User> connectedUsers){
        modelUserList.clear();
        modelUserList.addAll(connectedUsers);
        repaint();
        revalidate();
    }


}
