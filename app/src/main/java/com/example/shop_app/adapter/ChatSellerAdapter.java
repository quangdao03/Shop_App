package com.example.shop_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shop_app.R;
import com.example.shop_app.activity.ChatUserSeller;

import java.util.List;

public class ChatSellerAdapter extends RecyclerView.Adapter<ChatSellerAdapter.MyViewHolder> {
    Context context;
    List<User> userList;

    public ChatSellerAdapter(Context context, List<User> userList) {
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
        User user = userList.get(position);

        holder.tv_User.setText(userList.get(position).getId());
        holder.tv_User_name.setText(userList.get(position).getUsername());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatUserSeller.class);
                intent.putExtra("id",user.getId());
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
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_User = itemView.findViewById(R.id.tv_User);
            tv_User_name = itemView.findViewById(R.id.tv_User_name);
        }
    }
}
