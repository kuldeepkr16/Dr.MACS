package com.example.lenovo.dra.Activities;

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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.lenovo.dra.R;
import com.example.lenovo.dra.RegisterWithActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    //reference of layout fields
    private EditText txtUname;
    private EditText txtPassword;
    private Button btnLogin;
    private TextView linkToRegister;
    private ProgressBar spinner;

    //boolean value for checking users existence
    private boolean chk = true;

    //firebase references
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabse;
    private FirebaseFirestore mFirestore;

    //variable for current user
    private String USERNAME;
    private String EMAIL;
    private String PHONE;

    private SharedPreferences preferences;
    SharedPreferences.Editor editor;

    // everytime on resume this will chk if user is logged in or not
    //if user has logged in this app previously then MainActivity will open
    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initializing firebase instances
        mAuth=FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mDatabse= FirebaseDatabase.getInstance().getReference().child("users");

        //initializing sharedpreferences
        preferences = getSharedPreferences("currentUserData", Context.MODE_PRIVATE);
        editor = preferences.edit();

        //initializing spinner
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        //hiding spinner at beginning
        spinner.setVisibility(View.GONE);

        //initializing layout field references
        txtUname = (EditText) findViewById(R.id.txtUserName);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        //on login button click
        btnLogin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //show spinner
                            spinner.setVisibility(View.VISIBLE);
                            //call method checkUser
                            checkUser();
              }
        });

        linkToRegister = (TextView) findViewById(R.id.LinkToRegister);
        //on register button click
        linkToRegister.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                      Intent i = new Intent(LoginActivity.this, RegisterWithActivity.class);
                      startActivity(i);
                       }
                    });
                }

        //method called on login button clicked
        private void checkUser() {
            final String UserId=txtUname.getText().toString().trim();
            String password=txtPassword.getText().toString().trim();
                if(!TextUtils.isEmpty(UserId)&&!TextUtils.isEmpty(password)){
                    //if fields are not empty
                    mAuth.signInWithEmailAndPassword(UserId,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            if(checkUserExist()){
                               //getting device token_id
                               String token_id = FirebaseInstanceId.getInstance().getToken();
                               //getting current user id
                               String current_id = mAuth.getCurrentUser().getUid();

                               //creating a map for Firestore
                               Map<String, Object> tokenMap = new HashMap<String, Object>();
                               //setting name as current username
                               tokenMap.put("name", "kuldeep");
                               //setting token_id as device token_id
                               tokenMap.put("token_id", token_id);
                               Log.i("error","Logging you in  "+USERNAME);

                               //storing device token id and username in Firestore collection user as child of current user id
                               mFirestore.collection("users").document(current_id).set(tokenMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                  @Override
                                  public void onSuccess(Void aVoid) {
                                     Log.i("error","inside success");
                                     //if all else are error free then send user to MainActivity
                                     Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                     i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                     //hide the spinner
                                     spinner.setVisibility(View.GONE);
                                     //store user data in shared preferences
                                     storeUserData();
                                     startActivity(i);
                                      }
                                 });
                        }else{
                                //if user details are wrong
                            spinner.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "Wrong Username/Password", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                            //if task is unsuccessful i.e. error at firebase
                        spinner.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this,"Wrong Username/Password",Toast.LENGTH_LONG).show();
                    }
                }
                    });
        }else{
                    //if user left fields empty
            spinner.setVisibility(View.GONE);
            Toast.makeText(LoginActivity.this, "Please Fill The Details" , Toast.LENGTH_SHORT).show();
        }
    }

    //taking out user data from firebase and storing it to shared preferences
    private void storeUserData() {
        Log.i("error", "in storedata "+USERNAME+EMAIL+PHONE);
        editor.putString("USERNAME",USERNAME);
        editor.putString("EMAIL",EMAIL);
        editor.putString("PHONE",PHONE);
        editor.commit();
    }

    //method to check if the requested user is present in Firebase or not
    private boolean checkUserExist() {
       final String s = mAuth.getCurrentUser().getUid();
        mDatabse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(s)){
                   USERNAME = dataSnapshot.child(s).child("name").getValue().toString();
                    EMAIL = dataSnapshot.child(s).child("email").getValue().toString();
                    PHONE = dataSnapshot.child(s).child("phoneNo").getValue().toString();
                    chk = true;
                }else{
                    chk =  false;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    return chk;
    }
}