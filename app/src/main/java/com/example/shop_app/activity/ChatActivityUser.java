package com.example.shop_app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.shop_app.R;
import com.example.shop_app.adapter.ChatUserAdapter;
import com.example.shop_app.databinding.ActivityChatUserBinding;
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

public class ChatActivityUser extends AppCompatActivity {
    TextView tvTitleToolbar;
    ImageView ivToolbarLeft,ivToolbarRight;
    ActivityChatUserBinding binding;
    RecyclerView rcy_Chat;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    String ID_Seller;
    ChatUserAdapter chatUserAdapter;
    List<ChatMessage> chatMessageList = new ArrayList<>();
    ProgressDialog progressDialog;

    String shop_name;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        mapping();
        getSupportActionBar().hide();
        ID_Seller = getIntent().getStringExtra("id");
        shop_name = getIntent().getStringExtra("name");
        tvTitleToolbar.setText(shop_name);


        ivToolbarRight.setVisibility(View.GONE);
        ivToolbarLeft.setImageResource(R.drawable.ic_left);
        ivToolbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
        listenMess();
        insertUser();


        binding.edtMessage.setFocusableInTouchMode(true);
        binding.edtMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Collections.sort(chatMessageList,(obj1,obj2)-> obj1.dateObj.compareTo(obj2.dateObj));
//                chatUserAdapter.notifyItemRangeInserted(chatMessageList.size(),chatMessageList.size());
//                rcy_Chat.smoothScrollToPosition(chatMessageList.size()-1);
                if (chatMessageList.size() != 0){
                    Collections.sort(chatMessageList,(obj1,obj2)-> obj1.dateObj.compareTo(obj2.dateObj));
                    chatUserAdapter.notifyItemRangeInserted(chatMessageList.size(),chatMessageList.size());
                    rcy_Chat.smoothScrollToPosition(chatMessageList.size()-1);
                }
            }
        });

        View.OnTouchListener touchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager inputMethodManager = (InputMethodManager) ChatActivityUser.this.getSystemService(Activity.INPUT_METHOD_SERVICE);

                if(ChatActivityUser.this.getCurrentFocus() != null)
                {
                    inputMethodManager.hideSoftInputFromWindow(ChatActivityUser.this.getCurrentFocus().
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
//                Collections.sort(chatMessageList,(obj1,obj2)-> obj1.dateObj.compareTo(obj2.dateObj));
//                chatUserAdapter.notifyItemRangeInserted(chatMessageList.size(),chatMessageList.size());
//                rcy_Chat.smoothScrollToPosition(chatMessageList.size()-1);
                // Cập nhật Adapter hoặc hiển thị danh sách
                if (chatMessageList.size() != 0){
                    Collections.sort(chatMessageList,(obj1,obj2)-> obj1.dateObj.compareTo(obj2.dateObj));
                    chatUserAdapter.notifyItemRangeInserted(chatMessageList.size(),chatMessageList.size());
                    rcy_Chat.smoothScrollToPosition(chatMessageList.size()-1);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

//        progressDialog = new ProgressDialog(this);
//        progressDialog.setTitle(getText(R.string.please_wait));
//        progressDialog.setCancelable(false);
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.create();
//        progressDialog.show();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                progressDialog.dismiss();
//            }
//        },2000);
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void insertUser() {

        HashMap<String,Object> user = new HashMap<>();
        user.put("id",firebaseAuth.getUid());
        user.put("name",Utils.Name);
        user.put("img",Utils.Image);
        db.collection("users").document(String.valueOf(firebaseAuth.getUid())).set(user);
    }

    private String format_date(Date date){
        return new SimpleDateFormat("dd,MMMM,yyyy - HH:mm", Locale.getDefault()).format(date);
    }
    private void sendMessage() {
        String edtMessage = binding.edtMessage.getText().toString().trim();
        if (TextUtils.isEmpty(edtMessage)){
            binding.edtMessage.getFocusable();

        }else {
            HashMap<String,Object> message = new HashMap<>();
            message.put(Utils.SENDID,String.valueOf(firebaseAuth.getUid()));
            message.put(Utils.RECEIVEDID,ID_Seller);
            message.put(Utils.MESS,edtMessage);
            message.put(Utils.DATETIME,new Date());
            message.put(Utils.IMAGE,Utils.Image);
            db.collection(Utils.PATH_CHAT).add(message);
            binding.edtMessage.setText("");
        }
    }
    private void listenMess(){

        db.collection(Utils.PATH_CHAT)
                .whereEqualTo(Utils.SENDID,String.valueOf(firebaseAuth.getUid()))
                .whereEqualTo(Utils.RECEIVEDID,ID_Seller)
                .addSnapshotListener(eventListener);
        db.collection(Utils.PATH_CHAT)
                .whereEqualTo(Utils.SENDID,ID_Seller)
                .whereEqualTo(Utils.RECEIVEDID,String.valueOf(firebaseAuth.getUid()))
                .addSnapshotListener(eventListener);

    }
    private final EventListener<QuerySnapshot> eventListener = (value,error)->{
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
            Collections.sort(chatMessageList,(obj1,obj2)-> obj1.dateObj.compareTo(obj2.dateObj));
            if (count == 0){
                chatUserAdapter.notifyDataSetChanged();
            }else {
                chatUserAdapter.notifyItemRangeInserted(chatMessageList.size(),chatMessageList.size());
                rcy_Chat.smoothScrollToPosition(chatMessageList.size()-1);


            }
        }
    };
    private void mapping() {

        rcy_Chat = findViewById(R.id.rcy_Chat);
        tvTitleToolbar = findViewById(R.id.tvTitleToolbar);
        ivToolbarLeft = findViewById(R.id.ivToolbarLeft);
        ivToolbarRight = findViewById(R.id.ivToolbarRight);
        db = FirebaseFirestore.getInstance();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rcy_Chat.setLayoutManager(layoutManager);
        rcy_Chat.setHasFixedSize(true);
        chatUserAdapter = new ChatUserAdapter(getApplicationContext(), chatMessageList,String.valueOf(firebaseAuth.getUid()));
        rcy_Chat.setAdapter(chatUserAdapter);
    }
}