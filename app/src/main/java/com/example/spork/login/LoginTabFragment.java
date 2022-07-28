package com.example.spork.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.spork.MainActivity;
import com.example.spork.R;
import com.parse.ParseUser;

public class LoginTabFragment extends Fragment {

    public static final int v = 0;
    public static final String TAG = "LoginTabFragment";

    private TextView tvUsername;
    private EditText etUsername;
    private TextView tvPassword;
    private EditText etPassword;
    private TextView tvForgotPassword;
    private Button btnLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment, container, false);

        tvUsername = root.findViewById(R.id.tvUsername);
        etUsername = root.findViewById(R.id.etUsername);
        tvPassword = root.findViewById(R.id.tvPassword);
        etPassword = root.findViewById(R.id.etPassword);
        tvForgotPassword = root.findViewById(R.id.tvForgotPassword);
        btnLogin = root.findViewById(R.id.btnLogin);

        // animations
        tvUsername.setTranslationX(800);
        etUsername.setTranslationX(800);
        tvPassword.setTranslationX(800);
        etPassword.setTranslationX(800);
        tvForgotPassword.setTranslationX(800);
        btnLogin.setTranslationX(800);

        // opacity
        tvUsername.setAlpha(v);
        etUsername.setAlpha(v);
        tvPassword.setAlpha(v);
        etPassword.setAlpha(v);
        tvForgotPassword.setAlpha(v);
        btnLogin.setAlpha(v);

        displayAnimation(tvUsername);
        displayAnimation(etUsername);
        displayAnimation(tvPassword);
        displayAnimation(etPassword);
        displayAnimation(tvForgotPassword);
        displayAnimation(btnLogin);

        // if user is already logged in, go to main activity
        if (ParseUser.getCurrentUser() != null) {
            goMainActivity();
        }

        loginButton();
        forgotPassword();

        return root;
    }

    public void displayAnimation(View v) {
        v.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
    }

    private void loginButton() {
        btnLogin.setOnClickListener(v -> {
            Log.i(TAG,"onClick login button");
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            loginUser(username, password);
        });
    }

    private void loginUser(String username, String password) {
        Log.i(TAG, "attempting to log in user " + username);
        ParseUser.logInInBackground(username, password, (user, e) -> {
            if (e != null) {
                Log.e(TAG, "Issue with login", e);
                Toast.makeText(getContext(), "Incorrect credentials", Toast.LENGTH_LONG).show();
                return;
            }
            goMainActivity();
            Toast.makeText(getContext(), "Login success!", Toast.LENGTH_SHORT).show();

        });
    }

    private void forgotPassword() {
        tvForgotPassword.setOnClickListener(v -> {
            Log.i(TAG,"onClick forget password button");
            Intent i = new Intent(getContext(), ForgotPassword.class);
            startActivity(i);
        });
    }


    private void goMainActivity() {
        Intent i = new Intent(getContext(), MainActivity.class);
        startActivity(i);
    }
}

