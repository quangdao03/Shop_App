package com.example.shop_app.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.shop_app.R;
import com.example.shop_app.database.CartDatabase;
import com.example.shop_app.database.CartRoom;
import com.example.shop_app.database.MyDatabaseHelper;
import com.example.shop_app.model.Product;

import com.example.shop_app.utils.CustomToast;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDetail extends AppCompatActivity {
    ImageView img_product_detail,ivToolbarLeft,ivToolbarRight;
    TextView tv_product_name,tv_creator,tv_variant,tv_desc,tv_price,tvTitleToolbar;
    String name;
    FirebaseAuth firebaseAuth;
    TextView btn_Add_to_cart;

    String url;
    String IDProduct;

    private int cost = 0;
    private int finalCost = 0;

    int quantityProduct = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        getSupportActionBar().hide();
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

        checkUser();
        tvTitleToolbar.setText(getText(R.string.product_details));
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
                addProduct();
//                btn_Add_to_cart.setOnClickListener(null);
//                btn_Add_to_cart.setBackgroundResource(R.drawable.bg_buy_fail);
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

                CustomToast.makeText(ProductDetail.this,""+getText(R.string.add_wishlist),CustomToast.LENGTH_LONG,CustomToast.SUCCESS,true).show();
            }
        });


    }
    private void addProduct(){
        ImageView img_Product,count_down,count_add,btn_close;
        TextView  tv_dialog_name,tv_dialog_creator,tv_dialog_variant,tv_quantity,tv_price_product,tv_price_product_final;
        Button btn_AddToCart;
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View dialog = LayoutInflater.from(this).inflate(R.layout.layout_dialog_buy, null);
        bottomSheetDialog.setContentView(dialog);
        tv_dialog_name = dialog.findViewById(R.id.tv_dialog_name);
        tv_dialog_creator = dialog.findViewById(R.id.tv_dialog_creator);
        tv_dialog_variant = dialog.findViewById(R.id.tv_dialog_variant);
        img_Product = dialog.findViewById(R.id.img_Product);
        tv_price_product = dialog.findViewById(R.id.tv_price_product);
        tv_price_product_final = dialog.findViewById(R.id.tv_price_product_final);
        tv_quantity = dialog.findViewById(R.id.tv_quantity);
        count_down = dialog.findViewById(R.id.count_down);
        count_add = dialog.findViewById(R.id.count_add);
        btn_AddToCart = dialog.findViewById(R.id.btn_AddToCart);
        btn_close = dialog.findViewById(R.id.btn_close);


        tv_dialog_name.setText(tv_product_name.getText().toString().trim());
        tv_dialog_creator.setText(tv_creator.getText().toString().trim());
        tv_dialog_variant.setText(tv_variant.getText().toString().trim());
        tv_price_product.setText(tv_price.getText().toString().trim().replace("",""));
        Glide.with(getApplicationContext()).load(url).into(img_Product);
        tv_quantity.setText(""+quantityProduct);
        String price = tv_price_product.getText().toString().trim();
        cost = Integer.parseInt(price.replaceAll("$", ""));
        finalCost = Integer.parseInt(price.replaceAll("$", ""));
        tv_price_product_final.setText(""+finalCost);
        count_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalCost = finalCost + cost;
                quantityProduct++;
                tv_price_product_final.setText(""+finalCost);
                tv_quantity.setText(""+quantityProduct);
            }
        });
        count_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantityProduct>1) {
                    finalCost = finalCost - cost;
                    quantityProduct--;
                    tv_price_product_final.setText(""+finalCost);
                    tv_quantity.setText(""+quantityProduct);
                }
            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });

        btn_AddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomSheetDialog.dismiss();
                final Dialog dialog = new Dialog(ProductDetail.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layout);
                dialog.getWindow().getAttributes().windowAnimations = androidx.appcompat.R.style.Animation_AppCompat_DropDownUp;

                Window window = dialog.getWindow();
                window.setGravity(Gravity.BOTTOM);
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(true);
                Button btn_Ok = dialog.findViewById(R.id.btn_Ok);
                TextView tv_tap = dialog.findViewById(R.id.tv_tap);
                tv_tap.setPaintFlags(tv_tap.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                String image = url;
                String name = tv_dialog_name.getText().toString().trim();
                String price_name = price;
                String creator = tv_dialog_creator.getText().toString().trim();
                String variant = tv_dialog_variant.getText().toString().trim();
                String totalprice = tv_price_product_final.getText().toString().trim().replace("","");
                String quantity = tv_quantity.getText().toString().trim();
                String idProduct = IDProduct;
                addToCart(idProduct,image,name,creator,variant,price_name,totalprice,quantity);

                CartRoom cartRoom = new CartRoom();
                cartRoom.setProductID(idProduct);
                cartRoom.setImage(url);
                cartRoom.setName(name);
                cartRoom.setCreator(creator);
                cartRoom.setVariant(variant);
                cartRoom.setPrice(price_name);
                cartRoom.setPriceEach(totalprice);
                cartRoom.setQuantity(quantity);
                CartDatabase.getInstance(ProductDetail.this).cartDAO().insertCart(cartRoom);




                btn_Ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        CustomToast.makeText(ProductDetail.this,""+getText(R.string.product_cart),CustomToast.LENGTH_LONG,CustomToast.SUCCESS,true).show();
                        btn_Add_to_cart.setOnClickListener(null);
                        btn_Add_to_cart.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_buy_fail));
                        btn_Add_to_cart.setTextColor(Color.parseColor("#414040"));
                        btn_Add_to_cart.setText("Đã thêm vào giỏ hàng");
                    }
                });
                tv_tap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                dialog.show();
            }
        });

        bottomSheetDialog.show();

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

    private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(this, LoginActivity.class));
        }
    }




}