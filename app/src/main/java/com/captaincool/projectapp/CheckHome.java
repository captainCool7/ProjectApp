package com.captaincool.projectapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class CheckHome extends Fragment {


    public CheckHome() {
        // Required empty public constructor
    }

//    public void CheckTrainAvailability(View view)
//    {
//        Intent intent = new Intent(CheckHome.this,TrainOptions.class);
//        startActivity(intent);
//    }
//    public void BusForm(View view)
//    {
//        Intent intent = new Intent(this,Bus_form.class);
//        startActivity(intent);
//    }
//    public void FlightForm(View view)
//    {
//        Intent intent = new Intent(this,Flight_form.class);
//        startActivity(intent);
//    }
//

    ImageButton bus, flight, train, hotel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_check_home, container, false);
        bus = (ImageButton) view.findViewById(R.id.bus);
        train  = (ImageButton) view.findViewById(R.id.train);
        flight = (ImageButton) view.findViewById(R.id.flight);
        hotel = (ImageButton) view.findViewById(R.id.hotel);
        bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity(), Bus_form.class);
                startActivity(in);
            }
        });
        train.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity(), TrainOptions.class);
                startActivity(in);
            }
        });
        flight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity(), Flight_form.class);
                startActivity(in);
            }
        });
        hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity(), Hotel_form.class);
                startActivity(in);
            }
        });
        return view;
    }
}
