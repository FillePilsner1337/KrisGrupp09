import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private JTabbedPane tabs = new JTabbedPane();
    private KartPanel kartPanel = new KartPanel();
    private CheckInPanel checkInPanel = new CheckInPanel();

    private VmaPanel vmaPanel = new VmaPanel();


    public  MainFrame() {
        this.setTitle("KRIS");
        this.setSize(800,600);
        setupGUI();
        VmaController.fetchAndDisplayVmaData(vmaPanel);
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
}

