package com.example.shop_app.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shop_app.R;
import com.example.shop_app.adapter.BrandAdapter;
import com.example.shop_app.adapter.ListProductAdapter;
import com.example.shop_app.model.Brand;
import com.example.shop_app.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Category_Product extends AppCompatActivity {
    TextView tvTitleToolbar, tv_product_name;
    ImageView ivToolbarRight, ivToolbarLeft;
    private String categoryID;
    RecyclerView rcyBrand, rcy_ProdcutCategory;
    ListProductAdapter adapter;
    List<Product> productList = new ArrayList<>();
    BrandAdapter brandAdapter;
    List<Brand> brandList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_wishlist);
        init();
        getSupportActionBar().hide();
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
        getProduct();

    }


    private void getBrand() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Category_Product.this, RecyclerView.HORIZONTAL, false);
        rcyBrand.setLayoutManager(linearLayoutManager);
        rcyBrand.setHasFixedSize(true);
        brandAdapter = new BrandAdapter(Category_Product.this, brandList);
        rcyBrand.setAdapter(brandAdapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Categorys/" + categoryID);
        myRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (brandList != null) {
                    brandList.clear();
                }
                for (DataSnapshot getSnapshot : dataSnapshot.child("brand").getChildren()) {

                    Brand brand = getSnapshot.getValue(Brand.class);
                    brand.setImage(getSnapshot.child("url").getValue().toString());
                    brand.setName(categoryID);
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

    private void getProduct() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(Category_Product.this, 2, GridLayoutManager.VERTICAL, false);

        rcy_ProdcutCategory.setLayoutManager(gridLayoutManager);
        rcy_ProdcutCategory.setHasFixedSize(true);
        adapter = new ListProductAdapter(Category_Product.this, productList);
        rcy_ProdcutCategory.setAdapter(adapter);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myProdcut = database.getReference("Product");
        myProdcut.orderByChild("category").equalTo(categoryID).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (productList != null) {
                    productList.clear();
                }
                for (DataSnapshot getData : dataSnapshot.getChildren()) {
                    Product product = new Product();
                    product.setUrl(getData.child("image").getValue().toString());
                    product.setName(getData.child("name").getValue().toString());
                    product.setPrice("Rp " + getData.child("price").getValue().toString());
                    product.setQuantity(getData.child("quantity").getValue().toString());
                    Log.d("AAA", "" + getData);
                    productList.add(product);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void init() {
        rcy_ProdcutCategory = findViewById(R.id.rcy_ProdcutCategory);
        rcyBrand = findViewById(R.id.rcyBrand);
        tv_product_name = findViewById(R.id.tv_product_name);
        tvTitleToolbar = findViewById(R.id.tvTitleToolbar);
        ivToolbarLeft = findViewById(R.id.ivToolbarLeft);
        ivToolbarRight = findViewById(R.id.ivToolbarRight);
    }
}
