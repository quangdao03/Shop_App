package com.example.shop_app.activity;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.shop_app.R;
import com.example.shop_app.database.CartDatabase;
import com.example.shop_app.database.CartRoom;
import com.example.shop_app.model.Product;
import com.example.shop_app.utils.CustomToast;
import com.example.shop_app.utils.SystemUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ProductDetail extends AppCompatActivity {
    ImageView img_product_detail, ivToolbarLeft, ivToolbarRight;
    TextView tv_product_name, tv_creator, tv_variant, tv_desc, tv_price, tvTitleToolbar;
    String name;
    FirebaseAuth firebaseAuth;
    TextView btn_Add_to_cart;

    String url;
    String IDProduct;

    private int cost = 0;
    private int finalCost = 0;

    int quantityProduct = 1;
    boolean fv = false;
    String shop_uid;

    String name_product, price, quantity, creator, variant;
    ImageView img_ava_shop;
    TextView tv_name_shop, tv_shop_detail, tv_category_product;
    RelativeLayout rl_view_shop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SystemUtil.setLocale(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        getSupportActionBar().hide();
        name = getIntent().getStringExtra("id");
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
        img_ava_shop = findViewById(R.id.img_ava_shop);
        tv_name_shop = findViewById(R.id.tv_name_shop);
        tv_shop_detail = findViewById(R.id.tv_shop_detail);
        rl_view_shop = findViewById(R.id.rl_view_shop);
        tv_category_product = findViewById(R.id.tv_category_product);

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
        getWishList();
        ivToolbarRight.setOnClickListener(view -> {
            if (!fv) {
                onClickWishList();
                fv = true;
            } else {
                onClickRemoveWishList();
                fv = false;
            }

        });


        btn_Add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProduct();
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

                Product product = dataSnapshot.getValue(Product.class);
                tv_product_name.setText(dataSnapshot.child("name").getValue().toString());
                url = dataSnapshot.child("image").getValue().toString();
                Glide.with(getApplicationContext()).load(url).into(img_product_detail);
                tv_creator.setText(dataSnapshot.child("creator").getValue().toString());
                tv_variant.setText(dataSnapshot.child("variant").getValue().toString());
                tv_desc.setText(dataSnapshot.child("desc").getValue().toString());
                tv_price.setText(dataSnapshot.child("price").getValue().toString());
                IDProduct = dataSnapshot.child("id").getValue().toString();

                shop_uid = dataSnapshot.child("uid").getValue().toString();
                name_product = dataSnapshot.child("name").getValue().toString();
                price = dataSnapshot.child("price").getValue().toString();
                quantity = dataSnapshot.child("quantity").getValue().toString();
                creator = dataSnapshot.child("creator").getValue().toString();
                variant = dataSnapshot.child("variant").getValue().toString();
                if (shop_uid != null) {
                    getInfoShop();
                    tv_category_product.setText(dataSnapshot.child("category").getValue().toString());
                    tv_shop_detail.setOnClickListener(view -> {
                        Intent intent = new Intent(ProductDetail.this, ShopDetailActivity.class);
                        intent.putExtra("uid", shop_uid);
                        startActivity(intent);
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value

            }
        });
    }

    private void getInfoShop() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");
        myRef.orderByChild("uid").equalTo(shop_uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String name = "" + dataSnapshot.child("shop_name").getValue();
                    String email = "" + dataSnapshot.child("email").getValue();
                    String phone = "" + dataSnapshot.child("phone").getValue();
                    String profileImage = "" + dataSnapshot.child("profileImage").getValue();
                    String accountType = "" + dataSnapshot.child("accountType").getValue();

                    tv_name_shop.setText(name);

                    try {
                        Glide.with(ProductDetail.this).load(profileImage).placeholder(R.drawable.profile).into(img_ava_shop);
                    } catch (Exception e) {
                        img_ava_shop.setImageResource(R.drawable.profile);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getWishList() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Wishlist");
        reference.child(firebaseAuth.getUid()).child(name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    fv = Boolean.parseBoolean(snapshot.child("favourite").getValue().toString());
                    Log.d("fv", fv + "");
                    if (fv) {
                        ivToolbarRight.setImageResource(R.drawable.ic_heat_click);
                    } else {
                        ivToolbarRight.setImageResource(R.drawable.icon_love);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProductDetail.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onClickWishList() {
        String timestamp = "" + System.currentTimeMillis();
        Map<String, Object> mapUpdate = new HashMap<>();
        mapUpdate.put("favourite", true);
        mapUpdate.put("uid", firebaseAuth.getUid());
        mapUpdate.put("id", name);
        mapUpdate.put("shop_id", shop_uid);
        mapUpdate.put("image", url);
        mapUpdate.put("name", name_product);
        mapUpdate.put("price", price);
        mapUpdate.put("quantity", quantity);
        mapUpdate.put("creator", creator);
        mapUpdate.put("variant", variant);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Wishlist");
        reference.child(firebaseAuth.getUid()).child(name).setValue(mapUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                CustomToast.makeText(ProductDetail.this, "" + getText(R.string.add_wishlist), CustomToast.LENGTH_LONG, CustomToast.SUCCESS, true).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProductDetail.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void onClickRemoveWishList() {

        String timestamp = "" + System.currentTimeMillis();
        Map<String, Object> mapUpdate = new HashMap<>();
        mapUpdate.put("favourite", false);
        mapUpdate.put("uid", firebaseAuth.getUid());
        mapUpdate.put("id", timestamp);
        mapUpdate.put("id_product", name);
        mapUpdate.put("image", url);
        mapUpdate.put("name", name_product);
        mapUpdate.put("price", price);
        mapUpdate.put("quantity", quantity);
        mapUpdate.put("creator", creator);
        mapUpdate.put("variant", variant);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Wishlist");
        reference.child(firebaseAuth.getUid()).child(name).updateChildren(mapUpdate, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                CustomToast.makeText(ProductDetail.this, "Remove wishlist", CustomToast.LENGTH_LONG, CustomToast.SUCCESS, true).show();
            }
        });

    }

    private void addProduct() {
        ImageView img_Product, count_down, count_add, btn_close;
        TextView tv_dialog_name, tv_dialog_creator, tv_dialog_variant, tv_quantity, tv_price_product, tv_price_product_final, tv_detail;
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
        tv_detail = dialog.findViewById(R.id.tv_detail);
        tv_detail.setVisibility(View.GONE);

        tv_dialog_name.setText(tv_product_name.getText().toString().trim());
        tv_dialog_creator.setText(tv_creator.getText().toString().trim());
        tv_dialog_variant.setText(tv_variant.getText().toString().trim());
        tv_price_product.setText(tv_price.getText().toString().trim().replace("", ""));
        Glide.with(getApplicationContext()).load(url).into(img_Product);
        tv_quantity.setText("" + quantityProduct);
        String price = tv_price_product.getText().toString().trim();
        cost = Integer.parseInt(price.replaceAll("$", ""));
        finalCost = Integer.parseInt(price.replaceAll("$", ""));
        tv_price_product_final.setText("" + finalCost);
        count_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalCost = finalCost + cost;
                quantityProduct++;
                tv_price_product_final.setText("" + finalCost);
                tv_quantity.setText("" + quantityProduct);
            }
        });
        count_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantityProduct > 1) {
                    finalCost = finalCost - cost;
                    quantityProduct--;
                    tv_price_product_final.setText("" + finalCost);
                    tv_quantity.setText("" + quantityProduct);
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
                int quantity_product = Integer.parseInt(quantity);
                int quantity_final = Integer.parseInt(tv_quantity.getText().toString().trim());

                if (quantity_product > 0) {
                    if (quantity_final <= quantity_product) {
                        bottomSheetDialog.dismiss();
                        final Dialog dialog = new Dialog(ProductDetail.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.layout);
                        dialog.getWindow().getAttributes().windowAnimations = androidx.appcompat.R.style.Animation_AppCompat_DropDownUp;

                        Window window = dialog.getWindow();
                        window.setGravity(Gravity.BOTTOM);
                        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
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
                        String totalprice = tv_price_product_final.getText().toString().trim().replace("", "");
                        String quantity = tv_quantity.getText().toString().trim();
                        String idProduct = IDProduct;

                        CartRoom cartRoom = new CartRoom();
                        cartRoom.setProductID(idProduct);
                        cartRoom.setImage(url);
                        cartRoom.setName(name);
                        cartRoom.setCreator(creator);
                        cartRoom.setVariant(variant);
                        cartRoom.setPrice(price_name);
                        cartRoom.setPriceEach(totalprice);
                        cartRoom.setQuantity(quantity);
                        cartRoom.setShop_id(shop_uid);
                        CartDatabase.getInstance(ProductDetail.this).cartDAO().insertCart(cartRoom);


                        btn_Ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                CustomToast.makeText(ProductDetail.this, "" + getText(R.string.product_cart), CustomToast.LENGTH_LONG, CustomToast.SUCCESS, true).show();
                                btn_Add_to_cart.setOnClickListener(null);
                                btn_Add_to_cart.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_buy_fail));
                                btn_Add_to_cart.setTextColor(Color.parseColor("#414040"));
                                btn_Add_to_cart.setText(getString(R.string.added_cart));
                            }
                        });
                        tv_tap.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });
                        dialog.show();
                    } else {
                        Toast.makeText(ProductDetail.this, getString(R.string.product_low), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ProductDetail.this, getString(R.string.sorry), Toast.LENGTH_SHORT).show();
                }
            }
        });

        bottomSheetDialog.show();

    }


    private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finishAffinity();
        }
    }


}