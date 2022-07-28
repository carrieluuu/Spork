package com.example.spork;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.spork.feed.FeedFragment;
import com.example.spork.home.HomeFragment;
import com.example.spork.profile.ProfileFragment;
import com.example.spork.search.SearchFragment;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    final FragmentManager fragmentManager = getSupportFragmentManager();
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        bottomNavigation();

        // Initialize the SDK
        String placesAPIKey = BuildConfig.MAPS_API_KEY;
        Places.initialize(getApplicationContext(), placesAPIKey);

        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(this);


    }

    private void bottomNavigation() {
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.action_home:
                    fragment = fragmentManager.findFragmentById(R.id.action_home);
                    if (fragment == null)
                        fragment = new HomeFragment();
                    break;
                case R.id.action_search:
                    fragment = new SearchFragment();
                    break;
                case R.id.action_feed:
                    fragment = new FeedFragment();
                    break;
                case R.id.action_profile:
                default:
                    fragment = new ProfileFragment();
                    break;
            }
            fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
            return true;
        });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }

}