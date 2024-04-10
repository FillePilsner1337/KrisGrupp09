import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;

public class Controller {

    private MainFrame mainFrame;
    private KartaController kc;
    public Controller(){
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }


         this.mainFrame = new MainFrame();
        this.kc = new KartaController(mainFrame, this);


    }


















    public static void main(String[] args) {
        new Controller();


    }


    public void checkIn(String id) {
        System.out.println("Metoden i controller för inchckning");
        mainFrame.getCheckInPanel().getCheckaUt().setEnabled(true);
        mainFrame.getCheckInPanel().getIncheckad().setText("Incheckad i skyddsrum " + id);
    }
}

