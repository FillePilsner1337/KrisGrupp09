package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NorthPanel extends JPanel implements ActionListener {

    private MainFrame mF;
    private MainPanel mP;
    private int width;
    private JLabel scrollText;


    public NorthPanel(MainFrame mF,MainPanel mP, int width) {

        this.mF = mF;
        this.mP = mP;
        this.width = width;

        setLayout(new FlowLayout());

        scrollText = new JLabel();
        scrollText.setHorizontalTextPosition(JLabel.CENTER);
        scrollText.setForeground(Color.BLUE);
        scrollText.setFont(new Font("Arial", Font.BOLD, 16));
        scrollText.setOpaque(false);

        add(scrollText);

        //Make the panel clickable
        addMouseListener( new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mF.mouseClickedNorth();
            }
        }               );//End method
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mF.mouseClickedNorth();
    }

    public void startScroll(String text) {

        scrollText.setText(text);

        Rectangle bounds = scrollText.getBounds();
        int newX = bounds.x - 1;
        if (newX + bounds.width < 0) {
            newX = getWidth();
        }
        scrollText.setBounds(newX, bounds.y, bounds.width, bounds.height);
    }




}
