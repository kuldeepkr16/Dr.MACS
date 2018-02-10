package com.example.lenovo.dra.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.dra.R;
import com.example.lenovo.dra.ShowTerms;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterDocActivity extends AppCompatActivity {
    ImageView btnBackTop;
    EditText txtUname;
    EditText txtEmail;
    EditText txtPassword;
    RadioButton rbtnMale;
    RadioButton rbtnFemale;
    EditText txtPhoneNo;
    EditText txtHouseNo;
    EditText txtLocality;
    EditText txtCity;
    EditText txtState;
    EditText txtPin;
    EditText txtSpeciality;
    TextView textViewTerms;
    CheckBox chkConfirmation;
    Button btnRegister;
    FirebaseAuth mAuth;
    TextView txtTitle;
    DatabaseReference mDatabase;
    String gender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_doc);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("doctors");
        mAuth = FirebaseAuth.getInstance();
        txtUname = (EditText) findViewById(R.id.txtuname);
        txtEmail = (EditText) findViewById(R.id.txtemail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        rbtnMale = (RadioButton) findViewById(R.id.rbtnmale);
        rbtnFemale = (RadioButton) findViewById(R.id.rbtnFemale);
        txtPhoneNo = (EditText) findViewById(R.id.txtPhoneNo);
        txtHouseNo = (EditText) findViewById(R.id.txtHouseNo);
        txtLocality = (EditText) findViewById(R.id.txtLocality);
        txtCity = (EditText) findViewById(R.id.txtCity);
        txtState = (EditText) findViewById(R.id.txtState);
        txtPin = (EditText) findViewById(R.id.txtPin);
        txtSpeciality = (EditText) findViewById(R.id.txtSpeciality);
        chkConfirmation = (CheckBox) findViewById(R.id.chkConfirm);
        textViewTerms = (TextView) findViewById(R.id.textViewTerms);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/barbar.ttf");
        txtTitle.setTypeface(custom_font);
        btnBackTop = (ImageView) findViewById(R.id.btnBackTop);
        btnBackTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        textViewTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterDocActivity.this, ShowTerms.class));
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInputs() == 1){
                    Toast.makeText(RegisterDocActivity.this, "Please Fill All The Details!!", Toast.LENGTH_LONG).show();
                    return;
                }
                else if(validateInputs() == 2){
                    Toast.makeText(RegisterDocActivity.this, "You Must Accept Terms And Conditions!!", Toast.LENGTH_LONG).show();
                    return;
                }
                else if(validateInputs() == 3){
                    Toast.makeText(RegisterDocActivity.this, "Please Choose Your Gender!", Toast.LENGTH_LONG).show();
                    return;
                }
                else if(validateInputs() == 4){
                    Toast.makeText(RegisterDocActivity.this, "Please Choose A Password Of Atleast 6 Characters!", Toast.LENGTH_LONG).show();
                    return;
                }
                else if(validateInputs() == 5){
                    Toast.makeText(RegisterDocActivity.this, "Enter Correct Phone Number", Toast.LENGTH_LONG).show();
                    return;
                }
                else if(validateInputs() == 6){
                    Toast.makeText(RegisterDocActivity.this, "Enter Correct PIN", Toast.LENGTH_LONG).show();
                    return;
                }
                registerUser();
                    Toast.makeText(RegisterDocActivity.this, "Registration Successful! Login To Continue", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterDocActivity.this, LoginActivity.class));
                }
        });
    }

    private int validateInputs(){
        int stat = 0;
        if(txtUname.getText().toString().isEmpty() ||
                txtEmail.getText().toString().isEmpty() ||
                txtPassword.getText().toString().isEmpty() ||
                txtPhoneNo.getText().toString().isEmpty() ||
                txtHouseNo.getText().toString().isEmpty() ||
                txtLocality.getText().toString().isEmpty() ||
                txtCity.getText().toString().isEmpty() ||
                txtState.getText().toString().isEmpty() ||
                txtSpeciality.getText().toString().isEmpty() ||
                txtPin.getText().toString().isEmpty()){
            stat = 1;
        }
        else if(!chkConfirmation.isChecked()){
            stat = 2;
        }
        else if(!rbtnMale.isChecked() && !rbtnFemale.isChecked()){
            stat = 3;
        }
        if(txtPassword.getText().toString().trim().length()<6){
            stat = 4;
        }
        if(txtPhoneNo.getText().toString().trim().length()<10){
            stat = 5;
        }
        if(txtPin.getText().toString().trim().length()<6){
            stat = 6;
        }
        return stat;
    }

    private void registerUser() {
        final String name = txtUname.getText().toString().trim();
        final String email = txtEmail.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();
        if(rbtnMale.isChecked()){
            gender = "Male";
        }
        else
        if(rbtnFemale.isChecked()){
            gender = "Female";
        }
        final String phoneNo = txtPhoneNo.getText().toString().trim();
        final String houseNo = txtHouseNo.getText().toString().trim();
        final String locality = txtLocality.getText().toString().trim();
        final String city = txtCity.getText().toString().trim();
        final String state = txtState.getText().toString().trim();
        final String pin = txtPin.getText().toString().trim();
        final String speciality = txtSpeciality.getText().toString().trim();
        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) ){
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    String userId = null;
                    if(task.isSuccessful()){
                       try{
                            userId = mAuth.getCurrentUser().getUid();
                       }catch (NullPointerException e){
                           e.printStackTrace();
                       }
                        if(userId!=null) {
                            DatabaseReference current_user_db = mDatabase.child(userId);
                            current_user_db.child("name").setValue(name);
                            current_user_db.child("email").setValue(email);
                            current_user_db.child("gender").setValue(gender);
                            current_user_db.child("phoneNo").setValue(phoneNo);
                            current_user_db.child("houseNo").setValue(houseNo);
                            current_user_db.child("locality").setValue(locality);
                            current_user_db.child("city").setValue(city);
                            current_user_db.child("state").setValue(state);
                            current_user_db.child("pin").setValue(pin);
                            current_user_db.child("Speciality").setValue(speciality);
                        }
                    }
                }
            });
        }
    }
}
