package com.example.shop_app.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shop_app.R;
import com.example.shop_app.adapter.OrderSellerAdapter;
import com.example.shop_app.databinding.LayoutStatisticalSellerBinding;
import com.example.shop_app.model.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class StatisticalActivity extends AppCompatActivity {
    LayoutStatisticalSellerBinding binding;
    OrderSellerAdapter orderSellerAdapter;

    List<Order> orderList = new ArrayList<>();
    FirebaseAuth firebaseAuth;
    Order orderSeller;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LayoutStatisticalSellerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        binding.toolbar.ivToolbarLeft.setImageResource(R.drawable.ic_left);
        binding.toolbar.ivToolbarRight.setVisibility(View.GONE);
        binding.toolbar.tvTitleToolbar.setText(getText(R.string.Statistical));
        binding.toolbar.ivToolbarLeft.setOnClickListener(view -> {
            onBackPressed();
            finish();
        });
        firebaseAuth = FirebaseAuth.getInstance();
        loadAllOrder();
        binding.rlDateStart.setOnClickListener(view -> showDatePickerDialogStart());
        binding.rlDateEnd.setOnClickListener(v -> showDatePickerDialogEnd());

    }
    public void showDatePickerDialogStart() {
        // Lấy ngày hiện tại
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int year = calendar.get(java.util.Calendar.YEAR);
        int month = calendar.get(java.util.Calendar.MONTH);
        int dayOfMonth = calendar.get(java.util.Calendar.DAY_OF_MONTH);

        // Tạo một DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view1, year1, monthOfYear, dayOfMonth1) -> {
                    // Xử lý ngày đã chọn
                    String selectedDate = dayOfMonth1 + "/" + (monthOfYear + 1) + "/" + year1;
                    binding.dateStart.setText(selectedDate);
//                    Long times = getTimestampFromDate(year1, monthOfYear, dayOfMonth1);
//                    SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
//                    String date = formatDate.format(times);
//                    loadAllOrder(date);
//                    Log.d("aaa",date + "timestamp");
                },
                year,
                month,
                dayOfMonth
        );

        // Hiển thị DatePickerDialog
        datePickerDialog.show();
    }
    public void showDatePickerDialogEnd() {
        // Lấy ngày hiện tại
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Tạo một DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view1, year1, monthOfYear, dayOfMonth1) -> {
                    // Xử lý ngày đã chọn
                    String selectedDate = dayOfMonth1 + "/" + (monthOfYear + 1) + "/" + year1;
                    binding.dateEnd.setText(selectedDate);
                    Long times = getTimestampFromDate(year1, monthOfYear, dayOfMonth1);
                    SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    String date = formatDate.format(times);
                    loadAllOrderDate(binding.dateStart.getText().toString(), date);
                },
                year,
                month,
                dayOfMonth
        );

        // Hiển thị DatePickerDialog
        datePickerDialog.show();
    }

    private void loadAllOrder(String selectedTimestamp) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        binding.rcyOrder.setLayoutManager(linearLayoutManager);
        binding.rcyOrder.setHasFixedSize(true);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child("Orders").orderByChild("shop_uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (orderList != null) {
                            orderList.clear();
                        }
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            orderSeller = ds.getValue(Order.class);
                            if (orderSeller != null && orderSeller.getOrderStatus().equals("Đã xác nhận")) {
                                Long times = Long.parseLong(orderSeller.getOrderTime());
                                SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                                String date = formatDate.format(times);
                                if (selectedTimestamp.equals(date)) {
                                    orderList.add(0, orderSeller);
                                }
                            }
                        }
                        orderSellerAdapter = new OrderSellerAdapter(StatisticalActivity.this, orderList);
                        binding.rcyOrder.setAdapter(orderSellerAdapter);

                        int sum = 0;
                        for (int i = 0; i < orderList.size(); i++) {
                            sum += Integer.parseInt(orderList.get(i).getOrderCost());
                        }
                        binding.sumTotal.setText(sum + " $");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadAllOrderDate(String startDate, String endDate) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        binding.rcyOrder.setLayoutManager(linearLayoutManager);
        binding.rcyOrder.setHasFixedSize(true);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child("Orders").orderByChild("shop_uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (orderList != null) {
                            orderList.clear();
                        }
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            orderSeller = ds.getValue(Order.class);
                            if (orderSeller != null && orderSeller.getOrderStatus().equals("Đã xác nhận")) {
                                Long times = Long.parseLong(orderSeller.getOrderTime());
                                SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                                String date = formatDate.format(times);
                                if (isDateInRange(date, startDate, endDate)) {
                                    orderList.add(0, orderSeller);
                                }
                            }
                        }
                        orderSellerAdapter = new OrderSellerAdapter(StatisticalActivity.this, orderList);
                        binding.rcyOrder.setAdapter(orderSellerAdapter);

                        int sum = 0;
                        for (int i = 0; i < orderList.size(); i++) {
                            sum += Integer.parseInt(orderList.get(i).getOrderCost());
                        }
                        binding.sumTotal.setText(sum + " $");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private boolean isDateInRange(String dateToCheck, String startDate, String endDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date date = sdf.parse(dateToCheck);
            Date startDateObj = sdf.parse(startDate);
            Date endDateObj = sdf.parse(endDate);

            // Kiểm tra xem ngày đặt hàng có nằm trong khoảng từ ngày bắt đầu đến ngày kết thúc không
            return !date.before(startDateObj) && !date.after(endDateObj);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private long getTimestampFromDate(int year, int month, int dayOfMonth) {
        Calendar calendar = new GregorianCalendar(year, month, dayOfMonth);
        return calendar.getTimeInMillis();
    }
    private void loadAllOrder() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        binding.rcyOrder.setLayoutManager(linearLayoutManager);
        binding.rcyOrder.setHasFixedSize(true);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child("Orders").orderByChild("shop_uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (orderList != null) {
                            orderList.clear();
                        }
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            orderSeller = ds.getValue(Order.class);
                            if (orderSeller != null && orderSeller.getOrderStatus().equals("Đã xác nhận")) {
                                orderList.add(0, orderSeller);
                            }
                        }
                        orderSellerAdapter = new OrderSellerAdapter(StatisticalActivity.this, orderList);
                        binding.rcyOrder.setAdapter(orderSellerAdapter);

                        int sum = 0;
                        for (int i = 0; i < orderList.size(); i++) {
                            sum += Integer.parseInt(orderList.get(i).getOrderCost());
                        }
                        binding.sumTotal.setText(sum + " $");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}
