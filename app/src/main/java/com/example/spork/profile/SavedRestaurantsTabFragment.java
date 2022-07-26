package com.example.spork.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.spork.R;

public class SavedRestaurantsTabFragment extends Fragment {

    private static final String TAG = "ProfileFeedTabFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.saved_restaurants_tab_fragment, container, false);


        return root;
    }


}
