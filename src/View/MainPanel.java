package View;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {

    private MainFrame mF;
    private SouthPanel sP;
    private CentrePanel cP;

    public MainPanel( MainFrame mF, int width, int height ) {

        this.mF = mF;

        this.setSize(width, height);
        this.setLayout(new BorderLayout());
        this.setVisible(true);

        //South
        sP = new SouthPanel(mF);
        this.add(sP, BorderLayout.SOUTH);

        //Centre
        cP = new CentrePanel(mF, this, width, height);
        this.add(cP, BorderLayout.CENTER);
    }

    public SouthPanel getSouthPanel() {
        return sP;
    }

    public CentrePanel getCentrePanel() {
        return cP;
    }

}
