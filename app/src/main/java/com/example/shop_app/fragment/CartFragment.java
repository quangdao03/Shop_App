package com.example.shop_app.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shop_app.R;
import com.example.shop_app.adapter.CartAdapter;
import com.example.shop_app.database.MyDatabaseHelper;
import com.example.shop_app.model.Cart;
import com.example.shop_app.model.User;

import java.util.ArrayList;
import java.util.List;


public class CartFragment extends Fragment {

    ImageView ivToolbarLeft,ivToolbarRight;
    TextView txt_Price,tvTitleToolbar;
    RecyclerView rcy_Cart;
    Button btn_BuyCart;

    CartAdapter cartAdapter;
    List<Cart> cartList = new ArrayList<>();
    public double allTotalPrice = 0.0;
    public TextView totalPrice;
    View view;


    private double cost = 0;
    private double finalCost = 0;
    private int Qquantity = 1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cart, container,false);
        mapping();
        loadCart();
        rcy_Cart.setAdapter(cartAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getActivity(), RecyclerView.VERTICAL, false);
        rcy_Cart.setLayoutManager (linearLayoutManager);
        rcy_Cart.setHasFixedSize(true);
        tvTitleToolbar.setText("Cart");
        ivToolbarLeft.setVisibility(View.GONE);
        ivToolbarRight.setVisibility(View.GONE);
        return view;
    }

    private void loadCart() {

        MyDatabaseHelper myDatabaseHelper = new MyDatabaseHelper(getActivity());
        Cursor cursor = myDatabaseHelper.readAllData();
        while (cursor.moveToNext()){
            String id = cursor.getString(0);
            String idProduct = cursor.getString(1);
            String image = cursor.getString(2);
            String name = cursor.getString(3);
            String creator = cursor.getString(4);
            String variant = cursor.getString(5);
            String price = cursor.getString(6);
            String priceEach = cursor.getString(7);
            String quantity = cursor.getString(8);

            allTotalPrice = allTotalPrice + Double.parseDouble(priceEach);
            totalPrice.setText("$"+allTotalPrice);
            Cart cart = new Cart(""+id,""+idProduct,""+image,""+name,""+creator,""+variant,""+price,""+priceEach,""+quantity);
            cartList.add(cart);
            Log.e("cart",cart.toString());
        }
        cartAdapter = new CartAdapter(getContext(), cartList, new CartAdapter.iClickListener() {
            @Override
            public void onClickUpdateItem(Cart cart) {

            }


            @Override
            public void onClickDeleteItem(Cart cart) {
                String priceEach = cart.getPriceEach();
                String price = cart.getPrice();
                cost = Double.parseDouble(priceEach);
                finalCost = Double.parseDouble(price);
                Qquantity = 1;
                MyDatabaseHelper myDB = new MyDatabaseHelper(getContext());
                String productID = cart.getProductID();
                myDB.deleteData(productID);
                cartAdapter.notifyDataSetChanged();
                double tx = Double.parseDouble((totalPrice.getText().toString().trim().replace("$","")));
                double totalPrice1 =  tx - Double.parseDouble(priceEach.replace("$",""));
                totalPrice.setText("$"+totalPrice1);

            }
        });


    }

    private void mapping() {
        tvTitleToolbar = view.findViewById(R.id.tvTitleToolbar);
        ivToolbarLeft = view.findViewById(R.id.ivToolbarLeft);
        ivToolbarRight = view.findViewById(R.id.ivToolbarRight);
        rcy_Cart = view.findViewById(R.id.rcy_Cart);
        btn_BuyCart = view.findViewById(R.id.btn_BuyCart);
        totalPrice = view.findViewById(R.id.txt_Price);
    }
}
