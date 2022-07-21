package com.example.spork.search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spork.R;
import com.example.spork.Restaurant;
import com.example.spork.feed.Post;
import com.example.spork.feed.PostsAdapter;
import com.example.spork.restaurant.FetchYelpData;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";

    protected SearchAdapter adapter;
    protected List<Restaurant> featuredRestaurants;
    private RecyclerView rvFeatured;

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

        rvFeatured = view.findViewById(R.id.rvFeatured);

        StringBuilder sb = new StringBuilder("https://api.yelp.com/v3/businesses/search?");
        sb.append("term=restaurants");
        sb.append("&latitude=" + ParseUser.getCurrentUser().getParseGeoPoint("currentLocation").getLatitude());
        sb.append("&longitude=" + ParseUser.getCurrentUser().getParseGeoPoint("currentLocation").getLongitude());
        sb.append("&sort_by=rating");

        String businessSearchUrl = sb.toString();

        featuredRestaurants = new ArrayList<>();
        Object businessSearchData[] = new Object[4];
        businessSearchData[0] = businessSearchUrl;
        businessSearchData[1] = featuredRestaurants;
        businessSearchData[2] = adapter;
        businessSearchData[3] = rvFeatured;

        YelpBusinessSearch businessSearch  = new YelpBusinessSearch(getContext());
        businessSearch.execute(businessSearchData);

    }

    public void setFeaturedRestaurants(List<Restaurant> featuredRestaurants) {
        this.featuredRestaurants = featuredRestaurants;
    }


}