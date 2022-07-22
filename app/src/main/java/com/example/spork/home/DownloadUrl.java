package com.example.spork.home;

import android.util.Log;

import com.example.spork.BuildConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadUrl {
    public static final String TAG = "DownloadUrl";

    public String retrievePlacesUrl(String url) throws IOException {

        String urlData = "";
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try {
            URL getUrl = new URL(url);
            httpURLConnection = (HttpURLConnection) getUrl.openConnection();
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();

            String line = "";

            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            urlData = stringBuffer.toString();
            bufferedReader.close();
        } catch (Exception e) {
            Log.d(TAG, "Exception: " + e.toString());
        } finally {
            inputStream.close();
            httpURLConnection.disconnect();
        }
        return urlData;
    }

}
