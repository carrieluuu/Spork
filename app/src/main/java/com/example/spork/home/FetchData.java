package com.example.spork.home;

import android.os.AsyncTask;
import android.util.Log;

import com.example.spork.BuildConfig;
import com.example.spork.Restaurant;
import com.example.spork.recommendation.RecommendationScore;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FetchData extends AsyncTask <Object, String, String> {

    private static final String TAG = "FetchData";

    private String googleNearbyRestaurantsData;
    private GoogleMap googleMap;
    private String url;
    private List<Restaurant> restaurantList;

    @Override
    protected void onPostExecute(String s) {
        try {
            restaurantList = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(s);
            String next_page_token = null;
            JSONArray jsonArray = jsonObject.getJSONArray("results");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject restaurant = jsonArray.getJSONObject(i);

                JSONObject getLocation = restaurant.getJSONObject("geometry").getJSONObject("location");
                String lat = getLocation.getString("lat");
                String lng = getLocation.getString("lng");
                LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));

                String restaurantName = restaurant.getString("name");

                boolean isOpen = false;

                if (restaurant.has("opening_hours")) {
                    isOpen = Boolean.parseBoolean(restaurant.getJSONObject("opening_hours").getString("open_now"));
                }

                String placeId = restaurant.getString("place_id");

                if (!restaurant.has("price_level"))
                    continue;

                int priceLevel = Integer.parseInt(restaurant.getString("price_level"));

                double stars = Double.parseDouble(restaurant.getString("rating"));

                int numOfReviews = Integer.parseInt(restaurant.getString("user_ratings_total"));

                Restaurant restaurant1 = new Restaurant(placeId, restaurantName, latLng, priceLevel, stars, numOfReviews, isOpen);

                restaurantList.add(restaurant1);

                }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        RecommendationScore rs = new RecommendationScore(restaurantList);
        restaurantList = rs.sortList(restaurantList);

        for (int i = 0; i < 10; i++) {
            MarkerOptions markerOptions = new MarkerOptions();
              markerOptions.title(restaurantList.get(i).getName());
              markerOptions.position(restaurantList.get(i).getLocation());
              googleMap.addMarker(markerOptions);
        }


    }
    @Override
    protected String doInBackground(Object... objects) {

        try {
                googleMap = (GoogleMap) objects[0];
                url = (String) objects[1];
                DownloadUrl downloadUrl = new DownloadUrl();
                googleNearbyRestaurantsData = downloadUrl.retrieveUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return googleNearbyRestaurantsData;
    }
}
