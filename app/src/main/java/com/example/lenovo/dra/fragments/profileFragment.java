package com.example.lenovo.dra.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.lenovo.dra.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profileFragment extends Fragment {

    private DatabaseReference mDatabse;
    private FirebaseAuth mAuth;
    String GENDER;
    String HOUSENO;
    String LOCALITY;
    String CITY;
    String STATE;
    String PIN;
    String USERNAME;
    String EMAIL;
    String USERTYPE;
    String PHONE;
    SharedPreferences sharedPreferences;
    TextView txtpUserName;
    TextView txtpEmailId;
    TextView txtpPhone;
    TextView txtpGender;
    TextView txtpHouseNo;
    TextView txtpLocality;
    TextView txtpCity;
    TextView txtpState;
    TextView txtpPin;

    public profileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        txtpUserName = (TextView) getView().findViewById(R.id.txtpUserName);
        txtpEmailId =  (TextView) getView().findViewById(R.id.txtpEmailId);
        txtpPhone = (TextView) getView().findViewById(R.id.txtpPhone);
        txtpGender = (TextView) getView().findViewById(R.id.txtpGender);
        txtpHouseNo = (TextView) getView().findViewById(R.id.txtpHouseNo);
        txtpLocality = (TextView) getView().findViewById(R.id.txtpLocality);
        txtpCity = (TextView) getView().findViewById(R.id.txtpCity);
        txtpState = (TextView) getView().findViewById(R.id.txtpState);
        txtpPin = (TextView) getView().findViewById(R.id.txtpPin);

        //get firebase auth instance
        mAuth = FirebaseAuth.getInstance();

        //getting values of current user using shared preferences
        sharedPreferences = getActivity().getSharedPreferences("currentUserData", Context.MODE_PRIVATE);
        USERTYPE = sharedPreferences.getString("USERTYPE","");
        mDatabse = FirebaseDatabase.getInstance().getReference().child(USERTYPE);
        ShowProfile();
    }


    // storing user details to Profilepojo class
    public void ShowProfile(){
        final String currentUID = mAuth.getCurrentUser().getUid();
        mDatabse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(currentUID)){
                    USERNAME = dataSnapshot.child(currentUID).child("name").getValue().toString();
                    EMAIL = dataSnapshot.child(currentUID).child("email").getValue().toString();
                    PHONE = dataSnapshot.child(currentUID).child("phoneNo").getValue().toString();
                    GENDER = dataSnapshot.child(currentUID).child("gender").getValue().toString();
                    HOUSENO = dataSnapshot.child(currentUID).child("houseNo").getValue().toString();
                    LOCALITY = dataSnapshot.child(currentUID).child("locality").getValue().toString();
                    CITY = dataSnapshot.child(currentUID).child("city").getValue().toString();
                    STATE = dataSnapshot.child(currentUID).child("state").getValue().toString();
                    PIN = dataSnapshot.child(currentUID).child("pin").getValue().toString();

                    // Storing data using parameterized constructor of Profilepojo class
                   // Profilepojo profile = new Profilepojo(USERNAME,EMAIL,PHONE,GENDER,HOUSENO,LOCALITY,CITY,STATE,PIN);

                    txtpUserName.append(USERNAME);
                    txtpEmailId.append(EMAIL);
                    txtpPhone.append(PHONE);
                    txtpGender.append(GENDER);
                    txtpHouseNo.append(HOUSENO);
                    txtpLocality.append(LOCALITY);
                    txtpCity.append(CITY);
                    txtpState.append(STATE);
                    txtpPin.append(PIN);
                }
                else{
                    Toast.makeText(getActivity(), "Database Error", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }


}