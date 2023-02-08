package com.example.shop_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shop_app.R;
import com.example.shop_app.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListSPNew extends RecyclerView.Adapter<ListSPNew.ListViewHolder> {
    Context context;
    private List<Product> sanPhams;

    public ListSPNew(Context context, List<Product> sanPhams) {
        this.context = context;
        this.sanPhams = sanPhams;
    }

    @NonNull
    @Override
    public ListSPNew.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_new_arrivals,parent,false);
        return new ListSPNew.ListViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ListSPNew.ListViewHolder holder, int position) {
        Product sanPham = sanPhams.get(position);
        if (sanPham==null){
            return;
        }
        Picasso.get().load(sanPham.getImage()).into(holder.productImage);
        holder.txtprice.setText(sanPham.getPrice());
        holder.txtname.setText(sanPham.getName());
        holder.txtnumber.setText(sanPham.getNumber());
    }

    @Override
    public int getItemCount() {
        if(sanPhams!=null){
            return sanPhams.size();
        }
        return 0;
    }

    public class ListViewHolder extends RecyclerView.ViewHolder{
        LinearLayout layout;
        ImageView productImage;
        TextView productName_TextView,txtname,txtprice,txtnumber;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.imgAnh);
            txtprice = itemView.findViewById(R.id.txtprice);
            txtname = itemView.findViewById(R.id.txtname);
            txtnumber = itemView.findViewById(R.id.txtnumber);
            layout = itemView.findViewById(R.id.layout);

        }
    }
}