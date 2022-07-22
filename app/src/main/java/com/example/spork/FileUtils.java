package com.example.spork;

public class FileUtils {
    public static String buildPlacesUrl(double currentLat, double currentLng, int radius) {
        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json");
        sb.append("?fields=name%2Cgeometry/location");
        sb.append("&location=" + currentLat + "%2C" + currentLng);
        sb.append("&radius=" + radius);
        sb.append("&type=restaurant");
        sb.append("&key=" + BuildConfig.MAPS_API_KEY);
        return sb.toString();
    }
}
