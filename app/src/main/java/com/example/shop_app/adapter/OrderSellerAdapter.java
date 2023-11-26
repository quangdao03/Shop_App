package com.example.shop_app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shop_app.R;
import com.example.shop_app.activity.OrderSellerDetail;
import com.example.shop_app.model.FilterOrderSeller;
import com.example.shop_app.model.Order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderSellerAdapter extends RecyclerView.Adapter<OrderSellerAdapter.ListViewHolder> implements Filterable {
    Context context;

    public List<Order> orderList, filterList;
    public FilterOrderSeller filter;
    private double cost = 0;
    private double finalCost = 0;
    private int Qquantity = 1;
    public OrderSellerAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
        this.filterList = orderList;
    }
    @NonNull
    @Override
    public OrderSellerAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_listorder,parent,false);
        return new OrderSellerAdapter.ListViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull OrderSellerAdapter.ListViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Order order = orderList.get(position);

        String OrderId  = order.getOrderID();
        String time  = order.getOrderTime();
        String cost  = order.getOrderCost();
        String orderBy  = order.getOrderBy();
        String orderTo  = order.getOrderTo();
        String status  = order.getOrderStatus();

        Long times = Long.parseLong(time);
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String date = formatDate.format(times);

        if (status.equals("Đang xử lý")){
            holder.tv_Order_Status.setTextColor(context.getResources().getColor(R.color.primary));
        }else if (status.equals("Đã xác nhận")){
            holder.tv_Order_Status.setTextColor(context.getResources().getColor(R.color.colorGreen));
        }else if (status.equals("Đã hủy")){
            holder.tv_Order_Status.setTextColor(context.getResources().getColor(R.color.red));
        }

        holder.tv_order_time.setText(date);
        holder.tv_Order_Status.setText(status);
        holder.tv_order_id.setText(time);
        holder.tv_Order_Name.setText(order.getOrderNameProduct());
        holder.tv_Order_Cost.setText(cost+" $");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OrderSellerDetail.class);
                intent.putExtra("orderId", time);
                intent.putExtra("orderBy",orderBy);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(orderList!=null){
            return orderList.size();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        if (filter == null){
            filter = new FilterOrderSeller(this, (ArrayList<Order>) filterList);
        }
        return filter;
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder{
        CardView ll_Order_detail;
        TextView tv_order_time,tv_Order_Name,tv_Order_Cost,tv_Order_Status,tv_order_id;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            ll_Order_detail = itemView.findViewById(R.id.ll_Order_detail);
            tv_order_time = itemView.findViewById(R.id.tv_order_time);
            tv_order_id = itemView.findViewById(R.id.tv_order_id);
            tv_Order_Name = itemView.findViewById(R.id.tv_Order_Name);
            tv_Order_Cost = itemView.findViewById(R.id.tv_Order_Cost);
            tv_Order_Status = itemView.findViewById(R.id.tv_Order_Status);
        }
    }
}
