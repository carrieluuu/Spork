package com.example.spork.login;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.spork.R;
import com.parse.ParseUser;

public class LoginTabFragment extends Fragment {

    public static final int v = 0;
    public static final String TAG = "LoginTabFragment";

    private TextView tvUsername;
    private EditText etUsername;
    private TextView tvPassword;
    private EditText etPassword;
    private TextView tvForgetPassword;
    private Button btnLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment, container, false);

        tvUsername = root.findViewById(R.id.tvUsername);
        etUsername = root.findViewById(R.id.etUsername);
        tvPassword = root.findViewById(R.id.tvPassword);
        etPassword = root.findViewById(R.id.etPassword);
        tvForgetPassword = root.findViewById(R.id.tvForgetPassword);
        btnLogin = root.findViewById(R.id.btnLogin);

        // animations
        tvUsername.setTranslationX(800);
        etUsername.setTranslationX(800);
        tvPassword.setTranslationX(800);
        etPassword.setTranslationX(800);
        tvForgetPassword.setTranslationX(800);
        btnLogin.setTranslationX(800);

        // opacity
        tvUsername.setAlpha(v);
        etUsername.setAlpha(v);
        tvPassword.setAlpha(v);
        etPassword.setAlpha(v);
        tvForgetPassword.setAlpha(v);
        btnLogin.setAlpha(v);

        tvUsername.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        etUsername.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        tvPassword.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        etPassword.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        tvForgetPassword.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        btnLogin.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();

        // if user is already logged in, go to main activity
        if (ParseUser.getCurrentUser() != null) {
            goMainActivity();
        }

        loginButton();


        return root;
    }

    private void loginButton() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"onClick login button");
                String username = tvUsername.getText().toString();
                String password = etPassword.getText().toString();
                loginUser(username, password);
            }
        });
    }

    private void loginUser(String username, String password) {
        Log.i(TAG, "attempting to log in user " + username);
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with login", e);
                    Toast.makeText(LoginActivity.this, "Incorrect credentials", Toast.LENGTH_LONG).show();
                    return;
                }
                goMainActivity();
                Toast.makeText(LoginActivity.this, "Login success!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}

}
