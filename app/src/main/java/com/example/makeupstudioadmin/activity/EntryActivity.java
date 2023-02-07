package com.example.makeupstudioadmin.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.makeupstudioadmin.R;
import com.example.makeupstudioadmin.databinding.ActivityEntryBinding;
import com.example.makeupstudioadmin.fragments.SignInFragment;
import com.example.makeupstudioadmin.fragments.SignUpFragment;

public class EntryActivity extends AppCompatActivity {

    ActivityEntryBinding binding;
    SignInFragment signInFragment = new SignInFragment();
    SignUpFragment signUpFragment = new SignUpFragment();
    Intent intent;
    String signup, signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEntryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportFragmentManager().beginTransaction().replace(R.id.entryLayout, signInFragment).commit();

        intent = getIntent();
        signup = intent.getStringExtra("signup");
        signin = intent.getStringExtra("signin");

        if (signup != null){
            if (signup.equals("signup")){
                getSupportFragmentManager().beginTransaction().replace(R.id.entryLayout, signUpFragment).commit();
            }
        }

        if (signin != null){
            if (signin.equals("signin")){
                getSupportFragmentManager().beginTransaction().replace(R.id.entryLayout, signInFragment).commit();
            }
        }

    }
}