package Client.View;

import Client.Model.VMAobject;
import org.jxmapviewer.viewer.GeoPosition;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class VMApanel extends JPanel {

    private JScrollPane scrollPane;
    private MainFrame mainFrame;
    private JList<VMAobject> vmaHeadlines;
    private DefaultListModel<VMAobject> listModel;
    private JTextArea vmaMessage;

    private JButton showOnMap;

    public VMApanel(MainFrame mainframe) {
        this.mainFrame = mainframe;
        setLayout(null);
        listModel = new DefaultListModel<>();
        vmaHeadlines = new JList<>(listModel);
        vmaMessage = new JTextArea();
        this.showOnMap = new JButton();
        setUpPanel();
        setUpListeners();
    }

    private void setUpPanel() {
        JLabel headline = new JLabel("VMA: ");
        headline.setText("VMA: ");
        headline.setLocation(100, 50);
        headline.setVisible(true);
        headline.setFont(new Font(null, Font.BOLD, 15));
        headline.setSize(180,50);
        this.add(headline);

        JLabel info = new JLabel("Mer information: ");
        info.setLocation(350, 50);
        info.setVisible(true);
        info.setFont(new Font(null, Font.BOLD, 15));
        info.setSize(250,50);
        this.add(info);

        vmaHeadlines.setSize(180, 300);
        vmaHeadlines.setLocation(100, 100);
        vmaHeadlines.setVisible(true);
        this.add(vmaHeadlines);

        vmaMessage.setSize(300,300);
        vmaMessage.setLocation(350,100);
        vmaMessage.setVisible(true);
        vmaMessage.setLineWrap(true);
        vmaMessage.setWrapStyleWord(true);
        this.add(vmaMessage);

        showOnMap.setEnabled(true);
        showOnMap.setText("Visa på kartan");
        showOnMap.setSize(300, 40);
        showOnMap.setLocation(350,420);
        this.add(showOnMap);
    }
    private void setUpListeners() {
        vmaHeadlines.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    VMAobject selectedObject = vmaHeadlines.getSelectedValue();
                    if (selectedObject != null) {
                        vmaMessage.setText(selectedObject.getDetailedInfo());  // Antag att getDetailedInfo() ger den detaljerade informationen
                    }
                }
            }
        });
        showOnMap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VMAobject selectedObject = vmaHeadlines.getSelectedValue();
                if (selectedObject != null) {
                    GeoPosition position = selectedObject.getGeoPosition();
                    mainFrame.getGuiController().getMapController().takeMeHere(position);
                    mainFrame.getTabs().setSelectedIndex(0);

                }
            }
        });

    }

    public void addVma(ArrayList<VMAobject> vmaObjects) {
        listModel.clear();
        for (VMAobject vmaObject : vmaObjects) {
            listModel.addElement(vmaObject);
        }
    }
}
