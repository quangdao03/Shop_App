package com.example.shop_app.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shop_app.R;
import com.example.shop_app.adapter.BrandAdapter;
import com.example.shop_app.adapter.ListCategoryAdapter;
import com.example.shop_app.model.Brand;
import com.example.shop_app.model.Category;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Category_Product extends AppCompatActivity {
    TextView tvTitleToolbar,tv_product_name;
    ImageView ivToolbarRight, ivToolbarLeft;
    private String categoryID;
    RecyclerView rcyBrand;

    BrandAdapter brandAdapter;
    List<Brand> brandList = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_wishlist);
        init();

        tvTitleToolbar.setText(R.string.product);
        ivToolbarRight.setImageResource(R.drawable.icon_shopping_cart);
        ivToolbarLeft.setImageResource(R.drawable.ic_left);

        ivToolbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        categoryID = getIntent().getStringExtra("name");
        tv_product_name.setText(categoryID);

        getBrand();

    }

    private void getBrand() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( Category_Product.this, RecyclerView.HORIZONTAL, false);
        rcyBrand.setLayoutManager (linearLayoutManager);
        rcyBrand.setHasFixedSize(true);
        brandAdapter = new BrandAdapter(Category_Product.this,brandList);
        rcyBrand.setAdapter(brandAdapter);

//        String pathObject = String.valueOf(user.getId());
//        myRef.child(pathObject).setValue(user);
        String name = "";

        if (categoryID.equals("Fragrance")){
            name = "Category1";
        } else if (categoryID.equals("Bodycare")) {
            name = "Category2";
        }else if (categoryID.equals("Haircare")) {
            name = "Category3";
        }else if (categoryID.equals("Facial")) {
            name = "Category4";
        }


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Category/"+name);
        myRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (brandList!= null){
                    brandList.clear();
                }
                for (DataSnapshot getSnapshot: dataSnapshot.child("brand").getChildren()) {

                    Brand brand = getSnapshot.getValue(Brand.class);
                    brand.setImage(getSnapshot.child("url").getValue().toString());
                    brandList.add(brand);

                }
                brandAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Category_Product.this, "Get Fail Category", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void init(){

        rcyBrand = findViewById(R.id.rcyBrand);
        tv_product_name = findViewById(R.id.tv_product_name);
        tvTitleToolbar = findViewById(R.id.tvTitleToolbar);
        ivToolbarLeft = findViewById(R.id.ivToolbarLeft);
        ivToolbarRight = findViewById(R.id.ivToolbarRight);
    }
}
