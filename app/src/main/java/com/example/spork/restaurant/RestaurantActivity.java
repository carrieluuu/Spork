package com.example.spork.restaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.text.InputType;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.spork.R;
import com.example.spork.Restaurant;
import com.example.spork.feed.ComposeFragment;
import com.example.spork.feed.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;

import org.parceler.Parcels;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RestaurantActivity extends AppCompatActivity {

    public static final String TAG = "RestaurantActivity";

    protected ReviewsAdapter adapter;
    protected List<Review> allReviews;
    private Restaurant restaurant;
    private TextView tvRestaurantName;
    private TextView tvAddress;
    private ImageView ivRestaurant;
    private TextView tvOpenNow;
    private RatingBar ratingBar;
    private TextView tvWebsite;
    private TextView tvPhoneNumber;
    private RecyclerView rvReviews;
    private Button btnAddReview;

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

        rvReviews = findViewById(R.id.rvReviews);
        btnAddReview = findViewById(R.id.btnAddReview);

        allReviews = new ArrayList<>();
        adapter = new ReviewsAdapter(this, allReviews);

        // set the adapter on the recycler view
        rvReviews.setAdapter(adapter);
        // set the layout manager on the recycler view
        rvReviews.setLayoutManager(new LinearLayoutManager(this));

        queryReviews();
        addReview();
        setUpLiveQuery();

    }

    protected void queryReviews() {
        ParseQuery<Review> query = ParseQuery.getQuery(Review.class);
        query.include(Review.KEY_USER)
                .whereEqualTo("restaurantId", restaurant.getYelpId())
                .addDescendingOrder("createdAt")
                .findInBackground(new FindCallback<Review>() {
                    @Override
                    public void done(List<Review> reviews, ParseException e) {
                        // check for errors
                        if (e != null) {
                            Log.e(TAG, "Issue with getting posts", e);
                            return;
                        }

                        // for debugging purposes let's print every review username to logcat
                        for (Review review : reviews) {
                            Log.i(TAG, "Review by username: " + review.getUser().getUsername());
                        }

                        // save received posts to list and notify adapter of new data
                        allReviews.addAll(reviews);
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void setUpLiveQuery() {
        ParseLiveQueryClient parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient();

        ParseQuery<Review> query = ParseQuery.getQuery(Review.class);

        // Connect to Parse server
        SubscriptionHandling<Review> subscriptionHandling = parseLiveQueryClient.subscribe(query);

        // Listen for CREATE events
        subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE, new
                SubscriptionHandling.HandleEventCallback<Review>() {
                    @Override
                    public void onEvent(ParseQuery<Review> query, Review review) {

                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                                rvReviews.scrollToPosition(0);
                            }

                        });
                    }
                });
    }

    private void addReview() {
        btnAddReview = findViewById(R.id.btnAddReview);
        btnAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RestaurantActivity.this);
                builder.setTitle("What do you think of " + restaurant.getName() + "?");

                // Set up the input
                final EditText etReview = new EditText(RestaurantActivity.this);
                etReview.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(etReview);

                // Set up the buttons
                builder.setPositiveButton("Post", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String review = etReview.getText().toString();
                        if (review.isEmpty()) {
                            Toast.makeText(RestaurantActivity.this, "Description cannot be empty", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        ParseUser currentUser = ParseUser.getCurrentUser();
                        savePost(review, currentUser);

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

            }
        });
    }

    private void savePost(String description, ParseUser currentUser) {
        Review review = new Review();
        review.setReview(description);
        review.setUser(currentUser);
        review.setRestaurantId(restaurant.getYelpId());
        review.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(RestaurantActivity.this, "Error while saving", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Post save was successful");
            }
        });
        allReviews.add(review);
        adapter.notifyDataSetChanged();
    }


}