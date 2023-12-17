package com.example.shop_app.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.shop_app.R;
import com.example.shop_app.adapter.ListProductNew;
import com.example.shop_app.adapter.ProductAdapterSeller;
import com.example.shop_app.databinding.ActivityShopDetailBinding;
import com.example.shop_app.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShopDetailActivity extends AppCompatActivity {
    ActivityShopDetailBinding binding;
    String uid;
    List<Product> productList = new ArrayList<>();
    ListProductNew listProductNew;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShopDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        binding.toolbar.ivToolbarLeft.setImageResource(R.drawable.ic_left);
        binding.toolbar.ivToolbarRight.setVisibility(View.GONE);
        binding.toolbar.tvTitleToolbar.setText("Shop");
         uid = getIntent().getStringExtra("uid");
        if (uid != null){
            getInfoShop();
            getProductSeller();
        }
        binding.toolbar.ivToolbarLeft.setOnClickListener(view -> {
            onBackPressed();
            finish();
        });
    }
    private void getInfoShop(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");
        myRef.orderByChild("uid").equalTo(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String name = ""+dataSnapshot.child("shop_name").getValue();
                    String email = ""+dataSnapshot.child("email").getValue();
                    String phone = ""+dataSnapshot.child("phone").getValue();
                    String profileImage = ""+dataSnapshot.child("profileImage").getValue();
                    String accountType = ""+dataSnapshot.child("accountType").getValue();
                    binding.tvAddress.setText(dataSnapshot.child("address").getValue().toString());
                    binding.tvNameShop.setText(name);

                    try {
                        Glide.with(ShopDetailActivity.this).load(profileImage).placeholder(R.drawable.profile).into(binding.imgShop);
                    }catch (Exception e){
                        binding.imgShop.setImageResource(R.drawable.profile);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getProductSeller() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.rcyProductShop.setLayoutManager(linearLayoutManager);
        binding.rcyProductShop.setHasFixedSize(true);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);

        binding.rcyProductShop.addItemDecoration(decoration);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myProdcut = database.getReference("Product");

        myProdcut.orderByChild("uid").equalTo(uid).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (productList != null) {
                    productList.clear();
                }
                for (DataSnapshot getData : dataSnapshot.getChildren()) {
                    Product product = getData.getValue(Product.class);
                    productList.add(product);
                }
                listProductNew = new ListProductNew(ShopDetailActivity.this, productList);
                binding.rcyProductShop.setAdapter(listProductNew);
                listProductNew.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}