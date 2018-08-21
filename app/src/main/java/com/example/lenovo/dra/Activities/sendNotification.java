package com.example.lenovo.dra.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.lenovo.dra.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import java.util.HashMap;
import java.util.Map;

public class sendNotification extends AppCompatActivity {

    private EditText txtMessage;
    SharedPreferences sharedPreferences;
    private FirebaseFirestore mFirestore;
    private ProgressDialog mprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);

        txtMessage = (EditText) findViewById(R.id.txtMessage);
        Button btnSend = (Button) findViewById(R.id.btnSendNotification);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // startProgress();
                mprogress = ProgressDialog.show(sendNotification.this, "","Please Wait...", true);

                new Thread()
                {
                    public void run()
                    {

                            mFirestore = FirebaseFirestore.getInstance();
                            String message = txtMessage.getText().toString();
                            if(!TextUtils.isEmpty(message)) {
                                Map<String, Object> noti = new HashMap<String, Object>();
                                noti.put("message", message);

                                //getting values of current user using shared preferences
                                sharedPreferences = getSharedPreferences("currentUserData", Context.MODE_PRIVATE);
                                String USERNAME = sharedPreferences.getString("USERNAME", "");
                                noti.put("from", USERNAME);

                                //getting device token_id
                                String token_id = FirebaseInstanceId.getInstance().getToken();
                                noti.put("token_id", token_id);


                                mFirestore.collection("noti").add(noti)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(sendNotification.this, "Notification Sent", Toast.LENGTH_SHORT).show();
                                                // dismiss the progressdialog
                                                mprogress.dismiss();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(sendNotification.this, "Not Sent", Toast.LENGTH_LONG).show();
                                        // dismiss the progressdialog
                                        mprogress.dismiss();
                                    }
                                });
                            }}
                }.start();
            }
        });
    }
}
