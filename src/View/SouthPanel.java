package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SouthPanel extends JPanel implements ActionListener {

    JButton btn1;
    JButton btn2;
    JButton btn3;
    JButton btn4;

    MainFrame mF;

    public SouthPanel(MainFrame mF) {

        this.mF = mF;

        this.setBorder(BorderFactory.createEmptyBorder());
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setPreferredSize(new Dimension(75, 75));

        createButtons();

        this.add(btn1);
        this.add(btn2);
        this.add(btn3);
        this.add(btn4);
    }

    private void createButtons(){

        btn1 = new JButton("Karta");
        btn2 = new JButton("Aviseringar");
        btn3 = new JButton("Info");
        btn4 = new JButton("PrepperLista");

        btn1.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        btn2.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        btn3.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        btn4.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        btn1.addActionListener(this);
        btn2.addActionListener(this);
        btn3.addActionListener(this);
        btn4.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if      (e.getSource() == btn1) {
            mF.mouseClicked(1);
        }

        else if (e.getSource() == btn2) {
            mF.mouseClicked(2);
        }

        else if (e.getSource() == btn3) {
            mF.mouseClicked(3);
        }

        else if (e.getSource() == btn4) {
            mF.mouseClicked(4);
        }
    }
}
