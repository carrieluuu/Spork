package com.example.spork.profile;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spork.FileUtils;
import com.example.spork.Restaurant;
import com.example.spork.restaurant.YelpService;
import com.example.spork.search.SearchAdapter;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class YelpSearchById extends AsyncTask<Object, String, String> {
    private static final String TAG = "YelpSearchById";

    private WeakReference<Context> context;
    private String url;
    private String businessDetailYelpData;
    private Restaurant restaurant;
    private List<Restaurant> savedRestaurants;
    private SearchAdapter adapter;
    private RecyclerView rvSaved;
    private ArrayList<String> savedRestaurantIds;
    private CircularProgressIndicator loadingCircle;

    public YelpSearchById (Context context) {
        this.context = new WeakReference<>(context);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (savedRestaurants == null || savedRestaurants.size() <= 0) {
            return;
        }

        adapter = new SearchAdapter(context.get(), savedRestaurants);
        // set the adapter on the recycler view
        rvSaved.setAdapter(adapter);
        // set the layout manager on the recycler view
        rvSaved.setLayoutManager(new LinearLayoutManager(context.get()));

        loadingCircle.setVisibility(View.GONE);

    }

    @Override
    protected String doInBackground(Object... objects) {

        savedRestaurants = (List<Restaurant>) objects[0];
        adapter = (SearchAdapter) objects[1];
        rvSaved = (RecyclerView) objects[2];
        savedRestaurantIds = (ArrayList<String>) objects[3];
        loadingCircle = (CircularProgressIndicator) objects[4];

        savedRestaurants = new ArrayList<>();

        YelpService yelpService = new YelpService();

        for (int i = 0; i < savedRestaurantIds.size(); i++) {
            url = FileUtils.buildSavedUrl(savedRestaurantIds.get(i));

            try {

                businessDetailYelpData = yelpService.getRequest(url);
                JSONObject businessDetailJSON = new JSONObject(businessDetailYelpData);

                String id = savedRestaurantIds.get(i);
                String name = businessDetailJSON.getString("name");
                String image_url = businessDetailJSON.getString("image_url");
                JSONArray categories = businessDetailJSON.getJSONArray("categories");
                String category = categories.getJSONObject(0).getString("title");
                String price = businessDetailJSON.getString("price");

                restaurant = new Restaurant(id, name, image_url, category, price);
                savedRestaurants.add(restaurant);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

}

