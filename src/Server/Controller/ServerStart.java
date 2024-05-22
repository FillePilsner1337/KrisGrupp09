package Server.Controller;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;

/**
 *  Klassen startar servern och sätter LookAndFeel för GUI komponenter
 *
 *  @author Ola Persson och Jonatan Tempel
 */

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
