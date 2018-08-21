package com.example.lenovo.dra;

import android.os.AsyncTask;
import android.util.Log;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GetNearbyPlacesData extends AsyncTask<Object, String, String > {

    String googlePlacesData;
    String url;
    String placeName, vicinity;
    double latitude, longitude;
    String phone;
    @Override
    protected String doInBackground(Object... params) {
        url = (String)params[0];
        //creating object of my class DownloadUrl
        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            googlePlacesData = downloadUrl.readUrl(url);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String s) {
        //recieving googlePlacesData here as String s
        Log.i("error", "recieved string is "+ s);
        List<HashMap<String, String>> nearByPlaceList;
        DataParser parser = new DataParser();
        //sending string to my DataParser class
        nearByPlaceList = parser.parse(s);
        showNearByPlaces(nearByPlaceList);
    }

    private void showNearByPlaces(List<HashMap<String, String>> nearByPlaceList){
        for(int i= 0; i<nearByPlaceList.size(); i++)
            {
                //MarkerOptions markerOptions = new MarkerOptions();
                HashMap<String, String> googlePlace = nearByPlaceList.get(i);
                placeName = googlePlace.get("place_name");
                vicinity = googlePlace.get("vicinity");
                latitude = Double.parseDouble(googlePlace.get("lat"));
                longitude = Double.parseDouble(googlePlace.get("lng"));
                phone = googlePlace.get("phone_no");

                PlacesDetails p = new PlacesDetails(placeName, vicinity,googlePlace.get("photos"), googlePlace.get("place_id"),
                                  phone, latitude,longitude, Double.parseDouble(googlePlace.get("rating")));
                PlaceDetailsStore.setPlaceDetails(p);

                Log.i("error", latitude+" "+longitude);

                //LatLng latLng = new LatLng(latitude, longitude);
                //markerOptions.position(latLng);
                //markerOptions.title(placeName+" : "+vicinity);
                //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                //mMap.addMarker(markerOptions);
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                //mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
            }
    }
}