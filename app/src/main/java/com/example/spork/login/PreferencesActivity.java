package com.example.spork.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.spork.MainActivity;
import com.example.spork.R;
import com.parse.ParseUser;

public class PreferencesActivity extends AppCompatActivity {

    public static final String TAG = "PreferencesActivity";

    private ParseUser currentUser;
    private CheckBox cbPrice;
    private CheckBox cbRating;
    private CheckBox cbPopularity;
    private CheckBox cbProximity;
    private Button btnDone;

    private double priceWeight;
    private double ratingWeight;
    private double popularityWeight;
    private double proximityWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        currentUser = ParseUser.getCurrentUser();

        cbPrice = findViewById(R.id.cbPrice);
        cbRating = findViewById(R.id.cbRating);
        cbPopularity = findViewById(R.id.cbPopularity);
        cbProximity = findViewById(R.id.cbProximity);
        btnDone = findViewById(R.id.btnDone);

        btnDone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                goMainActivity();
            }
        });


    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.cbPrice:
                if (checked)
                    priceWeight = 0.25;
                else
                    priceWeight = 0;
                break;
            case R.id.cbRating:
                if (checked)
                    ratingWeight = 0.25;
                else
                    ratingWeight = 0;
                break;
            case R.id.cbPopularity:
                if (checked)
                    popularityWeight = 0.25;
                else
                    popularityWeight = 0;
                break;

            case R.id.cbProximity:
                if(checked)
                    proximityWeight = 0.25;
                else
                    proximityWeight = 0;
        }
    }

    private void goMainActivity() {
        Log.i(TAG, "priceWeight: " + priceWeight + " ratingWeight: " + ratingWeight + " popularityWeight: " + popularityWeight + "proximityWeight: " + proximityWeight);
        currentUser.put("priceWeight", priceWeight);
        currentUser.put("ratingWeight", ratingWeight);
        currentUser.put("popularityWeight", popularityWeight);
        currentUser.put("proximityWeight", proximityWeight);

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}