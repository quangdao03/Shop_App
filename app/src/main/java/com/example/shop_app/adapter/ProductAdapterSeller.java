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

import java.util.ArrayList;
import java.util.List;

public class ProductAdapterSeller extends RecyclerView.Adapter<ProductAdapterSeller.ListViewHolder> {
    Context context;
    private List<Product> productList;

    public ProductAdapterSeller(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductAdapterSeller.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_seller,parent,false);
        return new ProductAdapterSeller.ListViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapterSeller.ListViewHolder holder, int position) {
        Product product = productList.get(position);
        if (product==null){
            return;
        }
        String url;
        url = product.getUrl();
        Glide.with(context).load(url).into(holder.image_product);
        holder.price_product_seller.setText(product.getPrice());
        holder.name_product_seller.setText(product.getName());
        holder.tv_variant.setText(product.getVariant());
        holder.tv_detail.setText(product.getDesc());
        holder.image_edit.setOnClickListener(view -> {

        });
        holder.image_delete.setOnClickListener(view -> {

        });

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, ProductDetail.class);
//                intent.putExtra("name",product.getName());
//                context.startActivity(intent);
//            }
//        });

    }
    public void filterList(ArrayList<Product> filterllist) {
        productList = filterllist;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        if(productList!=null){
            return productList.size();
        }
        return 0;
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder{
        ImageView image_product,image_edit,image_delete;
        TextView name_product_seller,price_product_seller,tv_variant,tv_detail;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            image_product = itemView.findViewById(R.id.image_product);
            name_product_seller = itemView.findViewById(R.id.name_product_seller);
            price_product_seller = itemView.findViewById(R.id.price_product_seller);
            tv_variant = itemView.findViewById(R.id.tv_variant);
            image_delete = itemView.findViewById(R.id.image_delete);
            image_edit = itemView.findViewById(R.id.image_edit);
            tv_detail = itemView.findViewById(R.id.tv_detail);


        }
    }
}
