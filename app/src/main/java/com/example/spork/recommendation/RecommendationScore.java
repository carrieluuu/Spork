package com.example.spork.recommendation;

import com.example.spork.Restaurant;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RecommendationScore {
    public static final String TAG = "RecommendationScore";
    public static final int PRICE_SCALE = 4;
    public static final int RATING_SCALE = 5;
    public static final int POPULARITY_SCALE = 5;
    public static final int PROXIMITY_SCALE = 5;

    private List<Restaurant> restaurantList;

    // assigned arbitrary weights for testing purposes
    private double priceWeight = 0.4;
    private double ratingWeight = 0.3;
    private double popularityWeight = 0.2;
    private double proximityWeight = 0.1;

    private double priceScore;
    private double ratingScore;
    private double popularityScore;
    private double proximityScore;
    private double totalScore;

    public RecommendationScore(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
        standardizePopularity();
        standardizeProximity();
        
        for (Restaurant restaurant : restaurantList) {
            calculateScore(restaurant);
        }

    }

    public List<Restaurant> sortList(List<Restaurant> restaurantList) {
        Collections.sort(restaurantList, new Comparator<Restaurant>() {
            @Override
            public int compare(Restaurant r1, Restaurant r2) {
                return Double.compare(r2.getScore(), r1.getScore());
            }
        });

        return restaurantList;

    }

    public void standardizePopularity() {
        double[] popularity = new double[restaurantList.size()];
        for (int i = 0; i < restaurantList.size(); i++) {
            popularity[i] = restaurantList.get(i).getReviews();
        }
        ZScore zscore = new ZScore();
        popularity = zscore.compute(popularity);

        for (int i = 0; i < restaurantList.size(); i++) {
            restaurantList.get(i).setPopularity(popularity[i]);
        }
    }

    public void standardizeProximity() {
        double[] distance = new double[restaurantList.size()];
        for (int i = 0; i < restaurantList.size(); i++) {
            distance[i] = restaurantList.get(i).getDistance();
        }
        ZScore zscore = new ZScore();
        distance = zscore.compute(distance);

        for (int i = 0; i < restaurantList.size(); i++) {
            restaurantList.get(i).setProximity(distance[i]);
        }
    }

    public void calculateScore(Restaurant restaurant) {
        priceScore = ((double) restaurant.getPrice() / PRICE_SCALE) * priceWeight;

        ratingScore = (restaurant.getRating() / RATING_SCALE) * ratingWeight;

        popularityScore = (restaurant.getPopularity() / POPULARITY_SCALE) * popularityWeight;

        proximityScore = (restaurant.getProximity() / PROXIMITY_SCALE) * popularityWeight;

        totalScore = priceScore + ratingScore + popularityScore + proximityScore;

        restaurant.setScore(totalScore);

    }

}
