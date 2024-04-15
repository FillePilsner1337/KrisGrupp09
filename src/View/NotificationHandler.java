package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NotificationHandler {

//    private JLabel      scrollText;
    private Timer      scrollTimer;
    private MainPanel           mP;
    private JScrollPane scrollPane;
    private JTextArea     textArea;


    public NotificationHandler(JScrollPane scrollPane, MainPanel mP) {

        this.mP = mP;
        this.scrollPane = scrollPane;

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setOpaque(false);

        textArea.setForeground(Color.BLUE);
        textArea.setFont(new Font("Arial", Font.BOLD, 16));
        textArea.setVisible(false);

        scrollPane.setViewportView(textArea);
        scrollPane.setForeground(Color.BLUE);
        scrollPane.setFont(new Font("Arial", Font.BOLD, 16));
        scrollPane.setVisible(false);

        mP.add(scrollPane, BorderLayout.NORTH);
    }


    public void setScrollTextVisible(boolean b) {
        scrollPane.setVisible(b);
        textArea.setVisible(b);
    }

    public void startScroll(String text) {

        textArea.setText(text);

        mP.revalidate();

        scrollTimer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Rectangle bounds = textArea.getBounds();
                int currentX = bounds.x - 1;
                if (currentX + bounds.width < 0) {
                    textArea.setLocation(scrollPane.getWidth(), textArea.getY()); // Reset to right edge of scroll pane
                } else {
                    textArea.setLocation(currentX, textArea.getY());
                }
            }
        });
    scrollTimer.start();
    }

    public void stopScroll() {
        scrollTimer.stop();
    }



}
