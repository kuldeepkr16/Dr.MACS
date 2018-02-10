package com.example.lenovo.dra.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lenovo.dra.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class sendNotification extends AppCompatActivity {

    private EditText txtMessage;
    private Button btnSend;
    private String mCurrentId;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);

        txtMessage = (EditText) findViewById(R.id.txtMessage);
        btnSend = (Button) findViewById(R.id.btnSendNotification);
        mCurrentId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirestore = FirebaseFirestore.getInstance();
                String message = txtMessage.getText().toString();
                if(!TextUtils.isEmpty(message)){
                    Map<String, Object> noti = new HashMap<String, Object>();
                    noti.put("message", message);
                    noti.put("from", mCurrentId);

                    mFirestore.collection("users").document(mCurrentId).collection("noti").add(noti)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(sendNotification.this, "Sent", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

    }
}
