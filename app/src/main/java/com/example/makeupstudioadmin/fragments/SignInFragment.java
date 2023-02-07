package com.example.makeupstudioadmin.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.makeupstudioadmin.R;
import com.example.makeupstudioadmin.activity.EntryActivity;
import com.example.makeupstudioadmin.activity.MainActivity;
import com.example.makeupstudioadmin.databinding.FragmentSignInBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInFragment extends Fragment {

    FragmentSignInBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignInBinding.inflate(getLayoutInflater(), container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null){
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }

        binding.loginBtn.setOnClickListener(view -> {
            String email = binding.userEmail.getText().toString().trim();
            String password = binding.userPassword.getText().toString().trim();
            if (email.equals("")){
                binding.userEmail.setError("Required");
            }else if (password.equals("")){
                binding.userPassword.setError("Required");
            }else {
                UserLogin(email, password);
            }
        });

        binding.registerNowBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), EntryActivity.class);
            intent.putExtra("signup","signup");
            startActivity(intent);
        });

        return binding.getRoot();
    }

    private void UserLogin(String email, String password) {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.loginBtn.setVisibility(View.GONE);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(getActivity(), "Log In Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                binding.progressBar.setVisibility(View.GONE);
                binding.loginBtn.setVisibility(View.VISIBLE);
                ShowAlert(e.getLocalizedMessage());
            }
        });
    }

    private void ShowAlert(String errorMsg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Caution!");
        builder.setMessage(errorMsg);
        builder.setIcon(android.R.drawable.stat_notify_error);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}