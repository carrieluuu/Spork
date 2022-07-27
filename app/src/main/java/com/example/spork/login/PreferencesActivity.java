package com.example.spork.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.spork.MainActivity;
import com.example.spork.R;
import com.parse.ParseUser;

public class PreferencesActivity extends AppCompatActivity {

    public static final String TAG = "PreferencesActivity";
    public static final double VERY_IMPORTANT = 0.4;
    public static final double IMPORTANT = 0.3;
    public static final double NEUTRAL = 0.2;
    public static final double UNIMPORTANT = 0.1;
    public static final double VERY_UNIMPORTANT = 0;

    private ParseUser currentUser;
    private Spinner priceSpinner;
    private Spinner ratingSpinner;
    private Spinner popularitySpinner;
    private Spinner proximitySpinner;
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

        priceSpinner = findViewById(R.id.price_spinner);
        ratingSpinner = findViewById(R.id.rating_spinner);
        popularitySpinner=  findViewById(R.id.popularity_spinner);
        proximitySpinner = findViewById(R.id.proximity_spinner);
        btnDone = findViewById(R.id.btnDone);

        createSpinnerAdapter(priceSpinner);
        createSpinnerAdapter(ratingSpinner);
        createSpinnerAdapter(popularitySpinner);
        createSpinnerAdapter(proximitySpinner);

        priceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                priceWeight = getWeight(selected);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });

        ratingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                ratingWeight = getWeight(selected);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });

        popularitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                popularityWeight = getWeight(selected);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });

        proximitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                proximityWeight = getWeight(selected);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });


        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMainActivity();
            }
        });

    }

    private void createSpinnerAdapter(Spinner spinner) {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.preferences_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    private double getWeight(String selected) {

        if (selected.equals("Very important"))
            return VERY_IMPORTANT;
        else if (selected.equals("Important"))
            return IMPORTANT;
        else if (selected.equals("Neutral"))
            return NEUTRAL;
        else if (selected.equals("Unimportant"))
            return UNIMPORTANT;
        else if (selected.equals("Very unimportant"))
            return VERY_UNIMPORTANT;
        return 0.0;
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