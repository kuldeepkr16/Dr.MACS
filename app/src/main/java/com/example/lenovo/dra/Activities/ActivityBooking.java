package com.example.lenovo.dra.Activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.example.lenovo.dra.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ActivityBooking extends AppCompatActivity implements View.OnClickListener{

    private Button btnBook;
    private TextView txtPlaceDetails;
    private ImageView btnBackTop;
    private Button btnDatePicker, btnTimePicker;
    private TextView txtDate, txtTime;
    private String chosenDate, chosenTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private boolean chk;
    private ProgressDialog mprogress;
    private FirebaseFirestore mFirestore;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        btnBackTop = (ImageView) findViewById(R.id.btnBackTop);
        btnBackTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnBook = (Button) findViewById(R.id.btnBook);

        txtPlaceDetails = (TextView) findViewById(R.id.txtplaceDetails);
        Intent intent = getIntent();
        String placeName = intent.getExtras().getString("placeName");
        String address = intent.getExtras().getString("placeAddress");
      /*  String placeId = intent.getExtras().getString("placeId");
        String photoLink = intent.getExtras().getString("photoLink");
        String phoneNo = intent.getExtras().getString("phoneNo");
        String rating = intent.getExtras().getString("rating");
        String latitude = intent.getExtras().getString("latitude");
        String longitude = intent.getExtras().getString("longitude");
        */
        String str = "Name :- "+placeName +"\n\n"+
                "Address :- "+address ;
        txtPlaceDetails.setText(str);
        btnDatePicker=(Button) findViewById(R.id.btn_date);
        btnTimePicker=(Button) findViewById(R.id.btn_time);
        txtDate=(TextView) findViewById(R.id.txtDate);
        txtTime=(TextView) findViewById(R.id.txtTime);
        chosenDate = txtDate.getText().toString().trim();
        chosenTime = txtTime.getText().toString().trim();

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);


        //on send button pressed
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // startProgress();
                mprogress = ProgressDialog.show(ActivityBooking.this, "Please Wait","Sending A Request To Hospital...", true, false);
                if(!validateInputs()){
                    Toast.makeText(ActivityBooking.this, "Please choose date/time", Toast.LENGTH_LONG).show();
                    return;
                }
                new Thread()
                {
                    public void run() {

                        //storing details to firestore
                        mFirestore = FirebaseFirestore.getInstance();
                        Map<String, Object> noti = new HashMap<String, Object>();
                        noti.put("date", chosenDate);
                        noti.put("time", chosenTime);
                        //getting device token_id
                        String token_id = FirebaseInstanceId.getInstance().getToken();
                        noti.put("from_token_id", token_id);

                        //getting values of current user from shared preferences
                        sharedPreferences = getSharedPreferences("currentUserData", Context.MODE_PRIVATE);
                        String USERNAME = sharedPreferences.getString("USERNAME", "");
                        String PHONE = sharedPreferences.getString("PHONE", "");
                        noti.put("from", USERNAME);
                        noti.put("phone_no", PHONE);

                        //adding this notification to firestore
                        mFirestore.collection("appointmentRequest").add(noti)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(ActivityBooking.this, "Notification Sent", Toast.LENGTH_LONG).show();
                                        sendToMainActivity();
                                        mprogress.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ActivityBooking.this, "Not Sent", Toast.LENGTH_LONG).show();
                                mprogress.dismiss();
                            }
                        });
                    }}.start();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == btnDatePicker) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (view == btnTimePicker) {
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hrofday, int minute) {
                    txtTime.setText(hrofday + ":" + minute);

                }

            }, mHour, mMinute, DateFormat.is24HourFormat(ActivityBooking.this));
            timePickerDialog.show();
        }
    }
    private void sendToMainActivity() {
        startActivity(new Intent(ActivityBooking.this, MainActivity.class));
        finish();
    }
    private boolean validateInputs(){
        if (!Objects.equals(chosenDate, "") && !Objects.equals(chosenTime, "")) chk = true;
        else chk = false;
        return chk;
    }
}
