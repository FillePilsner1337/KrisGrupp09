package Client.View;

import Client.Controller.GUIcontroller;
import Client.Model.KrisWayPoint;
import org.jxmapviewer.viewer.GeoPosition;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;

public class CheckInPanel extends JPanel implements ActionListener {
    private JLabel incheckad;
    private JButton checkaUtBtn;
    private JButton laggTillVannBtn;
    private  JList<String>  listAllaVanner;
    private  JList<String> listIncheckadeVanner;
    private DefaultListModel<String> modelAllaVanner;
    private DefaultListModel<String> modelIncheckaddeVanner;
    private GUIcontroller guIcontroller;

    private JButton showOnMap;



    public CheckInPanel(GUIcontroller guIcontroller){
        this.guIcontroller = guIcontroller;
        incheckad = new JLabel("Du är inte incheckad");
        checkaUtBtn = new JButton("Checka ut");
        laggTillVannBtn = new JButton("Lägg till vän");
        this.modelIncheckaddeVanner = new DefaultListModel<>();
        this.modelAllaVanner = new DefaultListModel<>();
        this.showOnMap = new JButton("Visa på karta");

        listAllaVanner = new JList<>(modelAllaVanner);
        listIncheckadeVanner = new JList<>(modelIncheckaddeVanner);
        setUp();
        setUpShowOnMapListeners();
    }

    private void setUp() {
        setLayout(null);
        incheckad.setSize(400, 30);
        incheckad.setLocation(200, 50);
        incheckad.setFont(new Font(null, Font.BOLD, 15));
        checkaUtBtn.setSize(180, 30);
        checkaUtBtn.setLocation(200, 100);
        checkaUtBtn.setEnabled(false);
        checkaUtBtn.addActionListener(this);
        laggTillVannBtn.setSize(180, 30);
        laggTillVannBtn.setLocation(400, 100);
        laggTillVannBtn.setEnabled(true);
        laggTillVannBtn.addActionListener(this);
        add(incheckad);
        add(checkaUtBtn);

        add(laggTillVannBtn);

        JLabel allaVanner = new JLabel("Alla vänner");
        JLabel incheckadeVanner = new JLabel("Incheckade vänner");
        allaVanner.setSize(200, 30);
        allaVanner.setLocation(200, 150);
        allaVanner.setFont(new Font(null, Font.BOLD, 15));

        incheckadeVanner.setSize(200, 30);
        incheckadeVanner.setLocation(400, 150);
        incheckadeVanner.setFont(new Font(null, Font.BOLD, 15));

        add(allaVanner);
        add(incheckadeVanner);

        listIncheckadeVanner.setSize(180, 250);
        listIncheckadeVanner.setLocation(400 , 180);
        listIncheckadeVanner.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        showOnMap.setText("Visa på kartan");
        showOnMap.setSize(180, 40);
        showOnMap.setLocation(400,440);
        showOnMap.setEnabled(false);
        this.add(showOnMap);

        add(listIncheckadeVanner);

        listAllaVanner.setSize(180, 250);
        listAllaVanner.setLocation(200 , 180);
        listAllaVanner.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(listAllaVanner);
    }

    public JList<String> getListAllaVanner() {
        return listAllaVanner;
    }

    public JList<String> getListIncheckadeVanner() {
        return listIncheckadeVanner;
    }

    public JLabel getIncheckad() {
        return incheckad;
    }

    public JButton getCheckaUt() {
        return checkaUtBtn;
    }
    public void updateAllaVanner(ArrayList<String> vanner) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                modelAllaVanner.clear();
                for (String van : vanner) {
                    modelAllaVanner.addElement(van);

                }
                revalidate();
                repaint();
            }
        });

    }


    public void updateIncheckadeVanner(ArrayList<String> incheckade) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                modelIncheckaddeVanner.clear();
                for (String incheckad : incheckade) {
                    modelIncheckaddeVanner.addElement(incheckad);

                }
                repaint();
                revalidate();
            }

        });

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == checkaUtBtn) {
            checkaUtBtn.setEnabled(false);
            guIcontroller.checkout();
        }
        if (e.getSource() == laggTillVannBtn){
            guIcontroller.reqToFollow();
        }
    }

    public void setUpShowOnMapListeners(){
        listIncheckadeVanner.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()){
                    String nameAndID = listIncheckadeVanner.getSelectedValue();
                    if (nameAndID != null) {
                        showOnMap.setEnabled(true);
                    }
                }
            }
        });

        showOnMap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nameAndID = listIncheckadeVanner.getSelectedValue();
                if (nameAndID != null){
                    String[] parts = nameAndID.split(",");
                    String shelterId = parts[1].substring(1);
                    System.out.println(parts[1]);
                    System.out.println(shelterId);
                    HashSet<KrisWayPoint> waypoints = guIcontroller.getKartaController().getWaypoints();
                    for (KrisWayPoint wayPoint : waypoints){
                        if (wayPoint.getId().matches(shelterId)){
                            GeoPosition friendsLocation = wayPoint.getGeo();
                            guIcontroller.getKartaController().takeMeHereFriendLocation(friendsLocation);
                            guIcontroller.getMainFrame().getTabs().setSelectedIndex(0);
                        }
                    }
                    }
                }
        });
    }




}
