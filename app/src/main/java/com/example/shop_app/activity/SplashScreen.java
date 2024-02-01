package com.example.shop_app.activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.shop_app.R;
import com.example.shop_app.utils.Checkconnection;
import com.example.shop_app.utils.DatalocalManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SplashScreen extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        firebaseAuth = FirebaseAuth.getInstance();

        loadData();
    }
    private  void loadData(){
         if (Checkconnection.isNetworkAvaible(this)){
             //Network connected
             if(!DatalocalManager.getFirstInstalled()) {
                 new Handler().postDelayed(new Runnable() {
                     @Override
                     public void run() {
                         startActivity(new Intent(SplashScreen.this, OnboardingActivity.class));
                         DatalocalManager.setFirstInstalled(true);
                         finish();
                     }
                 }, 2000);
             }else if(!DatalocalManager.getFirstInstalled2()){
                 new Handler().postDelayed(new Runnable() {
                     @Override
                     public void run() {
                         FirebaseUser user = firebaseAuth.getCurrentUser();
                         if (user == null){
                             startActivity(new Intent(SplashScreen.this,LoginActivity.class));
                             finish();
                         }else {
                             DatalocalManager.setFirstInstalled2(true);
                             checkUserType();
                         }

                     }
                 }, 2000);
             }else {
                 new Handler().postDelayed(new Runnable() {
                     @Override
                     public void run() {
                         FirebaseUser user = firebaseAuth.getCurrentUser();
                         if (user == null){
                             startActivity(new Intent(SplashScreen.this,LoginActivity.class));
                             finish();
                         }else {
                             checkUserType();
                         }
                     }
                 }, 2000);
             }

         }else {
             Toast.makeText(this, ""+getText(R.string.network_disconect), Toast.LENGTH_SHORT).show();
         }
    }
    private void checkUserType() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()){
                            String accountType = ""+ds.child("accountType").getValue();
                            if (accountType.equals("User")) {
                                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                                finish();
                            }else {
                                startActivity(new Intent(SplashScreen.this,MainActivitySeller.class));
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}