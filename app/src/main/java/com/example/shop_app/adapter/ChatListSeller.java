package com.example.shop_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shop_app.R;
import com.example.shop_app.activity.ChatActivityUser;
import com.example.shop_app.activity.ChatUserSeller;
import com.example.shop_app.model.Seller;

import java.util.List;

public class ChatListSeller extends RecyclerView.Adapter<ChatListSeller.MyViewHolder> {
    Context context;
    List<Seller> userList;

    public ChatListSeller(Context context, List<Seller> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chat_seller,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Seller user = userList.get(position);
        holder.tv_User_name.setText(userList.get(position).getUsername());
        String img  = userList.get(position).getImg();
        Glide.with(context).load(img).into(holder.img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivityUser.class);
                intent.putExtra("name",user.getUsername());
                intent.putExtra("id",user.getUid());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_User,tv_User_name;
        ImageView img;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_User = itemView.findViewById(R.id.tv_User);
            tv_User_name = itemView.findViewById(R.id.tv_User_name);
            img = itemView.findViewById(R.id.img);
        }
    }
}
