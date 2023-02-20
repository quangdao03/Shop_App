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

import com.example.shop_app.R;
import com.example.shop_app.adapter.OrderItemAdapter;
import com.example.shop_app.model.OrderItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_user);
        mapping();
        ivToolbarLeft.setImageResource(R.drawable.ic_left);
        ivToolbarRight.setVisibility(View.GONE);
        tvTitleToolbar.setText("Payment Details");
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
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
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

    private void loadOrderDetails() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
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



                        Long tine = Long.parseLong(orderTime);
                        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
                        String date = formatDate.format(tine);
                        tv_OrderTime.setText(date);

                        if (status.equals("Đang xử lý")){
                            ll_order.setBackgroundResource(R.drawable.bg_order_detail_success);
                            tv_OrderStatus.setTextColor(getResources().getColor(R.color.primary));
                        }else if (status.equals("Đã xác nhận")){
                            ll_order.setBackgroundResource(R.drawable.bg_order_detail_success_green);
                            tv_OrderStatus.setTextColor(getResources().getColor(R.color.colorGreen));
                        }else if (status.equals("Đã hủy")){
                            ll_order.setBackgroundResource(R.drawable.bg_order_detail_fail);
                            tv_OrderStatus.setTextColor(getResources().getColor(R.color.red));
                        }
                        tv_OrderID.setText(orderId);
                        tv_OrderStatus.setText(status);

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
    }
}