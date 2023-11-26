package com.example.shop_app.dash.sellerdash;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shop_app.R;
import com.example.shop_app.activity.AddProductSeller;
import com.example.shop_app.activity.ChatActivitySeller;
import com.example.shop_app.adapter.ProductAdapterSeller;
import com.example.shop_app.databinding.FragmentHomeSellerBinding;
import com.example.shop_app.model.CostOrder;
import com.example.shop_app.model.Order;
import com.example.shop_app.model.Product;
import com.example.shop_app.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;


public class HomeFragmentSeller extends Fragment {


    TextView tvTitleToolbar, txt_PriceAll;
    ImageView ivToolbarLeft, ivToolbarRight, ivToolbarRight_message;
    RecyclerView rcy_Prodcut_Seller;
    List<Product> productList = new ArrayList<>();

    ProductAdapterSeller productAdapter;
    List<Order> orderList = new ArrayList<>();
    FirebaseAuth firebaseAuth;
    List<CostOrder> costOrderList = new ArrayList<>();

    SearchView edt_search;


    FragmentHomeSellerBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeSellerBinding.inflate(getLayoutInflater());
        mapping();
        firebaseAuth = FirebaseAuth.getInstance();
        getObjectUser();
        viewClick();
        getProductSeller();
        loadAllOrder();
        onToken();
        return binding.getRoot();
    }

    private void onToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()){
                    String token = task.getResult();
                    Log.i("token_app",token);
                }
            }
        });
    }

    private void viewClick() {
        tvTitleToolbar.setText("Product");
        ivToolbarLeft.setVisibility(View.GONE);
        ivToolbarRight.setImageResource(R.drawable.icons8_add);
        ivToolbarRight_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ChatActivitySeller.class));
            }
        });

        edt_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        binding.toolbar.ivToolbarRight.setOnClickListener(view -> {
            startActivity(new Intent(requireActivity(), AddProductSeller.class));
        });
    }

    private void loadAllOrder() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users/Orders");
        ref.orderByChild("shop_uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (costOrderList != null) {
                            costOrderList.clear();
                        }
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            Order orderSeller = ds.getValue(Order.class);
                            CostOrder costOrder = ds.getValue(CostOrder.class);
                            costOrderList.add(costOrder);
                        }

                        List<CostOrder> myList1 = costOrderList;
                        int sum = 0;
                        for (int i = 0; i < myList1.size(); i++) {
                            sum += Integer.parseInt(myList1.get(i).getOrderCost());
                        }
                        txt_PriceAll.setText(sum + " $");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getProductSeller() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcy_Prodcut_Seller.setLayoutManager(linearLayoutManager);
        rcy_Prodcut_Seller.setHasFixedSize(true);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myProdcut = database.getReference("Product");

        myProdcut.orderByChild("uid").equalTo(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (productList != null) {
                    productList.clear();
                }
                for (DataSnapshot getData : dataSnapshot.getChildren()) {
//                    Product product = new Product();
//                    product.setImage(getData.child("image").getValue().toString());
//                    product.setName(getData.child("name").getValue().toString());
//                    product.setPrice(getData.child("price").getValue().toString() + " $");
//                    String quantity = "";
//                    quantity = getData.child("quantity").getValue().toString();
//                    product.setQuantity("(" + quantity + ")");
//                    product.setDesc(getData.child("desc").getValue().toString());
//                    product.setId(getData.child("id").getValue().toString());
                    Product product = getData.getValue(Product.class);
                    productList.add(product);
                }
                productAdapter = new ProductAdapterSeller(getContext(), productList);
                rcy_Prodcut_Seller.setAdapter(productAdapter);
                productAdapter.notifyDataSetChanged();
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
            productAdapter.filterList(filteredlist);
        }
    }

    private void getObjectUser() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String image = "" + dataSnapshot.child("profileImage").getValue();
                            String shop_name = "" + dataSnapshot.child("shop_name").getValue();
                            Utils.ImageSeller = image;
                            Utils.name_shop = shop_name;
                            binding.tvShopname.setText("Shop name: " + shop_name);

                        }
                    }

                    @Override
                    public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                    }
                });
    }

    private void mapping() {
        rcy_Prodcut_Seller = binding.rcyProdcutSeller;
        tvTitleToolbar = binding.toolbar.tvTitleToolbar;
        ivToolbarLeft = binding.toolbar.ivToolbarLeft;
        ivToolbarRight = binding.toolbar.ivToolbarRight;
        txt_PriceAll = binding.txtPriceAll;
        ivToolbarRight_message = binding.toolbar.ivToolbarRightMessage;
        edt_search = binding.edtSearch;
    }
}
