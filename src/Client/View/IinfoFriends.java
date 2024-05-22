package Client.View;

import java.util.ArrayList;

/**
 * Interface som används för att visa information som hör till vänlista-funktionen
 * @Author Ola Persson, Jonatan Tempel
 */
public interface IinfoFriends {

    public void displayFriends(ArrayList<String> friendList);
    public void displayFriendsInShelter(ArrayList<String> friendsInShelter);
}
