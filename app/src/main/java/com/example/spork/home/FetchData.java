package com.example.spork.home;

import android.os.AsyncTask;
import android.os.Build;
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
    private String nextPageToken;

    @Override
    protected void onPostExecute(String s) {
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
            restaurantList = new ArrayList<>();

            DownloadUrl downloadUrl = new DownloadUrl();

            for (int i = 0; i < 3; i ++) {
                googleNearbyRestaurantsData = downloadUrl.retrieveUrl(url);

                JSONObject jsonObject = new JSONObject(googleNearbyRestaurantsData);

                JSONArray jsonArray = jsonObject.getJSONArray("results");

                Log.i(TAG, "array size: " + jsonArray.length());

                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject restaurant = jsonArray.getJSONObject(j);

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

                if (jsonObject.has("next_page_token")) {
                    nextPageToken = jsonObject.getString("next_page_token");
                    url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?pagetoken=" + nextPageToken + "&key=" + BuildConfig.MAPS_API_KEY;

                    Log.i(TAG, "new url:" + url);
                } else {
                    break;
                }
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return googleNearbyRestaurantsData;
    }
}
