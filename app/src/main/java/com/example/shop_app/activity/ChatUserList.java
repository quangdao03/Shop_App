package com.example.shop_app.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shop_app.R;
import com.example.shop_app.adapter.ChatListSeller;
import com.example.shop_app.databinding.ActivityChatUserListBinding;
import com.example.shop_app.model.Seller;
import com.example.shop_app.utils.SystemUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatUserList extends AppCompatActivity {
    ActivityChatUserListBinding binding;
    List<Seller> sellerList = new ArrayList<>();
    ChatListSeller chatListSeller;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SystemUtil.setLocale(this);
        super.onCreate(savedInstanceState);
        binding = ActivityChatUserListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        checkUserType();
        binding.toolbar.ivToolbarRight.setVisibility(View.GONE);
        binding.toolbar.ivToolbarLeft.setImageResource(R.drawable.ic_left);
        binding.toolbar.tvTitleToolbar.setText(getText(R.string.chat));
        binding.toolbar.ivToolbarLeft.setOnClickListener(view -> {
            onBackPressed();
            finish();
        });
    }

    private void checkUserType() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        binding.rcyChatSeller.setLayoutManager(linearLayoutManager);
        binding.rcyChatSeller.setHasFixedSize(true);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.orderByChild("uid").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String accountType = "" + ds.child("accountType").getValue();
                    if (accountType.equals("Seller")) {
                        Seller seller = new Seller();
                        seller.setUsername(ds.child("shop_name").getValue().toString());
                        seller.setUid(ds.child("uid").getValue().toString());
                        seller.setImg(ds.child("profileImage").getValue().toString());
                        sellerList.add(seller);
                    }

                }
                chatListSeller = new ChatListSeller(ChatUserList.this, sellerList);
                binding.rcyChatSeller.setAdapter(chatListSeller);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
