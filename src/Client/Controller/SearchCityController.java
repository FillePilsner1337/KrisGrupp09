package Client.Controller;

import Client.Model.CityObject;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Klass som sköter logiken för det sökfönster som används till kartfunktionen.
 * @Author Ola Persson
 */
public class SearchCityController {
    private ArrayList<CityObject> cities = new ArrayList<>();
    private GUIcontroller guiController;
    private ClientController clientController;


    public SearchCityController(ClientController controller, GUIcontroller guIcontroller){
        this.guiController = guIcontroller;
        this.clientController = controller;
        loadFile();
    }

    /**
     * Metod som läser in filen cityObjects.
     */
    private void loadFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("files/cityObjects.dat"))) {
            cities = (ArrayList<CityObject>) ois.readObject();
        } catch (IOException e) {
            if (e instanceof EOFException) {
                System.out.println("Fel i inläsning av fil  cityObjects.dat ");
            } else {
                System.out.println("Fel i inläsning av fil  cityObjects.dat");
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Fel i inläsning av fil  cityObjects.dat");
        }

    }

    /**
     * Metod som visar städer som matchar den input användaren ger så länge användaren har matat in mer än 2 bokstäver. Om ingen match finns
     * visas inget i fönstret. Annars visas den/de städer som matchar det sökta namnet.
     * @param phrase Det användaren matar in för att söka efter städer.
     */
    public void search(String phrase){
        ArrayList<CityObject> result = new ArrayList<>();
        if (phrase != null && phrase.length() > 2  ) {
            String searchPhrase = phrase.toLowerCase(Locale.ROOT);
            int toBeIgnored = -1;
            for (int i = 0; i < cities.size(); i++) {
                if (cities.get(i).getCityName().toLowerCase(Locale.ROOT).equals(searchPhrase)) {
                    result.add(cities.get(i));
                    toBeIgnored = i;
                }
            }

            for (int i = 0; i < cities.size(); i++) {
                if (cities.get(i).getCityName().toLowerCase(Locale.ROOT).contains(searchPhrase)) {
                    if (i != toBeIgnored) {
                        result.add(cities.get(i));
                    }
                }
            }
        }
        guiController.displaySearchResult(result);
    }
}
