package com.example.makeupstudioadmin.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.makeupstudioadmin.R;
import com.example.makeupstudioadmin.databinding.FragmentAdManageBinding;
import com.example.makeupstudioadmin.model.Ad;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdManageFragment extends Fragment {

    FragmentAdManageBinding binding;
    DatabaseReference database;
    String banner, interstitial;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAdManageBinding.inflate(getLayoutInflater(), container, false);

        database = FirebaseDatabase.getInstance().getReference("AD_Mange");

        binding.bannerProgress.setVisibility(View.VISIBLE);
        binding.interstitialProgress.setVisibility(View.VISIBLE);

        binding.bannerStartBtn.setOnClickListener(view -> {
            String bannerStart = binding.bannerStartBtn.getText().toString().trim();
            binding.bannerAdShow.setText(bannerStart);
            banner = binding.bannerAdShow.getText().toString().trim();
            Map<String, Object> bannerMap = new HashMap<>();
            bannerMap.put("banner",banner);
            database.child("Banner").setValue(bannerMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(getActivity(), "Banner AD Run", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });

        binding.bannerEndBtn.setOnClickListener(view -> {
            String bannerEnd = binding.bannerEndBtn.getText().toString().trim();
            binding.bannerAdShow.setText(bannerEnd);
            banner = binding.bannerAdShow.getText().toString().trim();
            Map<String, Object> bannerMap = new HashMap<>();
            bannerMap.put("banner",banner);
            database.child("Banner").setValue(bannerMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(getActivity(), "Banner AD Stop", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });

        binding.interstitialStartBtn.setOnClickListener(view -> {
            String interstitialStart = binding.interstitialStartBtn.getText().toString().trim();
            binding.interstitialAdShow.setText(interstitialStart);
            interstitial = binding.interstitialAdShow.getText().toString().trim();
            Map<String, Object> interstitialMap = new HashMap<>();
            interstitialMap.put("interstitial",interstitial);
            database.child("Interstitial").setValue(interstitialMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(getActivity(), "Interstitial AD Run", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });

        binding.interstitialEndBtn.setOnClickListener(view -> {
            String interstitialEnd = binding.interstitialEndBtn.getText().toString().trim();
            binding.interstitialAdShow.setText(interstitialEnd);
            interstitial = binding.interstitialAdShow.getText().toString().trim();
            Map<String, Object> interstitialMap = new HashMap<>();
            interstitialMap.put("interstitial",interstitial);
            database.child("Interstitial").setValue(interstitialMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(getActivity(), "Interstitial AD Stop", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });

        database.child("Banner").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Ad ad = snapshot.getValue(Ad.class);
                binding.bannerAdShow.setText(ad.getBanner());
                binding.bannerProgress.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database.child("Interstitial").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Ad ad = snapshot.getValue(Ad.class);
                binding.interstitialAdShow.setText(ad.getInterstitial());
                binding.interstitialProgress.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.backBtn.setOnClickListener(view -> {
            getActivity().finish();
        });

        return binding.getRoot();
    }
}