package Client.View;

import Client.Controller.ClientController;
import Client.Controller.GUIcontroller;
import Client.Controller.SearchCityController;
import Client.Model.CityObject;
import org.jxmapviewer.viewer.GeoPosition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Klass för att visa ett fönster för sök funktionen i GUI
 * @Author Ola Persson, Jonatan Tempel
 */
public class SearchFrame extends JFrame implements ActionListener, KeyListener {

    private ClientController clientController;
    private GUIcontroller guiController;
    private SearchCityController searchCityController;
    private JTextField searchTextField;
    private JButton searchButton;
    private JButton confirmButton;
    private JLabel searchLabel;
    private JList<CityObject> searchResult;
    private DefaultListModel<CityObject> modelSearchResult;

    public SearchFrame(GUIcontroller guiController) {
        this.guiController = guiController;
        setUpFrame();
        setSearchResultKeyListener();
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
    public void setUpFrame() {
        setLayout(null);
        setSize(335, 410);
        setLocation(guiController.getMainFrame().getX()+250, guiController.getMainFrame().getY()+100);
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

        searchButton = new JButton("Sök");
        searchButton.setSize(60, 28);
        searchButton.setLocation(180, 25);
        searchButton.setEnabled(true);
        searchButton.setVisible(true);
        searchButton.addActionListener(this);
        add(searchButton);

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
                        guiController.getMapController().startPosition(pos);
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
        guiController.searchCity(s);
    }

    public void showOnMap(){
        try {
            int index = searchResult.getSelectedIndex();
            GeoPosition pos = searchResult.getModel().getElementAt(index).getGeoPosition();
            guiController.getMapController().startPosition(pos);
            modelSearchResult.clear();
            searchTextField.setText("");
            setVisible(false);
        }
        catch (ArrayIndexOutOfBoundsException e){
            guiController.displayMessage(this, "Felaktig input");
            GeoPosition defaultPos = new GeoPosition(55.610348059975394, 12.994770622696002);
            guiController.getMapController().startPosition(defaultPos);
            modelSearchResult.clear();
            searchTextField.setText("");
            setVisible(false);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
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

