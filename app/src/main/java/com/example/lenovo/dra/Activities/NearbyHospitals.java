package com.example.lenovo.dra.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.lenovo.dra.GetNearbyPlacesData;
import com.example.lenovo.dra.PlaceDetailsStore;
import com.example.lenovo.dra.PlacesDetails;
import com.example.lenovo.dra.R;
import java.util.ArrayList;

public class NearbyHospitals extends AppCompatActivity {

    ImageView btnBackTop;
    ListView myList;
    ArrayAdapter<PlacesDetails> adapter;
    private ArrayList<PlacesDetails> placesDetailsListView = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_hospitals);

        btnBackTop = (ImageView) findViewById(R.id.btnBackTop);
        btnBackTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finish();
            }
        });

        adapter = new customAdapter();
        myList = (ListView) findViewById(R.id.myListView);

        //generating url
        String url = getUrl();
        //creating object of my class GetNearbyPlacesData
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
        getNearbyPlacesData.execute(url);
        Toast.makeText(NearbyHospitals.this, "Showing nearby hospitals", Toast.LENGTH_SHORT).show();

        ArrayList<PlacesDetails> plist = PlaceDetailsStore.getPlaceList();
        for(PlacesDetails p: plist){
            placesDetailsListView.add(p);
        }
        myList.setAdapter(adapter);
        myList.getDisplay();
        adapter.notifyDataSetChanged();
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlacesDetails myPlace = placesDetailsListView.get(position);
                Intent i= new Intent(NearbyHospitals.this, ActivityBooking.class);
                i.putExtra("placeName", myPlace.getNameOfPlace());
                i.putExtra("placeAddress", myPlace.getAddressOfPlace());
                i.putExtra("phoneNo", myPlace.getPhoneNo());
                startActivity(i);
            }
        });
    }
    private String getUrl(){

        String type = "hospital";

        //getting latitude and longitude from previous activity
        Bundle b = getIntent().getExtras();
        double latitude = b.getDouble("lat");
        double longitude = b.getDouble("lng");
        String mapRadius = b.getString("mapRadius");
        int mapRadii = Integer.parseInt(mapRadius);
        mapRadii = mapRadii*1000;
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location=").append(latitude).append(",").append(longitude);
        googlePlaceUrl.append("&radius="+mapRadii);
        googlePlaceUrl.append("&type="+type);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+"AIzaSyBGRlFwQLMnaVUsrzlp07kShIZqFwe8o1Y");
        return googlePlaceUrl.toString();
    }

    private class customAdapter extends ArrayAdapter<PlacesDetails>{
        public customAdapter() {
            super(NearbyHospitals.this, R.layout.hosp_list_row, placesDetailsListView);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.hosp_list_row, parent, false);
            }

            PlacesDetails myPlace = placesDetailsListView.get(position);

            ImageView img = (ImageView) convertView.findViewById(R.id.hospIcon);
            TextView myHeading = (TextView) convertView.findViewById(R.id.heading);
            TextView myDesc = (TextView) convertView.findViewById(R.id.description);

            //img.setImageResource(hosp);
            myHeading.setText(myPlace.getNameOfPlace());
            myDesc.setText(myPlace.getAddressOfPlace());

            return convertView;
        }
    }
}
