package com.example.shop_app.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
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

    private int Qquantity = 1;
    double ship = 2;

    public double b = 0.0;

    public  String shopId, OrderId;

    public double totalpriceAll = 0.0;

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

        totalpriceAll = totalpriceAll + Double.parseDouble(a);

        tv_price_total.setText(String.format("%.0f",totalpriceAll));
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
        double total_ship = Double.parseDouble(tv_price_total.getText().toString().trim());
        if (total_ship > 0){
            double totalAll = Double.parseDouble(tv_price_total.getText().toString().trim());

            tv_price_paymentAll.setText(""+String.format("%.0f",totalAll+ship));
        }else {
            double totalAll = Double.parseDouble(tv_price_total.getText().toString().trim());
            tv_price_paymentAll.setText(""+String.format("%.0f",totalAll));
        }

        btn_submitOder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    submitOder();

            }
        });

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
            public double totalPrice1;
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
                double tx = Double.parseDouble(tv_price_total.getText().toString().trim());
                totalPrice1 =  tx - Double.parseDouble(priceEach);

                double pricefinal = Double.parseDouble(String.valueOf(totalPrice1));

                tv_price_total.setText(String.format("%.0f",pricefinal));

                 b = Double.parseDouble(String.valueOf(pricefinal));
                if (pricefinal == 0.0){
                    tv_price_paymentAll.setText(""+String.format("%.0f",b));
                }else {
                    tv_price_paymentAll.setText(""+String.format("%.0f",b+ship));

                }
                if (cartList==null){
                    finish();
                }

            }
        });
        paymentAdapter.notifyDataSetChanged();

    }
    String nameProduct;
    String productID;
    private void submitOder() {

                double total  = Double.parseDouble(tv_price_paymentAll.getText().toString().trim());
                if (total == 0){
                    Toast.makeText(this, "Vui lòng thêm sản phẩm vào để thanh toán", Toast.LENGTH_SHORT).show();
                }else {
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

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child("Orders");
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
                                    for (int i = 0; i<cartList.size(); i++){
                                        productID   = cartList.get(i).getProductID();
                                    }
//                                    MyDatabaseHelper myDB = new MyDatabaseHelper(CheckoutActivity.this);
//                                    myDB.deleteAllData();
                                    // thanh toán thành công nhưng chưa xóa đc cart list

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