package Client.Controller;

import Client.Model.CityObject;
import Client.View.ImapDisplay;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Locale;


public class SearchCityController {
    private ArrayList<CityObject> cities = new ArrayList<>();
    private GUIcontroller guiController;
    private ControllerKlient controllerKlient;


    public SearchCityController(ControllerKlient controller, GUIcontroller guIcontroller){
        this.guiController = guIcontroller;
        this.controllerKlient = controller;
        loadfile();
    }

    private void loadfile() {

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
        guiController.diplaySearchResult(result);
    }

    public ArrayList<CityObject> getCities(){
        return cities;
    }


}
