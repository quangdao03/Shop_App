package com.example.shop_app.dash.userdash;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shop_app.R;
import com.example.shop_app.adapter.ListProductNew;
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

public class FavoriteFragment extends Fragment  {
    TextView tvTitleToolbar;
    ImageView ivToolbarLeft,ivToolbarRight;
    RecyclerView rcy_Wishlist,rcy_ListProduct_More;
    private FirebaseAuth firebaseAuth;
    WishListAdapter wishListAdapter;
    List<Product> productList = new ArrayList<>();


    List<Product> products = new ArrayList<>();
    ProductAdapter productAdapter;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.fragment_favorite, container,false);
        init();
        firebaseAuth = FirebaseAuth.getInstance();
        ivToolbarLeft.setVisibility(View.GONE);
        ivToolbarRight.setVisibility(View.GONE);
        tvTitleToolbar.setText(getText(R.string.wishlist));

        getWishListProduct1();

        getProductMore();

        return view;
    }

    private void getWishListProduct() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getActivity(), RecyclerView.VERTICAL, false);
        rcy_Wishlist.setLayoutManager (linearLayoutManager);
        rcy_Wishlist.setHasFixedSize(true);

        wishListAdapter = new WishListAdapter(getActivity(),productList);
        rcy_Wishlist.setAdapter(wishListAdapter);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);

        rcy_Wishlist.addItemDecoration(decoration);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myProdcut = database.getReference("Product");

        //Lọc 2 phẩn tử đầu tiên của bảng dùng Query và limit
        Query query = myProdcut.orderByChild("uid").equalTo(firebaseAuth.getUid());
        query.addValueEventListener(new ValueEventListener() {
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
                    product.setPrice("Rp " +getData.child("price").getValue().toString());
                    product.setCreator(getData.child("creator").getValue().toString());
                    Log.d("AAA",""+getData);
                    productList.add(product);

                }
                wishListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void getWishListProduct1() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getActivity(), RecyclerView.VERTICAL, false);
        rcy_Wishlist.setLayoutManager (linearLayoutManager);
        rcy_Wishlist.setHasFixedSize(true);

        wishListAdapter = new WishListAdapter(getActivity(),productList);
        rcy_Wishlist.setAdapter(wishListAdapter);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);

        rcy_Wishlist.addItemDecoration(decoration);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myProdcut = database.getReference("Product");



        Query query = myProdcut.orderByChild("uid").equalTo(firebaseAuth.getUid());
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Boolean value = Boolean.valueOf(snapshot.child("favourite").getValue().toString());
                        if (value == true){
                            Product product =  snapshot.getValue(Product.class);
                            if (product != null){
                                String url = snapshot.child("image").getValue().toString();
                                product.setUrl(url);
                                productList.add(product);
                                wishListAdapter.notifyDataSetChanged();
                            }
                        }



            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getProductMore(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getActivity(), RecyclerView.HORIZONTAL, false);
        rcy_ListProduct_More.setLayoutManager (linearLayoutManager);
        rcy_ListProduct_More.setHasFixedSize(true);

        productAdapter = new ProductAdapter(getActivity(),products);
        rcy_ListProduct_More.setAdapter(productAdapter);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myProdcut = database.getReference("Product");
        Query query = myProdcut.orderByChild("rate").startAfter(3);
        query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (products != null){
                    products.clear();
                }

                for (DataSnapshot getData : dataSnapshot.getChildren()){
//                    Product product = getData.getValue(Product.class);
                    Product product = new Product();
                    product.setUrl(getData.child("image").getValue().toString());
                    product.setName(getData.child("name").getValue().toString());
                    product.setPrice(getData.child("price").getValue().toString()+" $");
                    String quantity = "";
                    quantity = getData.child("quantity").getValue().toString();
                    product.setQuantity("("+quantity+")");
                    Log.d("AAA",""+getData);
                    products.add(product);

                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void init() {
        rcy_ListProduct_More = view.findViewById(R.id.rcy_ListProduct_More);
        rcy_Wishlist = view.findViewById(R.id.rcy_Wishlist);
        tvTitleToolbar = view.findViewById(R.id.tvTitleToolbar);
        ivToolbarLeft = view.findViewById(R.id.ivToolbarLeft);
        ivToolbarRight = view.findViewById(R.id.ivToolbarRight);
    }
}
