package Client.View;

import Client.Controller.ClientController;
import Client.Controller.GUIcontroller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class LogInFrame extends JFrame implements ActionListener, KeyListener {

    private GUIcontroller guiController;
    private JTextField username;
    private JPasswordField password;
    private JButton login;
    private JButton register;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private ClientController clientController;
    private final Taskbar taskbar = Taskbar.getTaskbar();

    public LogInFrame(GUIcontroller guiController, ClientController clientController) {
        this.guiController = guiController;
        this.clientController = clientController;
        setUpFrame();
        ImageIcon img = new ImageIcon("files/1.png");
        Image img1 = img.getImage();
        taskbar.setIconImage(img1);
        this.setIconImage(img.getImage());
    }

    public void setUpFrame() {
        setLayout(null);
        setSize(200, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        JLabel kris = new JLabel("KRIS");
        kris.setFont(new Font("Congenial", Font.BOLD, 30));
        kris.setSize(200, 40);
        kris.setLocation(0,10);
        kris.setHorizontalAlignment(SwingConstants.CENTER);

        this.add(kris);
        JLabel regi = new JLabel("Registrera nytt konto");
        regi.setFont(new Font("Congenial", Font.BOLD, 15));
        regi.setSize(200, 40);
        regi.setLocation(0,260);
        regi.setHorizontalAlignment(SwingConstants.CENTER);

        this.add(regi);
        username = new JTextField();
        password = new JPasswordField();
        usernameLabel = new JLabel("Användarnamn: ");
        passwordLabel = new JLabel("Lösenord: ");
        add(usernameLabel);
        add(passwordLabel);
        add(username);
        add(password);
        login = new JButton("Logga in");
        register = new JButton("Registrera");
        usernameLabel.setLocation(20, 65);
        usernameLabel.setSize(200, 15);
        username.setLocation(20, 80);
        username.setSize(150, 30);
        password.setSize(150, 30);
        passwordLabel.setLocation(20, 115);
        passwordLabel.setSize(200, 15);
        password.setLocation(20, 130);
        login.setEnabled(true);
        login.setSize(100, 40);
        login.setLocation(40, 180);
        login.addActionListener(this);
        password.addKeyListener(this);
        register.setSize(100, 40);
        register.setLocation(40, 300);
        register.addActionListener(this);
        add(login);
        add(register);
        username.setVisible(true);
        login.setVisible(true);
        password.setVisible(true);
        register.setVisible(true);
        usernameLabel.setVisible(true);
        passwordLabel.setVisible(true);
        setVisible(true);
    }

    public void login() {
        String password = new String(this.password.getPassword());
        clientController.login(username.getText(), password);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == login) {
            login();
        }
        if (e.getSource() == register){
            guiController.startRegistrationFrame();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER && login.isEnabled()) {
            login();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}

