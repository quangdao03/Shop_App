package com.example.shop_app.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shop_app.R;
import com.example.shop_app.adapter.PaymentAdapter;
import com.example.shop_app.database.CartDatabase;
import com.example.shop_app.database.CartRoom;
import com.example.shop_app.zalo.CreateOrder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import vn.momo.momo_partner.AppMoMoLib;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class CheckoutActivity extends AppCompatActivity {
    TextView tv_edit_location, tv_username, tv_phone, tv_address, tv_addresS_dialog, tvTitleToolbar, tv_price_total, tv_price_paymentAll;
    ImageView ivToolbarLeft, ivToolbarRight;
    public double allTotalPrice = 0.0;
    RecyclerView rcy_Payment;
    List<CartRoom> cartList = new ArrayList<>();
    FirebaseAuth firebaseAuth;
    PaymentAdapter paymentAdapter;
    private ProgressDialog progressDialog;
    String a = "";
    Button btn_submitOder, btn_submitOderMomo,btn_zalopay;
    private double cost = 0;
    private double finalCost = 0;

    private int Qquantity = 1;
    double ship = 2;

    public double b = 0.0;

    public String shopId, OrderId;

    public double totalpriceAll = 0.0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT);
        //zalopay
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);

        mapping();
        getSupportActionBar().hide();
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
        progressDialog.setTitle(getText(R.string.please_wait));
        progressDialog.setCanceledOnTouchOutside(false);
        checkUser();
        loadPaymentCart();


        a = getIntent().getStringExtra("total");

        totalpriceAll = totalpriceAll + Double.parseDouble(a);

        tv_price_total.setText(String.format("%.0f", totalpriceAll));
        rcy_Payment.setAdapter(paymentAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcy_Payment.setLayoutManager(linearLayoutManager);
        rcy_Payment.setHasFixedSize(true);
        tv_edit_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CheckoutActivity.this, EditUser.class));
            }
        });
        tv_addresS_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        double total_ship = Double.parseDouble(tv_price_total.getText().toString().trim());
        if (total_ship > 0) {
            double totalAll = Double.parseDouble(tv_price_total.getText().toString().trim());

            tv_price_paymentAll.setText("" + String.format("%.0f", totalAll + ship));
        } else {
            double totalAll = Double.parseDouble(tv_price_total.getText().toString().trim());
            tv_price_paymentAll.setText("" + String.format("%.0f", totalAll));
        }

        btn_submitOder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitOder();

            }
        });

//        btn_zalopay.setOnClickListener(view -> {
//            double total = Double.parseDouble(tv_price_paymentAll.getText().toString().trim());
//            if (total == 0) {
//                Toast.makeText(this, "" + getText(R.string.please_wait_cart), Toast.LENGTH_SHORT).show();
//            } else {
//                for (int i = 0; i < cartList.size(); i++) {
//                    nameProduct = cartList.get(i).getName();
//                    shop_uid = cartList.get(i).getShop_id();
//                }
//                String timestamp = "" + System.currentTimeMillis();
//                String cost = tv_price_paymentAll.getText().toString().trim();
//                String address = tv_address.getText().toString().trim();
//                String name = tv_username.getText().toString().trim();
//                String phone = tv_phone.getText().toString().trim();
//                HashMap<String, String> hashMap = new HashMap<>();
//                hashMap.put("orderId", timestamp);
//                hashMap.put("orderTime", timestamp);
//                hashMap.put("orderStatus", "Đang xử lý");
//                hashMap.put("orderName", name);
//                hashMap.put("orderPhone", phone);
//                hashMap.put("orderAddress", "" + address);
//                hashMap.put("orderCost", "" + cost);
//                hashMap.put("orderBy", "" + firebaseAuth.getUid());
//                hashMap.put("orderNameProduct", nameProduct);
//                hashMap.put("shop_uid",shop_uid);
//
//                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child("Orders");
//                reference.child(timestamp).setValue(hashMap)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//                                for (int i = 0; i < cartList.size(); i++) {
//                                    id = cartList.get(i).getCart_ID();
//                                    String name = cartList.get(i).getName();
//                                    String imgage = cartList.get(i).getImage();
//                                    String productID = cartList.get(i).getProductID();
//                                    String price = cartList.get(i).getPrice();
//                                    String priceEach = cartList.get(i).getPriceEach();
//                                    String quantity = cartList.get(i).getQuantity();
//                                    String variant = cartList.get(i).getVariant();
//                                    String shop_uid = cartList.get(i).getShop_id();
//                                    HashMap<String, String> hashMap1 = new HashMap<>();
//                                    hashMap1.put("productID", productID);
//                                    hashMap1.put("name", name);
//                                    hashMap1.put("price", price);
//                                    hashMap1.put("priceEach", priceEach);
//                                    hashMap1.put("quantity", quantity);
//                                    hashMap1.put("image", imgage);
//                                    hashMap1.put("variant", variant);
//                                    hashMap1.put("shop_uid",shop_uid);
//                                    reference.child(timestamp).child("Items").child(productID).setValue(hashMap1);
//                                }
//
//                                requestZalo(timestamp,shop_uid);
//                                for (int i = 0; i < cartList.size(); i++) {
//                                    productID = cartList.get(i).getProductID();
//                                }
//                                for (int i = 0; i < cartList.size(); i++) {
//                                    CartRoom cart = cartList.get(i);
//                                    Log.d("cart", cart.toString());
//                                    CartDatabase.getInstance(CheckoutActivity.this).cartDAO().deleteCart(cart);
//                                }
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                progressDialog.dismiss();
//                                Toast.makeText(CheckoutActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
//
//
//            }
//
//        });
        btn_zalopay.setOnClickListener(view -> {
            CreateOrder orderApi = new CreateOrder();
            try {
                JSONObject data = orderApi.createOrder("10000");
                String code = data.getString("return_code");
                Log.d("test_daoo",code + orderApi.toString());
                if (code.equals("1")) {
                    String token = data.getString("zp_trans_token");
                    Log.d("test_daoo",token);
                    ZaloPaySDK.getInstance().payOrder(CheckoutActivity.this, token, "demozpdk://app", new PayOrderListener() {
                        @Override
                        public void onPaymentSucceeded(String s, String s1, String s2) {
                            Toast.makeText(CheckoutActivity.this, "" + getText(R.string.success_order), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onPaymentCanceled(String s, String s1) {

                        }

                        @Override
                        public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {

                        }
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void requestZalo(String timestamp,String shop_uid){
        CreateOrder orderApi = new CreateOrder();
        try {
            JSONObject data = orderApi.createOrder("100000");
            String code = data.getString("return_code");
            if (code.equals("1")) {
                String token = data.getString("zp_trans_token");
                ZaloPaySDK.getInstance().payOrder(CheckoutActivity.this, token, "demozpdk://app", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(String s, String s1, String s2) {
                        Toast.makeText(CheckoutActivity.this, "" + getText(R.string.success_order), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CheckoutActivity.this, OrderDetailUser.class);
                        intent.putExtra("orderId", timestamp);
                        intent.putExtra("orderTo", shop_uid);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onPaymentCanceled(String s, String s1) {

                    }

                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {

                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Get token through MoMo app
//    private void requestPayment() {
//        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
//        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);
////        if (edAmount.getText().toString() != null && edAmount.getText().toString().trim().length() != 0)
////            amount = edAmount.getText().toString().trim();
//
//        Map<String, Object> eventValue = new HashMap<>();
//        //client Required
//        eventValue.put("merchantname", merchantName); //Tên đối tác. được đăng ký tại https://business.momo.vn. VD: Google, Apple, Tiki , CGV Cinemas
//        eventValue.put("merchantcode", merchantCode); //Mã đối tác, được cung cấp bởi MoMo tại https://business.momo.vn
//        eventValue.put("amount", amount); //Kiểu integer
//        eventValue.put("orderId", "orderId123456789"); //uniqueue id cho Bill order, giá trị duy nhất cho mỗi đơn hàng
//        eventValue.put("orderLabel", "Mã đơn hàng"); //gán nhãn
//
//        //client Optional - bill info
//        eventValue.put("merchantnamelabel", "Dịch vụ");//gán nhãn
//        eventValue.put("fee", fee); //Kiểu integer
//        eventValue.put("description", description); //mô tả đơn hàng - short description
//
//        //client extra data
//        eventValue.put("requestId",  merchantCode+"merchant_billId_"+System.currentTimeMillis());
//        eventValue.put("partnerCode", merchantCode);
//        //Example extra data
//        JSONObject objExtraData = new JSONObject();
//        try {
//            objExtraData.put("site_code", "008");
//            objExtraData.put("site_name", "CGV Cresent Mall");
//            objExtraData.put("screen_code", 0);
//            objExtraData.put("screen_name", "Special");
//            objExtraData.put("movie_name", "Kẻ Trộm Mặt Trăng 3");
//            objExtraData.put("movie_format", "2D");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        eventValue.put("extraData", objExtraData.toString());
//
//        eventValue.put("extra", "");
//        AppMoMoLib.getInstance().requestMoMoCallBack(this, eventValue);
//
//
//    }
//    //Get token callback from MoMo app an submit to server side
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
//            if(data != null) {
//                if(data.getIntExtra("status", -1) == 0) {
//                    //TOKEN IS AVAILABLE
//                    tvMessage.setText("message: " + "Get token " + data.getStringExtra("message"));
//                    String token = data.getStringExtra("data"); //Token response
//                    String phoneNumber = data.getStringExtra("phonenumber");
//                    String env = data.getStringExtra("env");
//                    if(env == null){
//                        env = "app";
//                    }
//
//                    if(token != null && !token.equals("")) {
//                        // TODO: send phoneNumber & token to your server side to process payment with MoMo server
//                        // IF Momo topup success, continue to process your order
//                    } else {
//                        tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
//                    }
//                } else if(data.getIntExtra("status", -1) == 1) {
//                    //TOKEN FAIL
//                    String message = data.getStringExtra("message") != null?data.getStringExtra("message"):"Thất bại";
//                    tvMessage.setText("message: " + message);
//                } else if(data.getIntExtra("status", -1) == 2) {
//                    //TOKEN FAIL
//                    tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
//                } else {
//                    //TOKEN FAIL
//                    tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
//                }
//            } else {
//                tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
//            }
//        } else {
//            tvMessage.setText("message: " + this.getString(R.string.not_receive_info_err));
//        }
//    }


    private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(CheckoutActivity.this, LoginActivity.class));
        } else {
            onGetDataUser();
        }
    }

    private void onGetDataUser() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String name = "" + dataSnapshot.child("name").getValue();
                            String phone = "" + dataSnapshot.child("phone").getValue();
                            String address = "" + dataSnapshot.child("address").getValue();
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
        cartList = CartDatabase.getInstance(CheckoutActivity.this).cartDAO().getAllCart();
        paymentAdapter = new PaymentAdapter(this, cartList, new PaymentAdapter.iClickListener() {
            @Override
            public void onClickUpdateItem(CartRoom cart) {

            }

            public double totalPrice1;

            @Override
            public void onClickDeleteItem(CartRoom cart) {
                String priceEach = cart.getPriceEach();
                String price = cart.getPrice();
                cost = Double.parseDouble(priceEach);

                Qquantity = 1;
                double tx = Double.parseDouble(tv_price_total.getText().toString().trim());
                totalPrice1 = tx - Double.parseDouble(priceEach);

                double pricefinal = Double.parseDouble(String.valueOf(totalPrice1));

                tv_price_total.setText(String.format("%.0f", pricefinal));

                b = Double.parseDouble(String.valueOf(pricefinal));
                if (pricefinal == 0.0) {
                    tv_price_paymentAll.setText("" + String.format("%.0f", b));
                } else {
                    tv_price_paymentAll.setText("" + String.format("%.0f", b + ship));

                }
                if (cartList == null) {
                    finish();
                }

            }
        });
        paymentAdapter.notifyDataSetChanged();

    }

    String nameProduct;
    String productID;
    int id;
    String shop_uid;

    private void submitOder() {

        double total = Double.parseDouble(tv_price_paymentAll.getText().toString().trim());
        if (total == 0) {
            Toast.makeText(this, "" + getText(R.string.please_wait_cart), Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < cartList.size(); i++) {
                nameProduct = cartList.get(i).getName();
                shop_uid = cartList.get(i).getShop_id();
            }
            String timestamp = "" + System.currentTimeMillis();
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
            hashMap.put("orderAddress", "" + address);
            hashMap.put("orderCost", "" + cost);
            hashMap.put("orderBy", "" + firebaseAuth.getUid());
            hashMap.put("orderNameProduct", nameProduct);
            hashMap.put("shop_uid",shop_uid);

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child("Orders");
            reference.child(timestamp).setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            for (int i = 0; i < cartList.size(); i++) {
                                id = cartList.get(i).getCart_ID();
                                String name = cartList.get(i).getName();
                                String imgage = cartList.get(i).getImage();
                                String productID = cartList.get(i).getProductID();
                                String price = cartList.get(i).getPrice();
                                String priceEach = cartList.get(i).getPriceEach();
                                String quantity = cartList.get(i).getQuantity();
                                String variant = cartList.get(i).getVariant();
                                String shop_uid = cartList.get(i).getShop_id();
                                HashMap<String, String> hashMap1 = new HashMap<>();
                                hashMap1.put("productID", productID);
                                hashMap1.put("name", name);
                                hashMap1.put("price", price);
                                hashMap1.put("priceEach", priceEach);
                                hashMap1.put("quantity", quantity);
                                hashMap1.put("image", imgage);
                                hashMap1.put("variant", variant);
                                hashMap1.put("shop_uid",shop_uid);
                                reference.child(timestamp).child("Items").child(productID).setValue(hashMap1);
                            }
                            progressDialog.dismiss();
                            Toast.makeText(CheckoutActivity.this, "" + getText(R.string.success_order), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CheckoutActivity.this, OrderDetailUser.class);
                            intent.putExtra("orderId", timestamp);
                            intent.putExtra("orderTo", shop_uid);
                            startActivity(intent);
                            for (int i = 0; i < cartList.size(); i++) {
                                productID = cartList.get(i).getProductID();
                            }
                            for (int i = 0; i < cartList.size(); i++) {
                                CartRoom cart = cartList.get(i);
                                Log.d("cart", cart.toString());
                                CartDatabase.getInstance(CheckoutActivity.this).cartDAO().deleteCart(cart);
                            }
                            finish();


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(CheckoutActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
        btn_submitOderMomo = findViewById(R.id.btn_submitOderMomo);
        btn_zalopay = findViewById(R.id.btn_zalopay);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
}