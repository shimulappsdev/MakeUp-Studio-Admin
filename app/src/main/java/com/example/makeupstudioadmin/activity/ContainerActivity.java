package com.example.makeupstudioadmin.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.makeupstudioadmin.R;
import com.example.makeupstudioadmin.databinding.ActivityContainerBinding;
import com.example.makeupstudioadmin.fragments.AdManageFragment;
import com.example.makeupstudioadmin.fragments.AddMakeUpItemFragment;
import com.example.makeupstudioadmin.fragments.BrandFragment;
import com.example.makeupstudioadmin.fragments.CategoryFragment;
import com.example.makeupstudioadmin.fragments.ItemDetailsFragment;
import com.example.makeupstudioadmin.fragments.MakeUpItemSliderFragment;
import com.example.makeupstudioadmin.fragments.MakeUpSliderFragment;
import com.example.makeupstudioadmin.fragments.MakeupItemFragment;
import com.example.makeupstudioadmin.fragments.PopularMakeUpFragment;
import com.example.makeupstudioadmin.fragments.ProductFragment;
import com.example.makeupstudioadmin.fragments.UpdateBrandFragment;
import com.example.makeupstudioadmin.fragments.UpdateCategoryFragment;
import com.example.makeupstudioadmin.fragments.UpdateMakeupItemFragment;
import com.example.makeupstudioadmin.fragments.UpdatePopularMakeupFragment;
import com.example.makeupstudioadmin.fragments.UpdateProductFragment;

public class ContainerActivity extends AppCompatActivity {

    ActivityContainerBinding binding;
    MakeUpSliderFragment sliderFragment = new MakeUpSliderFragment();
    CategoryFragment categoryFragment = new CategoryFragment();
    PopularMakeUpFragment popularMakeUpFragment = new PopularMakeUpFragment();
    ProductFragment productFragment = new ProductFragment();
    BrandFragment brandFragment = new BrandFragment();
    MakeupItemFragment makeupItemFragment = new MakeupItemFragment();
    AddMakeUpItemFragment addMakeUpItemFragment = new AddMakeUpItemFragment();
    MakeUpItemSliderFragment makeUpItemSliderFragment = new MakeUpItemSliderFragment();
    ItemDetailsFragment itemDetailsFragment = new ItemDetailsFragment();
    UpdateCategoryFragment updateCategoryFragment = new UpdateCategoryFragment();
    UpdateMakeupItemFragment updateMakeupItemFragment = new UpdateMakeupItemFragment();
    UpdatePopularMakeupFragment updatePopularMakeupFragment = new UpdatePopularMakeupFragment();
    UpdateProductFragment updateProductFragment = new UpdateProductFragment();
    UpdateBrandFragment updateBrandFragment = new UpdateBrandFragment();
    AdManageFragment adManageFragment = new AdManageFragment();

    Intent intent;
    String makeUpSlider, categories, popularMakeup,
            products, brand, makeupItem, addMakeupItem,
            itemSlider, details, updateCategory,
            updateMakeupItem, updatePopularMakeup, updateProduct,
            updateBrand, adManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContainerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        intent = getIntent();

        makeUpSlider = intent.getStringExtra("makeUpSlider");
        categories = intent.getStringExtra("categories");
        popularMakeup = intent.getStringExtra("popularMakeup");
        products = intent.getStringExtra("products");
        brand = intent.getStringExtra("brand");
        makeupItem = intent.getStringExtra("makeupItem");
        addMakeupItem = intent.getStringExtra("addMakeupItem");
        itemSlider = intent.getStringExtra("itemSlider");
        details = intent.getStringExtra("details");
        updateCategory = intent.getStringExtra("updateCategory");
        updateMakeupItem = intent.getStringExtra("updateMakeupItem");
        updatePopularMakeup = intent.getStringExtra("updatePopularMakeup");
        updateProduct = intent.getStringExtra("updateProduct");
        updateBrand = intent.getStringExtra("updateBrand");
        adManage = intent.getStringExtra("adManage");

        if (makeUpSlider != null){
            if (makeUpSlider.equals("makeUpSlider")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, sliderFragment).commit();
            }
        }

        if (categories != null){
            if (categories.equals("categories")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, categoryFragment).commit();
            }
        }

        if (popularMakeup != null){
            if (popularMakeup.equals("popularMakeup")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, popularMakeUpFragment).commit();
            }
        }

        if (products != null){
            if (products.equals("products")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, productFragment).commit();
            }
        }

        if (brand != null){
            if (brand.equals("brand")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, brandFragment).commit();
            }
        }

        if (makeupItem != null){
            if (makeupItem.equals("makeupItem")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, makeupItemFragment).commit();
            }
        }

        if (addMakeupItem != null){
            if (addMakeupItem.equals("addMakeupItem")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, addMakeUpItemFragment).commit();
            }
        }

        if (itemSlider != null){
            if (itemSlider.equals("itemSlider")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, makeUpItemSliderFragment).commit();
            }
        }

        if (details != null){
            if (details.equals("details")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, itemDetailsFragment).commit();
            }
        }

        if (updateCategory != null){
            if (updateCategory.equals("updateCategory")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, updateCategoryFragment).commit();
            }
        }

        if (updateMakeupItem != null){
            if (updateMakeupItem.equals("updateMakeupItem")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, updateMakeupItemFragment).commit();
            }
        }

        if (updatePopularMakeup != null){
            if (updatePopularMakeup.equals("updatePopularMakeup")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, updatePopularMakeupFragment).commit();
            }
        }

        if (updateProduct != null){
            if (updateProduct.equals("updateProduct")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, updateProductFragment).commit();
            }
        }

        if (updateBrand != null){
            if (updateBrand.equals("updateBrand")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, updateBrandFragment).commit();
            }
        }

        if (adManage != null){
            if (adManage.equals("adManage")){
                getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, adManageFragment).commit();
            }
        }

    }
}