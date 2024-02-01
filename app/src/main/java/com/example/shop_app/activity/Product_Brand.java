package com.example.shop_app.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shop_app.R;
import com.example.shop_app.adapter.ListProductAdapter;
import com.example.shop_app.adapter.ProductBrandAdapter;
import com.example.shop_app.model.Product;
import com.example.shop_app.utils.SystemUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Product_Brand extends AppCompatActivity {
    TextView tvTitleToolbar,tv_desc_brand,tv_brandname,txt_NumberProduct;
    ImageView ivToolbarRight, ivToolbarLeft,img_brand;
    RecyclerView rcy_Product_Brand;

    SearchView search;

    ProductBrandAdapter productBrandAdapter;
    List<Product> productList = new ArrayList<>();
    String id = "";
    String name = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SystemUtil.setLocale(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_product_brand);
        mapping();
        getSupportActionBar().hide();
        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        ivToolbarLeft.setImageResource(R.drawable.ic_left);
        tvTitleToolbar.setText(getText(R.string.brand));
        ivToolbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



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

        loadBrand();
        loadProductBrand();
    }

    private void loadBrand() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("brand");
        // Read from the database
        myRef.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tv_brandname.setText(dataSnapshot.child("name").getValue().toString());
                tv_desc_brand.setText(dataSnapshot.child("desc").getValue().toString());
                String url = dataSnapshot.child("image").getValue().toString();
                Glide.with(getApplicationContext()).load(url).into(img_brand);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void loadProductBrand(){
        GridLayoutManager gridLayoutManager = new GridLayoutManager( Product_Brand.this,2, GridLayoutManager.VERTICAL, false);

        rcy_Product_Brand.setLayoutManager (gridLayoutManager);
        rcy_Product_Brand.setHasFixedSize(true);
        productBrandAdapter = new ProductBrandAdapter(Product_Brand.this,productList);
        rcy_Product_Brand.setAdapter(productBrandAdapter);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myProdcut = database.getReference("Product");
        myProdcut.orderByChild("creator").equalTo(name).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot dataSnapshot) {

                if (productList != null){
                    productList.clear();
                }
                for (DataSnapshot getData : dataSnapshot.getChildren()){
                    Product product = getData.getValue(Product.class);
                    productList.add(product);
                }
                txt_NumberProduct.setText(productList.size() +" Products");
                productBrandAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.actionSearch);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        return true;
    }
    private void filter(String text) {
        ArrayList<Product> filteredlist = new ArrayList<>();
        for (Product item : productList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        if (!filteredlist.isEmpty()) {
            productBrandAdapter.filterList(filteredlist);
        }
    }

    private void mapping() {
        tvTitleToolbar = findViewById(R.id.tvTitleToolbar);
        ivToolbarLeft = findViewById(R.id.ivToolbarLeft);
        ivToolbarRight = findViewById(R.id.ivToolbarRight);
        img_brand = findViewById(R.id.img_brand);
        tv_brandname = findViewById(R.id.tv_brandname);
        tv_desc_brand = findViewById(R.id.tv_desc_brand);
        rcy_Product_Brand = findViewById(R.id.rcy_Product_Brand);
        search = findViewById(R.id.search);
        txt_NumberProduct = findViewById(R.id.txt_NumberProduct);
    }
}
