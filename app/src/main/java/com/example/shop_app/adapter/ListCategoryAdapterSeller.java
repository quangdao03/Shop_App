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
import com.example.shop_app.activity.Category_Product;
import com.example.shop_app.model.Category;

import java.util.List;

public class ListCategoryAdapterSeller extends RecyclerView.Adapter<ListCategoryAdapterSeller.ListViewHolder> {


    Context context;
    private List<Category> categoryList;

    public ListCategoryAdapterSeller(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_danhmuc_seller,parent,false);
        return new ListViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        Category category = categoryList.get(position);
        if (category==null){
            return;
        }
        String url = category.getImage();

        Glide.with(context).load(url).placeholder(R.drawable.ic_hoa1).into(holder.productImage);

        holder.productName_TextView.setText(category.getName());
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, Category_Product.class);
//                intent.putExtra("name",category.getName());
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        if(categoryList!=null){
            return categoryList.size();
        }
        return 0;
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder{
        LinearLayout layout;
        ImageView productImage;
        TextView productName_TextView;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName_TextView = itemView.findViewById(R.id.productName_TextView);
            layout = itemView.findViewById(R.id.layout);

        }
    }
}
