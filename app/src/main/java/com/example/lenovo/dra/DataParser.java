package com.example.lenovo.dra;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataParser {
    private HashMap<String, String> getPlace(JSONObject googlePlaceJson){
        HashMap<String, String> googlePlacesMap = new HashMap<>();
        String placeName = "-NA-";
        String vicinity = "-NA-";
        String latitude = "";
        String longitude = "";
        String reference = "";
        String photos = "";
        String place_id = "";
        String phone_no = "";
        Double rating = 0.0;
        try{
            if(!googlePlaceJson.isNull("name")){
                placeName = googlePlaceJson.getString("name");
            }
            if(!googlePlaceJson.isNull("vicinity")){
                vicinity = googlePlaceJson.getString("vicinity");
            }
            if(!googlePlaceJson.isNull("rating")){
                rating = Double.parseDouble(googlePlaceJson.getString("rating"));
            }
            if(!googlePlaceJson.isNull("place_id")){
                place_id = googlePlaceJson.getString("place_id");
            }
            if(!googlePlaceJson.isNull("photos")){
                photos = googlePlaceJson.getString("photos");
            }

            if(!googlePlaceJson.isNull("phone_no")){
                photos = googlePlaceJson.getString("phone_no");
            }
            latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");

            reference = googlePlaceJson.getString("reference");

            googlePlacesMap.put("place_name", placeName);
            googlePlacesMap.put("vicinity", vicinity);
            googlePlacesMap.put("lat", latitude);
            googlePlacesMap.put("lng", longitude);
            googlePlacesMap.put("reference", reference);
            googlePlacesMap.put("rating", rating+"");
            googlePlacesMap.put("place_id", place_id);
            googlePlacesMap.put("photos", photos);
            googlePlacesMap.put("phone_no", phone_no);

        }catch (JSONException e){
            e.printStackTrace();
        }
        return googlePlacesMap;
    }

    private List<HashMap<String, String>> getPlaces(JSONArray jsonArray){
        int count = jsonArray.length();
        List<HashMap<String, String>> placesList = new ArrayList<>();
        HashMap<String, String> placeMap;

        for(int i=0; i<count; i++){
            try {
                placeMap = getPlace((JSONObject) jsonArray.get(i));
                placesList.add(placeMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return placesList;
    }

    public List<HashMap<String, String>> parse(String jsonData){
      //  Log.i("error", "14. inside DataParser class");
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getPlaces(jsonArray);
    }
}