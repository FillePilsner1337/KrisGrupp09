

import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;


public class SwingWaypoint extends DefaultWaypoint implements Serializable {
    private final JButton button;
    private final String adress;

    private final String id;

    private final String kapacitet;


    public SwingWaypoint(String adress, String id, String kapacitet, GeoPosition kordinat) {
        super(kordinat);
        this.adress = adress;
        this.id = id;
        this.kapacitet = kapacitet;


        button = new JButton("S");
        button.setBackground(Color.red);



        button.setPreferredSize(new Dimension(10, 10));
        button.addMouseListener(new SwingWaypointMouseListener());
        button.setVisible(true);

    }

    JButton getButton() {
        return button;
    }

    private class SwingWaypointMouseListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            int response = JOptionPane.showConfirmDialog(null, "Fastighet: " + adress + "\nAntal platser: " + kapacitet + "\nId nummer " + id + "\n\nVill du checka in?" , "Skyddsrum nr " + id, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);



        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }
}
