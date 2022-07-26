package com.example.spork;

import com.google.android.gms.maps.model.Marker;

public class FileUtils {
    public static String buildPlacesUrl(double currentLat, double currentLng, int radius, boolean openNow) {
        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json");
        sb.append("?fields=name%2Cgeometry/location");
        sb.append("&location=" + currentLat + "%2C" + currentLng);
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

        String businessSearchUrl = sb.toString();
    }
}
