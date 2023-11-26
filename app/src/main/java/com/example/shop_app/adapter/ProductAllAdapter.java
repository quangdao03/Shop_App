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

public class ProductAllAdapter extends RecyclerView.Adapter<ProductAllAdapter.ListViewHolder> {
    Context context;
    private List<Product> productList;

    public ProductAllAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductAllAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_danhmuc_home,parent,false);
        return new ProductAllAdapter.ListViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ProductAllAdapter.ListViewHolder holder, int position) {
        Product product = productList.get(position);
        if (product==null){
            return;
        }
        String url;
        url = product.getImage();
        Glide.with(context).load(url).into(holder.productImage);
        // Picasso.get().load(product.getImage()).into(holder.productImage);
        holder.txtprice.setText(product.getPrice());
        holder.txtname.setText(product.getName());
        holder.txtnumber.setText(product.getQuantity());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductDetail.class);
                intent.putExtra("id",product.getId());
                context.startActivity(intent);
            }
        });

    }
    public void filterList(ArrayList<Product> filterllist) {
        // below line is to add our filtered
        // list in our course array list.
        productList = filterllist;
        // below line is to notify our adapter
        // as change in recycler view data.
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
        LinearLayout layout;
        ImageView productImage,imag_wishlist;
        TextView productName_TextView,txtname,txtprice,txtnumber,tv_name,tv_creator,tv_price;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName_TextView = itemView.findViewById(R.id.productName_TextView);
            txtprice = itemView.findViewById(R.id.txtprice);
            txtname = itemView.findViewById(R.id.txtname);
            txtnumber = itemView.findViewById(R.id.txnumber);
            layout = itemView.findViewById(R.id.layout);

        }
    }
}
