package Client.View;

import Client.Controller.GUIcontroller;
import Client.Model.ShelterWaypoint;
import org.jxmapviewer.viewer.GeoPosition;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;

public class InfoFriendsPanel extends JPanel implements ActionListener {
    private JLabel checkedIn;
    private JButton checkOutButton;
    private JButton addFriendButton;
    private  JList<String> listAllFriends;
    private  JList<String> listFriendsInShelter;
    private DefaultListModel<String> listAllFriendsModel;
    private DefaultListModel<String> listFriendsInShelterModel;
    private GUIcontroller guiController;

    private JButton showOnMapButton;



    public InfoFriendsPanel(GUIcontroller guiController){
        this.guiController = guiController;
        checkedIn = new JLabel("Du är inte incheckad");
        checkOutButton = new JButton("Checka ut");
        addFriendButton = new JButton("Lägg till vän");
        this.listFriendsInShelterModel = new DefaultListModel<>();
        this.listAllFriendsModel = new DefaultListModel<>();
        this.showOnMapButton = new JButton("Visa på karta");

        listAllFriends = new JList<>(listAllFriendsModel);
        listFriendsInShelter = new JList<>(listFriendsInShelterModel);
        setUp();
        setUpShowOnMapListeners();
    }

    private void setUp() {
        setLayout(null);
        checkedIn.setSize(400, 30);
        checkedIn.setLocation(200, 50);
        checkedIn.setFont(new Font(null, Font.BOLD, 15));
        checkOutButton.setSize(180, 30);
        checkOutButton.setLocation(200, 100);
        checkOutButton.setEnabled(false);
        checkOutButton.addActionListener(this);
        addFriendButton.setSize(180, 30);
        addFriendButton.setLocation(400, 100);
        addFriendButton.setEnabled(true);
        addFriendButton.addActionListener(this);
        add(checkedIn);
        add(checkOutButton);

        add(addFriendButton);

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

        listFriendsInShelter.setSize(180, 250);
        listFriendsInShelter.setLocation(400 , 180);
        listFriendsInShelter.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        showOnMapButton.setText("Visa på kartan");
        showOnMapButton.setSize(180, 40);
        showOnMapButton.setLocation(400,440);
        showOnMapButton.setEnabled(false);
        this.add(showOnMapButton);

        add(listFriendsInShelter);

        listAllFriends.setSize(180, 250);
        listAllFriends.setLocation(200 , 180);
        listAllFriends.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(listAllFriends);
    }

    public JList<String> getListAllFriends() {
        return listAllFriends;
    }

    public JList<String> getListFriendsInShelter() {
        return listFriendsInShelter;
    }

    public JLabel getCheckedIn() {
        return checkedIn;
    }

    public JButton getCheckOutButton() {
        return checkOutButton;
    }
    public void updateAllFriends(ArrayList<String> vanner) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                listAllFriendsModel.clear();
                for (String van : vanner) {
                    listAllFriendsModel.addElement(van);

                }
                revalidate();
                repaint();
            }
        });

    }


    public void updateFriendsInShelter(ArrayList<String> incheckade) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                listFriendsInShelterModel.clear();
                for (String incheckad : incheckade) {
                    listFriendsInShelterModel.addElement(incheckad);

                }
                repaint();
                revalidate();
            }

        });

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == checkOutButton) {
            checkOutButton.setEnabled(false);
            guiController.checkout();
        }
        if (e.getSource() == addFriendButton){
            guiController.requestToFollow();
        }
    }

    public void setUpShowOnMapListeners(){
        listFriendsInShelter.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()){
                    String nameAndID = listFriendsInShelter.getSelectedValue();
                    if (nameAndID != null) {
                        showOnMapButton.setEnabled(true);
                    }
                }
            }
        });

        showOnMapButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nameAndID = listFriendsInShelter.getSelectedValue();
                if (nameAndID != null){
                    String[] parts = nameAndID.split(",");
                    String shelterId = parts[1].substring(1);
                    System.out.println(parts[1]);
                    System.out.println(shelterId);
                    HashSet<ShelterWaypoint> waypoints = guiController.getMapController().getWaypoints();
                    for (ShelterWaypoint wayPoint : waypoints){
                        if (wayPoint.getIdNumber().matches(shelterId)){
                            GeoPosition friendsLocation = wayPoint.getGeoPosition();
                            guiController.getMapController().takeMeHereFriendLocation(friendsLocation);
                            guiController.getMainFrame().getTabs().setSelectedIndex(0);
                        }
                    }
                    }
                }
        });
    }




}
