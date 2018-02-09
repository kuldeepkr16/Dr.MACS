package com.example.lenovo.dra.Activities;

import android.os.Bundle;
import com.example.lenovo.dra.R;

public class BloodRequest extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.blood_request,null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(0).setChecked(true);
    }
}
