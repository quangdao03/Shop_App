package com.example.shop_app.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.shop_app.R;
import com.example.shop_app.database.MyDatabaseHelper;
import com.example.shop_app.model.Product;

import com.example.shop_app.utils.CustomToast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
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
    FirebaseAuth firebaseAuth;
    Button btn_Add_to_cart;

    String url;
    String IDProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        name = getIntent().getStringExtra("name");
        firebaseAuth = FirebaseAuth.getInstance();
        tv_product_name = findViewById(R.id.tv_product_name);
        tv_creator = findViewById(R.id.tv_product_creator);
        tv_variant = findViewById(R.id.tv_variant);
        tv_desc = findViewById(R.id.tv_desc);
        tv_price = findViewById(R.id.tv_price);
        tvTitleToolbar = findViewById(R.id.tvTitleToolbar);
        ivToolbarLeft = findViewById(R.id.ivToolbarLeft);
        ivToolbarRight = findViewById(R.id.ivToolbarRight);
        img_product_detail = findViewById(R.id.img_product_detail);
        btn_Add_to_cart = findViewById(R.id.btn_Add_to_cart);


        tvTitleToolbar.setText("Product Detail");
        ivToolbarRight.setImageResource(R.drawable.icon_love);

        ivToolbarLeft.setImageResource(R.drawable.ic_left);
        ivToolbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getProductDetail();
        ivToolbarRight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ivToolbarRight.setImageResource(R.drawable.ic_heat_click);
                // onClickToWishList();
                onClickWishList();
            }
        });
        btn_Add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCart();
            }
        });


    }




    private void getProductDetail() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Product");
        // Read from the database
        myRef.child(name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Product product = new Product();
                tv_product_name.setText(dataSnapshot.child("name").getValue().toString());
                url = dataSnapshot.child("image").getValue().toString();
                Glide.with(getApplicationContext()).load(url).into(img_product_detail);
                tv_creator.setText(dataSnapshot.child("creator").getValue().toString());
                tv_variant.setText(dataSnapshot.child("variant").getValue().toString());
                tv_desc.setText(dataSnapshot.child("desc").getValue().toString());
                tv_price.setText(dataSnapshot.child("price").getValue().toString());
                IDProduct = dataSnapshot.child("id").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value

            }
        });
    }

    private void onClickWishList() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference data = database.getReference("Product/"+name);
        Product product = new Product();

        Map<String,Object> mapUpdate = new HashMap<>();
        mapUpdate.put("favourite",true);
        mapUpdate.put("uid",firebaseAuth.getUid());

        data.updateChildren(mapUpdate, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @androidx.annotation.NonNull DatabaseReference ref) {

                CustomToast.makeText(ProductDetail.this,"Added to Wishlist",CustomToast.LENGTH_LONG,CustomToast.SUCCESS,true).show();
            }
        });


    }
    private void addToCart() {
        String image = url;
        String name = tv_product_name.getText().toString().trim();
        String price_name = tv_price.getText().toString().trim().replace("","");
        String creator = tv_creator.getText().toString().trim();
        String variant = tv_variant.getText().toString().trim();
        String totalprice = tv_price.getText().toString().trim().replace("","");
        int quantity = 1;
        String idProduct = IDProduct.toString();
        addToCart(idProduct,image,name,creator,variant,price_name,totalprice, String.valueOf(quantity));
        CustomToast.makeText(ProductDetail.this,"Sản phẩm đã được thêm vào giỏ hàng",CustomToast.LENGTH_LONG,CustomToast.SUCCESS,true).show();
    }
    private void addToCart(String productID,String image, String name, String creator, String variant, String price, String priceEach, String quantity ){
        MyDatabaseHelper myDatabaseHelper = new MyDatabaseHelper(this);
        myDatabaseHelper.addCart(productID,image,name,creator,variant,price,priceEach,quantity);

    }


    private void onClickToWishList() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myWish = database.getReference("Product/"+name);
        myWish.child("name").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@androidx.annotation.NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                Product product = new Product();
                tv_product_name.setText(dataSnapshot.child("name").getValue().toString());
                String image = dataSnapshot.child("image").getValue().toString();
                Glide.with(ProductDetail.this).load(image).placeholder(R.drawable.fram1).into(img_product_detail);
                tv_creator.setText(dataSnapshot.child("creator").getValue().toString());
                tv_variant.setText(dataSnapshot.child("variant").getValue().toString());
                tv_desc.setText(dataSnapshot.child("desc").getValue().toString());
                tv_price.setText(dataSnapshot.child("price").getValue().toString());

                Boolean value = (Boolean) dataSnapshot.child("favourite").getValue();
                if (value == true){
                    ivToolbarRight.setImageResource(R.drawable.ic_heat_click);
                }else {
                    ivToolbarRight.setImageResource(R.drawable.icon_love);
                }
            }

            @Override
            public void onChildChanged(@androidx.annotation.NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@androidx.annotation.NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@androidx.annotation.NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

            }
        });

    }






}