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
import com.example.shop_app.model.Cart;
import com.example.shop_app.model.OrderItem;

import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ListViewHolder> {

    Context context;
    private List<OrderItem> orderItemList;


    private double cost = 0;
    private double finalCost = 0;
    private int Qquantity = 1;
    public OrderItemAdapter(Context context, List<OrderItem> orderItemList) {
        this.context = context;
        this.orderItemList = orderItemList;
    }
    @NonNull
    @Override
    public OrderItemAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_detail,parent,false);
        return new OrderItemAdapter.ListViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemAdapter.ListViewHolder holder, @SuppressLint("RecyclerView") int position) {
        OrderItem orderItem = orderItemList.get(position);

        String url = orderItem.getImage();
        Glide.with(context).load(url).into(holder.imag_Order);
        holder.tv_name_order.setText(orderItem.getName());
        holder.tv_variant_order.setText(orderItem.getVariant());
        holder.tv_quantity_order.setText(orderItem.getQuantity());
        holder.tv_price_order.setText(orderItem.getPrice());
    }

    @Override
    public int getItemCount() {
        if(orderItemList!=null){
            return orderItemList.size();
        }
        return 0;
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder{

        ImageView imag_Order;
        TextView tv_name_order,tv_variant_order,tv_price_order,tv_quantity_order;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            imag_Order = itemView.findViewById(R.id.imag_Order);
            tv_name_order = itemView.findViewById(R.id.tv_name_order);
            tv_variant_order = itemView.findViewById(R.id.tv_variant_order);
            tv_quantity_order = itemView.findViewById(R.id.tv_quantity_order);
            tv_price_order = itemView.findViewById(R.id.tv_price_order);
        }
    }
}
