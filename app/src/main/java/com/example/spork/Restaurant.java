package com.example.spork;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import org.parceler.Parcel;

@Parcel

public class Restaurant {
    private String mapId;
    private String yelpId;
    private String name;
    private LatLng location;
    private double proximity;
    private int price;
    private double rating;
    private int reviews;
    private double popularity;
    private boolean openNow;
    private double score;
    private String address;
    private String image;
    private String website;
    private String phone;

    public Restaurant() {

    }

    public Restaurant(String placeId, String restaurantName, LatLng latlng, int priceLevel, double stars, int numReviews, boolean isOpen) {
        mapId = placeId;
        name = restaurantName;
        location = latlng;
        price = priceLevel;
        rating = stars;
        reviews = numReviews;
        openNow = isOpen;

    }
    public Restaurant(String id, String restaurantName, String location, String imageUrl, boolean isOpenNow, double stars, String websiteUrl, String phoneNumber) {
        yelpId = id;
        name = restaurantName;
        address = location;
        image = imageUrl;
        openNow = isOpenNow;
        rating = stars;
        website = websiteUrl;
        phone = phoneNumber;

    }

    public String getMapId() {
        return mapId;
    }

    public String getYelpId() {
        return yelpId;
    }

    public String getName() {
        return name;
    }

    public LatLng getLocation() {
        return location;
    }

    public double getDistance() {
        float[] result = new float[1];
        ParseGeoPoint currentLocation = ParseUser.getCurrentUser().getParseGeoPoint("currentLocation");
        Location.distanceBetween (currentLocation.getLatitude(), currentLocation.getLongitude(), location.latitude, location.longitude,  result);
        return result[0];
    }

    public double getProximity() {
        return proximity;
    }

    public void setProximity(double proximity) {
        this.proximity = proximity;
    }

    public int getPrice() {

        return price;
    }

    public double getRating() {

        return rating;
    }

    public int getReviews() {
        return reviews;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }


    public boolean isOpenNow() {
        return openNow;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getAddress() {
        return address;
    }

    public String getImage() {
        return image;
    }

    public String getWebsite() {
        return website;
    }

    public String getPhone() {
        return phone;
    }
}
