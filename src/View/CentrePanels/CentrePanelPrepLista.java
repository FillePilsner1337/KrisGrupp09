package View.CentrePanels;

import View.MainFrame;
import View.MainPanel;

import javax.swing.*;
import java.awt.*;

public class CentrePanelPrepLista extends JPanel {

    private MainFrame mF;
    private MainPanel mP;
    private int width;
    private int height;

    public CentrePanelPrepLista(MainFrame mF, MainPanel mP, int width, int height) {

        this.mF = mF;
        this.mP = mP;

        this.width = width;
        this.height = height;

        this.setBackground(Color.getHSBColor(120, 100, 240));
        this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        this.setLayout(null);

        makeSomething();

    }

    private void makeSomething() {

    }

}
