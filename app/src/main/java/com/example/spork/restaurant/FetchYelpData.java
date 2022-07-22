package com.example.spork.restaurant;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.spork.MainActivity;
import com.example.spork.R;
import com.example.spork.Restaurant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class FetchYelpData extends AsyncTask<Object, String, String> {

    private static final String TAG = "BusinessSearchYelp";

    private WeakReference<Context> context;
    private String url;
    private String businessSearchYelpData;
    private String businessDetailsYelpData;
    private Restaurant restaurant;

    public FetchYelpData (Context context) {
        this.context = new WeakReference<Context>(context);
    }

    @Override
    protected void onPostExecute(String s) {

        // create intent for the new activity
        Intent i = new Intent(context.get(), RestaurantActivity.class);
        i.putExtra("restaurant", Parcels.wrap(restaurant));
        // show the activity
        context.get().startActivity(i);

    }

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

            JSONArray getLocation = businessDetailsJSON.getJSONObject("location").getJSONArray("display_address");
            String location = "";
            for (int i = 0; i < getLocation.length(); i++) {
                location += " " + getLocation.get(i);
            }
            String imageUrl = businessDetailsJSON.getString("image_url");

            boolean isOpenNow = false;
            if (!businessDetailsJSON.has("hours")) {
                isOpenNow = false;
            } else {
                String getOpenNow = (businessDetailsJSON.getString("hours")).split("is_open_now")[1];
                isOpenNow = getOpenNow.contains("true");
            }
            double stars = Double.parseDouble(businessDetailsJSON.getString("rating"));
            String websiteUrl = businessDetailsJSON.getString("url");
            String phoneNumber = businessDetailsJSON.getString("display_phone");

            restaurant = new Restaurant(id, restaurantName, location, imageUrl, isOpenNow, stars, websiteUrl, phoneNumber);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

}

