package Client.View;


import Client.Controller.ClientController;
import Client.Controller.GUIcontroller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RegisterFrame extends JFrame implements ActionListener, KeyListener {

    private GUIcontroller guiController;
    private JTextField username;
    private JPasswordField password;
    private JButton register;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private ClientController clientController;
    private int defaultCloseOperation = DISPOSE_ON_CLOSE;

    public RegisterFrame(GUIcontroller guiController, ClientController clientController) {
        this.guiController = guiController;
        this.clientController = clientController;
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
        clientController.register(username.getText(), s);
    }

    protected void processWindowEvent(final WindowEvent e) {
        super.processWindowEvent(e);

        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            switch (defaultCloseOperation) {
                case HIDE_ON_CLOSE:
                    setVisible(false);
                    guiController.getLogInFrame().setVisible(true);
                    break;
                case DISPOSE_ON_CLOSE:
                    dispose();
                    guiController.getLogInFrame().setVisible(true);
                    break;
                case EXIT_ON_CLOSE:
                    // This needs to match the checkExit call in
                    // setDefaultCloseOperation
                    System.exit(0);
                    break;
                case DO_NOTHING_ON_CLOSE:
                default:
            }
        }
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

