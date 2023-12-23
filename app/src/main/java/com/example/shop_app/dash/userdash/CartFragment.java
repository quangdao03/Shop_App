package com.example.shop_app.dash.userdash;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shop_app.EventBus.TotalEventCart;
import com.example.shop_app.R;
import com.example.shop_app.activity.CheckoutActivity;
import com.example.shop_app.activity.LoginActivity;
import com.example.shop_app.adapter.CartAdapter;
import com.example.shop_app.database.CartDatabase;
import com.example.shop_app.database.CartRoom;
import com.example.shop_app.database.MyDatabaseHelper;
import com.example.shop_app.model.Cart;
import com.example.shop_app.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


public class CartFragment extends Fragment {

    ImageView ivToolbarLeft,ivToolbarRight;
    TextView tvTitleToolbar;
    RecyclerView rcy_Cart;
    Button btn_BuyCart;

    CartAdapter cartAdapter;
    List<CartRoom> cartList1 = new ArrayList<>();
    public TextView totalPrice;

    FirebaseAuth firebaseAuth;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cart, container,false);
        firebaseAuth = FirebaseAuth.getInstance();
        mapping();
        loadCart();
        tvTitleToolbar.setText(requireContext().getString(R.string.cart));
        ivToolbarLeft.setVisibility(View.GONE);
        ivToolbarRight.setVisibility(View.GONE);

        btn_BuyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkUser();
                if (cartList1.size() == 0){
                    Toast.makeText(getContext(), ""+getText(R.string.cart_empty), Toast.LENGTH_SHORT).show();
                    return;
                }
                String total = totalPrice.getText().toString().trim();
                Intent intent = new Intent(getContext(), CheckoutActivity.class);
                intent.putExtra("total",total);
                startActivity(intent);


            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void eventTotal(TotalEventCart totalEventCart){
        if (totalEventCart != null){
            totalPrice();
        }
    }
    private void totalPrice(){
        double total = 0;
        for (int i = 0; i < cartList1.size(); i++){
            total = total + (Double.parseDouble(cartList1.get(i).getPrice()) * Double.parseDouble(cartList1.get(i).getQuantity()));
        }
        totalPrice.setText(total+"");
    }

    private void loadCart() {
        cartList1.clear();
        cartList1 = CartDatabase.getInstance(getContext()).cartDAO().getAllCart();

        totalPrice();
        cartAdapter = new CartAdapter(requireActivity(),cartList1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false);
        rcy_Cart.setLayoutManager (linearLayoutManager);
        rcy_Cart.setHasFixedSize(true);
        rcy_Cart.setAdapter(cartAdapter);
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d("resume","resume");
        loadCart();
    }

    private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finishAffinity();
        }
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
