package com.example.lenovo.dra.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.lenovo.dra.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by lenovo on 2/7/2018.
 */

public class HospitalSearch extends AppCompatActivity {
    Button btnHosp;
    Button btnMedicalStore;
    Button btnBloodRequest;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_nearby_places);
        btnHosp = (Button) findViewById(R.id.btnHosp);
        btnMedicalStore = (Button) findViewById(R.id.btnMedicalStores);
        btnBloodRequest = (Button) findViewById(R.id.btnBloodReq);
        mAuth = FirebaseAuth.getInstance();
        btnHosp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] hosp = {"Chirayu", "Hamidiya", "AIIMS"};

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(HospitalSearch.this, R.layout.list_layout, hosp);

                ListView myList = (ListView) findViewById(R.id.myList);
                myList.setAdapter(adapter);
            }
        });
        btnMedicalStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
