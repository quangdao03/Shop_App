package com.example.shop_app.dash.userdash;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shop_app.R;
import com.example.shop_app.activity.ProductDetail;
import com.example.shop_app.adapter.ProductAdapter;
import com.example.shop_app.adapter.WishListAdapter;
import com.example.shop_app.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {
    TextView tvTitleToolbar;
    ImageView ivToolbarLeft, ivToolbarRight;
    RecyclerView rcy_Wishlist, rcy_ListProduct_More;
    private FirebaseAuth firebaseAuth;
    WishListAdapter wishListAdapter;
    List<Product> productList = new ArrayList<>();
    List<Product> products = new ArrayList<>();
    ProductAdapter productAdapter;
    View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favorite, container, false);
        init();
        firebaseAuth = FirebaseAuth.getInstance();
        ivToolbarLeft.setVisibility(View.GONE);
        ivToolbarRight.setVisibility(View.GONE);
        tvTitleToolbar.setText(getText(R.string.wishlist));

        getWishList();
        getProductMore();

        return view;
    }
    private void getWishList(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        rcy_Wishlist.setLayoutManager(linearLayoutManager);
        rcy_Wishlist.setHasFixedSize(true);

        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rcy_Wishlist.addItemDecoration(decoration);
        wishListAdapter = new WishListAdapter(getActivity(), productList);
        rcy_Wishlist.setAdapter(wishListAdapter);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Wishlist");
        reference.child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (productList != null) {
                    productList.clear();
                }
                for (DataSnapshot getData : snapshot.getChildren()) {
                    Product product = getData.getValue(Product.class);
                    boolean favourite = product.getFavourite();
                    if (favourite){
                        productList.add(product);
                    }
                }
                wishListAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getProductMore() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        rcy_ListProduct_More.setLayoutManager(linearLayoutManager);
        rcy_ListProduct_More.setHasFixedSize(true);

        productAdapter = new ProductAdapter(getActivity(), products);
        rcy_ListProduct_More.setAdapter(productAdapter);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myProdcut = database.getReference("Product");
        myProdcut.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (products != null) {
                    products.clear();
                }

                for (DataSnapshot getData : dataSnapshot.getChildren()) {
                    Product product = getData.getValue(Product.class);
                    products.add(product);

                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void init() {
        rcy_ListProduct_More = view.findViewById(R.id.rcy_ListProduct_More);
        rcy_Wishlist = view.findViewById(R.id.rcy_Wishlist);
        tvTitleToolbar = view.findViewById(R.id.tvTitleToolbar);
        ivToolbarLeft = view.findViewById(R.id.ivToolbarLeft);
        ivToolbarRight = view.findViewById(R.id.ivToolbarRight);
    }
}
