package com.example.makeupstudioadmin.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.makeupstudioadmin.R;
import com.example.makeupstudioadmin.activity.ContainerActivity;
import com.example.makeupstudioadmin.adapter.CategoryAdapter;
import com.example.makeupstudioadmin.adapter.MakeupItemAdapter;
import com.example.makeupstudioadmin.databinding.FragmentMakeupItemBinding;
import com.example.makeupstudioadmin.model.Category;
import com.example.makeupstudioadmin.model.MakeupItem;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MakeupItemFragment extends Fragment {

    FragmentMakeupItemBinding binding;
    FirebaseFirestore database;
    MakeupItemAdapter adapter;
    List<MakeupItem> makeupItemList;
    Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMakeupItemBinding.inflate(getLayoutInflater(), container, false);

        makeupItemList = new ArrayList<>();
        binding.progressBar.setVisibility(View.VISIBLE);
        intent = getActivity().getIntent();
        String categoryId = intent.getStringExtra("categoryId");
        String categoryName = intent.getStringExtra("categoryName");

        binding.heading.setText("Items of "+categoryName);

        database = FirebaseFirestore.getInstance();

        database.collection("MakeUp")
                .document("category")
                .collection("categoryList")
                .document(categoryId)
                .collection("makeupItemList").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        makeupItemList.clear();
                        List<MakeupItem> data = value.toObjects(MakeupItem.class);
                        makeupItemList.addAll(data);
                        adapter = new MakeupItemAdapter(getActivity(), makeupItemList);
                        binding.makeItemRecyclerView.setAdapter(adapter);
                        binding.progressBar.setVisibility(View.GONE);
                    }
                });


        binding.addMakeItemBtn.setOnClickListener(view -> {
            Intent intent1 = new Intent(getActivity(), ContainerActivity.class);
            intent1.putExtra("addMakeupItem", "addMakeupItem");
            intent1.putExtra("categoryId", categoryId);
            startActivity(intent1);
        });

        binding.backBtn.setOnClickListener(view -> {
            getActivity().finish();
        });

        return binding.getRoot();
    }
}