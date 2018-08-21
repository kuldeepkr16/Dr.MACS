package com.example.lenovo.dra.fragments;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.lenovo.dra.Activities.NearbyDentalClinics;
import com.example.lenovo.dra.Activities.NearbyHospitals;
import com.example.lenovo.dra.Activities.NearbyMedStores;
import com.example.lenovo.dra.Activities.NearbyVet;
import com.example.lenovo.dra.Permissions;
import com.example.lenovo.dra.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Objects;

public class homeFragment extends Permissions {

    private double lat,lng;
    private static final int REQUEST_PERMISSION = 10;
    private Spinner mapRadius;
    private String mapRadii;

    public homeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {

    //checking permissions using class Permissions
    requestAppPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE}, R.string.msg,REQUEST_PERMISSION);

        //dropdown for map radius

        mapRadius = (Spinner) view.findViewById(R.id.mapRadius);
        // Creating an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterBlood = ArrayAdapter.createFromResource(getActivity(),
                R.array.map_radius_list, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterBlood.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mapRadius.setAdapter(adapterBlood);


        ImageButton hospbtn = (ImageButton) view.findViewById(R.id.IbtnHospital);
        ImageButton medicalstorebtn = (ImageButton) view.findViewById(R.id.IbtnMedicalStore);
        ImageButton vetbtn = (ImageButton) view.findViewById(R.id.IbtnVet);
        ImageButton dentalbtn = (ImageButton) view.findViewById(R.id.IbtnDentHopital);
        hospbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateInputs()) {
                    Intent i = new Intent(getActivity(), NearbyHospitals.class);
                    Log.i("error", "before intent" + lat + lng);
                    mapRadii = mapRadius.getSelectedItem().toString();
                    Bundle b = new Bundle();
                    b.putDouble("lat", lat);
                    b.putDouble("lng", lng);
                    b.putString("mapRadius", mapRadii);
                    i.putExtras(b);
                    startActivity(i);
                }else{
                    Toast.makeText(getActivity(), "Please choose a radius", Toast.LENGTH_SHORT).show();
                }
            }

        });

        medicalstorebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateInputs()){
                Intent i = new Intent(getActivity(),NearbyMedStores.class);
                mapRadii = mapRadius.getSelectedItem().toString();
                Bundle b = new Bundle();
                b.putDouble("lat", lat);
                b.putDouble("lng", lng);
                b.putString("mapRadius", mapRadii);
                i.putExtras(b);
                startActivity(i);
                }else{
                    Toast.makeText(getActivity(), "Please choose a radius", Toast.LENGTH_SHORT).show();
                }
            }
        });

        vetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateInputs()){
                Intent i = new Intent(getActivity(),NearbyVet.class);
                mapRadii = mapRadius.getSelectedItem().toString();
                Bundle b = new Bundle();
                b.putDouble("lat", lat);
                b.putDouble("lng", lng);
                b.putString("mapRadius", mapRadii);
                i.putExtras(b);
                startActivity(i);
                }else{
                    Toast.makeText(getActivity(), "Please choose a radius", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dentalbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateInputs()){
                Intent i = new Intent(getActivity(),NearbyDentalClinics.class);
                mapRadii = mapRadius.getSelectedItem().toString();
                Bundle b = new Bundle();
                b.putDouble("lat", lat);
                b.putDouble("lng", lng);
                b.putString("mapRadius", mapRadii);
                i.putExtras(b);
                startActivity(i);
                }else{
                    Toast.makeText(getActivity(), "Please choose a radius", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //for validating map radius dropdown
    private boolean validateInputs() {
        boolean chk = true;
        if(Objects.equals(mapRadius.getSelectedItem().toString(), "Choose-")){
            chk = false;
        }
        return chk;
    }

    //getting current location of user
    private void getDeviceLocation(){
       FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        try{
            final Task location = mFusedLocationClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    Location currentLocation = (Location) task.getResult();
                    lat = currentLocation.getLatitude();
                    lng = currentLocation.getLongitude();
                    Log.i("error", "in device location Lat lng"+lat+lng);
                }else{
                    Toast.makeText(getActivity(),"Unable to find current location", Toast.LENGTH_SHORT).show();
                }
                }
            });
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }
    @Override
    public void onPermissionsGranted(int requestCode) {
        getDeviceLocation();
    }
}