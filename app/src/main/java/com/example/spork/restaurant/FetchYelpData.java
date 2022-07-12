package com.example.spork.restaurant;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.spork.Restaurant;
import com.example.spork.home.DownloadUrl;
import com.example.spork.home.HomeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class FetchYelpData extends AsyncTask<Object, String, String> {

    private static final String TAG = "BusinessSearchYelp";

    private String url;
    private String businessSearchYelpData;
    private String businessDetailsYelpData;
    private Restaurant restaurant;

    @Override
    protected String doInBackground(Object... objects) {

        url = (String) objects[0];
        YelpService yelpService = new YelpService();

        try {

            // get id from business search
            businessSearchYelpData = yelpService.getRequest(url);
            JSONObject businessSearchJSON = new JSONObject(businessSearchYelpData);

            JSONArray businessesJSON = businessSearchJSON.getJSONArray("businesses");
            String id = businessesJSON.getJSONObject(0).getString("id");
            Log.i(TAG, "id: " + id);

            // get business details using acquired id
            url = "https://api.yelp.com/v3/businesses/" + id;
            businessDetailsYelpData = yelpService.getRequest(url);

            JSONObject businessDetailsJSON = new JSONObject(businessDetailsYelpData);

            String restaurantName = businessDetailsJSON.getString("name");
            String location = businessDetailsJSON.getJSONObject("location").getString("display_address");
            String imageUrl = businessDetailsJSON.getString("image_url");
            boolean isClosed = Boolean.parseBoolean(businessDetailsJSON.getString("is_closed"));
            double stars = Double.parseDouble(businessDetailsJSON.getString("rating"));
            String websiteUrl = businessDetailsJSON.getString("url");
            String phoneNumber = businessDetailsJSON.getString("display_phone");

            restaurant = new Restaurant(id, restaurantName, location, imageUrl, isClosed, stars, websiteUrl, phoneNumber);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}

