package Controller;

import View.MainFrame;
import View.MainPanel;
import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIcontroller /*implements ActionListener*/ {

    MainFrame mainFrame;
//    JPanel panel1;
//    JButton btn1;
//    JLabel lbl1;

    int nbrClicks = 0;

    public GUIcontroller () {

        mainFrame = new MainFrame();

    }

}
