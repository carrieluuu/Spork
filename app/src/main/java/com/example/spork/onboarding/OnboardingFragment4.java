package com.example.spork.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.spork.R;
import com.example.spork.login.LoginActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class OnboardingFragment4 extends Fragment {

    private FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_onboarding4, container, false);

        fab = root.findViewById(R.id.fab);

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });


        return root;
    }
}
