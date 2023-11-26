package com.example.shop_app.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shop_app.R;
import com.example.shop_app.adapter.ProductAdapter;
import com.example.shop_app.adapter.ProductAllAdapter;
import com.example.shop_app.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Product_All extends AppCompatActivity {
    TextView tvTitleToolbar;
    ImageView ivToolbarLeft,ivToolbarRight;
    RecyclerView rcy_ProductAll;
    ProductAllAdapter productAllAdapter;
    List<Product> productList = new ArrayList<>();
    SearchView search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_all);
        mapping();
        getSupportActionBar().hide();
        tvTitleToolbar.setText(getText(R.string.list_product));
        ivToolbarLeft.setImageResource(R.drawable.ic_left);
        ivToolbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ivToolbarRight.setVisibility(View.GONE);
        getProductAll();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });

    }

    private void getProductAll() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager( Product_All.this,2, GridLayoutManager.VERTICAL, false);

        rcy_ProductAll.setLayoutManager (gridLayoutManager);
        rcy_ProductAll.setHasFixedSize(true);

        productAllAdapter = new ProductAllAdapter(Product_All.this,productList);
        rcy_ProductAll.setAdapter(productAllAdapter);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myProdcut = database.getReference("Product");

        myProdcut.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (productList != null){
                    productList.clear();
                }
                for (DataSnapshot getData : dataSnapshot.getChildren()){
                    Product product = getData.getValue(Product.class);
                    productList.add(product);
                }
                productAllAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void filter(String text) {
        ArrayList<Product> filteredlist = new ArrayList<>();
        for (Product item : productList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        if (!filteredlist.isEmpty()) {
            productAllAdapter.filterList(filteredlist);
        }
    }

    private void mapping() {
        tvTitleToolbar = findViewById(R.id.tvTitleToolbar);
        ivToolbarLeft = findViewById(R.id.ivToolbarLeft);
        ivToolbarRight = findViewById(R.id.ivToolbarRight);
        rcy_ProductAll = findViewById(R.id.rcy_ProductAll);
        search = findViewById(R.id.search);
    }
}