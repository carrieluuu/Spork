package com.example.spork;

import static com.example.spork.Configuration.applicationId;
import static com.example.spork.Configuration.clientKey;
import static com.example.spork.Configuration.server;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {

    // Initializes Parse SDK as soon as the application is created
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(applicationId)
                .clientKey(clientKey)
                .server(server)
                .build()
        );
    }
}
