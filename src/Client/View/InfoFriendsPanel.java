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

/**
 * GUI klass som visar information om användaren är incheckad och i vilket skyddsrum hen är incheckad i.
 * Visar också användarens kontaktlista och vilka skyddsrum hens kontakter är incheckade i.
 * @Author Ola Persson, Jonatan Tempel
 */
public class InfoFriendsPanel extends JPanel implements ActionListener {
    private JLabel checkedIn;
    private JButton checkOutButton;
    private JButton addFriendButton;
    private  JList<String> listAllFriends;
    private  JList<String> listFriendsInShelter;
    private DefaultListModel<String> listAllFriendsModel;
    private DefaultListModel<String> listFriendsInShelterModel;
    private GUIcontroller guiController;
    private JButton removeFriend;
    private JButton showOnMapButton;

    public InfoFriendsPanel(GUIcontroller guiController){
        this.guiController = guiController;
        checkedIn = new JLabel("Du är inte incheckad");
        checkOutButton = new JButton("Checka ut");
        addFriendButton = new JButton("Lägg till vän");

        this.listFriendsInShelterModel = new DefaultListModel<>();
        this.listAllFriendsModel = new DefaultListModel<>();
        this.showOnMapButton = new JButton("Visa på karta");
        this.removeFriend = new JButton("Ta bort vän");

        listAllFriends = new JList<>(listAllFriendsModel);
        listFriendsInShelter = new JList<>(listFriendsInShelterModel);
        setUp();
        setUpShowOnMapListeners();
        setUpRemoveFriendListeners();
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

        removeFriend.setSize(180, 30);
        removeFriend.setLocation(200, 440);
        removeFriend.setEnabled(false);
        //removeFriend.addActionListener(this);
        add(removeFriend);

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
        showOnMapButton.setSize(180, 30);
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
    public void updateAllFriends(ArrayList<String> friends) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                listAllFriendsModel.clear();
                for (String friend : friends) {
                    listAllFriendsModel.addElement(friend);

                }
                revalidate();
                repaint();
            }
        });

    }


    public void updateFriendsInShelter(ArrayList<String> checkedInFriends) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                listFriendsInShelterModel.clear();
                for (String checkedIn : checkedInFriends) {
                    listFriendsInShelterModel.addElement(checkedIn);
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
        /*
        if (e.getSource() == removeFriend){
            String namn = listAllFriends.getSelectedValue();
            guiController.removeFrind(namn);

        }
         */
    }

    public void setUpRemoveFriendListeners(){
        listAllFriends.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()){
                    String name = listAllFriends.getSelectedValue();
                    if (name != null) {
                        removeFriend.setEnabled(true);
                    }
                }
            }
        });
        removeFriend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = listAllFriends.getSelectedValue();
                guiController.removeFrind(name);
                listAllFriends.clearSelection();
                if (listAllFriends.isSelectionEmpty()){
                    removeFriend.setEnabled(false);
                }
            }
        });
    }


    /**
     * Metod som lyssnar efter en användares input. Om en användare markerar en incheckad vän (som finns) så sätts showOnMapButtons enable till ture.
     */
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
        /**
         * Metod som tar skyddsrumsID från den markerade användaren i listan och loopar igenom alla waypoints för att hitta vilket
         * skyddsrum som matchar den ID som användaren är incheckad i. Sedan visas det skyddsrummet på kartan.
         */
        showOnMapButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nameAndID = listFriendsInShelter.getSelectedValue();
                if (nameAndID != null){
                    String[] parts = nameAndID.split(",");
                    String shelterId = parts[1].substring(1);
                    HashSet<ShelterWaypoint> waypoints = guiController.getMapController().getWaypoints();
                    for (ShelterWaypoint wayPoint : waypoints){
                        if (wayPoint.getIdNumber().matches(shelterId)){
                            GeoPosition friendsLocation = wayPoint.getGeoPosition();
                            guiController.getMapController().takeMeHereFriendLocation(friendsLocation);
                            guiController.getMainFrame().getTabs().setSelectedIndex(0);
                        }
                    }
                    listFriendsInShelter.clearSelection();
                    }
                if (listFriendsInShelter.isSelectionEmpty()){
                    showOnMapButton.setEnabled(false);
                }
                }
        });
    }




}
