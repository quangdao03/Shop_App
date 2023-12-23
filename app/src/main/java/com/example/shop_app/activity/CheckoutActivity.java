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

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.shop_app.R;
import com.example.shop_app.adapter.PaymentAdapter;
import com.example.shop_app.database.CartDatabase;
import com.example.shop_app.database.CartRoom;
import com.example.shop_app.utils.SystemUtil;
import com.example.shop_app.utils.Utils;
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
import java.util.Map;

import vn.momo.momo_partner.AppMoMoLib;
//import vn.zalopay.sdk.Environment;
//import vn.zalopay.sdk.ZaloPayError;
//import vn.zalopay.sdk.ZaloPaySDK;
//import vn.zalopay.sdk.listeners.PayOrderListener;

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
        SystemUtil.setLocale(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT);
        //zalopay
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
//        ZaloPaySDK.init(2553, Environment.SANDBOX);

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
//        btn_zalopay.setOnClickListener(view -> {
//            CreateOrder orderApi = new CreateOrder();
//            try {
//                JSONObject data = orderApi.createOrder("10000");
//                if (data.has("return_code")){
//                    String code = data.getString("return_code");
//                    Log.d("test_daoo",code + orderApi);
//                    if (code.equals("1")) {
//                        String token = data.getString("zp_trans_token");
//                        Log.d("test_daoo",token);
//                        ZaloPaySDK.getInstance().payOrder(CheckoutActivity.this, token, "demozpdk://app", new PayOrderListener() {
//                            @Override
//                            public void onPaymentSucceeded(String s, String s1, String s2) {
//                                Toast.makeText(CheckoutActivity.this, "" + getText(R.string.success_order), Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onPaymentCanceled(String s, String s1) {
//
//                            }
//
//                            @Override
//                            public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
//
//                            }
//                        });
//                    }
//                }
//
//
//
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
    }

    private void requestZalo(String timestamp,String shop_uid){
        CreateOrder orderApi = new CreateOrder();
        try {
            JSONObject data = orderApi.createOrder("100000");
            String code = data.getString("return_code");
            if (code.equals("1")) {
                String token = data.getString("zp_trans_token");
//                ZaloPaySDK.getInstance().payOrder(CheckoutActivity.this, token, "demozpdk://app", new PayOrderListener() {
//                    @Override
//                    public void onPaymentSucceeded(String s, String s1, String s2) {
//                        Toast.makeText(CheckoutActivity.this, "" + getText(R.string.success_order), Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(CheckoutActivity.this, OrderDetailUser.class);
//                        intent.putExtra("orderId", timestamp);
//                        intent.putExtra("orderTo", shop_uid);
//                        startActivity(intent);
//                        finish();
//                    }
//
//                    @Override
//                    public void onPaymentCanceled(String s, String s1) {
//
//                    }
//
//                    @Override
//                    public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
//
//                    }
//                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(CheckoutActivity.this, LoginActivity.class));
            finishAffinity();
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
                CartDatabase.getInstance(CheckoutActivity.this).cartDAO().deleteCart(cart);
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
            hashMap.put("orderPayments","Tiền mặt");
            checkToken();
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
                            prepareNotificationMessage(timestamp,shop_uid);
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
//        ZaloPaySDK.getInstance().onResult(intent);
    }
    String token_user;
    private void prepareNotificationMessage(String orderId, String shop_uid){
        String NOTIFICATION_TOPIC = token_user;
        String NOTIFICATION_TITLE = "Đơn hàng mới " + orderId;
        String NOTIFICATION_MESSAGE = "Chúc mừng..! Bạn có đơn hàng mới. ";
        String NOTIFICATION_TYPE = "NewOrder";

        JSONObject notificationJson = new JSONObject();
        JSONObject notificationBodyJson = new JSONObject();
        try {
            notificationBodyJson.put("notificationType" , NOTIFICATION_TYPE);
            notificationBodyJson.put("buyerUid" , firebaseAuth.getUid());
            notificationBodyJson.put("sellerUid" , shop_uid);
            notificationBodyJson.put("orderId" , orderId);
            notificationBodyJson.put("notificationTitle" , NOTIFICATION_TITLE);
            notificationBodyJson.put("notificationMessage" , NOTIFICATION_MESSAGE);

            notificationJson.put("to", NOTIFICATION_TOPIC);
            notificationJson.put("data", notificationBodyJson);

        }catch (Exception e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        sendFcmNotification(notificationJson, orderId, shop_uid);
    }

    private void sendFcmNotification(JSONObject notificationJson, String orderId, String shop_uid) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("https://fcm.googleapis.com/fcm/send", notificationJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("FCM", "Thành công: " + response.toString());
                Intent intent = new Intent(CheckoutActivity.this, OrderDetailUser.class);
                intent.putExtra("orderId", orderId);
                intent.putExtra("orderTo", shop_uid);
                startActivity(intent);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("FCM", "Lỗi: " + error.toString());
                Intent intent = new Intent(  CheckoutActivity.this, OrderDetailUser.class);
                intent.putExtra("orderId", orderId);
                intent.putExtra("orderTo", shop_uid);
                startActivity(intent);

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "key=" + Utils.FCM_KEY);
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
    private void checkToken(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.orderByChild("uid").equalTo(shop_uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    token_user = (String) dataSnapshot.child("token").getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}