package com.example.lenovo.dra.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.lenovo.dra.R;

public class DisplayMediStoreDetails extends AppCompatActivity {

    private ImageView btnBackTop;
    private Button btnMediOK;
    private TextView txtShowDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_medi_store_details);

        btnBackTop = (ImageView) findViewById(R.id.btnBackTop);
        btnBackTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnMediOK = (Button) findViewById(R.id.btnMediOK);
        btnMediOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtShowDetails = (TextView) findViewById(R.id.txtplaceDetails);

        Intent intent = getIntent();
        String placeName = intent.getExtras().getString("placeName");
        String address = intent.getExtras().getString("placeAddress");

        String str = "Name :- "+placeName +"\n\n"+
                "Address :- "+address ;
        txtShowDetails.setText(str);


    }
}
