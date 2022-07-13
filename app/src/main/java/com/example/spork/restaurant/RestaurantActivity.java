package com.example.spork.restaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.spork.R;
import com.example.spork.Restaurant;

import org.parceler.Parcels;

public class RestaurantActivity extends AppCompatActivity {

    public static final String TAG = "RestaurantActivity";

    private Restaurant restaurant;
    private TextView tvRestaurantName;
    private TextView tvAddress;
    private ImageView ivRestaurant;
    private TextView tvOpenNow;
    private RatingBar ratingBar;
    private TextView tvWebsite;
    private TextView tvPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        tvRestaurantName = findViewById(R.id.tvRestaurantName);
        tvAddress = findViewById(R.id.tvAddress);
        ivRestaurant = findViewById(R.id.ivRestaurant);
        tvOpenNow = findViewById(R.id.tvOpenNow);
        ratingBar = findViewById(R.id.ratingBar);
        tvWebsite = findViewById(R.id.tvWebsite);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);

        restaurant = (Restaurant) Parcels.unwrap(getIntent().getParcelableExtra("restaurant"));

        tvRestaurantName.setText(restaurant.getName());
        tvAddress.setText(restaurant.getAddress());
        Glide.with(this)
                .load(restaurant.getImage())
                .placeholder(R.drawable.restaurant_placeholder)
                .apply(new RequestOptions()
                        .centerCrop()
                        .transform(new RoundedCorners(100)))
                .into(ivRestaurant);
        if (restaurant.isOpenNow()) {
            tvOpenNow.setText("OPEN NOW");
        } else {
            tvOpenNow.setText("CLOSED");
        }
        ratingBar.setRating((float) restaurant.getRating());

        tvWebsite.setClickable(true);
        tvWebsite.setMovementMethod(LinkMovementMethod.getInstance());
        String link = "<a href='" + restaurant.getWebsite() + "'> Visit our website </a>";
        tvWebsite.setText(Html.fromHtml(link, Html.FROM_HTML_MODE_COMPACT));

        tvPhoneNumber.setText(restaurant.getPhone());

    }
}