package com.example.shop_app.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shop_app.R;
import com.example.shop_app.adapter.PaymentAdapter;
import com.example.shop_app.database.MyDatabaseHelper;
import com.example.shop_app.model.Cart;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {
    TextView tv_edit_location,tv_username,tv_phone,tv_address,tv_addresS_dialog,tvTitleToolbar,tv_price_total,tv_price_paymentAll;
    ImageView ivToolbarLeft,ivToolbarRight;
    public double allTotalPrice = 0.0;
    RecyclerView rcy_Payment;
    List<Cart> cartList = new ArrayList<>();
    FirebaseAuth firebaseAuth;
    PaymentAdapter paymentAdapter;
    private ProgressDialog progressDialog;
    String a ="";
    Button btn_submitOder;
    private double cost = 0;
    private double finalCost = 0;
    public double totalPrice1;
    private int Qquantity = 1;
    double ship = 2;

    public  String shopId, OrderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        mapping();
        firebaseAuth = FirebaseAuth.getInstance();
        tvTitleToolbar.setText("Shipment");
        ivToolbarRight.setVisibility(View.GONE);
        ivToolbarLeft.setImageResource(R.drawable.ic_left);
        ivToolbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Vui lòng đợi");
        progressDialog.setCanceledOnTouchOutside(false);
        checkUser();
        loadPaymentCart();

        a = getIntent().getStringExtra("total");
//        Toast.makeText(this, ""+a, Toast.LENGTH_SHORT).show();
        tv_price_total.setText(a);
        rcy_Payment.setAdapter(paymentAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( this, RecyclerView.VERTICAL, false);
        rcy_Payment.setLayoutManager (linearLayoutManager);
        rcy_Payment.setHasFixedSize(true);
        tv_edit_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CheckoutActivity.this,EditUser.class));
            }
        });
        tv_addresS_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btn_submitOder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cartList == null){
                    Toast.makeText(CheckoutActivity.this, "Vui lòng thêm sản phẩm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                }else {
                    submitOder();
                }
            }
        });

        double b = Double.parseDouble(tv_price_total.getText().toString().trim());
        tv_price_paymentAll.setText(""+b+ship);
    }




    private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(CheckoutActivity.this, LoginActivity.class));
        }
        else {
            onGetDataUser();
        }
    }

    private void onGetDataUser() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            String name = ""+dataSnapshot.child("name").getValue();
                            String phone = ""+dataSnapshot.child("phone").getValue();
                            String address = ""+ dataSnapshot.child("address").getValue();
                            tv_username.setText(name);
                            tv_address.setText(address);
                            tv_phone.setText(phone);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
    private void loadPaymentCart() {

        MyDatabaseHelper myDatabaseHelper = new MyDatabaseHelper(this);
        Cursor cursor = myDatabaseHelper.readAllData();
        while (cursor.moveToNext()){
            String id = cursor.getString(0);
            String idProduct = cursor.getString(1);
            String image = cursor.getString(2);
            String name = cursor.getString(3);
            String creator = cursor.getString(4);
            String variant = cursor.getString(5);
            String price = cursor.getString(6);
            String priceEach = cursor.getString(7);
            String quantity = cursor.getString(8);
            Cart cart = new Cart(""+id,""+idProduct,""+image,""+name,""+creator,""+variant,""+price,""+priceEach,""+quantity);
            cartList.add(cart);
        }
        paymentAdapter = new PaymentAdapter(this, cartList, new PaymentAdapter.iClickListener() {
            @Override
            public void onClickUpdateItem(Cart cart) {

            }

            @Override
            public void onClickDeleteItem(Cart cart) {
                String priceEach = cart.getPriceEach();
                String price = cart.getPrice();
                cost = Double.parseDouble(priceEach);

                Qquantity = 1;
//                MyDatabaseHelper myDB = new MyDatabaseHelper(CheckoutActivity.this);
//                String productID = cart.getProductID();
//                myDB.deleteData(productID);
//                paymentAdapter.notifyDataSetChanged();
                double tx = Double.parseDouble((tv_price_total.getText().toString().trim().replace("$","")));
                totalPrice1 =  tx - Double.parseDouble(priceEach.replace("$",""));
                tv_price_total.setText("$"+totalPrice1);
                if (cartList==null){
                    finish();
                }
                // tổng tiền đang không trừ khi xóa mặt hàng


            }
        });

    }
    String nameProduct;
    private void submitOder() {
        for (int i = 0; i<cartList.size(); i++){
            nameProduct = cartList.get(i).getName();
        }
        String timestamp = ""+ System.currentTimeMillis();
        String cost = tv_price_paymentAll.getText().toString().trim();
        String address = tv_address.getText().toString().trim();
        String name = tv_username.getText().toString().trim();
        String phone = tv_phone.getText().toString().trim();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("orderId", timestamp);
        hashMap.put("orderTime", timestamp);
        hashMap.put("orderStatus", "Đang xử lý");
        hashMap.put("orderName", name);
        hashMap.put("orderPhone", phone);
        hashMap.put("orderAddress", ""+address);
        hashMap.put("orderCost", ""+cost);
        hashMap.put("orderBy", ""+firebaseAuth.getUid());
        hashMap.put("orderNameProduct", nameProduct);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User").child("Orders");
        reference.child(timestamp).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        for (int i = 0; i<cartList.size(); i++ ){
                            String id = cartList.get(i).getCart_ID();
                            String name = cartList.get(i).getName();
                            String imgage = cartList.get(i).getImage();
                            String productID = cartList.get(i).getProductID();
                            String price = cartList.get(i).getPrice();
                            String priceEach = cartList.get(i).getPriceEach();
                            String quantity = cartList.get(i).getQuantity();
                            String variant = cartList.get(i).getVariant();
                            HashMap<String, String> hashMap1 = new HashMap<>();
                            hashMap1.put("productID", productID);
                            hashMap1.put("name", name);
                            hashMap1.put("price", price);
                            hashMap1.put("priceEach", priceEach);
                            hashMap1.put("quantity", quantity);
                            hashMap1.put("image", imgage);
                            hashMap1.put("variant", variant);
                            reference.child(timestamp).child("Items").child(productID).setValue(hashMap1);
                        }
                        progressDialog.dismiss();
                        Toast.makeText(CheckoutActivity.this, "Tạo đơn hàng thành công ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CheckoutActivity.this, OrderDetailUser.class);
                        intent.putExtra("orderId", timestamp);
                        intent.putExtra("orderTo", shopId);
                        startActivity(intent);
                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(CheckoutActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void mapping() {
        rcy_Payment = findViewById(R.id.rcy_Payment);
        tv_price_total = findViewById(R.id.tv_price_total);
        tvTitleToolbar = findViewById(R.id.tvTitleToolbar);
        ivToolbarLeft = findViewById(R.id.ivToolbarLeft);
        ivToolbarRight = findViewById(R.id.ivToolbarRight);
        tv_edit_location = findViewById(R.id.tv_edit_location);
        tv_username = findViewById(R.id.tv_username);
        tv_phone = findViewById(R.id.tv_phone);
        tv_address = findViewById(R.id.tv_address);
        tv_addresS_dialog = findViewById(R.id.tv_addresS_dialog);
        btn_submitOder = findViewById(R.id.btn_submitOder);
        tv_price_paymentAll = findViewById(R.id.tv_price_paymentAll);
    }
}