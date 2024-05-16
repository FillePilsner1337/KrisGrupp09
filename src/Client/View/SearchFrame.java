package Client.View;

import Client.Controller.ControllerKlient;
import Client.Controller.GUIcontroller;
import Client.Controller.SearchCityController;
import Client.Model.CityObject;
import SharedModel.User;
import org.jxmapviewer.viewer.GeoPosition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;

public class SearchFrame extends JFrame implements ActionListener, KeyListener {

    private ControllerKlient controllerKlient;
    private GUIcontroller guIcontroller;
    private SearchCityController searchCityController;
    private JTextField searchTextField;
    private JButton searchBtn;
    private JButton confirmButton;
    private JLabel searchLabel;
    private JList<CityObject> searchResult;
    private DefaultListModel<CityObject> modelSearchResult;


    public SearchFrame(GUIcontroller guIcontroller) {
        this.guIcontroller = guIcontroller;
        setUpFrame();
        setSearchResultKeyListener();
    }



    public void setUpFrame() {
        setLayout(null);
        setSize(335, 400);
        setLocation(guIcontroller.getMainFrame().getX()+250, guIcontroller.getMainFrame().getY()+100);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setResizable(false);
        setTitle("Sök");

        searchLabel = new JLabel("Sök:");
        searchLabel.setSize(100,10);
        searchLabel.setLocation(20,10);
        searchLabel.setVisible(true);
        add(searchLabel);

        searchTextField = new JTextField();
        searchTextField.setSize(150, 30);
        searchTextField.setLocation(20,25);
        searchTextField.setVisible(true);
        searchTextField.addKeyListener(this);
        add(searchTextField);

        searchBtn = new JButton("Sök");
        searchBtn.setSize(60, 28);
        searchBtn.setLocation(180, 25);
        searchBtn.setEnabled(true);
        searchBtn.setVisible(true);
        searchBtn.addActionListener(this);
        add(searchBtn);

        confirmButton = new JButton("Ok");
        confirmButton.setSize(60, 28);
        confirmButton.setLocation(255, 340);
        confirmButton.setEnabled(false);
        confirmButton.setVisible(true);
        confirmButton.addActionListener(this);
        add(confirmButton);

        modelSearchResult = new DefaultListModel<CityObject>();
        searchResult = new JList<>(modelSearchResult);
        searchResult.setSize(300, 250);
        searchResult.setLocation(15,80 );
        searchResult.setVisible(true);
        JScrollPane scrollPane = new JScrollPane(searchResult);
        scrollPane.setSize(300, 250);
        scrollPane.setLocation(15,80 );
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVisible(true);


        add(scrollPane);
        searchResult.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2){
                    int index = searchResult.locationToIndex(e.getPoint());
                    if (index >= 0){
                        GeoPosition pos = searchResult.getModel().getElementAt(index).getGeoPosition();
                        guIcontroller.getKartaController().startPos(pos);
                        modelSearchResult.clear();
                        searchTextField.setText("");
                        setVisible(false);
                    }
                }
            }
        });
        setVisible(true);
    }



    public void search() {
        String s = searchTextField.getText();
        guIcontroller.searchCity(s);
    }

    public void showOnMap(){
        try {
            int index = searchResult.getSelectedIndex();
            GeoPosition pos = searchResult.getModel().getElementAt(index).getGeoPosition();
            guIcontroller.getKartaController().startPos(pos);
            modelSearchResult.clear();
            searchTextField.setText("");
            setVisible(false);
        }
        catch (ArrayIndexOutOfBoundsException e){
            guIcontroller.displayMessage(this, "Felaktig input");
            GeoPosition defaultPos = new GeoPosition(55.610348059975394, 12.994770622696002);
            guIcontroller.getKartaController().startPos(defaultPos);
            modelSearchResult.clear();
            searchTextField.setText("");
            setVisible(false);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchBtn) {
            search();
        }
        if (e.getSource() == confirmButton){
            showOnMap();
            confirmButton.setEnabled(false);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
               search();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public void updateSearchResult(ArrayList<CityObject> list){
        modelSearchResult.clear();
        modelSearchResult.addAll(list);
        if (!list.isEmpty()) {
            confirmButton.setEnabled(true);
        }
        revalidate();
        repaint();
    }

    public void setSearchResultKeyListener(){
        searchResult.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && !searchResult.isSelectionEmpty()){
                    showOnMap();
                    confirmButton.setEnabled(false);
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }
}

