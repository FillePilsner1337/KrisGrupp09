package View;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private MainPanel mP;
    private int width = 350;
    private int height = 650;

    public MainFrame() {

        this.mP = new MainPanel(this, width, height);
        this.add(mP);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mP);
//        this.getContentPane().setBackground(Color.getHSBColor(120, 100, 240));
        this.setSize(width, height);
        this.setResizable(false);
        this.setVisible(true);
//        mainFrame.pack();

    }

    public void mouseClicked(int i){

        //ToDO
        switch (i) {
            case 0:
                this.mP.getCentrePanel().setLabel( "Button: "+i+" pressed." );
                System.out.println( "Button: "+i+" pressed." );
                break;
            case 1:
                this.mP.getCentrePanel().setLabel( "Button: "+i+" pressed." );
                System.out.println( "Button: "+i+" pressed." );
                break;
            case 2:
                this.mP.getCentrePanel().setLabel( "Button: "+i+" pressed." );
                System.out.println( "Button: "+i+" pressed." );
                break;
            case 3:
                this.mP.getCentrePanel().setLabel( "Button: "+i+" pressed." );
                System.out.println( "Button: "+i+" pressed." );
                break;
            default:
                this.mP.getCentrePanel().setLabel( "Button: "+i+" pressed." );
                System.out.println( "Button: "+i+" pressed." );
                break;
        }

    }



}
