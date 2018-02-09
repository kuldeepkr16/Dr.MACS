package com.example.lenovo.dra.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.lenovo.dra.R;

public class MedicineAvailibility extends AppCompatActivity {
    EditText txtMediName;
    Button btnMediSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicine_availibility);
    txtMediName = (EditText) findViewById(R.id.txtMediName);
        btnMediSearch = (Button) findViewById(R.id.btnSeachMedicine);
    }
}
