package com.example.spork.home;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.example.spork.BuildConfig;
import com.example.spork.Restaurant;
import com.example.spork.recommendation.RecommendationScore;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.parse.Parse;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FetchPlacesData extends AsyncTask <Object, String, String> {

    private static final String TAG = "FetchPlacesData";

    private String googleNearbyRestaurantsData;
    private GoogleMap googleMap;
    private String url;
    private List<Restaurant> restaurantList;
    private String nextPageToken;
    private CircularProgressIndicator loadingCircle;

    @Override
    protected void onPostExecute(String s) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        RecommendationScore rs = new RecommendationScore(restaurantList, currentUser);
        Log.i(TAG, "List size: " + restaurantList.size());
        restaurantList = rs.sortList(restaurantList);

        int max = 10;
        if (restaurantList.isEmpty()) {
            max = 0;
        }
         else if (restaurantList.size() < 10) {
             max = restaurantList.size();
        }
         for (int i = 0; i < max; i++) {
             MarkerOptions markerOptions = new MarkerOptions();
             markerOptions.title(restaurantList.get(i).getName());
             markerOptions.position(restaurantList.get(i).getLocation());
             googleMap.addMarker(markerOptions);
         }

        loadingCircle.setVisibility(View.GONE);

    }

    @Override
    protected String doInBackground(Object... objects) {

        try {

            googleMap = (GoogleMap) objects[0];
            url = (String) objects[1];
            loadingCircle = (CircularProgressIndicator) objects[2];
            restaurantList = new ArrayList<>();

            DownloadUrl downloadUrl = new DownloadUrl();

            for (int i = 0; i < 3; i ++) {
                Log.i(TAG, "current url: " + url);

                String googleNearbyRestaurantsData = downloadUrl.retrievePlacesUrl(url);

                JSONObject jsonObject = new JSONObject(googleNearbyRestaurantsData);

                String status = jsonObject.getString("status");

                if (status.equals("INVALID_REQUEST")) {
                    for (int k = 1; k < 5; k++) {
                        Thread.sleep(1000);
                        Log.i(TAG, "Retrying for invalid request." + k + " times.");
                        googleNearbyRestaurantsData = downloadUrl.retrievePlacesUrl(url);

                        jsonObject = new JSONObject(googleNearbyRestaurantsData);
                        status = jsonObject.getString("status");
                        if (!status.equals("INVALID_REQUEST")) {
                            break;
                        }
                    }
                }

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

                    Thread.sleep(2000);

                    Log.i(TAG, "new url:" + url);
                } else {
                    break;
                }
            }

        } catch (IOException | JSONException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
