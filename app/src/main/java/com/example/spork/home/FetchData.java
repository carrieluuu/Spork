package com.example.spork.home;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class FetchData extends AsyncTask <Object, String, String> {

    private static final String TAG = "FetchData";

    private String googleNearbyRestaurantsData;
    private GoogleMap googleMap;
    private String url;
    private int ratingWeight;
    private int popularityWeight;

    @Override
    protected void onPostExecute(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            Log.i(TAG, "array size: " + jsonArray.length());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject restaurant = jsonArray.getJSONObject(i);
                JSONObject getLocation = restaurant.getJSONObject("geometry").getJSONObject("location");

                String lat = getLocation.getString("lat");
                String lng = getLocation.getString("lng");

                String name = restaurant.getString("name");

                if (ratingWeight == 1) {
                    String rating = restaurant.getString("rating");
                    if (Double.parseDouble(rating) < 2.5)
                        continue;
                }

                if (popularityWeight == 1) {
                    String numRatings = restaurant.getString("user_ratings_total");
                    if (Integer.parseInt(numRatings) < 1500)
                        continue;
                }

                LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title(name);
                markerOptions.position(latLng);
                googleMap.addMarker(markerOptions);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected String doInBackground(Object... objects) {

        try {
            googleMap = (GoogleMap) objects[0];
            url = (String) objects[1];
            DownloadUrl downloadUrl = new DownloadUrl();
            googleNearbyRestaurantsData = downloadUrl.retrieveUrl(url);
            ratingWeight = (int) objects[2];
            popularityWeight = (int) objects[3];

        } catch (IOException e) {
            e.printStackTrace();
        }
        return googleNearbyRestaurantsData;
    }
}
