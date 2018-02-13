package com.example.lenovo.dra.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.lenovo.dra.Activities.MedicineAvailibility;
import com.example.lenovo.dra.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class readFragment extends Fragment {
    Button txtNewPost;

    public readFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_read, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        txtNewPost = (Button) view.findViewById(R.id.txtNewPost);
        txtNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MedicineAvailibility.class));
            }
        });
    }
}
