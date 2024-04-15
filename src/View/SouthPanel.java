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
        this.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        this.setPreferredSize(new Dimension(75, 75));

        createButtons();

        this.add(btn1);
        this.add(btn2);
        this.add(btn3);
        this.add(btn4);
    }

    private void createButtons(){

        btn1 = new JButton();
        btn2 = new JButton();
        btn3 = new JButton();
        btn4 = new JButton();

        btn1.setText("<html>Karta</html>");
        btn2.setText("<html>Aviseringar</html>");
        btn3.setText("<html>Info</html>");
        btn4.setText("<html>Prepperlista</html>");

        Dimension buttonSize = new Dimension(83, 75);
        btn1.setPreferredSize(buttonSize);
        btn2.setPreferredSize(buttonSize);
        btn3.setPreferredSize(buttonSize);
        btn4.setPreferredSize(buttonSize);

        btn1.addActionListener(this);
        btn2.addActionListener(this);
        btn3.addActionListener(this);
        btn4.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if      (e.getSource() == btn1) {
            mF.mouseClickedSouth(1);
        }

        else if (e.getSource() == btn2) {
            mF.mouseClickedSouth(2);
        }

        else if (e.getSource() == btn3) {
            mF.mouseClickedSouth(3);
        }

        else if (e.getSource() == btn4) {
            mF.mouseClickedSouth(4);
        }
    }

    public void setNotificationText(String str) {

        Timer timer = new Timer(100, this);
        timer.start();

    }
}
