package com.example.spork.restaurant;

import com.example.spork.BuildConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class YelpService {

    public static String getRequest(String url) throws IOException {
        URL getUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + BuildConfig.YELP_API_KEY);
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        return in.readLine();
    }
}
