package com.example.shop_app.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shop_app.R;
import com.example.shop_app.activity.ProductDetail;
import com.example.shop_app.database.CartDatabase;
import com.example.shop_app.database.CartRoom;
import com.example.shop_app.model.Product;

import java.util.List;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ListViewHolder> {
    Context context;
    private List<Product> productList;
    private int cost = 0;
    private int finalCost = 0;
    private int quantity = 0;
    String price;


    public WishListAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;

    }

    @NonNull
    @Override
    public WishListAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_wishlist, parent, false);
        return new WishListAdapter.ListViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull WishListAdapter.ListViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Product product = productList.get(position);
        if (product == null) {
            return;
        }

        String url;
        url = product.getImage();
        Glide.with(context).load(url).into(holder.imag_wishlist);
        holder.tv_price.setText(product.getPrice());
        holder.tv_name.setText(product.getName());
        holder.tv_creator.setText(product.getCreator());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductDetail.class);
                intent.putExtra("id", product.getId());
                context.startActivity(intent);
            }
        });


        holder.btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView img_Product, count_down, count_add, btn_close;
                TextView tv_dialog_name, tv_dialog_creator, tv_dialog_variant, tv_quantity, tv_price_product, tv_price_product_final, tv_detail;
                Button btn_AddToCart;
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layout_dialog_buy);
                dialog.getWindow().getAttributes().windowAnimations = androidx.appcompat.R.style.Animation_AppCompat_DropDownUp;

                Window window = dialog.getWindow();
                window.setGravity(Gravity.BOTTOM);
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(true);

                tv_dialog_name = dialog.findViewById(R.id.tv_dialog_name);
                tv_dialog_creator = dialog.findViewById(R.id.tv_dialog_creator);
                tv_dialog_variant = dialog.findViewById(R.id.tv_dialog_variant);
                img_Product = dialog.findViewById(R.id.img_Product);
                tv_price_product = dialog.findViewById(R.id.tv_price_product);
                tv_price_product_final = dialog.findViewById(R.id.tv_price_product_final);
                tv_quantity = dialog.findViewById(R.id.tv_quantity);
                count_down = dialog.findViewById(R.id.count_down);
                count_add = dialog.findViewById(R.id.count_add);
                btn_AddToCart = dialog.findViewById(R.id.btn_AddToCart);
                btn_close = dialog.findViewById(R.id.btn_close);
                tv_detail = dialog.findViewById(R.id.tv_detail);

                tv_dialog_name.setText(product.getName());
                tv_dialog_creator.setText(product.getCreator());
                tv_dialog_variant.setText(product.getVariant());

                price = product.getPrice();
                tv_price_product.setText(price);
                Glide.with(context).load(url).into(img_Product);

                quantity = 1;
                tv_quantity.setText("" + quantity);
                cost = Integer.parseInt(price.replaceAll("$", ""));
                finalCost = Integer.parseInt(price.replaceAll("$", ""));
                tv_price_product_final.setText("" + finalCost);

                count_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finalCost = finalCost + cost;
                        quantity++;
                        tv_price_product_final.setText("" + finalCost);
                        tv_quantity.setText("" + quantity);
                    }
                });
                count_down.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (quantity > 1) {
                            finalCost = finalCost - cost;
                            quantity--;
                            tv_price_product_final.setText("" + finalCost);
                            tv_quantity.setText("" + quantity);
                        }
                    }
                });

                btn_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


                btn_AddToCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int quantity_product = Integer.parseInt(product.getQuantity());
                        if (quantity_product <= 0) {
                            Toast.makeText(context, context.getString(R.string.sorry), Toast.LENGTH_SHORT).show();
                        }else{
                            dialog.dismiss();
                            final Dialog dialog = new Dialog(context);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.layout);
                            dialog.getWindow().getAttributes().windowAnimations = androidx.appcompat.R.style.Animation_AppCompat_DropDownUp;

                            Window window = dialog.getWindow();
                            window.setGravity(Gravity.BOTTOM);
                            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.setCancelable(true);
                            Button btn_Ok = dialog.findViewById(R.id.btn_Ok);
                            TextView tv_tap = dialog.findViewById(R.id.tv_tap);
                            tv_tap.setPaintFlags(tv_tap.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                            String image = url;
                            String name = tv_dialog_name.getText().toString().trim();
                            String price_name = price;
                            String creator = tv_dialog_creator.getText().toString().trim();
                            String variant = tv_dialog_variant.getText().toString().trim();
                            String totalprice = tv_price_product_final.getText().toString().trim().replace("", "");
                            String quantity = tv_quantity.getText().toString().trim();
                            String idProduct = String.valueOf(product.getId());


                            CartRoom cartRoom = new CartRoom();
                            cartRoom.setProductID(idProduct);
                            cartRoom.setImage(image);
                            cartRoom.setName(name);
                            cartRoom.setCreator(creator);
                            cartRoom.setVariant(variant);
                            cartRoom.setPrice(price_name);
                            cartRoom.setPriceEach(totalprice);
                            cartRoom.setQuantity(quantity);
                            cartRoom.setShop_id(product.getUid());
                            CartDatabase.getInstance(context).cartDAO().insertCart(cartRoom);

                            btn_Ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();


                                }
                            });
                            tv_tap.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                            dialog.show();
                        }

                    }
                });
                tv_detail.setOnClickListener(view1 -> {
                    Intent intent = new Intent(context, ProductDetail.class);
                    intent.putExtra("id", product.getId());
                    context.startActivity(intent);
                    dialog.dismiss();
                });


                dialog.show();
            }
        });

    }


    @Override
    public int getItemCount() {
        if (productList != null) {
            return productList.size();
        }
        return 0;
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView imag_wishlist;


        TextView tv_name, tv_creator, tv_price;
        Button btn_buy;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            imag_wishlist = itemView.findViewById(R.id.imag_wishlist);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_creator = itemView.findViewById(R.id.tv_creator);
            tv_price = itemView.findViewById(R.id.tv_price);
            btn_buy = itemView.findViewById(R.id.btn_buy);


        }
    }
}
