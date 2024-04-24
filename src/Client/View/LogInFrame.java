package Client.View;

import Client.Controller.GUIcontroller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class LogInFrame extends JFrame implements ActionListener, KeyListener {

    private GUIcontroller guIcontroller;
    private JTextField username;
    private JPasswordField password;
    private JButton login;
    private JButton register;
    private JLabel usernameLabel;
    private JLabel passwordLabel;

    public LogInFrame(GUIcontroller guIcontroller){
        setLayout(null);
        setSize(200, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
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
        usernameLabel.setLocation(28,15);
        usernameLabel.setSize(200,15);
        username.setLocation(25,30);
        username.setSize(150,30);
        password.setSize(150, 30);
        passwordLabel.setLocation(28, 65);
        passwordLabel.setSize(200, 15);
        password.setLocation(25, 80);
        login.setEnabled(true);
        login.setSize(100,50);
        login.setLocation(50, 130);
        register.setSize(100, 50);
        register.setLocation(50, 300);
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

    public void login(){
        this.setVisible(false);
        //Kalla på guiController att skapa mainframe här kanske?
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == login){
            //Någon Login-metod här
        }
        else{
            // Skicka vidare till GUIController och öppna ett RegistreraFönster
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER){
            //Någon login-metod här också
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public static void main(String[] args) {
        //new LogInFrame(new GUIcontroller());
    }
}