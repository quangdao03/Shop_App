package com.example.shop_app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.shop_app.R;
import com.example.shop_app.databinding.ActivityBarChartBinding;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BarChartActivity extends AppCompatActivity {
    ActivityBarChartBinding binding;

    private List<String> xValues = Arrays.asList("Maths","Name","English","IT","ID");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBarChartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
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
        entries.add(new BarEntry(4,95f));
        YAxis yAxis = binding.barChart.getAxisLeft();
        yAxis.setAxisMaximum(0f);
        yAxis.setAxisMaximum(100f);
        yAxis.setAxisLineWidth(2f);
        yAxis.setAxisLineColor(Color.BLACK);
        yAxis.setLabelCount(10);
        BarDataSet barDataSet = new BarDataSet(entries,"Subject");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        BarData barData = new BarData(barDataSet);
        binding.barChart.setData(barData);
        binding.barChart.getDescription().setEnabled(false);
        binding.barChart.invalidate();
        binding.barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xValues));
        binding.barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        binding.barChart.getXAxis().setGranularity(1f);
        binding.barChart.getXAxis().setGranularityEnabled(true);

    }
}