package com.example.shop_app.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shop_app.R;

public class RealtimeAdapter {
    public class UserViewHoder extends RecyclerView.ViewHolder{
        private TextView txtid,txt_name;
        public UserViewHoder(@NonNull View itemView) {
            super(itemView);
            txtid = itemView.findViewById(R.id.txtid);
            txt_name = itemView.findViewById(R.id.txt_name);

        }
    }
}
