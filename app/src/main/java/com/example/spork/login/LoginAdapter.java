package com.example.spork.login;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.lang.ref.WeakReference;

public class LoginAdapter extends FragmentPagerAdapter {

    private WeakReference<Context> context;
    private int totalTabs;

    public LoginAdapter(FragmentManager fm, Context context, int totalTabs) {
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
                LoginTabFragment loginTabFragment = new LoginTabFragment();
                return loginTabFragment;

            case 1:
                RegistrationTabFragment registrationTabFragment = new RegistrationTabFragment();
                return registrationTabFragment;
            default:
                return null;

        }

    }
}
