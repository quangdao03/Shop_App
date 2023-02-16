package com.example.shop_app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shop_app.R;
import com.example.shop_app.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ProductDetail extends AppCompatActivity {
    ImageView img_product_detail,ivToolbarLeft,ivToolbarRight;
    TextView tv_product_name,tv_creator,tv_variant,tv_desc,tv_price,tvTitleToolbar;
    String name;

    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        name = getIntent().getStringExtra("name");

        tv_product_name = findViewById(R.id.tv_product_name);
        tv_creator = findViewById(R.id.tv_product_creator);
        tv_variant = findViewById(R.id.tv_variant);
        tv_desc = findViewById(R.id.tv_desc);
        tv_price = findViewById(R.id.tv_price);
        tvTitleToolbar = findViewById(R.id.tvTitleToolbar);
        ivToolbarLeft = findViewById(R.id.ivToolbarLeft);
        ivToolbarRight = findViewById(R.id.ivToolbarRight);
        img_product_detail = findViewById(R.id.img_product_detail);


        tvTitleToolbar.setText("Product Detail");
        ivToolbarRight.setImageResource(R.drawable.icon_love);
        ivToolbarRight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ivToolbarRight.setImageResource(R.drawable.ic_heat_click);
            }
        });
        ivToolbarLeft.setImageResource(R.drawable.ic_left);
        ivToolbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getProductDetail();

    }


    private void getProductDetail() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Product");
        // Read from the database
        myRef.child(name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Product product = dataSnapshot.getValue(Product.class);
                tv_product_name.setText(product.getName());
                String image = dataSnapshot.child("image").getValue().toString();
                Glide.with(ProductDetail.this).load(image).into(img_product_detail);
                tv_creator.setText(product.getCreator());
                tv_variant.setText(product.getVariant());
                tv_desc.setText(product.getDesc());
                tv_price.setText(product.getPrice());

                Boolean value = product.getFavourite();
                if (value == true){
                    ivToolbarRight.setImageResource(R.drawable.ic_heat_click);
                }else {
                    ivToolbarRight.setImageResource(R.drawable.icon_love);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value

            }
        });
    }





}