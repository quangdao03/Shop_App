package com.example.shop_app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shop_app.R;
import com.example.shop_app.dash.userdash.CartFragment;
import com.example.shop_app.model.Cart;

import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ListViewHolder>{
    Context context;
    private List<Cart> cartList;

    private CartFragment fragment;

    iClickListener mClick;
    public interface iClickListener {
        void onClickUpdateItem(Cart cart);
        void onClickDeleteItem(Cart cart);
    }

    private double cost = 0;
    private double finalCost = 0;
    private int Qquantity = 1;
    public PaymentAdapter(Context context, List<Cart> cartList, iClickListener iClickListener) {
        this.context = context;
        this.cartList = cartList;
        this.mClick = iClickListener;

    }

    @NonNull
    @Override
    public PaymentAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment_product,parent,false);
        return new PaymentAdapter.ListViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull PaymentAdapter.ListViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Cart cart = cartList.get(position);


        String url = cart.getImage();
        Glide.with(context).load(url).into(holder.imag_PaymnetList);
        holder.tv_name_payment.setText(cart.getName());
        holder.tv_variant_payment.setText(cart.getVariant());
        String price = cart.getPrice();
        holder.tv_price_payment.setText(price);
        holder.tv_quantity_payment.setText(cart.getQuantity());
        holder.tv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClick.onClickDeleteItem(cart);
                cartList.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(cartList!=null){
            return cartList.size();
        }
        return 0;
    }
    public static class ListViewHolder extends RecyclerView.ViewHolder{

        ImageView imag_PaymnetList;
        TextView tv_name_payment,tv_variant_payment,tv_price_payment,tv_remove,tv_quantity_payment;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            imag_PaymnetList = itemView.findViewById(R.id.imag_PaymnetList);
            tv_remove = itemView.findViewById(R.id.tv_remove);
            tv_name_payment = itemView.findViewById(R.id.tv_name_payment);
            tv_variant_payment = itemView.findViewById(R.id.tv_variant_payment);
            tv_price_payment = itemView.findViewById(R.id.tv_price_payment);
            tv_quantity_payment = itemView.findViewById(R.id.tv_quantity_payment);




        }
    }
}
