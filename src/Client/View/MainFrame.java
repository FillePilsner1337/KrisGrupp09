package Client.View;

import javax.swing.*;
import java.awt.*;
import Client.Controller.*;

/**
 * Klass som används som huvudklass för GUI.
 * @Author Ola Persson, Jonatan Tempel
 */
public class MainFrame extends JFrame {

    private JTabbedPane tabs = new JTabbedPane();
    private MapPanel mapPanel = new MapPanel();
    private InfoFriendsPanel infoFriendsPanel;
    private GUIcontroller guiController;
    private VMApanel vmaPanel;



    public  MainFrame(GUIcontroller guiController) {
        this.guiController = guiController;
        this.setTitle("KRIS");
        this.setSize(800,600);
        this.infoFriendsPanel = new InfoFriendsPanel(guiController);
        this.vmaPanel =new VMApanel(this);
        setupGUI();
        setIcons();

    }
    private void setIcons() {
        ImageIcon img = new ImageIcon("files/1.png");
        this.setIconImage(img.getImage());

        if (Taskbar.isTaskbarSupported()) {
            Taskbar taskbar = Taskbar.getTaskbar();
            try {
                Image image = img.getImage();
                taskbar.setIconImage(image);
            }
            catch (Exception e){

            }
        }
    }

    private void setupGUI() {
        tabs.addTab("Karta", mapPanel);
        tabs.addTab("Checka in/ut", infoFriendsPanel);
        tabs.addTab("Visa VMA-meddelande",vmaPanel);
        this.add(tabs, BorderLayout.CENTER);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void setMapPanel(Component c){
        mapPanel.add(c, BorderLayout.CENTER);
    }

    public InfoFriendsPanel getCheckInPanel() {
        return infoFriendsPanel;
    }

    public VMApanel getVmaPanel() {
        return vmaPanel;
    }

    public GUIcontroller getGuiController() {
        return guiController;
    }

    public JTabbedPane getTabs() {
        return tabs;
    }
}

