package com.example.spork.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.spork.R;

public class LoginTabFragment extends Fragment {

    public static final int v = 0;

    private TextView tvEmail;
    private EditText etEmail;
    private TextView tvPassword;
    private EditText etPassword;
    private TextView tvForgetPassword;
    private Button btnLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment, container, false);

        tvEmail = root.findViewById(R.id.tvEmail);
        etEmail = root.findViewById(R.id.etEmail);
        tvPassword = root.findViewById(R.id.tvPassword);
        etPassword = root.findViewById(R.id.etPassword);
        tvForgetPassword = root.findViewById(R.id.tvForgetPassword);
        btnLogin = root.findViewById(R.id.btnLogin);

        // animations
        tvEmail.setTranslationX(800);
        etEmail.setTranslationX(800);
        tvPassword.setTranslationX(800);
        etPassword.setTranslationX(800);
        tvForgetPassword.setTranslationX(800);
        btnLogin.setTranslationX(800);

        // opacity
        tvEmail.setAlpha(v);
        etEmail.setAlpha(v);
        tvPassword.setAlpha(v);
        etPassword.setAlpha(v);
        tvForgetPassword.setAlpha(v);
        btnLogin.setAlpha(v);

        tvEmail.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        etEmail.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        tvPassword.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        etPassword.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        tvForgetPassword.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        btnLogin.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();

        return root;
    }
}
