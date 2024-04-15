package View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {

    private MainPanel mP;
    private int width = 350;
    private int height = 650;
    private String notificationText;
    private boolean showNotification = false;

    public MainFrame() {

        this.setLayout(null);
        this.mP = new MainPanel(this, width, height);
        this.add(mP);

        this.notificationText = new String();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mP);
        this.setSize(width, height);
        this.setResizable(false);
        this.setVisible(true);
//        mainFrame.pack();

    }

    public void mouseClickedSouth(int i){

        //ToDO
        switch (i) {
            case 1:
                // ** Code for Controller to backend goes here **
//                startNotificationBanner("Testing 1, Testing 2, Testing 3, Testing 4, Testing 5, Testing 6, Testing 7, Testing 8, Testing 9, Testing 10");
                System.out.println( "Button: "+i+" pressed." );
                break;
            case 2:
                // ** Code for Controller to backend goes here **
                System.out.println( "Button: "+i+" pressed." );
                break;
            case 3:
                // ** Code for Controller to backend goes here **
                System.out.println( "Button: "+i+" pressed." );
                break;
            case 4:
                // ** Code for Controller to backend goes here **
                System.out.println( "Button: "+i+" pressed." );
                break;
        }

        centrePanelDisplaySelection(i);
    }

    public void mouseClickedNorth(){

        //ToDO

        // ** Code for Controller to backend goes here **

        System.out.println( "North panel pressed." );
        setShowNotificationFalse();

        centrePanelDisplaySelection(1);
    }


    private void centrePanelDisplaySelection(int i) {
        switch (i) {

            case 1:
                mP.getCentrePanelKarta().setVisible(true);
                mP.getCentrePanelNotifications().setVisible(false);
                mP.getCentrePanelInfo().setVisible(false);
                mP.getCentrePanelPrepLista().setVisible(false);
                break;
            case 2:
                mP.getCentrePanelKarta().setVisible(false);
                mP.getCentrePanelNotifications().setVisible(true);
                mP.getCentrePanelInfo().setVisible(false);
                mP.getCentrePanelPrepLista().setVisible(false);
                break;
            case 3:
                mP.getCentrePanelKarta().setVisible(false);
                mP.getCentrePanelNotifications().setVisible(false);
                mP.getCentrePanelInfo().setVisible(true);
                mP.getCentrePanelPrepLista().setVisible(false);
                break;
            case 4:
                mP.getCentrePanelKarta().setVisible(false);
                mP.getCentrePanelNotifications().setVisible(false);
                mP.getCentrePanelInfo().setVisible(false);
                mP.getCentrePanelPrepLista().setVisible(true);
                break;
        }
    }

    public void startNotificationBanner(String str) {

        notificationText = str;
        notificationThread nT = new notificationThread(this);
    }

    public void setShowNotificationFalse() {

        showNotification = false;
    }

    /*-------------------------------------------*/
    /*--- Inner class for notification thread ---*/
    /*-------------------------------------------*/
    private class notificationThread extends Thread {

        private MainFrame mF;
        private Timer timer;

        public notificationThread(MainFrame mF) {

            this.mF = mF;
            showNotification = true;

            mP.getNotificationHandler().startScroll(notificationText);
            mP.getNotificationHandler().setScrollTextVisible(true);

            timer = new Timer(3500, new TimerActionListener());
            timer.setRepeats(false);
            timer.start();
            start();
        }

        @Override
        public void run() {

            try {
                System.out.println("Notification thread running");
                while (showNotification) {
                    mP.getNotificationHandler().setScrollTextVisible(true);
                }
            } finally {
                System.out.println("Notification thread STOPPED");
                mP.getNotificationHandler().setScrollTextVisible(false);
                mP.getNotificationHandler().stopScroll();
            }
        }
    }

    /*-------------------------------------------*/
    /*--- Inner class for timer functionality ---*/
    /*-------------------------------------------*/
    private class TimerActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            setShowNotificationFalse();
        }
    }
}
