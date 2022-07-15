package com.example.spork;

import android.app.Application;

import com.example.spork.feed.Post;
import com.example.spork.restaurant.Review;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    // Initializes Parse SDK as soon as the application is created
    @Override
    public void onCreate() {
        super.onCreate();

        // Register post parse model
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Review.class);


        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(BuildConfig.PARSE_APPLICATION_ID)
                .clientKey(BuildConfig.PARSE_CLIENT_KEY)
                .server(BuildConfig.PARSE_SERVER)
                .build()
        );

    }
}
