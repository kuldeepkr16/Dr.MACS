package com.example.lenovo.dra;

import java.util.ArrayList;

public class PlaceDetailsStore {
    private static ArrayList<PlacesDetails> placesDetailsList = new ArrayList<>();;


    public static void setPlaceDetails(PlacesDetails p){
        placesDetailsList.add(p);
    }

    public static ArrayList<PlacesDetails> getPlaceList() {
        return placesDetailsList;
    }
}
