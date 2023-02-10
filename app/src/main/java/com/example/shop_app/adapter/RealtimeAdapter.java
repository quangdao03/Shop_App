package com.example.shop_app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shop_app.R;
import com.example.shop_app.model.User;

import java.util.List;

public class RealtimeAdapter extends RecyclerView.Adapter<RealtimeAdapter.UserViewHoder>{
    private List<User> mListUsers;

    private iClickListener mClick;
    public interface iClickListener {
        void onClickUpdateItem(User user);
        void onClickDeleteItem(User user);
    }
    public RealtimeAdapter(List<User> mListUsers,iClickListener iClickListener) {
        this.mClick = iClickListener;
        this.mListUsers = mListUsers;
    }

    @NonNull
    @Override
    public UserViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_realtime,parent, false);
        return new UserViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHoder holder, int position) {
        User user = mListUsers.get(position);
        if (user==null){
            return;
        }
        holder.txtid.setText("ID:" + user.getId());
        holder.txt_name.setText("Name"+ user.getName());

        holder.btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClick.onClickUpdateItem(user);
            }
        });
        holder.btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClick.onClickDeleteItem(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mListUsers!=null){
            return mListUsers.size();
        }
        return 0;
    }

    public class UserViewHoder extends RecyclerView.ViewHolder{
        private TextView txtid,txt_name;
        private Button btnSua, btnXoa;
        public UserViewHoder(@NonNull View itemView) {
            super(itemView);
            txtid = itemView.findViewById(R.id.txtid);
            txt_name = itemView.findViewById(R.id.txt_name);
            btnSua = itemView.findViewById(R.id.btnSua);
            btnXoa = itemView.findViewById(R.id.btnXoa);

        }
    }
}
