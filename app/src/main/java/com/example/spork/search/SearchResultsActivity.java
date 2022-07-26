package com.example.spork.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.spork.R;
import com.example.spork.Restaurant;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {

    public static final String TAG = "SearchResultsActivity";

    protected SearchAdapter adapter;
    protected List<Restaurant> searchedRestaurants;
    private RecyclerView rvResults;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        rvResults = findViewById(R.id.rvResults);

        String businessSearchUrl = getIntent().getExtras().getString("businessSearchUrl");

        searchedRestaurants = new ArrayList<>();
        Object businessSearchData[] = new Object[4];
        businessSearchData[0] = businessSearchUrl;
        businessSearchData[1] = searchedRestaurants;
        businessSearchData[2] = adapter;
        businessSearchData[3] = rvResults;

        YelpBusinessSearch businessSearch  = new YelpBusinessSearch(this);
        businessSearch.execute(businessSearchData);


    }
}