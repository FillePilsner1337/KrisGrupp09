package Client.View;

import Client.Controller.GUIcontroller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CheckInPanel extends JPanel implements ActionListener {
    private JLabel incheckad;
    private JButton checkaUtBtn;
    private JButton laggTillVannBtn;
    private  JList<String>  listAllaVanner;
    private  JList<String> listIncheckadeVanner;
    private DefaultListModel<String> modelAllaVanner;
    private DefaultListModel<String> modelIncheckaddeVanner;
    private GUIcontroller guIcontroller;



    public CheckInPanel(GUIcontroller guIcontroller){
        this.guIcontroller = guIcontroller;
        incheckad = new JLabel("Du är inte incheckad");
        checkaUtBtn = new JButton("Checka ut");
        laggTillVannBtn = new JButton("Lägg till vän");
        this.modelIncheckaddeVanner = new DefaultListModel<>();
        this.modelAllaVanner = new DefaultListModel<>();

        listAllaVanner = new JList<>(modelAllaVanner);
        listIncheckadeVanner = new JList<>(modelIncheckaddeVanner);
        setUp();
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
                    System.out.println("Run i check in panel");
                }
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
                    System.out.println("Run i chackinpanel");
                }
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



}
