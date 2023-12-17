package com.example.shop_app.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.shop_app.R;
import com.example.shop_app.adapter.OrderSellerAdapter;
import com.example.shop_app.databinding.ActivityBarChartBinding;
import com.example.shop_app.model.Order;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BarChartActivity extends AppCompatActivity {
    ActivityBarChartBinding binding;
    List<Order> orderList = new ArrayList<>();
    FirebaseAuth firebaseAuth;
    Order orderSeller;
    String[] xValues = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    Set<String> uniqueYears = new HashSet<>();

    List<String> yearList = new ArrayList<>(uniqueYears);


    List<Integer> monthlyOrderSumList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBarChartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        firebaseAuth = FirebaseAuth.getInstance();
        loadAllOrder();
        binding.toolbar.ivToolbarLeft.setImageResource(R.drawable.ic_left);
        binding.toolbar.ivToolbarRight.setVisibility(View.GONE);
        binding.toolbar.tvTitleToolbar.setText(getText(R.string.Statistical));
        binding.toolbar.ivToolbarLeft.setOnClickListener(view -> {
            onBackPressed();
            finish();
        });
        binding.barChart.getAxisRight().setDrawLabels(false);
        ArrayList <BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0,45f));
        entries.add(new BarEntry(1,59f));
        entries.add(new BarEntry(2,65f));
        entries.add(new BarEntry(3,80f));
        entries.add(new BarEntry(4,20f));
        entries.add(new BarEntry(5,155f));
        entries.add(new BarEntry(6,19f));
        entries.add(new BarEntry(7,30f));
        entries.add(new BarEntry(8,44f));
        entries.add(new BarEntry(9,66f));
        entries.add(new BarEntry(10,55f));
        entries.add(new BarEntry(11,95f));
        YAxis yAxis = binding.barChart.getAxisLeft();
        yAxis.setAxisMaximum(0f);
        yAxis.setAxisMaximum(500f);
        yAxis.setAxisLineWidth(2f);
        yAxis.setAxisLineColor(Color.BLACK);
        yAxis.setLabelCount(10);
        BarDataSet barDataSet = new BarDataSet(entries,"Month");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        BarData barData = new BarData(barDataSet);
        binding.barChart.setData(barData);
        binding.barChart.getDescription().setEnabled(false);

        binding.barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xValues));
        binding.barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        binding.barChart.getXAxis().setGranularity(1f);
        binding.barChart.getXAxis().setGranularityEnabled(true);

        binding.barChart.invalidate();
        binding.rlYear.setOnClickListener(view -> {
            String options[] = yearList.toArray(new String[0]);

            AlertDialog.Builder builder = new AlertDialog.Builder(BarChartActivity.this);
            builder.setTitle("Sắp xếp")
                    .setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String selectedOptions = options[i];
                            binding.year.setText(selectedOptions);
                            loadAllOrder1(selectedOptions);
                        }
                    }).show();
        });
    }
    private void loadAllOrder() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child("Orders").orderByChild("shop_uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (orderList != null) {
                            orderList.clear();
                        }

                        Calendar calendar = Calendar.getInstance();
                        int currentYear = calendar.get(Calendar.YEAR);

                        // Sử dụng Map để theo dõi tổng giá trị đơn hàng theo tháng
                        Map<Integer, Integer> monthlyOrderSumMap = new HashMap<>();

                        for (DataSnapshot ds : snapshot.getChildren()) {
                            orderSeller = ds.getValue(Order.class);
                            if (orderSeller != null && orderSeller.getOrderStatus().equals("Đã xác nhận")) {
                                // Lọc theo năm
                                Calendar orderCalendar = Calendar.getInstance();
                                orderCalendar.setTimeInMillis(Long.parseLong(orderSeller.getOrderTime()));
                                int orderYear = orderCalendar.get(Calendar.YEAR);
                                uniqueYears.add(String.valueOf(orderYear));
                                if (orderYear == currentYear) {
                                    orderList.add(0, orderSeller);
                                    // Lọc theo từng tháng trong năm
                                    int orderMonth = orderCalendar.get(Calendar.MONTH);

                                    // Tính tổng giá trị đơn hàng cho mỗi tháng
                                    int orderCost = Integer.parseInt(orderSeller.getOrderCost());
                                    if (monthlyOrderSumMap.containsKey(orderMonth)) {
                                        int currentSum = monthlyOrderSumMap.get(orderMonth);
                                        monthlyOrderSumMap.put(orderMonth, currentSum + orderCost);
                                    } else {
                                        monthlyOrderSumMap.put(orderMonth, orderCost);
                                    }
                                }
                            }
                        }

                        yearList = new ArrayList<>(uniqueYears);
                        for (String year : yearList) {
                            Log.d("Year List", year);
                        }

                        // Có thể thực hiện các công việc khác với tổng giá trị đơn hàng ở đây

                        // In ra tổng giá trị đơn hàng cho từng tháng (ví dụ)
                        for (Map.Entry<Integer, Integer> entry : monthlyOrderSumMap.entrySet()) {
                            int month = entry.getKey();
                            int orderSum = entry.getValue();
                            Log.d("Monthly Order Sum", "Month: " + month + ", Sum: " + orderSum);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Xử lý khi có lỗi truy xuất dữ liệu
                    }
                });
    }


    private void loadAllOrder1(String selectedYear) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child("Orders").orderByChild("shop_uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (orderList != null) {
                            orderList.clear();
                        }

                        Calendar calendar = Calendar.getInstance();
                        int currentYear = calendar.get(Calendar.YEAR);

                        // Sử dụng Map để theo dõi tổng giá trị đơn hàng theo tháng
                        Map<Integer, Integer> monthlyOrderSumMap = new HashMap<>();

                        for (DataSnapshot ds : snapshot.getChildren()) {
                            orderSeller = ds.getValue(Order.class);
                            if (orderSeller != null && orderSeller.getOrderStatus().equals("Đã xác nhận")) {
                                // Lọc theo năm đã chọn
                                Calendar orderCalendar = Calendar.getInstance();
                                orderCalendar.setTimeInMillis(Long.parseLong(orderSeller.getOrderTime()));
                                int orderYear = orderCalendar.get(Calendar.YEAR);

                                if (String.valueOf(orderYear).equals(selectedYear)) {
                                    orderList.add(0, orderSeller);
                                    // Lọc theo từng tháng trong năm
                                    int orderMonth = orderCalendar.get(Calendar.MONTH);

                                    // Tính tổng giá trị đơn hàng cho mỗi tháng
                                    int orderCost = Integer.parseInt(orderSeller.getOrderCost());
                                    if (monthlyOrderSumMap.containsKey(orderMonth)) {
                                        int currentSum = monthlyOrderSumMap.get(orderMonth);
                                        monthlyOrderSumMap.put(orderMonth, currentSum + orderCost);
                                    } else {
                                        monthlyOrderSumMap.put(orderMonth, orderCost);
                                    }
                                }
                            }
                        }

                        // Cập nhật dữ liệu cho BarChart
                        updateBarChart(monthlyOrderSumMap);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Xử lý khi có lỗi truy xuất dữ liệu
                    }
                });
    }


    private void updateBarChart(Map<Integer, Integer> monthlyOrderSumMap) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            double orderSum = monthlyOrderSumMap.containsKey(i) ? monthlyOrderSumMap.get(i) : 0;
            entries.add(new BarEntry(i, (float) orderSum));
        }

        BarDataSet barDataSet = new BarDataSet(entries, "Monthly Order Quantity");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        BarData barData = new BarData(barDataSet);
        binding.barChart.setData(barData);
        binding.barChart.getDescription().setEnabled(false);
        binding.barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xValues));
        binding.barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        binding.barChart.getXAxis().setGranularity(1f);
        binding.barChart.getXAxis().setGranularityEnabled(true);

        binding.barChart.invalidate();
    }
}