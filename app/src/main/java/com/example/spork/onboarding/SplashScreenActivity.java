package com.example.spork.onboarding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.airbnb.lottie.Lottie;
import com.airbnb.lottie.LottieAnimationView;
import com.example.spork.R;

public class SplashScreenActivity extends AppCompatActivity {

    private static final int NUM_PAGES = 4;

    private ImageView ivSplashLogo;
    private ImageView ivSplashBg;
    private LottieAnimationView lottieAnimation;
    private ViewPager viewPager;
    private ScreenSlidePagerAdapter pagerAdapter;
    private Animation anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ivSplashLogo = findViewById(R.id.ivSplashLogo);
        ivSplashBg = findViewById(R.id.ivSplashBg);
        lottieAnimation = findViewById(R.id.lottieAnimation);

        viewPager = findViewById(R.id.viewpager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        anim = AnimationUtils.loadAnimation(SplashScreenActivity.this, R.anim.onboarding_anim);
        viewPager.startAnimation(anim);

        ivSplashBg.animate().translationY(-1600).setDuration(1000).setStartDelay(4000);
        ivSplashLogo.animate().translationY(1400).setDuration(1000).setStartDelay(4000);
        lottieAnimation.animate().translationY(1400).setDuration(1000).setStartDelay(4000);


    }

    // class to connect splash screen to onboarding pages
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        // constructor
        public ScreenSlidePagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    OnboardingFragment1 tab1 = new OnboardingFragment1();
                    return tab1;
                case 1:
                    OnboardingFragment2 tab2 = new OnboardingFragment2();
                    return tab2;
                case 2:
                    OnboardingFragment3 tab3 = new OnboardingFragment3();
                    return tab3;
                case 3:
                    OnboardingFragment4 tab4 = new OnboardingFragment4();
                    return tab4;
            }
            return null;
        }

        @Override
        public int getCount() {
           return NUM_PAGES;
        }
    }
}