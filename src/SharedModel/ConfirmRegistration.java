package SharedModel;

import java.io.Serializable;

/**
 * Objekt som skickas för att bekräfta en registrering
 * @author Ola Persson och Jonatan Tempel
 */
public class ConfirmRegistration implements Serializable {
    private static final long serialVersionUID = 2;
    private boolean ok = true;
}
