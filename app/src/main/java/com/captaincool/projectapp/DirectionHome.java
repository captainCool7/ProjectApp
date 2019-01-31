package com.captaincool.projectapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class DirectionHome extends Fragment {


    public DirectionHome() {
        // Required empty public constructor
    }

    Button mapResult;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_direction_home, container, false);
        mapResult = view.findViewById(R.id.getD);
        mapResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity(),DirectionResult_activity.class);
                startActivity(in);
            }
        });
        return view;
    }
}
