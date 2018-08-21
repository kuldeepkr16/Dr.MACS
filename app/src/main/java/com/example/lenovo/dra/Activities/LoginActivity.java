package com.example.lenovo.dra.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import com.example.lenovo.dra.R;
import com.example.lenovo.dra.RegisterWithActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity  {

    private EditText txtUname;
    private EditText txtPassword;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabse;
    private RadioButton rbtnUsers,rbtnDoctors,rbtnMedical,rbtnHospital;
    private ProgressDialog mprogress;
    String USERNAME;
    String EMAIL;
    String userType;
    String PHONE;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth=FirebaseAuth.getInstance();

        //declaring shared preferenece
        SharedPreferences preferences = getSharedPreferences("currentUserData", Context.MODE_PRIVATE);
        editor = preferences.edit();

        //check for the shared preference each time
        USERNAME = preferences.getString("USERNAME","deleted");

        if(!Objects.equals(USERNAME, "deleted")){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }

            //if username is deleted from sharedpreference then show login frame to user
            setContentView(R.layout.activity_login);

            rbtnUsers = (RadioButton) findViewById(R.id.rbtnLUsers);
            rbtnDoctors = (RadioButton) findViewById(R.id.rbtnLDoctors);
            rbtnMedical = (RadioButton) findViewById(R.id.rbtnLMedical);
            rbtnHospital = (RadioButton) findViewById(R.id.rbtnLHospital);

        txtUname = (EditText) findViewById(R.id.txtUserName);
            txtPassword = (EditText) findViewById(R.id.txtPassword);
            Button btnLogin = (Button) findViewById(R.id.btnLogin);
            mAuth = FirebaseAuth.getInstance();

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                // startProgress();
                mprogress = ProgressDialog.show(LoginActivity.this, "Please Wait","Logging you in...", true, false);
                if(!validateInputs()){
                    return;
                }
                new Thread()
                {
                    public void run()
                        { //now mDatabase is referring to usertype root link in firebase database
                            mDatabse = FirebaseDatabase.getInstance().getReference().child(userType);
                            checkUser();
                        }
                }.start();
            }
        });

        Button linkToRegister = (Button) findViewById(R.id.LinkToRegister);
        linkToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterWithActivity.class);
                startActivity(i);
            }
        });
    }
            //validate inputs
            private boolean validateInputs(){
                boolean correct;
                if(txtUname.getText().toString().isEmpty() || txtPassword.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this,"Please input Username/Password",Toast.LENGTH_LONG).show();
                    mprogress.dismiss();
                    correct = false;
                    }else if(rbtnUsers.isChecked()){
                        userType = "users";
                        correct = true;
                    }else if(rbtnDoctors.isChecked()){
                        userType = "doctors";
                        correct = true;
                    }else if(rbtnMedical.isChecked()){
                        userType = "medical stores";
                        correct = true;
                    }else if(rbtnHospital.isChecked()){
                        userType = "hospital";
                        correct = true;
                    }else{
                        Toast.makeText(LoginActivity.this,"Please select user type",Toast.LENGTH_LONG).show();
                        mprogress.dismiss();
                        correct = false;
                    }
                    return correct;
                }

            //method to check if user credentials are correct
            private void checkUser() {
                String UserId=txtUname.getText().toString().trim();
                String password=txtPassword.getText().toString().trim();
                if(!TextUtils.isEmpty(UserId)&&!TextUtils.isEmpty(password)){
                    mAuth.signInWithEmailAndPassword(UserId,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                checkUserExist();
                            }else{
                                Toast.makeText(LoginActivity.this,"Incorrect Username/Password",Toast.LENGTH_LONG).show();
                                mprogress.dismiss();
                            }
                        }
                    });
                }else{
                    Toast.makeText(LoginActivity.this, "Please Fill The Details" , Toast.LENGTH_SHORT).show();
                    mprogress.dismiss();
                }
            }

            // method to fire up if user details are correct
            public void checkUserExist() {
                final String currentUID = mAuth.getCurrentUser().getUid();
                mDatabse.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(currentUID)){
                                USERNAME = dataSnapshot.child(currentUID).child("name").getValue().toString();
                                EMAIL = dataSnapshot.child(currentUID).child("email").getValue().toString();
                                PHONE = dataSnapshot.child(currentUID).child("phoneNo").getValue().toString();

                                //method call to store user details in sharedpreferences
                                storeUserData();

                                //getting device token_id
                                String token_id = FirebaseInstanceId.getInstance().getToken();

                             //storing token id to database
                                //creating a map for Firestore
                                Map<String, Object> tokenMap = new HashMap<>();
                                //setting token_id as device token_id
                                tokenMap.put("token_id", token_id);

                        if(Objects.equals(userType, "medical stores")){
                            //in meditokens store device token id of medical stores only
                            FirebaseDatabase.getInstance().getReference().child("meditokens").child(currentUID).setValue(tokenMap);
                        }if(Objects.equals(userType, "hospital")){
                            //in meditokens store device token id of medical stores only
                            FirebaseDatabase.getInstance().getReference().child("hospitokens").child(currentUID).setValue(tokenMap);
                            }
                                //store token id of all users
                        FirebaseDatabase.getInstance().getReference().child("tokens").child(currentUID).setValue(tokenMap);

                        //transfer user to main activity
                       Intent i = new Intent(LoginActivity.this, MainActivity.class);
                       i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                       startActivity(i);
                       mprogress.dismiss();
                     }
                        else{
                            Toast.makeText(LoginActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
                            mprogress.dismiss();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
          }
        //method to store data into shared preferences
        public void storeUserData() {
            Log.i("error", "in storedata "+USERNAME+EMAIL+PHONE);
            editor.putString("USERNAME",USERNAME);
            editor.putString("EMAIL",EMAIL);
            editor.putString("PHONE",PHONE);
            editor.putString("USERTYPE", userType);
            editor.commit();
     }
  }