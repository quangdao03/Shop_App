package com.example.shop_app.dash.sellerdash;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class OrderFragmentSeller extends Fragment {
    TextView tvTitleToolbar,tv_sort;
    ImageView ivToolbarLeft,ivToolbarRight;
    RecyclerView rcy_List_Order;

    OrderUserAdapter orderUserAdapter;

    List<Order> orderList = new ArrayList<>();
    FirebaseAuth firebaseAuth;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_seller, container,false);
        mapping();
        firebaseAuth = FirebaseAuth.getInstance();
        tvTitleToolbar.setText("Order");
        ivToolbarLeft.setVisibility(View.GONE);
        ivToolbarRight.setImageResource(R.drawable.icons8_add);
        loadAllOrder();
        tv_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String options[] = {"All", "Đang xử lý", "Đã xác nhận", "Đã hủy"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Sắp xếp")
                        .setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0){
                                    tv_sort.setText("All");
                                    orderUserAdapter.getFilter().filter("");
                                }else  {
                                    String optionClicked = options[i];
                                    tv_sort.setText(optionClicked);
                                    orderUserAdapter.getFilter().filter(optionClicked);
                                }
                            }
                        }).show();
            }
        });
        return  view;
    }
    private void loadAllOrder() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getContext(), RecyclerView.VERTICAL, false);
        rcy_List_Order.setLayoutManager (linearLayoutManager);
        rcy_List_Order.setHasFixedSize(true);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child("Orders").orderByChild("shop_uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (orderList!= null){
                            orderList.clear();
                        }
                        for (DataSnapshot ds: snapshot.getChildren()){
                            Order orderSeller = ds.getValue(Order.class);
                            orderList.add(0,orderSeller);
                        }
                        orderUserAdapter = new OrderUserAdapter(getContext(), orderList);
                        rcy_List_Order.setAdapter(orderUserAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
    private void mapping() {
        rcy_List_Order = view.findViewById(R.id.rcy_List_Order);
        tvTitleToolbar = view.findViewById(R.id.tvTitleToolbar);
        ivToolbarLeft = view.findViewById(R.id.ivToolbarLeft);
        ivToolbarRight = view.findViewById(R.id.ivToolbarRight);
        tv_sort = view.findViewById(R.id.tv_sort);
    }
}
