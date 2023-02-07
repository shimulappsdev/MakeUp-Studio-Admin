package com.example.makeupstudioadmin.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.makeupstudioadmin.R;
import com.example.makeupstudioadmin.adapter.SliderAdapter;
import com.example.makeupstudioadmin.databinding.FragmentMakeUpSliderBinding;
import com.example.makeupstudioadmin.model.Slider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MakeUpSliderFragment extends Fragment {

    FragmentMakeUpSliderBinding binding;
    FirebaseFirestore database;
    StorageReference storageReference;
    Uri sliderImgUri;
    String sliderImgUrl;
    ProgressDialog dialog;
    SliderAdapter adapter;
    List<Slider> sliderList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMakeUpSliderBinding.inflate(getLayoutInflater(), container, false);
        sliderList = new ArrayList<>();
        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Attaching Slider");
        dialog.setCancelable(false);

        database = FirebaseFirestore.getInstance();
        binding.progressBar.setVisibility(View.VISIBLE);

        binding.addSliderImgBtn.setOnClickListener(view -> {
            if (sliderImgUri != null){
                dialog.show();
                storageReference = FirebaseStorage.getInstance().getReference("MakeUp").child("slider").child("slider"+System.currentTimeMillis());
                storageReference.putFile(sliderImgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()){
                            dialog.dismiss();
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    sliderImgUrl = String.valueOf(uri);
                                    String sliderId = UUID.randomUUID().toString();
                                    Map<String, Object> sliderMap = new HashMap<>();
                                    sliderMap.put("slider_id", sliderId);
                                    sliderMap.put("slider_image", sliderImgUrl);
                                    database.collection("MakeUp")
                                            .document("slider_img")
                                            .collection("slider")
                                            .document(sliderId).set(sliderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        dialog.dismiss();
                                                        Toast.makeText(getActivity(), "Successfully Added", Toast.LENGTH_SHORT).show();
                                                        binding.sliderImg.setImageResource(R.drawable.imagehint);
                                                    }
                                                }
                                            });
                                }
                            });
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), "Oops...Upload Failed", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        database.collection("MakeUp")
                .document("slider_img")
                .collection("slider").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        sliderList.clear();
                        List<Slider> data = value.toObjects(Slider.class);
                        sliderList.addAll(data);
                        adapter = new SliderAdapter(getActivity(), sliderList);
                        binding.sliderImgRecyclerView.setAdapter(adapter);
                        binding.progressBar.setVisibility(View.GONE);
                    }
                });

        binding.addImg.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent,100);
        });

        binding.backBtn.setOnClickListener(view -> {
            getActivity().finish();
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    sliderImgUri = data.getData();
                    binding.sliderImg.setImageURI(sliderImgUri);
                }
            }
        }
    }
}