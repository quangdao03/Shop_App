package com.example.shop_app.dash.sellerdash;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.shop_app.R;
import com.example.shop_app.activity.Product_All;
import com.example.shop_app.adapter.ProductAdapter;
import com.example.shop_app.adapter.ProductAllAdapter;
import com.example.shop_app.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HomeFragmentSeller extends Fragment {
    View view;

    TextView tvTitleToolbar;
    ImageView ivToolbarLeft,ivToolbarRight;
    RecyclerView rcy_Prodcut_Seller;
    List<Product> productList = new ArrayList<>();
    ProductAdapter productAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_seller, container,false);
        mapping();
        tvTitleToolbar.setText("Product");
        ivToolbarLeft.setVisibility(View.GONE);
        ivToolbarRight.setImageResource(R.drawable.icons8_add);
        getProductSeller();
        return  view;
    }

    private void getProductSeller() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager( getContext(),2, GridLayoutManager.VERTICAL, false);

        rcy_Prodcut_Seller.setLayoutManager (gridLayoutManager);
        rcy_Prodcut_Seller.setHasFixedSize(true);

        productAdapter = new ProductAdapter(getContext(),productList);
        rcy_Prodcut_Seller.setAdapter(productAdapter);
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
                    Product product = new Product();
                    product.setUrl(getData.child("image").getValue().toString());
                    product.setName(getData.child("name").getValue().toString());
                    product.setPrice(getData.child("price").getValue().toString()+" $");
                    String quantity = "";
                    quantity = getData.child("quantity").getValue().toString();
                    product.setQuantity("("+quantity+")");
                    productList.add(product);
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void mapping() {
        rcy_Prodcut_Seller = view.findViewById(R.id.rcy_Prodcut_Seller);
        tvTitleToolbar = view.findViewById(R.id.tvTitleToolbar);
        ivToolbarLeft = view.findViewById(R.id.ivToolbarLeft);
        ivToolbarRight = view.findViewById(R.id.ivToolbarRight);
    }
}
