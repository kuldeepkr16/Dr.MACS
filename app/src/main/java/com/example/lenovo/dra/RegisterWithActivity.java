package com.example.lenovo.dra;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.lenovo.dra.Activities.RegisterDocActivity;
import com.example.lenovo.dra.Activities.RegisterMediStoreActivity;
import com.example.lenovo.dra.Activities.RegisterUserActivity;

public class RegisterWithActivity extends AppCompatActivity {

    RadioButton rbtnUsers;
    RadioButton rbtnDoc;
    RadioButton rbtnMed;
    RadioButton rbtnHosp;
    ImageView btnBackTop;
    Button btnRegRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_with);

        rbtnUsers = (RadioButton) findViewById(R.id.rbtnUser);
        rbtnDoc = (RadioButton) findViewById(R.id.rbtnDoc);
        rbtnHosp = (RadioButton) findViewById(R.id.rbtnHosp);
        rbtnMed = (RadioButton) findViewById(R.id.rbtnMedicalStore);
        btnRegRequest = (Button) findViewById(R.id.btnRegisterReq);
        btnBackTop = (ImageView) findViewById(R.id.btnBackTop);
        btnBackTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnRegRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!rbtnUsers.isChecked() && !rbtnDoc.isChecked() && !rbtnMed.isChecked() && !rbtnHosp.isChecked()){
                    Toast.makeText(RegisterWithActivity.this, "Please Make A Choice", Toast.LENGTH_SHORT ).show();
                    return;
                }
                if(rbtnUsers.isChecked()){
                    startActivity(new Intent(RegisterWithActivity.this,RegisterUserActivity.class));
                }
                else if(rbtnDoc.isChecked()){
                    startActivity(new Intent(RegisterWithActivity.this, RegisterDocActivity.class));
                }
                else if(rbtnMed.isChecked()){
                    startActivity(new Intent(RegisterWithActivity.this, RegisterMediStoreActivity.class));
                }
                else if(rbtnHosp.isChecked()){

                }
            }
        });
    }
}
