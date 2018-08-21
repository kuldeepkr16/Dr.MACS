package com.example.lenovo.dra.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

public class SendBloodRequest extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    //reference to firestore to store notification
    private FirebaseFirestore mFirestore;
    //shared preference for user data
    SharedPreferences sharedPreferences;
    //drop down list for bloodtype and urgency
    private Spinner spinnerBlood;
    private Spinner spinnerUrgency;
    private ProgressDialog mprogress;

    private OnFragmentInteractionListener mListener;

    public SendBloodRequest() {
        // Required empty public constructor
    }

    public static SendBloodRequest newInstance(String param1, String param2){
        SendBloodRequest fragment = new SendBloodRequest();
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
        return inflater.inflate(R.layout.fragment_blood_request, container, false);
    }

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
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        //initializing button send blood request
        Button btnSendBloodRequest = (Button) view.findViewById(R.id.btnSendBloodRequest);
        //creating dropdown list
        spinnerBlood = (Spinner) view.findViewById(R.id.blood_type_list);
        spinnerUrgency = (Spinner) view.findViewById(R.id.urgency_dropdown);

        // Creating an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterBlood = ArrayAdapter.createFromResource(getActivity(),
                R.array.blood_grp_list, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterUrgency = ArrayAdapter.createFromResource(getActivity(),
                R.array.urgency_dropdown, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapterBlood.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterUrgency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinnerBlood.setAdapter(adapterBlood);
        spinnerUrgency.setAdapter(adapterUrgency);

        final String NOW = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        //on send button pressed
        btnSendBloodRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // startProgress();
                mprogress = ProgressDialog.show(getActivity(), "Please Wait","Sending A Request To All Users...", true, false);

                //validate inputs
                if(validateInput()== 1) {
                    Toast.makeText(getActivity(), "Please choose a blood type", Toast.LENGTH_SHORT).show();
                    mprogress.dismiss();
                    return;
                }else if(validateInput()== 2) {
                    Toast.makeText(getActivity(), "Please choose urgency", Toast.LENGTH_SHORT).show();
                    mprogress.dismiss();
                    return;
                }
                new Thread()
                {
                    public void run() {
                        //if user has chosen something from both dropdown list
                        String selectedBloodType = spinnerBlood.getSelectedItem().toString();
                        String selectedUrgency = spinnerUrgency.getSelectedItem().toString();

                        //storing details to firestore
                        mFirestore = FirebaseFirestore.getInstance();
                        Map<String, Object> noti = new HashMap<String, Object>();
                        noti.put("selectedBloodType", selectedBloodType);
                        noti.put("selectedUrgency", selectedUrgency);
                        //getting device token_id
                        String token_id = FirebaseInstanceId.getInstance().getToken();
                        noti.put("from_token_id", token_id);

                        //getting values of current user from shared preferences
                        sharedPreferences = getActivity().getSharedPreferences("currentUserData", Context.MODE_PRIVATE);
                        String USERNAME = sharedPreferences.getString("USERNAME", "");
                        String PHONE = sharedPreferences.getString("PHONE", "");
                        noti.put("from", USERNAME);
                        noti.put("phone_no", PHONE);
                        noti.put("NOW", NOW);

                        //adding this notification to firestore
                        mFirestore.collection("bloodRequest").add(noti)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(getActivity(), "Notification Sent", Toast.LENGTH_LONG).show();
                                        //clear drop down list
                                        spinnerBlood.setSelection(0);
                                        spinnerUrgency.setSelection(0);
                                        sendToMainActivity();
                                        mprogress.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Not Sent", Toast.LENGTH_LONG).show();
                                spinnerBlood.setSelection(0);
                                spinnerUrgency.setSelection(0);
                                mprogress.dismiss();
                            }
                        });
                    }}.start();
            }
        });
    }

    private int validateInput() {
        if(Objects.equals(spinnerBlood.getSelectedItem().toString(), "Choose-")){
            //if bloodtype isn't chosen
            return 1;
        }else if(Objects.equals(spinnerUrgency.getSelectedItem().toString(), "Choose-")){
            //if urgency isn't chosen
            return 2;
        }
        else{
            //if appropriate values are chosen for both dropdowns
            return 3;
    }}

    private void sendToMainActivity() {
        startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();
    }
}