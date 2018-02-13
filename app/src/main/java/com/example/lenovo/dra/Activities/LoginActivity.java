package com.example.lenovo.dra.Activities;

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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.dra.R;
import com.example.lenovo.dra.RegisterWithActivity;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private Button btnLogin;
    private EditText txtUname;
    private EditText txtPassword;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabse;
    private TextView linkToRegister;
    private ProgressBar spinner;
    private boolean chk = true;
    private FirebaseFirestore mFirestore;

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
        mAuth=FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);
        mDatabse= FirebaseDatabase.getInstance().getReference().child("users");
                    txtUname = (EditText) findViewById(R.id.txtUserName);
                    txtPassword = (EditText) findViewById(R.id.txtPassword);
                    btnLogin = (Button) findViewById(R.id.btnLogin);
                    btnLogin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            spinner.setVisibility(View.VISIBLE);
                            checkUser();
                        }
                    });
                    linkToRegister = (TextView) findViewById(R.id.LinkToRegister);
                    linkToRegister.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(LoginActivity.this, RegisterWithActivity.class);
                            startActivity(i);
                        }
                    });
                }
    private void checkUser() {
        String UserId=txtUname.getText().toString().trim();
        String password=txtPassword.getText().toString().trim();
        if(!TextUtils.isEmpty(UserId)&&!TextUtils.isEmpty(password)){
            mAuth.signInWithEmailAndPassword(UserId,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                    boolean chk = checkUserExist();
                        if(chk){

                            mAuth.getCurrentUser().getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                                @Override
                                public void onSuccess(GetTokenResult getTokenResult) {
                                    String token_id = getTokenResult.getToken();
                                    String current_id = mAuth.getCurrentUser().getUid();

                                    Map<String, Object> tokenMap = new HashMap<String, Object>();
                                    tokenMap.put("token_id", token_id);
                                    Log.i("error","Logging you in"+ token_id);

                                    mFirestore.collection("users").document(current_id).set(tokenMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.i("error","inside success");
                                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            spinner.setVisibility(View.GONE);
                                            startActivity(i);
                                        }
                                    });
                                }
                            });

                        }else{
                            spinner.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "Wrong Username/Password", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        spinner.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this,"Wrong Username/Password",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }else{
            spinner.setVisibility(View.GONE);
            Toast.makeText(LoginActivity.this, "Please Fill The Details" , Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkUserExist() {
       final String s = mAuth.getCurrentUser().getUid();
        mDatabse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(s)){
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