package com.example.lenovo.dra;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class HospitalRecyclerList extends AppCompatActivity {
    private RecyclerView mHospitalList;
    private HospitalAdapter HospitalAdapter;
    private List<PlacesDetails> usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_recycler_list);
        mHospitalList=(RecyclerView)findViewById(R.id.HospitalRe);
        usersList=new ArrayList<>();
        HospitalAdapter=new HospitalAdapter(usersList);
        mHospitalList.setHasFixedSize(true);
        mHospitalList.setLayoutManager(new LinearLayoutManager(this));
        mHospitalList.setAdapter(HospitalAdapter);

        //generating url
        String url = getUrl();
        //creating object of my class GetNearbyPlacesData
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
        getNearbyPlacesData.execute(url);
      //  Toast.makeText(HospitalAdapter.this, "Showing nearby hospitals", Toast.LENGTH_SHORT).show();


        ArrayList<PlacesDetails> plist = PlaceDetailsStore.getPlaceList();
        for(PlacesDetails p: plist){
            usersList.add(p);
            HospitalAdapter.notifyDataSetChanged();
        }
    }

    private String getUrl(){

        String type = "hospital";

        //getting latitude and longitude from previous activity
        Bundle b = getIntent().getExtras();
        double latitude = b.getDouble("lat");
        double longitude = b.getDouble("lng");

        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location=").append(latitude).append(",").append(longitude);
        googlePlaceUrl.append("&radius="+1000);
        googlePlaceUrl.append("&type="+type);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+"AIzaSyBGRlFwQLMnaVUsrzlp07kShIZqFwe8o1Y");
        return googlePlaceUrl.toString();
    }

}
