package com.example.spork;

import com.google.android.gms.maps.model.Marker;
import com.parse.ParseUser;

public class FileUtils {
    public static String buildPlacesUrl(double currentLat, double currentLng, int radius, boolean openNow) {
        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json");
        sb.append("?fields=name%2Cgeometry/location");
        sb.append("&location=").append(currentLat).append("%2C").append(currentLng);
        sb.append("&radius=" + radius);
        sb.append("&type=restaurant");
        if (openNow)
            sb.append("&opennow=true");
        sb.append("&key=" + BuildConfig.MAPS_API_KEY);

        return sb.toString();
    }

    public static String buildBusinessSearchUrl(Marker marker) {
        StringBuilder sb = new StringBuilder("https://api.yelp.com/v3/businesses/search?");
        sb.append("term=" + marker.getTitle());
        sb.append("&latitude=" + marker.getPosition().latitude);
        sb.append("&longitude=" + marker.getPosition().longitude);
        sb.append("&limit=1");

        return sb.toString();
    }

    public static String buildFeaturedUrl(ParseUser currentUser) {
        StringBuilder sb = new StringBuilder("https://api.yelp.com/v3/businesses/search?");
        sb.append("term=restaurant");
        sb.append("&latitude=" + currentUser.getParseGeoPoint("currentLocation").getLatitude());
        sb.append("&longitude=" + currentUser.getParseGeoPoint("currentLocation").getLongitude());
        sb.append("&sort_by=rating");

        return sb.toString();
    }

    public static String buildSearchUrl(ParseUser currentUser, String query) {
        StringBuilder sb = new StringBuilder("https://api.yelp.com/v3/businesses/search?");
        sb.append("term=" + query);
        sb.append("&latitude=" + ParseUser.getCurrentUser().getParseGeoPoint("currentLocation").getLatitude());
        sb.append("&longitude=" + ParseUser.getCurrentUser().getParseGeoPoint("currentLocation").getLongitude());
        sb.append("&sort_by=rating");

        return sb.toString();
    }

    public static String buildSavedUrl(String id) {
        return "https://api.yelp.com/v3/businesses/" + id;
    }
}
