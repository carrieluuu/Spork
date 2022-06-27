package com.example.spork.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.spork.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class ForgotPassword extends AppCompatActivity {
    public static final String TAG = "ForgotPassword";

    private EditText etEmail;
    private Button btnResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        etEmail = findViewById(R.id.etEmail);
        btnResetPassword = findViewById(R.id.btnResetPassword);

        resetPasswordButton();
    }

    private void resetPasswordButton() {
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick reset password button");
                passwordReset();
            }
        });
    }

    private void passwordReset() {
        String email = etEmail.getText().toString();
        ParseUser.requestPasswordResetInBackground(email, e -> {
            if (e == null) {
                Log.i(TAG, "An email was successfully sent with reset instructions.");
                Toast.makeText(this, "Password reset sent to " + email, Toast.LENGTH_LONG).show();

            } else {
                Log.e(TAG, e.toString());
                Toast.makeText(this, "Unable to reset password, try again later.", Toast.LENGTH_LONG).show();
            }
        });
    }

}