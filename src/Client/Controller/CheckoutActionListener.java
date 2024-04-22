package Client.Controller;

import Server.Model.InUtStatus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CheckoutActionListener implements ActionListener {
    private ControllerKlient ck;

    public CheckoutActionListener(ControllerKlient ck){
        this.ck = ck;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ck.getMainFrame().getCheckInPanel().getCheckaUt().setEnabled(false);
        ck.getMainFrame().getCheckInPanel().getIncheckad().setText(ck.getUser().getUserName()+  ", Du är inte incheckad");
        InUtStatus utcheckning = new InUtStatus(false, null,null);
        ck.getUser().setInUtStatus(utcheckning);
        ck.getServerConnection().sendObject(utcheckning);
    }
}
