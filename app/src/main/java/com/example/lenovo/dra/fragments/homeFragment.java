package com.example.lenovo.dra.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.lenovo.dra.Activities.NearbyPlaces;
import com.example.lenovo.dra.Activities.sendNotification;
import com.example.lenovo.dra.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class homeFragment extends Fragment {
    Intent intent;
    Button btnGo;
    Button btnSend;

    public homeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        btnGo = (Button) view.findViewById(R.id.btnGo);
        btnSend = (Button) view.findViewById(R.id.send);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getActivity(), NearbyPlaces.class);
                startActivity(intent);
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),sendNotification.class);
                startActivity(i);
            }
        });

    }
}