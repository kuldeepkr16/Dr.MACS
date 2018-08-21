package com.example.lenovo.dra.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.lenovo.dra.R;

public class OnReceivingBloodConfirmation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_receiving_blood_confirmation);

        TextView txtConfirmName = (TextView) findViewById(R.id.txtNameConfirm);
        TextView txtConfirmPhoneNo = (TextView) findViewById(R.id.txtConfirmPhoneNo);
        ImageView btnBackTop = (ImageView) findViewById(R.id.btnBackTop);
        Button btnOK = (Button) findViewById(R.id.btnOK);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                senToMainActivity();
            }
        });
        btnBackTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                senToMainActivity();
            }
        });

        String name = getIntent().getStringExtra("from_Name");
        String phone_no = getIntent().getStringExtra("phone_no");

        txtConfirmName.setText(name);
        txtConfirmPhoneNo.setText(phone_no);

    }

    private void senToMainActivity() {
        startActivity(new Intent(OnReceivingBloodConfirmation.this, MainActivity.class));
        finish();
    }

}
