package Client.View;

import Client.View.CheckInPanel;
import Client.View.KartPanel;

import javax.swing.*;
import java.awt.*;
import Client.Controller.*;

public class MainFrame extends JFrame {

    private JTabbedPane tabs = new JTabbedPane();
    private KartPanel kartPanel = new KartPanel();
    private CheckInPanel checkInPanel;
    private GUIcontroller guIcontroller;

    private VmaPanel vmaPanel;


    public  MainFrame(GUIcontroller guIcontroller) {
        this.guIcontroller = guIcontroller;
        this.setTitle("KRIS");
        this.setSize(800,600);
        this.checkInPanel = new CheckInPanel(guIcontroller);
        this.vmaPanel =new VmaPanel(this);
        setupGUI();

    }

    private void setupGUI() {

        tabs.addTab("Karta", kartPanel);
        tabs.addTab("Checka in/ut", checkInPanel);
        tabs.addTab("Visa VMA-meddelande",vmaPanel);
        this.add(tabs, BorderLayout.CENTER);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void setKartPanel(Component c){
        kartPanel.add(c, BorderLayout.CENTER);
    }

    public CheckInPanel getCheckInPanel() {
        return checkInPanel;
    }

    public VmaPanel getVmaPanel() {
        return vmaPanel;
    }

    public GUIcontroller getGuIcontroller() {
        return guIcontroller;
    }

    public JTabbedPane getTabs() {
        return tabs;
    }
}

