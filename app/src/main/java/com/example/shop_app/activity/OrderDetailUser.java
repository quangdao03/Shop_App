package com.example.shop_app.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.shop_app.R;
import com.example.shop_app.adapter.OrderItemAdapter;
import com.example.shop_app.model.OrderItem;
import com.example.shop_app.utils.SystemUtil;
import com.example.shop_app.utils.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OrderDetailUser extends AppCompatActivity {
    TextView tv_OrderTime,tv_price_total,tv_price_paymentAll,tv_OrderID,tv_OrderName,tv_OrderAddress,tv_OrderStatus,
    tvTitleToolbar,tv_OrderPhone;
    ImageView ivToolbarLeft,ivToolbarRight;
    private String orderId, orderTo;
    RecyclerView rcy_Order;
    FirebaseAuth firebaseAuth;
    List<OrderItem> orderItemList = new ArrayList<>();
    OrderItemAdapter orderItemAdapter;
    LinearLayout ll_order;
    TextView tv_huy;
    String token_user;
    String shop_uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SystemUtil.setLocale(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_user);
        mapping();
        getSupportActionBar().hide();
        ivToolbarLeft.setImageResource(R.drawable.ic_left);
        ivToolbarRight.setVisibility(View.GONE);
        tvTitleToolbar.setText(getText(R.string.payment_details));
        firebaseAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        orderId = intent.getStringExtra("orderId");

        loadOrderItem();
        loadOrderDetails();
        ivToolbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    public double allTotalPrice = 0.0;
    public double ship = 2.0;
    private void loadOrderItem() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( this, RecyclerView.VERTICAL, false);
        rcy_Order.setLayoutManager (linearLayoutManager);
        rcy_Order.setHasFixedSize(true);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child("Orders").child(orderId).child("Items")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        orderItemList.clear();
                        for (DataSnapshot ds: snapshot.getChildren()){
                            String price = ""+ ds.child("priceEach").getValue();
                            OrderItem orderItem = ds.getValue(OrderItem.class);
                            orderItemList.add(orderItem);
                            allTotalPrice = allTotalPrice + Double.parseDouble(price);
                            tv_price_total.setText(String.valueOf(allTotalPrice));
                        }

                        orderItemAdapter = new OrderItemAdapter(OrderDetailUser.this, orderItemList);
                        rcy_Order.setAdapter(orderItemAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    private void editStatus() {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("orderStatus","Đã hủy");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child("Orders").child(orderId)
                .updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        String message = "Đơn hàng: Đã hủy";
                        Toast.makeText(OrderDetailUser.this, message, Toast.LENGTH_SHORT).show();
                        prepareNotificationMessage(orderId, message);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(OrderDetailUser.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void loadOrderDetails() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child("Orders").child(orderId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String orderBy = ""+snapshot.child("orderBy").getValue();
                        String orderCost = ""+snapshot.child("orderCost").getValue();
                        String orderId = ""+snapshot.child("orderId").getValue();
                        String status = ""+snapshot.child("orderStatus").getValue();
                        String orderTime = ""+snapshot.child("orderTime").getValue();
                        String orderAddress = ""+snapshot.child("orderAddress").getValue();
                        String orderName = ""+snapshot.child("orderName").getValue();
                        String orderPhone = ""+snapshot.child("orderPhone").getValue();
                        shop_uid  = ""+snapshot.child("shop_uid").getValue();
                        if (shop_uid != null){
                            checkToken();
                        }

                        Long tine = Long.parseLong(orderTime);
                        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
                        String date = formatDate.format(tine);
                        tv_OrderTime.setText(date);

                        if (status.equals("Đang xử lý")){
                            tv_huy.setVisibility(View.VISIBLE);
                            tv_huy.setOnClickListener(view -> {
                                editStatus();

                            });
                            ll_order.setBackgroundResource(R.drawable.bg_order_detail_success);
                            tv_OrderStatus.setTextColor(getResources().getColor(R.color.primary));
                            tv_OrderStatus.setText(R.string.process);
                        }else if (status.equals("Đã xác nhận")){
                            tv_huy.setVisibility(View.GONE);
                            ll_order.setBackgroundResource(R.drawable.bg_order_detail_success_green);
                            tv_OrderStatus.setTextColor(getResources().getColor(R.color.colorGreen));
                            tv_OrderStatus.setText(R.string.confirmed);
                        }else if (status.equals("Đã hủy")){
                            tv_huy.setVisibility(View.GONE);
                            ll_order.setBackgroundResource(R.drawable.bg_order_detail_fail);
                            tv_OrderStatus.setTextColor(getResources().getColor(R.color.red));
                            tv_OrderStatus.setText(R.string.cancel);
                        }
                        tv_OrderID.setText(orderId);


                        tv_price_paymentAll.setText(orderCost);
                        tv_OrderAddress.setText(orderAddress);
                        tv_OrderName.setText(orderName);
                        tv_OrderPhone.setText(orderPhone);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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

    private void prepareNotificationMessage(String orderId, String message){
        String NOTIFICATION_TOPIC = token_user;
        String NOTIFICATION_TITLE = "Đơn hàng của bạn " + orderId;
        String NOTIFICATION_MESSAGE = "" + message;
        String NOTIFICATION_TYPE = "OrderStatusChanged";

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

        sendFcmNotification(notificationJson);
    }

    private void sendFcmNotification(JSONObject notificationJson) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("https://fcm.googleapis.com/fcm/send", notificationJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
    private void mapping() {
        tv_OrderTime = findViewById(R.id.tv_OrderTime);
        tv_price_total = findViewById(R.id.tv_price_total);
        tv_price_paymentAll = findViewById(R.id.tv_price_paymentAll);
        tv_OrderID = findViewById(R.id.tv_OrderID);
        tv_OrderName = findViewById(R.id.tv_OrderName);
        tv_OrderAddress = findViewById(R.id.tv_OrderAddress);
        tv_OrderStatus = findViewById(R.id.tv_OrderStatus);
        rcy_Order = findViewById(R.id.rcy_Order);
        tvTitleToolbar = findViewById(R.id.tvTitleToolbar);
        ivToolbarLeft = findViewById(R.id.ivToolbarLeft);
        ivToolbarRight = findViewById(R.id.ivToolbarRight);
        tv_OrderPhone = findViewById(R.id.tv_OrderPhone);
        ll_order = findViewById(R.id.ll_order);
        tv_huy = findViewById(R.id.tv_huy);
    }
}