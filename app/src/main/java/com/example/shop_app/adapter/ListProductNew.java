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
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListProductNew extends RecyclerView.Adapter<ListProductNew.ListViewHolder> {
    Context context;
    private List<Product> products;

    public ListProductNew(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ListProductNew.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_new_arrivals,parent,false);
        return new ListProductNew.ListViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ListProductNew.ListViewHolder holder, int position) {
        Product product = products.get(position);
        if (product==null){
            return;
        }
        String url;
        url = product.getUrl();
        Glide.with(context).load(url).into(holder.productImage);
        // Picasso.get().load(product.getImage()).into(holder.productImage);
        holder.txtprice.setText(product.getPrice());
        holder.txtname.setText(product.getName());
        holder.txtnumber.setText(product.getQuantity());
        holder.txt_Creator.setText(product.getCreator());
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
        if(products!=null){
            return products.size();
        }
        return 0;
    }

    public class ListViewHolder extends RecyclerView.ViewHolder{
        LinearLayout layout;
        ImageView productImage;
        TextView txt_Creator,txtname,txtprice,txtnumber;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.imgAnh);
            txtprice = itemView.findViewById(R.id.txtprice);
            txtname = itemView.findViewById(R.id.txtname);
            txtnumber = itemView.findViewById(R.id.txtnumber);
            txt_Creator = itemView.findViewById(R.id.txt_Creator);
            layout = itemView.findViewById(R.id.layout);

        }
    }
}