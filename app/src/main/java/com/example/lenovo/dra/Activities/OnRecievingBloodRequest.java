package com.example.lenovo.dra.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.lenovo.dra.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class OnRecievingBloodRequest extends AppCompatActivity {

    private TextView txtGuide;
    private LinearLayout blood_layout;
    private SharedPreferences sharedPreferences;
    private ProgressDialog mprogress;
    private FirebaseFirestore mFirestore;
    private String token_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_recieving_blood_request);
        TextView txtSender = (TextView) findViewById(R.id.txtSender);
        TextView txtBloodGroup = (TextView) findViewById(R.id.txtBloodGroup);
        TextView txtContact = (TextView) findViewById(R.id.txtContact);
        TextView txtUrgency = (TextView) findViewById(R.id.txtUrgency);

        Button btnConfirm = (Button) findViewById(R.id.btnConfirm);
        Button btnNotAvailable = (Button) findViewById(R.id.btnNotAvailable);

        ImageView btnBackTop = (ImageView) findViewById(R.id.btnBackTop);

        btnBackTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callMainActivity();
            }
        });
        btnNotAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callMainActivity();
            }
        });

        final String fromName = getIntent().getStringExtra("fromName");
        String blood_type = getIntent().getStringExtra("blood_type");
        String urgency = getIntent().getStringExtra("urgency");
        String phone_no = getIntent().getStringExtra("phone_no");
        token_id = getIntent().getStringExtra("token_id");
        String NOW = getIntent().getStringExtra("NOW");

        txtSender.setText(fromName);
        txtBloodGroup.setText(blood_type);
        txtContact.setText(phone_no);
        txtUrgency.setText(urgency +"\n"+ NOW);

        //for dynamic creation of guide textview
        blood_layout = (LinearLayout) findViewById(R.id.blood_layout);
        txtGuide = (TextView) findViewById(R.id.txtGuide);
        txtGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView guide = new TextView(v.getContext());
                guide.setText("On clicking 'confirm' button your contact details will be send to "+ fromName +", who can then contact you." +
                        "\nAnd if you are not willing to help then you can just press 'not available' button");
                guide.setTextColor(Color.WHITE);
                guide.setTextSize(20);
                blood_layout.addView(guide);
                txtGuide.setEnabled(false);
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // startProgress();
                mprogress = ProgressDialog.show(OnRecievingBloodRequest.this, "Please Wait","Sending Your Confirmation...", true, false);

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
                        mFirestore.collection("confirmBloodRequest").add(noti)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(OnRecievingBloodRequest.this, "Confirmation Sent\n User may call You", Toast.LENGTH_LONG).show();
                                        callMainActivity();
                                        mprogress.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(OnRecievingBloodRequest.this, "Not Sent", Toast.LENGTH_LONG).show();
                                mprogress.dismiss();
                            }
                        });
                    }}.start();
            }
        });
    }
    private void callMainActivity(){
        startActivity(new Intent(OnRecievingBloodRequest.this, MainActivity.class));
        finish();
    }
}