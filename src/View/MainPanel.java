package View;

import View.CentrePanels.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPanel extends JPanel {

    private MainFrame                 mF;
    private SouthPanel                sP;
    private CentrePanelKarta         cPK;
    private CentrePanelNotifications cPN;
    private CentrePanelInfo          cPI;
    private CentrePanelPrepLista     cPP;
    private NorthPanel                nP;
    private NotificationHandler       nH;

    private JLabel            scrollText;
    private JScrollPane       scrollPane;


    public MainPanel( MainFrame mF, int width, int height ) {

        this.mF = mF;

        this.setSize    (width, height);
        this.setLayout  (new BorderLayout());
        this.setVisible (true);

        /*//North
        nP = new NorthPanel(mF, this, width);
        nP.setVisible(false);
        this.add(nP, BorderLayout.NORTH);*/

        //Notification handler
//        this.scrollText = new JLabel();
        this.scrollPane = new JScrollPane();
        nH = new NotificationHandler(scrollPane, this);

        //South
        sP = new SouthPanel(mF);
        this.add(sP, BorderLayout.SOUTH);

        //Centre Karta
        cPK = new CentrePanelKarta(mF, this, width, height);
        this.add(cPK, BorderLayout.CENTER);

        //Centre Info
        cPI = new CentrePanelInfo(mF, this, width, height);
        this.add(cPI, BorderLayout.CENTER);

        //Centre Notifications
        cPN = new CentrePanelNotifications(mF, this, width, height);
        this.add(cPN, BorderLayout.CENTER);

        //Centre PrepLista
        cPP = new CentrePanelPrepLista(mF, this, width, height);
        this.add(cPP, BorderLayout.CENTER);

    }

/*
    public NorthPanel getNorthPanel() {

        return nP;
    }
*/

    public SouthPanel getSouthPanel() {

        return sP;
    }

    public CentrePanelKarta getCentrePanelKarta() {

        return cPK;
    }

    public CentrePanelInfo getCentrePanelInfo() {

        return cPI;
    }

    public CentrePanelNotifications getCentrePanelNotifications() {

        return cPN;
    }

    public CentrePanelPrepLista getCentrePanelPrepLista() {

        return cPP;
    }

    public NotificationHandler getNotificationHandler() {

        return nH;
    }

}


