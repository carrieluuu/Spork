package com.example.spork.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.spork.MainActivity;
import com.example.spork.R;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.material.tabs.TabLayout;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    public static final int v = 0;
    private static final String EMAIL = "email";
    public static final String TAG = "LoginActivity";

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CallbackManager callbackManager = CallbackManager.Factory.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        tabLayout.addTab(tabLayout.newTab().setText("Login"));
        tabLayout.addTab(tabLayout.newTab().setText("Register"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final LoginAdapter adapter = new LoginAdapter(getSupportFragmentManager(), this, tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setTranslationY(300);

        // opacity for animation
        tabLayout.setAlpha(v);

        tabLayout.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();

        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        // If you are using in a fragment, call loginButton.setFragment(this);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                goMainActivity();
                Toast.makeText(LoginActivity.this, "Login success!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Cancelled login. Please try again.", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException exception) {
                Log.e(TAG, "Issue with login", exception);
                Toast.makeText(LoginActivity.this, "Incorrect credentials", Toast.LENGTH_LONG).show();
            }
        });

        loginButton.setOnClickListener(v -> LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile")));

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);

        tabLayout.animate()
                .translationY(0)
                .alpha(1)
                .setDuration(1000)
                .setStartDelay(400)
                .start();
    }
}