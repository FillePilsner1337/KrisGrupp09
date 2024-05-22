package SharedModel;

import java.io.Serializable;

/**
 * Objekt som skickas för att bekräfta inloggning
 * @author Ola Persson och Jonatan Tempel
 */
public class ConfirmLogin implements Serializable {
    private static final long serialVersionUID = 1;
    private boolean ok = true;
}
