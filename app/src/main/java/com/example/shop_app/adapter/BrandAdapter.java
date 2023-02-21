package com.example.shop_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.example.shop_app.activity.Product_Brand;
import com.example.shop_app.model.Brand;



import java.util.List;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.ListViewHolder>{

    Context context;
    private List<Brand>  brandList;

    public BrandAdapter(Context context, List<Brand> brandList) {
        this.context = context;
        this.brandList = brandList;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_brands,parent,false);
        return new ListViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        Brand brand = brandList.get(position);
        String url = brand.getImage();
        Glide.with(context).load(url).into(holder.iv_brand);
        String id = String.valueOf(brand.getId());
        String name = brand.getName();
        Log.d("brand",name+id);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Product_Brand.class);
                intent.putExtra("id",id);
                intent.putExtra("name",name);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(brandList!=null){
            return brandList.size();
        }
        return 0;
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder{
        LinearLayout layout;
        ImageView iv_brand;


        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_brand = itemView.findViewById(R.id.iv_brand);

            layout = itemView.findViewById(R.id.layout);

        }
    }
}
