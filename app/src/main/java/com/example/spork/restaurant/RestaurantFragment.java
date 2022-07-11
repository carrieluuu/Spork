package com.example.spork.restaurant;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.spork.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantFragment extends Fragment {

    public static final String TAG = "RestaurantFragment";

    private TextView tvRestaurantName;
    private TextView tvAddress;
    private ImageView ivRestaurant;
    private TextView tvOpenNow;
    private RatingBar ratingBar;
    private TextView tvWebsite;
    private TextView tvPhoneNumber;

    public RestaurantFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_restaurant, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvRestaurantName = view.findViewById(R.id.tvRestaurantName);
        tvAddress = view.findViewById(R.id.tvAddress);
        ivRestaurant = view.findViewById(R.id.ivRestaurant);
        tvOpenNow = view.findViewById(R.id.tvOpenNow);
        ratingBar = view.findViewById(R.id.ratingBar);
        tvWebsite = view.findViewById(R.id.tvWebsite);
        tvPhoneNumber = view.findViewById(R.id.tvPhoneNumber);






    }
}