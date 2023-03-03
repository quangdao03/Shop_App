package com.example.shop_app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shop_app.R;
import com.example.shop_app.adapter.ChatUserAdapter;
import com.example.shop_app.databinding.ActivityChatUserBinding;
import com.example.shop_app.databinding.ActivityChatUserSellerBinding;
import com.example.shop_app.model.ChatMessage;
import com.example.shop_app.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ChatUserSeller extends AppCompatActivity {
    String id,name;
    TextView tvTitleToolbar;
    ImageView ivToolbarLeft,ivToolbarRight;
    RecyclerView rcy_Chat;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    public static String ID_Seller;

    ActivityChatUserSellerBinding binding;
    ChatUserAdapter chatUserAdapter;
    List<ChatMessage> chatMessageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatUserSellerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        mapping();
        name = getIntent().getStringExtra("name");
        tvTitleToolbar.setText(name);
        ivToolbarRight.setVisibility(View.GONE);
        ivToolbarLeft.setImageResource(R.drawable.ic_left);
        ivToolbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getSupportActionBar().hide();
        id = getIntent().getStringExtra("id");// id nguoi nhan
        listenMess();
        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
        View.OnTouchListener touchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager inputMethodManager = (InputMethodManager) ChatUserSeller.this.getSystemService(Activity.INPUT_METHOD_SERVICE);

                if(ChatUserSeller.this.getCurrentFocus() != null)
                {
                    inputMethodManager.hideSoftInputFromWindow(ChatUserSeller.this.getCurrentFocus().
                            getWindowToken(), 0);
                }
                return false;
            }
        };
        rcy_Chat.setOnTouchListener(touchListener);
        binding.edtMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Collections.sort(chatMessageList,(obj1,obj2)-> obj1.dateObj.compareTo(obj2.dateObj));
                chatUserAdapter.notifyItemRangeInserted(chatMessageList.size(),chatMessageList.size());
                rcy_Chat.smoothScrollToPosition(chatMessageList.size()-1);

                // Cập nhật Adapter hoặc hiển thị danh sách
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });



    }

    private String format_date(Date date){
        return new SimpleDateFormat("dd MMMM,yyyy - hh:mm a", Locale.getDefault()).format(date);
    }
    private void sendMessage() {
        String edtMessage = binding.edtMessage.getText().toString().trim();
        if (TextUtils.isEmpty(edtMessage)){
            binding.edtMessage.getFocusable();

        }else {
            HashMap<String,Object> message = new HashMap<>();
            message.put(Utils.SENDID,String.valueOf(firebaseAuth.getUid()));
            message.put(Utils.RECEIVEDID,id);// id ng nhan
            message.put(Utils.MESS,edtMessage);
            message.put(Utils.DATETIME,new Date());
            message.put(Utils.IMAGE,Utils.ImageSeller);
            db.collection(Utils.PATH_CHAT).add(message);
            binding.edtMessage.setText("");
        }
    }
    private void listenMess(){
        db.collection(Utils.PATH_CHAT)
                .whereEqualTo(Utils.SENDID,String.valueOf(firebaseAuth.getUid()))
                .whereEqualTo(Utils.RECEIVEDID,id)
                .addSnapshotListener(eventListener);
        db.collection(Utils.PATH_CHAT)
                .whereEqualTo(Utils.SENDID,id)
                .whereEqualTo(Utils.RECEIVEDID,String.valueOf(firebaseAuth.getUid()))
                .addSnapshotListener(eventListener);

    }
    private final EventListener<QuerySnapshot> eventListener = (value, error)->{
        if (error != null){
            return;
        }
        if (value != null){
            int count = chatMessageList.size();
            for (DocumentChange documentChange : value.getDocumentChanges()){
                if (documentChange.getType() == DocumentChange.Type.ADDED){
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.url = documentChange.getDocument().getString(Utils.IMAGE);
                    chatMessage.senid = documentChange.getDocument().getString(Utils.SENDID);
                    chatMessage.receivedID = documentChange.getDocument().getString(Utils.RECEIVEDID);
                    chatMessage.mess = documentChange.getDocument().getString(Utils.MESS);
                    chatMessage.dateObj = documentChange.getDocument().getDate(Utils.DATETIME);
                    chatMessage.datetime = format_date(documentChange.getDocument().getDate(Utils.DATETIME));
                    chatMessageList.add(chatMessage);

                }
            }
            Collections.sort(chatMessageList,(obj1, obj2)-> obj1.dateObj.compareTo(obj2.dateObj));
            if (count == 0){
                chatUserAdapter.notifyDataSetChanged();
            }else {
                chatUserAdapter.notifyItemRangeInserted(chatMessageList.size(),chatMessageList.size());
                rcy_Chat.smoothScrollToPosition(chatMessageList.size()-1);


            }
        }
    };
    private void mapping() {
        tvTitleToolbar = findViewById(R.id.tvTitleToolbar);
        ivToolbarLeft = findViewById(R.id.ivToolbarLeft);
        ivToolbarRight = findViewById(R.id.ivToolbarRight);
        rcy_Chat = findViewById(R.id.rcy_Chat);
        db = FirebaseFirestore.getInstance();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rcy_Chat.setLayoutManager(layoutManager);
        rcy_Chat.setHasFixedSize(true);
        chatUserAdapter = new ChatUserAdapter(getApplicationContext(), chatMessageList,String.valueOf(firebaseAuth.getUid()));
        rcy_Chat.setAdapter(chatUserAdapter);
    }
}