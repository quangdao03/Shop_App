package com.example.shop_app.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shop_app.R;
import com.example.shop_app.activity.EditProductSeller;
import com.example.shop_app.activity.ProductDetail;
import com.example.shop_app.model.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        holder.image_delete.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder
                    .setMessage("Bạn có chắc chắn muốn xóa sản phẩm "+product.getName()+ "?")
                    .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            deleteProduct(product.getId());
                        }
                    })
                    .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    }).show();
        });
        holder.image_edit.setOnClickListener(view -> {
            Intent intent = new Intent(context, EditProductSeller.class);
            intent.putExtra("productId", product.getId());
            context.startActivity(intent);
        });

    }
    private void deleteProduct(String id) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Product");
        reference.child(id).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
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
