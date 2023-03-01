package com.example.shop_app.adapter;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shop_app.R;
import com.example.shop_app.dash.userdash.CartFragment;
import com.example.shop_app.database.MyDatabaseHelper;
import com.example.shop_app.model.Cart;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ListViewHolder>{
    Context context;
    private List<Cart> cartList;
    iClickListener mClick;
    public interface iClickListener {
        void onClickUpdateItem(Cart cart);
        void onClickDeleteItem(Cart cart);
    }

    private double cost = 0;
    private double finalCost = 0;
    int quantityProduct = 1;
    public CartAdapter(Context context, List<Cart> cartList,iClickListener iClickListener) {
        this.context = context;
        this.cartList = cartList;
        this.mClick = iClickListener;

    }

    @NonNull
    @Override
    public CartAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_checkout_product,parent,false);
        return new CartAdapter.ListViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ListViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Cart cart = cartList.get(position);


        String url = cart.getImage();
        Glide.with(context).load(url).into(holder.imag_CartList);
        holder.tv_name_cart.setText(cart.getName());
        holder.tv_creator_cart.setText(cart.getCreator());
        String price = cart.getPrice();
        holder.tv_price_cart.setText(cart.getPriceEach());
        String priceEach = cart.getPriceEach();



        holder.tv_quantity.setText(cart.getQuantity());

        quantityProduct = Integer.parseInt(cart.getQuantity());
        holder.item_delete_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClick.onClickDeleteItem(cart);
                cartList.remove(position);
                notifyDataSetChanged();
            }
        });

        cost = Double.parseDouble(price.replaceAll("$", ""));
        finalCost = Double.parseDouble(priceEach.replaceAll("$", ""));
        holder.count_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClick.onClickUpdateItem(cart);
                finalCost = finalCost + cost;
                quantityProduct++;
                holder.tv_price_cart.setText(""+finalCost);
                holder.tv_quantity.setText(""+quantityProduct);
                String rowID = cart.getCart_ID();
                String productID = cart.getProductID();
                String name = holder.tv_name_cart.getText().toString().trim();
                String creator = holder.tv_creator_cart.getText().toString().trim();
                String variant = cart.getVariant();
                String priceEach = holder.tv_price_cart.getText().toString().trim();
                updateData(rowID,productID,url,name,creator,variant,price,priceEach, String.valueOf(quantityProduct));
            }
        });


        holder.count_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantityProduct>1) {
                    finalCost = finalCost - cost;
                    quantityProduct--;
                    holder.tv_price_cart.setText(""+finalCost);
                    holder.tv_quantity.setText(""+quantityProduct);
                }
                String rowID = cart.getCart_ID();
                String productID = cart.getProductID();
                String name = holder.tv_name_cart.getText().toString().trim();
                String creator = holder.tv_creator_cart.getText().toString().trim();
                String variant = cart.getVariant();
                String priceEach = holder.tv_price_cart.getText().toString().trim();
                updateData(rowID,productID,url,name,creator,variant,price,priceEach, String.valueOf(quantityProduct));

            }
        });
    }


    public void updateData(String row_id, String productID, String image, String name, String creator, String variant, String price, String priceEach, String quantity){
        MyDatabaseHelper myDatabaseHelper = new MyDatabaseHelper(context);
        myDatabaseHelper.updateData(row_id,productID,image,name,creator,variant,price,priceEach,quantity);
    }

    @Override
    public int getItemCount() {
        if(cartList!=null){
            return cartList.size();
        }
        return 0;
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder{

        ImageView imag_CartList, item_delete_cart, count_down,count_add;
        TextView tv_name_cart,tv_creator_cart,tv_price_cart,tv_quantity;
        CheckBox check_item;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            imag_CartList = itemView.findViewById(R.id.imag_CartList);
            item_delete_cart = itemView.findViewById(R.id.item_delete_cart);
            count_down = itemView.findViewById(R.id.count_down);
            count_add = itemView.findViewById(R.id.count_add);
            tv_name_cart = itemView.findViewById(R.id.tv_name_cart);
            tv_creator_cart = itemView.findViewById(R.id.tv_creator_cart);
            tv_price_cart = itemView.findViewById(R.id.tv_price_cart);
            tv_quantity = itemView.findViewById(R.id.tv_quantity);
            check_item = itemView.findViewById(R.id.check_item);



        }
    }
}
