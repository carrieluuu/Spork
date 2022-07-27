package com.example.spork.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.spork.BuildConfig;
import com.example.spork.FileUtils;
import com.example.spork.R;
import com.example.spork.Restaurant;
import com.example.spork.restaurant.FetchYelpData;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import java.util.List;

/**
 * Home Fragment class to display map with markers connecting the user to the recommended restaurant pages.
 * Users can also explore the map by dragging or change the zoom-in/zoom-out by pinching or double tapping.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private static final int REQUEST_CODE = 100;
    public static final int MAX_RADIUS =  25000;

    private GoogleMap mMap;
    private FusedLocationProviderClient client;
    private ParseUser currentUser;
    private Chip chipOpenNow;
    private Chip chipPrice;
    private Chip chipTopRated;
    private Chip chipPopular;
    private Chip chipDistance;
    private FloatingActionButton fabZoomIn;
    private FloatingActionButton fabZoomOut;
    private CircularProgressIndicator loadingCircle;
    private double currentLat;
    private double currentLng;
    private String url;
    private int radius = 5000;
    private int zoom = 15;
    private boolean zoomIn = false;
    private boolean zoomOut = false;

    private boolean openNow = false;
    private double priceWeight = 0.0;
    private double ratingWeight = 0.0;
    private double popularityWeight = 0.0;
    private double proximityWeight = 0.0;

    public HomeFragment() {
        // Required empty public constructor
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chipOpenNow = view.findViewById(R.id.chipOpenNow);
        chipPrice = view.findViewById(R.id.chipPrice);
        chipTopRated = view.findViewById(R.id.chipTopRated);
        chipPopular = view.findViewById(R.id.chipPopular);
        chipDistance = view.findViewById(R.id.chipDistance);
        fabZoomIn = view.findViewById(R.id.fabZoomIn);
        fabZoomOut = view.findViewById(R.id.fabZoomOut);
        loadingCircle = view.findViewById(R.id.loading_circle);

        currentUser = ParseUser.getCurrentUser();

        chipOpenNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chipOpenNow.isChecked())
                    openNow = true;
                else
                    openNow = false;
            }
        });

        chipPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chipPrice.isChecked())
                    priceWeight = 0.25;
                else
                    priceWeight = 0;
            }

        });

        chipTopRated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chipTopRated.isChecked())
                    ratingWeight = 0.25;
                else
                    ratingWeight = 0;

            }
        });

        chipPopular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chipPopular.isChecked())
                    popularityWeight = 0.25;
                else
                    popularityWeight = 0;

            }
        });

        chipDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chipDistance.isChecked())
                    proximityWeight = 0.25;
                else
                    proximityWeight = 0;
            }
        });

        fabZoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radius -= 1000;
                zoomIn = true;
                zoomOut = false;
                getCurrentLocation();
            }
        });

        fabZoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radius >= MAX_RADIUS) {
                    radius = MAX_RADIUS;
                    Toast.makeText(getContext(), "Maximum zoom out reached", Toast.LENGTH_LONG).show();
                } else {
                    radius += 2000;
                    zoomIn = false;
                    zoomOut = true;
                }
                getCurrentLocation();
            }
        });

        currentUser.put("priceWeight", priceWeight);
        currentUser.put("ratingWeight", ratingWeight);
        currentUser.put("popularityWeight", popularityWeight);
        currentUser.put("proximityWeight", proximityWeight);

        client = LocationServices.getFusedLocationProviderClient(getActivity());

        // check condition
        if (ContextCompat.checkSelfPermission(
                getActivity(),
                Manifest.permission
                        .ACCESS_FINE_LOCATION)
                == PackageManager
                .PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
                getActivity(),
                Manifest.permission
                        .ACCESS_COARSE_LOCATION)
                == PackageManager
                .PERMISSION_GRANTED) {
            // When permission is granted
            // Call method
            getCurrentLocation();
        } else {
            // When permission is not granted
            // Call method
            requestPermissions(
                    new String[]{
                            Manifest.permission
                                    .ACCESS_FINE_LOCATION,
                            Manifest.permission
                                    .ACCESS_COARSE_LOCATION},
                    REQUEST_CODE);
        }

    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {

            mMap = googleMap;

            LatLng currentLatLng = new LatLng(currentLat, currentLng);
            mMap.addMarker(new MarkerOptions().position(currentLatLng).title("Current Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_current_location)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, zoom));

            url = FileUtils.buildPlacesUrl(currentLat, currentLng, radius, openNow);
            Log.i(TAG, url);

            // fetch data from json to add nearby restaurants onto the map
            Object dataFetchPlaces[] = new Object[3];
            dataFetchPlaces[0] = mMap;
            dataFetchPlaces[1] = url;
            dataFetchPlaces[2] = loadingCircle;

            FetchPlacesData fetchPlacesData  = new FetchPlacesData();
            fetchPlacesData.execute(dataFetchPlaces);

            if (zoomIn) {
                mMap.clear();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, zoom +=1));
            }
            if (zoomOut) {
                mMap.clear();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, zoom -=1));
            }

            mMap.getUiSettings().setMapToolbarEnabled(false);

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

                    // send data to business search data [connect places api to yelp api]
                    String businessSearchUrl = FileUtils.buildBusinessSearchUrl(marker);

                    Log.i(TAG, "Business search url: "+ businessSearchUrl);

                    Restaurant restaurant = new Restaurant();
                    Object yelpData[] = new Object[2];
                    yelpData[0] = businessSearchUrl;
                    yelpData[1] = null;

                    FetchYelpData fetchYelpData  = new FetchYelpData(getContext());
                    fetchYelpData.execute(yelpData);

                    return false;
                }
            });

        }
    };

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
        // Check condition
        if (requestCode == 100 && (grantResults.length > 0)
                && (grantResults[0] + grantResults[1]
                == PackageManager.PERMISSION_GRANTED)) {
            // When permission are granted
            // Call  method
            getCurrentLocation();
        }
        else {
            // When permission are denied
            // Display toast
            Toast
                    .makeText(getActivity(),
                            "Permission denied",
                            Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation()
    {
        // Initialize Location manager
        LocationManager locationManager
                = (LocationManager)getActivity()
                .getSystemService(
                        Context.LOCATION_SERVICE);
        // Check condition
        if (locationManager.isProviderEnabled(
                LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER)) {
            // When location service is enabled
            // Get last location
            client.getLastLocation().addOnCompleteListener(
                    new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(
                                @NonNull Task<Location> task)
                        {

                            // Initialize location
                            Location location
                                    = task.getResult();
                            // Check condition
                            if (location != null) {
                                // When location result is not
                                // null set latitude
                                currentLat = location.getLatitude();

                                // set longitude
                                currentLng = location.getLongitude();

                            }
                            else {
                                // When location result is null
                                // initialize location request
                                LocationRequest locationRequest
                                        = new LocationRequest()
                                        .setPriority(
                                                LocationRequest
                                                        .PRIORITY_HIGH_ACCURACY)
                                        .setInterval(10000)
                                        .setFastestInterval(
                                                1000)
                                        .setNumUpdates(1);

                                // Initialize location call back
                                LocationCallback
                                        locationCallback
                                        = new LocationCallback() {
                                    @Override
                                    public void
                                    onLocationResult(
                                            LocationResult
                                                    locationResult)
                                    {
                                        // Initialize
                                        // location
                                        Location location1
                                                = locationResult
                                                .getLastLocation();
                                        // Set latitude
                                        currentLat = location1.getLatitude();

                                        // Set longitude
                                        currentLng = location1.getLongitude();

                                    }


                                };

                                // Request location updates
                                client.requestLocationUpdates(
                                        locationRequest,
                                        locationCallback,
                                        Looper.myLooper());
                            }

                            ParseGeoPoint currentLocation = new ParseGeoPoint(currentLat, currentLng);
                            currentUser.put("currentLocation", currentLocation);

                            if (currentLat == 0.0 || currentLng == 0.0) {
                                currentLat = 37.484553142102676;
                                currentLng = -122.14773532902916;
                            }

                            LatLng currentLatLng = new LatLng(currentLat, currentLng);
                            Log.i(TAG, "currentLat: " + currentLat + " currentLng: " + currentLng);

                            SupportMapFragment mapFragment =
                                    (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
                            if (mapFragment != null) {
                                mapFragment.getMapAsync(callback);
                            }
                        }
                    });

        }
        else {
            // When location service is not enabled
            // open location setting
            startActivity(
                    new Intent(
                            Settings
                                    .ACTION_LOCATION_SOURCE_SETTINGS)
                            .setFlags(
                                    Intent.FLAG_ACTIVITY_NEW_TASK));
        }

    }



}