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
import com.example.shop_app.model.ChatMessage;
import com.example.shop_app.model.Product;

import java.util.List;

public class ChatUserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ChatMessage> chatMessages;
    private String sendID;
    private static final int TYPE_SEND = 1;
    private static final int TYPE_RECEIVED = 2;

    public ChatUserAdapter(Context context, List<ChatMessage> chatMessages, String sendID) {
        this.context = context;
        this.chatMessages = chatMessages;
        this.sendID = sendID;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_SEND){
            view = LayoutInflater.from(context).inflate(R.layout.item_chat,parent, false);
            return new SendMessViewHolder(view);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.item_chat_received,parent, false);
            return new ReceivedViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position)==TYPE_SEND){
            String url = chatMessages.get(position).url;
            Glide.with(context).load(url).into(((SendMessViewHolder) holder).img);
            ((SendMessViewHolder) holder).tv_chat.setText(chatMessages.get(position).mess);
            ((SendMessViewHolder) holder).tv_time.setText(chatMessages.get(position).datetime);
        }else {
            String url = chatMessages.get(position).url;
            Glide.with(context).load(url).into(((ReceivedViewHolder) holder).img);
            ((ReceivedViewHolder) holder).tv_chat_received.setText(chatMessages.get(position).mess);
            ((ReceivedViewHolder) holder).tv_time_received.setText(chatMessages.get(position).datetime);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (chatMessages.get(position).senid.equals(sendID)){
            return TYPE_SEND;
        }else {
            return TYPE_RECEIVED;
        }

    }

    public class SendMessViewHolder extends RecyclerView.ViewHolder{
        TextView tv_chat,tv_time;
        ImageView img;

        public SendMessViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img);
            tv_chat = itemView.findViewById(R.id.tv_chat);
            tv_time = itemView.findViewById(R.id.tv_time);

        }
    }
    public class ReceivedViewHolder extends RecyclerView.ViewHolder{
        TextView tv_chat_received,tv_time_received;
        ImageView img;

        public ReceivedViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img);
            tv_chat_received = itemView.findViewById(R.id.tv_chat_received);
            tv_time_received = itemView.findViewById(R.id.tv_time_received);

        }
    }
}
