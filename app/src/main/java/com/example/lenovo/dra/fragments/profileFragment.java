package com.example.lenovo.dra.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.lenovo.dra.Activities.LoginActivity;
import com.example.lenovo.dra.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class profileFragment extends Fragment {

    private FirebaseAuth mAuth;
    private Button btnSignOut;
    private FirebaseFirestore mFirestore;
    private String mUserId;
    private ProgressBar spinner;
    public profileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        btnSignOut = (Button) getView().findViewById(R.id.btnSignOut);

        //initializing spinner
        spinner = (ProgressBar) getView().findViewById(R.id.progressBar2);
        //hiding spinner at beginning
        spinner.setVisibility(View.GONE);


        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mUserId = mAuth.getCurrentUser().getUid();
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.setVisibility(View.VISIBLE);
                Map<String, Object> tokenMapRemove = new HashMap<String, Object>();
                tokenMapRemove.put("token_id", "");

                mFirestore.collection("users").document(mUserId).update(tokenMapRemove).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        mAuth.signOut();
                        startActivity(new Intent(getContext(), LoginActivity.class));
                        spinner.setVisibility(View.GONE);
                        getActivity().finish();
                    }
                });
            }
    });
}
}