package com.example.shop_app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shop_app.EventBus.TotalEventCart;
import com.example.shop_app.R;
import com.example.shop_app.database.CartDatabase;
import com.example.shop_app.database.CartRoom;
import com.example.shop_app.database.MyDatabaseHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ListViewHolder> {
    Context context;
    private List<CartRoom> cartList;
    iClickListener mClick;

    public interface iClickListener {
        void onClickUpdateItem(CartRoom cart);

        void onClickDeleteItem(CartRoom cart);
    }

    private double cost = 0;
    private double finalCost = 0;
    int quantityProduct = 1;

    public CartAdapter(Context context, List<CartRoom> cartList, iClickListener iClickListener) {
        this.context = context;
        this.cartList = cartList;
        this.mClick = iClickListener;

    }

    @NonNull
    @Override
    public CartAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_checkout_product, parent, false);
        return new CartAdapter.ListViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ListViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CartRoom cart = cartList.get(position);


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
                CartDatabase.getInstance(context).cartDAO().deleteCart(cart);
                cartList.remove(position);
                notifyItemChanged(position);
                EventBus.getDefault().postSticky(new TotalEventCart());
            }
        });

        cost = Double.parseDouble(price.replaceAll("$", ""));
        finalCost = Double.parseDouble(priceEach.replaceAll("$", ""));
        holder.count_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double price = Double.parseDouble(cart.getPrice().replaceAll("$", ""));
                int quantity = Integer.parseInt(cart.getQuantity());
                quantity++;
                double pricefinal = quantity * price;
                cart.setPriceEach(String.valueOf(pricefinal));
                cart.setQuantity(String.valueOf(quantity));
                holder.tv_price_cart.setText("" + pricefinal);
                holder.tv_quantity.setText("" + quantity);
                CartDatabase.getInstance(context).cartDAO().updateCart(cart);
                notifyItemChanged(position);
                EventBus.getDefault().postSticky(new TotalEventCart());
            }
        });


        holder.count_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                double price = Double.parseDouble(cart.getPrice().replaceAll("$", ""));
                int quantity = Integer.parseInt(cart.getQuantity());

                if (quantity > 1){
                    quantity--;
                    double pricefinal = quantity * price;
                    cart.setPriceEach(String.valueOf(pricefinal));
                    cart.setQuantity(String.valueOf(quantity));
                    holder.tv_price_cart.setText("" + pricefinal);
                    holder.tv_quantity.setText("" + quantity);
                    CartDatabase.getInstance(context).cartDAO().updateCart(cart);
                    notifyItemChanged(position);
                }
                EventBus.getDefault().postSticky(new TotalEventCart());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (cartList != null) {
            return cartList.size();
        }
        return 0;
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {

        ImageView imag_CartList, item_delete_cart, count_down, count_add;
        TextView tv_name_cart, tv_creator_cart, tv_price_cart, tv_quantity;
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
