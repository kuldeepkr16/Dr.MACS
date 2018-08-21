package com.example.lenovo.dra;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import static android.app.Activity.RESULT_OK;

public class MedicineAvailibility extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private OnFragmentInteractionListener mListener;
    private ImageButton mediImage;
    //reference to firestore to store notification
    private FirebaseFirestore mFirestore;
    //shared preference for user data
    SharedPreferences sharedPreferences;
    private String mediName;
    private EditText txtMediName;
    private static final int GALLERY_REQUEST=1;
    private Uri imageUri=null;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;
    private ProgressDialog mprogress;

    public MedicineAvailibility() {
        // Required empty public constructor
    }

    public static MedicineAvailibility newInstance(String param1, String param2) {
        MedicineAvailibility fragment = new MedicineAvailibility();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.medicine_availibility, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mediImage=(ImageButton) view.findViewById(R.id.mediimagebtn);
        txtMediName = (EditText) view.findViewById(R.id.txtMedicineName);
        Button btnSendMediRequest = (Button) view.findViewById(R.id.btnSendMediRequest);
        mStorage= FirebaseStorage.getInstance().getReference();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("mediImage");
        mediImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });

        btnSendMediRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediName = txtMediName.getText().toString();
                if(Objects.equals(mediName, "")){
                    Toast.makeText(getActivity(), "Please enter name of the medicine", Toast.LENGTH_SHORT).show();
                    return;
                }
                // startProgress();
                mprogress = ProgressDialog.show(getActivity(), "Please Wait","Sending Request To Medical Stores...", true, false);

                new Thread()
                {
                    public void run() {
                        startPosting();
                        //storing details to firestore
                        mFirestore = FirebaseFirestore.getInstance();
                        Map<String, Object> noti = new HashMap<>();
                        //getting device token_id
                        String token_id = FirebaseInstanceId.getInstance().getToken();
                        noti.put("from_token_id", token_id);
                        noti.put("imageURL", "imageURL");
                        noti.put("medi_name", mediName);

                        //getting values of current user from shared preferences
                        sharedPreferences = getActivity().getSharedPreferences("currentUserData", Context.MODE_PRIVATE);
                        String USERNAME = sharedPreferences.getString("USERNAME", "");
                        noti.put("from", USERNAME);

                        //adding this notification to firestore
                        mFirestore.collection("mediRequest").add(noti)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(getActivity(), "Medicine Request Sent", Toast.LENGTH_LONG).show();
                                        mprogress.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Not Sent", Toast.LENGTH_LONG).show();
                                mprogress.dismiss();
                            }
                        });
                    }}.start();
            }
        });

            }

    private void startPosting() {
        if(imageUri!=null){
            StorageReference filePath= mStorage.child("mediImage").child(imageUri.getLastPathSegment());
            filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl=taskSnapshot.getDownloadUrl();
                    DatabaseReference newPost= mDatabase.push();
                    newPost.child("Image").setValue(downloadUrl.toString());
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_REQUEST&&resultCode==RESULT_OK){
            imageUri= data.getData();
            mediImage.setImageURI(imageUri);
        }
    }
}