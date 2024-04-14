package View;

import javax.swing.*;
import java.awt.*;

public class CentrePanel extends JPanel {

    private MainFrame mF;
    private MainPanel mP;
    private int width;
    private int height;
    private JLabel lblTest;

    public CentrePanel(MainFrame mF, MainPanel mP, int width, int height) {

        this.mF = mF;
        this.mP = mP;

        this.width = width;
        this.height = height;

        this.setBackground(Color.getHSBColor(120, 100, 240));
        this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        this.setLayout(null);

//        this.setSize(mF.getWidth() - 3, getHeight() - mP.getSouthPanel().getHeight() - 3);

        lblTest = new JLabel("No button is pressed yet.");
        lblTest.setHorizontalAlignment(SwingConstants.CENTER);
        lblTest.setVerticalAlignment(SwingConstants.CENTER);
        lblTest.setForeground(Color.black);
//        this.add(lblTest);

        makeLabel();
    }

    private void makeLabel() {

        int labelWidth = 200;//lblTest.getPreferredSize().width;
        int labelHeight = lblTest.getPreferredSize().height;

        int x = (width - labelWidth) / 2;
        int y = (height - labelHeight) / 2;

        lblTest.setBounds(x, y, labelWidth, labelHeight);


        this.add(lblTest);
    }

    public void setLabel(String str) {
        lblTest.setText(str);
        lblTest.repaint();
    }

}
