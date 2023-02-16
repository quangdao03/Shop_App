package com.example.shop_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shop_app.R;
import com.example.shop_app.activity.ProductDetail;
import com.example.shop_app.model.Product;

import java.util.List;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ListViewHolder> {
    Context context;
    private List<Product> productList;

    public WishListAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public WishListAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_wishlist,parent,false);
        return new WishListAdapter.ListViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull WishListAdapter.ListViewHolder holder, int position) {
        Product product = productList.get(position);
        if (product==null){
            return;
        }
        String url;
        url = product.getUrl();
        Glide.with(context).load(url).into(holder.imag_wishlist);
        holder.tv_price.setText(product.getPrice());
        holder.tv_name.setText(product.getName());
        holder.tv_creator.setText(product.getCreator());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductDetail.class);
                intent.putExtra("name",product.getName());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(productList!=null){
            return productList.size();
        }
        return 0;
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder{
        ImageView imag_wishlist;
        TextView tv_name,tv_creator,tv_price;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            imag_wishlist = itemView.findViewById(R.id.imag_wishlist);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_creator = itemView.findViewById(R.id.tv_creator);
            tv_price = itemView.findViewById(R.id.tv_price);


        }
    }
}
