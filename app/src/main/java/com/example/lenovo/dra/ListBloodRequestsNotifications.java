package com.example.lenovo.dra;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.lenovo.dra.POJO.BloodRequestPojo;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class ListBloodRequestsNotifications extends AppCompatActivity {
    private FirebaseFirestore mFirestore;
    private RecyclerView mBloodList;
    private ImageView btnBackTop;
    private BloodUsersAdapter BloodUsersAdapter;
    private List<BloodRequestPojo> usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_blood_requests);
        mBloodList=(RecyclerView)findViewById(R.id.blood_list);

        btnBackTop = (ImageView) findViewById(R.id.btnBackTop);
        btnBackTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        usersList=new ArrayList<>();
        BloodUsersAdapter=new BloodUsersAdapter(usersList);
        mBloodList.setHasFixedSize(true);
        mBloodList.setLayoutManager(new LinearLayoutManager(this));
        mBloodList.setAdapter(BloodUsersAdapter);
        mFirestore=FirebaseFirestore.getInstance();
        mFirestore.collection("bloodRequest").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(e!=null){

                }
                for(DocumentChange doc: documentSnapshots.getDocumentChanges()){
                    if(doc.getType()==DocumentChange.Type.ADDED){
                      BloodRequestPojo users=doc.getDocument().toObject(BloodRequestPojo.class);
                        usersList.add(users);
                        BloodUsersAdapter.notifyDataSetChanged();
                    }

                }
            }
        });


    }}
