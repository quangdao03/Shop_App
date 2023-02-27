package com.example.shop_app.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shop_app.R;
import com.example.shop_app.adapter.OrderUserAdapter;
import com.example.shop_app.model.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListDetailOrder extends AppCompatActivity {
    TextView tvTitleToolbar,tv_sort, tv_sort_text;
    ImageView ivToolbarLeft,ivToolbarRight;
    RecyclerView rcy_List_Order;

    OrderUserAdapter orderUserAdapter;
    List<Order> orderList = new ArrayList<>();

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_detail_order);
        mapping();
        getSupportActionBar().hide();
        firebaseAuth = FirebaseAuth.getInstance();
        loadOrder();
        ivToolbarRight.setVisibility(View.GONE);
        ivToolbarLeft.setImageResource(R.drawable.ic_left);
        ivToolbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        tvTitleToolbar.setText(getText(R.string.list_order));
        tv_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String options[] = {"All", "Đang xử lý", "Đã xác nhận", "Đã hủy"};

                AlertDialog.Builder builder = new AlertDialog.Builder(ListDetailOrder.this);
                builder.setTitle("Sắp xếp")
                        .setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0){
                                    tv_sort_text.setText("All");
                                    orderUserAdapter.getFilter().filter("");
                                }else  {
                                    String optionClicked = options[i];
                                    tv_sort_text.setText(optionClicked);
                                    orderUserAdapter.getFilter().filter(optionClicked);
                                }

                            }
                        }).show();
            }
        });
    }

    private void loadOrder() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( this, RecyclerView.VERTICAL, false);
        rcy_List_Order.setLayoutManager (linearLayoutManager);
        rcy_List_Order.setHasFixedSize(true);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child("Orders");
            reference.orderByChild("orderBy").equalTo(firebaseAuth.getUid())
                    .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (orderList!= null){
                                        orderList.clear();
                                    }
                                    for (DataSnapshot ds: snapshot.getChildren()){
                                        Order order = ds.getValue(Order.class);
                                        orderList.add(order);
                                    }
                                    orderUserAdapter = new OrderUserAdapter(ListDetailOrder.this,orderList);
                                    rcy_List_Order.setAdapter(orderUserAdapter);
                                    orderUserAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

    }






    private void mapping() {

        tvTitleToolbar = findViewById(R.id.tvTitleToolbar);
        ivToolbarLeft = findViewById(R.id.ivToolbarLeft);
        ivToolbarRight = findViewById(R.id.ivToolbarRight);
        rcy_List_Order = findViewById(R.id.rcy_List_Order);
        tv_sort_text = findViewById(R.id.tv_sort_text);
        tv_sort = findViewById(R.id.tv_sort);

    }
}