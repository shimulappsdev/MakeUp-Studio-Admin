package com.example.makeupstudioadmin.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.makeupstudioadmin.R;
import com.example.makeupstudioadmin.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.makeSliderCard.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ContainerActivity.class);
            intent.putExtra("makeUpSlider", "makeUpSlider");
            startActivity(intent);
        });

        binding.categoryCard.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ContainerActivity.class);
            intent.putExtra("categories", "categories");
            startActivity(intent);
        });

        binding.popularMakeupCard.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ContainerActivity.class);
            intent.putExtra("popularMakeup", "popularMakeup");
            startActivity(intent);
        });

        binding.productCard.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ContainerActivity.class);
            intent.putExtra("products", "products");
            startActivity(intent);
        });

        binding.popularBrandCard.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ContainerActivity.class);
            intent.putExtra("brand", "brand");
            startActivity(intent);
        });
        
        binding.adManageCard.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ContainerActivity.class);
            intent.putExtra("adManage", "adManage");
            startActivity(intent);
        });

        binding.logoutBtn.setOnClickListener(view -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Logout");
            alertDialog.setMessage("What's in your mind?");
            alertDialog.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getApplicationContext(), EntryActivity.class);
                    intent.putExtra("signin", "signin");
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_SHORT).show();
                }
            });
            alertDialog.setNegativeButton("Stay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog dialog = alertDialog.create();
            dialog.show();
        });

    }
}