package com.example.spork;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {

    // Initializes Parse SDK as soon as the application is created
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("E4BUj5InUccqz460blVggB8nybJPwW9qf4WGoChd")
                .clientKey("WhIAra4U0k8TAsMj4t2VVROuElhzF5W5Xq3QpOKH")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
