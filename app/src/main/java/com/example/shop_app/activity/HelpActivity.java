package com.example.shop_app.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shop_app.R;
import com.example.shop_app.databinding.ActivityHelpBinding;
import com.example.shop_app.utils.CustomToast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class HelpActivity extends AppCompatActivity {
    ActivityHelpBinding binding;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHelpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        getSupportActionBar().hide();
        loadMyInfo();
        binding.toolbar.ivToolbarLeft.setImageResource(R.drawable.ic_left);
        binding.toolbar.ivToolbarRight.setVisibility(View.GONE);
        binding.toolbar.tvTitleToolbar.setText(getText(R.string.help));
        binding.toolbar.ivToolbarLeft.setOnClickListener(view -> {
            onBackPressed();
            finish();
        });
        binding.btnSubmit.setOnClickListener(view -> {
            checkInput();
        });
    }
    private void checkInput(){
        if (!validateName() | !validateEmail() | !validatePhone() | !validateComment()) {
            return;
        }
        CreateComment();
    }
    private void CreateComment(){
        String timestamp = "" + System.currentTimeMillis();
        Map<String, Object> mapUpdate = new HashMap<>();
        mapUpdate.put("uid", firebaseAuth.getUid());
        mapUpdate.put("id",timestamp);
        mapUpdate.put("name", binding.edtUserName.getText().toString().trim());
        mapUpdate.put("phone", binding.edtPhone.getText().toString().trim());
        mapUpdate.put("email", binding.edtEmail.getText().toString().trim());
        mapUpdate.put("comment", binding.edtComment.getText().toString().trim());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comments");
        reference.child(firebaseAuth.getUid()).child(timestamp).setValue(mapUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                textDefault();
                Toast.makeText(HelpActivity.this, "Your request has been sent", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HelpActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private Boolean validateEmail() {
        String val = binding.edtEmail.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (val.isEmpty()) {
            binding.edtEmail.setError(getText(R.string.please_info));
            binding.edtEmail.requestFocus();
            return false;
        } else if (!val.matches(emailPattern)) {
            binding.edtEmail.setError(getText(R.string.invalid_email));
            return false;
        } else {
            binding.edtEmail.setError(null);
            return true;
        }
    }


    private Boolean validateName() {
        String val = binding.edtUserName.getText().toString();
        if (val.isEmpty()) {
            binding.edtUserName.setError(getText(R.string.please_info));
            binding.edtUserName.requestFocus();
            return false;
        } else {
            binding.edtUserName.setError(null);
            return true;
        }
    }
    private Boolean validatePhone() {
        String val = binding.edtPhone.getText().toString();
        if (val.isEmpty()) {
            binding.edtPhone.setError(getText(R.string.please_info));
            binding.edtPhone.requestFocus();
            return false;
        } else {
            binding.edtPhone.setError(null);
            return true;
        }
    }
    private Boolean validateComment() {
        String val = binding.edtComment.getText().toString();
        if (val.isEmpty()) {
            binding.edtComment.setError(getText(R.string.please_info));
            binding.edtComment.requestFocus();
            return false;
        } else {
            binding.edtComment.setError(null);
            return true;
        }
    }
    private void textDefault(){
        binding.edtComment.setText("");
    }
    private void loadMyInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            String name = ""+dataSnapshot.child("name").getValue();
                            String email = ""+dataSnapshot.child("email").getValue();
                            String phone = ""+dataSnapshot.child("phone").getValue();
                            binding.edtUserName.setText(name);
                            binding.edtEmail.setText(email);
                            binding.edtPhone.setText(phone);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}