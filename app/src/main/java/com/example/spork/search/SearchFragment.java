package com.example.spork.search;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.spork.FileUtils;
import com.example.spork.R;
import com.example.spork.Restaurant;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Search Fragment to display featured restaurants and search view for users to search for
 * restaurants near them using keywords or by restaurant name
 */

public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";

    protected SearchAdapter adapter;
    protected List<Restaurant> featuredRestaurants;
    private ParseUser currentUser;
    private TextView tvGreeting;
    private RecyclerView rvFeatured;
    private SearchView svRestaurant;
    private CircularProgressIndicator loadingCircle;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvGreeting = view.findViewById(R.id.tvGreeting);
        rvFeatured = view.findViewById(R.id.rvFeatured);
        svRestaurant = view.findViewById(R.id.svRestaurant);
        loadingCircle = view.findViewById(R.id.loading_circle);

        currentUser = ParseUser.getCurrentUser();

        tvGreeting.setText("Hello, " + currentUser.getString("fullName") + ".");

        String featuredUrl = FileUtils.buildFeaturedUrl(currentUser);

        featuredRestaurants = new ArrayList<>();
        Object businessSearchData[] = new Object[5];
        businessSearchData[0] = featuredUrl;
        businessSearchData[1] = featuredRestaurants;
        businessSearchData[2] = adapter;
        businessSearchData[3] = rvFeatured;
        businessSearchData[4] = loadingCircle;

        YelpBusinessSearch businessSearch  = new YelpBusinessSearch(getContext());
        businessSearch.execute(businessSearchData);

        svRestaurant.setOnQueryTextListener(new SearchView.OnQueryTextListener()

        {
            @Override
            public boolean onQueryTextSubmit (String query){

                String businessSearchUrl = FileUtils.buildSearchUrl(currentUser, query);

                // create intent for the new activity
                Intent i = new Intent(getContext(), SearchResultsActivity.class);
                i.putExtra("businessSearchUrl", businessSearchUrl);
                // show the activity
                getContext().startActivity(i);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

}