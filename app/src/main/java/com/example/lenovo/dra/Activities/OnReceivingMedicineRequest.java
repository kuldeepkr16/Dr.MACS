package com.example.lenovo.dra.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.lenovo.dra.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class OnReceivingMedicineRequest extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private ProgressDialog mprogress;
    private FirebaseFirestore mFirestore;
    private ImageView imgShowRecievedImage;
    private String token_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_receiving_medicine_request);

        Button btnMediAvailable = (Button) findViewById(R.id.btnMediAvailable);
        Button btnMediNotAvailable = (Button) findViewById(R.id.btnMediNotAvailable);
        ImageView btnBackTop = (ImageView) findViewById(R.id.btnBackTop);
        imgShowRecievedImage = (ImageView) findViewById(R.id.imgShowRecievedImage);
        TextView txtSenderName = (TextView) findViewById(R.id.txtSenderName);
        TextView txtMediName = (TextView) findViewById(R.id.txtMediName);

        String SenderName = getIntent().getStringExtra("from_name");
        String MedicineName = getIntent().getStringExtra("medi_name");
        String imageURL = getIntent().getStringExtra("imageURL");
        token_id = getIntent().getStringExtra("token_id");

        txtSenderName.setText(SenderName);
        txtMediName.setText(MedicineName);

        //TODO: set image to imageURL

        btnMediAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // startProgress();
                mprogress = ProgressDialog.show(OnReceivingMedicineRequest.this, "Please Wait","Sending Your Confirmation...", true, false);

                new Thread()
                {
                    public void run() {

                        //storing details to firestore
                        mFirestore = FirebaseFirestore.getInstance();
                        Map<String, Object> noti = new HashMap<String, Object>();

                        //getting values of current user from shared preferences
                        sharedPreferences = getSharedPreferences("currentUserData", Context.MODE_PRIVATE);
                        String USERNAME = sharedPreferences.getString("USERNAME", "");
                        String PHONE = sharedPreferences.getString("PHONE", "");
                        noti.put("from", USERNAME);
                        noti.put("phone_no", PHONE);
                        noti.put("token_id", token_id);

                        //adding this notification to firestore
                        mFirestore.collection("confirmMediRequest").add(noti)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(OnReceivingMedicineRequest.this, "Confirmation Sent\n User may call You", Toast.LENGTH_LONG).show();
                                        callMainActivity();
                                        mprogress.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(OnReceivingMedicineRequest.this, "Not Sent", Toast.LENGTH_LONG).show();
                                mprogress.dismiss();
                            }
                        });
                    }}.start();
            }
        });

        btnBackTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callMainActivity();
            }
        });
        btnMediNotAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callMainActivity();
            }
        });
    }

    private void callMainActivity() {
        startActivity(new Intent(OnReceivingMedicineRequest.this, MainActivity.class));
        finish();
    }
}