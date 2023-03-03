package com.example.shop_app.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.shop_app.R;
import com.example.shop_app.adapter.ChatSellerAdapter;
import com.example.shop_app.adapter.ChatUserAdapter;
import com.example.shop_app.adapter.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChatActivitySeller extends AppCompatActivity {
    TextView tvTitleToolbar;
    ImageView ivToolbarLeft,ivToolbarRight;
    RecyclerView rcy_Chat_Seller;
    ChatSellerAdapter chatSellerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_seller);
        mapping();
        getSupportActionBar().hide();
        tvTitleToolbar.setText(R.string.chat);
        ivToolbarRight.setVisibility(View.GONE);
        ivToolbarLeft.setImageResource(R.drawable.ic_left);
        ivToolbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getUserChat();


    }

    private void getUserChat() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            List<User> userList = new ArrayList<>();
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                User user = new User();
                                user.setId(documentSnapshot.getString("id"));
                                user.setUsername(documentSnapshot.getString("name"));
                                String img = documentSnapshot.getString("img");
                                user.setImg(img);
                                userList.add(user);
                            }
                            if (userList.size()>0){
                                chatSellerAdapter = new ChatSellerAdapter(getApplicationContext(),userList);
                                rcy_Chat_Seller.setAdapter(chatSellerAdapter);
                            }
                        }
                    }
                });
    }

    private void mapping() {
        rcy_Chat_Seller = findViewById(R.id.rcy_Chat_Seller);
        tvTitleToolbar = findViewById(R.id.tvTitleToolbar);
        ivToolbarLeft = findViewById(R.id.ivToolbarLeft);
        ivToolbarRight = findViewById(R.id.ivToolbarRight);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rcy_Chat_Seller.setLayoutManager(layoutManager);
        rcy_Chat_Seller.setHasFixedSize(true);
    }
}