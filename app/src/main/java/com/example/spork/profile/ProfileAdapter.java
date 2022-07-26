package com.example.spork.profile;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.spork.login.LoginTabFragment;
import com.example.spork.login.RegistrationTabFragment;

import java.lang.ref.WeakReference;

public class ProfileAdapter extends FragmentPagerAdapter {

    private WeakReference<Context> context;
    private int totalTabs;

    public ProfileAdapter(FragmentManager fm, Context context, int totalTabs) {
        super(fm);
        this.context = new WeakReference<Context>(context);
        this.totalTabs = totalTabs;
    }

    @Override
    public int getCount() {
        return totalTabs;
    }

    // switch between login and register tabs
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                ProfileFeedTabFragment profileFeedTabFragment = new ProfileFeedTabFragment();
                return profileFeedTabFragment;

            case 1:
                SavedRestaurantsTabFragment savedRestaurantsTabFragment = new SavedRestaurantsTabFragment();
                return savedRestaurantsTabFragment;
            default:
                return null;

        }

    }
}
