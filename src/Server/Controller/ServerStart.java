package Server.Controller;


import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

public class ServerStart {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new ServerController();
    }
}
