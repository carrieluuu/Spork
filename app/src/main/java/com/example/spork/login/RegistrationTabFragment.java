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

import com.example.spork.R;
import com.parse.ParseUser;

public class RegistrationTabFragment extends Fragment {
    public static final int v = 0;
    public static final String TAG = "RegistrationTabFragment";

    private TextView tvName;
    private EditText etName;
    private TextView tvUsername;
    private EditText etUsername;
    private TextView tvEmail;
    private EditText etEmail;
    private TextView tvPassword;
    private EditText etPassword;
    private Button btnRegister;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.registration_tab_fragment, container, false);

        tvName = root.findViewById(R.id.tvFullName);
        etName = root.findViewById(R.id.etName);
        tvUsername = root.findViewById(R.id.tvUsername);
        etUsername = root.findViewById(R.id.etUsername);
        tvEmail = root.findViewById(R.id.tvEmail);
        etEmail = root.findViewById(R.id.etEmail);
        tvPassword = root.findViewById(R.id.tvPassword);
        etPassword = root.findViewById(R.id.etPassword);
        btnRegister = root.findViewById(R.id.btnRegister);

        // animations
        tvName.setTranslationX(800);
        etName.setTranslationX(800);
        tvUsername.setTranslationX(800);
        etUsername.setTranslationX(800);
        tvEmail.setTranslationX(800);
        etEmail.setTranslationX(800);
        tvPassword.setTranslationX(800);
        etPassword.setTranslationX(800);
        btnRegister.setTranslationX(800);

        // opacity
        tvName.setAlpha(v);
        etName.setAlpha(v);
        tvUsername.setAlpha(v);
        etUsername.setAlpha(v);
        tvEmail.setAlpha(v);
        etEmail.setAlpha(v);
        tvPassword.setAlpha(v);
        etPassword.setAlpha(v);
        btnRegister.setAlpha(v);

        displayAnimation(tvName);
        displayAnimation(etName);
        displayAnimation(tvUsername);
        displayAnimation(etUsername);
        displayAnimation(tvEmail);
        displayAnimation(etEmail);
        displayAnimation(tvPassword);
        displayAnimation(etPassword);
        displayAnimation(btnRegister);

        registerButton();

        return root;
    }

    private void displayAnimation(View v) {
        v.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();

    }

    private void registerButton() {
        btnRegister.setOnClickListener(v -> {
            Log.i(TAG,"onClick register button");
            String fullName = etName.getText().toString();
            String username = etUsername.getText().toString();
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            createUser(fullName, username, email, password);
        });
    }

    public void createUser(String fullName, String username, String email, String password) {
        ParseUser user = new ParseUser();
        user.put("fullName", fullName);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        // Other fields can be set just like any other ParseObject,
        // using the "put" method, like this: user.put("attribute", "its value");
        // If this field does not exists, it will be automatically created

        user.signUpInBackground(e -> {
            if (e == null) {
                // Hooray! Let them use the app now.
                goPreferencesActivity();
                Toast.makeText(getContext(), "Sign up success!", Toast.LENGTH_SHORT).show();
            } else {
                // Sign up didn't succeed. Look at the ParseException
                // to figure out what went wrong
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void goPreferencesActivity() {
        Intent i = new Intent(getContext(), PreferencesActivity.class);
        startActivity(i);
    }
}

