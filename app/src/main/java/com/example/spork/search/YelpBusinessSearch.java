package com.example.spork.search;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spork.Restaurant;
import com.example.spork.restaurant.RestaurantActivity;
import com.example.spork.restaurant.YelpService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class YelpBusinessSearch extends AsyncTask<Object, String, String> {

    private static final String TAG = "YelpBusinessSearch";

    private WeakReference<Context> context;
    private String url;
    private String id;
    private String businessSearchYelpData;
    private Restaurant restaurant;
    private List<Restaurant> featuredRestaurants;
    private SearchAdapter adapter;
    private RecyclerView rvFeatured;

    public YelpBusinessSearch (Context context) {
        this.context = new WeakReference<Context>(context);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        adapter = new SearchAdapter(context.get(), featuredRestaurants);

        // set the adapter on the recycler view
        rvFeatured.setAdapter(adapter);
        // set the layout manager on the recycler view
        rvFeatured.setLayoutManager(new LinearLayoutManager(context.get()));

    }

    @Override
    protected String doInBackground(Object... objects) {

        url = (String) objects[0];
        featuredRestaurants = (List<Restaurant>) objects[1];
        adapter = (SearchAdapter) objects[2];
        rvFeatured = (RecyclerView) objects[3];

        featuredRestaurants = new ArrayList<>();

        YelpService yelpService = new YelpService();

        try {

            businessSearchYelpData = yelpService.getRequest(url);
            JSONObject businessSearchJSON = new JSONObject(businessSearchYelpData);
            JSONArray businessesJSON = businessSearchJSON.getJSONArray("businesses");

            int maxResults = (businessesJSON.length() > 30 ? 30 : businessesJSON.length());
            for (int i = 0; i < maxResults; i++) {
                JSONObject restaurantObject = businessesJSON.getJSONObject(i);
                String id = restaurantObject.getString("id");
                String name = restaurantObject.getString("name");
                String image_url = restaurantObject.getString("image_url");

                JSONArray categories = restaurantObject.getJSONArray("categories");
                String category = categories.getJSONObject(0).getString("title");

                String price = restaurantObject.getString("price");

                restaurant = new Restaurant(id, name, image_url, category, price);
                featuredRestaurants.add(restaurant);
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

}

