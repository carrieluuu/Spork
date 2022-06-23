package com.example.spork.onboarding;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.airbnb.lottie.Lottie;
import com.airbnb.lottie.LottieAnimationView;
import com.example.spork.R;

public class SplashScreenActivity extends AppCompatActivity {

    private ImageView ivSplashLogo;
    private ImageView ivSplashBg;
    private LottieAnimationView lottieAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ivSplashLogo = findViewById(R.id.ivSplashLogo);
        ivSplashBg = findViewById(R.id.ivSplashBg);
        lottieAnimation = findViewById(R.id.lottieAnimation);

        ivSplashBg.animate().translationY(-1600).setDuration(1000).setStartDelay(4000);
        ivSplashLogo.animate().translationY(1400).setDuration(1000).setStartDelay(4000);
        lottieAnimation.animate().translationY(1400).setDuration(1000).setStartDelay(4000);


    }
}