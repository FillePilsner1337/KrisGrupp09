package View;

import javax.swing.*;

public class MainFrame extends JFrame {

    private MainPanel mP;
    private int width = 350;
    private int height = 650;

    public MainFrame() {

        this.mP = new MainPanel(this, width, height);
        this.add(mP);

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
            case 0:
                //Code for Controller to backend goes here
                System.out.println( "Button: "+i+" pressed." );
                break;
            case 1:
                //Code for Controller to backend goes here
                System.out.println( "Button: "+i+" pressed." );
                break;
            case 2:
                //Code for Controller to backend goes here
                System.out.println( "Button: "+i+" pressed." );
                break;
            case 3:
                //Code for Controller to backend goes here
                System.out.println( "Button: "+i+" pressed." );
                break;
        }

        centrePanelDisplaySelection(i);
    }

    public void mouseClickedNorth(){

        //ToDO

        //Code for Controller to backend goes here
        System.out.println( "North panel pressed." );

        centrePanelDisplaySelection(1);
    }


    private void centrePanelDisplaySelection(int i) {
        switch (i) {

            case 0:
                mP.getCentrePanelKarta().setVisible(true);
                mP.getCentrePanelNotifications().setVisible(false);
                mP.getCentrePanelInfo().setVisible(false);
                mP.getCentrePanelPrepLista().setVisible(false);
                break;
            case 1:
                mP.getCentrePanelKarta().setVisible(false);
                mP.getCentrePanelNotifications().setVisible(true);
                mP.getCentrePanelInfo().setVisible(false);
                mP.getCentrePanelPrepLista().setVisible(false);
                break;
            case 2:
                mP.getCentrePanelKarta().setVisible(false);
                mP.getCentrePanelNotifications().setVisible(false);
                mP.getCentrePanelInfo().setVisible(true);
                mP.getCentrePanelPrepLista().setVisible(false);
                break;
            case 3:
                mP.getCentrePanelKarta().setVisible(false);
                mP.getCentrePanelNotifications().setVisible(false);
                mP.getCentrePanelInfo().setVisible(false);
                mP.getCentrePanelPrepLista().setVisible(true);
                break;

        }
    }

}
