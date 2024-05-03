package Client.View;


import Client.Controller.ControllerKlient;
import Client.Controller.GUIcontroller;
import SharedModel.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

public class RegisterFrame extends JFrame implements ActionListener, KeyListener {

    private GUIcontroller guIcontroller;
    private JTextField username;
    private JPasswordField password;
    private JButton register;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private ControllerKlient controllerKlient;

    public RegisterFrame(GUIcontroller guIcontroller, ControllerKlient controllerKlient) {
        this.guIcontroller = guIcontroller;
        this.controllerKlient = controllerKlient;
        setUpFrame();
    }

    public void setUpFrame() {
        setLayout(null);
        setSize(200, 400);

        setResizable(false);

        JLabel kris = new JLabel("KRIS");
        kris.setFont(new Font("Congenial", Font.BOLD, 30));
        kris.setSize(200, 40);
        kris.setLocation(0,10);
        kris.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(kris);
        JLabel regi = new JLabel("NYTT KONTO");
        regi.setFont(new Font("Congenial", Font.BOLD, 17));
        regi.setSize(200, 40);
        regi.setLocation(0,60);
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
        register = new JButton("Registrera");

        usernameLabel.setLocation(20, 115);
        usernameLabel.setSize(200, 15);
        username.setLocation(20, 130);
        username.setSize(150, 30);
        password.setSize(150, 30);
        passwordLabel.setLocation(20, 165);
        passwordLabel.setSize(200, 15);
        password.setLocation(20, 180);

        password.addKeyListener(this);
        register.setSize(100, 50);
        register.setLocation(50, 250);
        register.addActionListener(this);
        add(register);
        username.setVisible(true);
        password.setVisible(true);
        register.setVisible(true);
        usernameLabel.setVisible(true);
        passwordLabel.setVisible(true);
        setVisible(true);
    }

    public void register() {
        String s = new String(password.getPassword());
        controllerKlient.register(username.getText(), s);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == register) {
            register();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER && register.isEnabled()) {
            register();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }


}

