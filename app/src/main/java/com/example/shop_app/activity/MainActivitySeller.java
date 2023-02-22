package com.example.shop_app.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.shop_app.R;
import com.example.shop_app.databinding.ActivityMainSellerBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivitySeller extends AppCompatActivity {
    private ActivityMainSellerBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMainSellerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home_seller, R.id.nav_order_seller, R.id.nav_category_seller,R.id.nav_account_seller)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main_seller);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }
}
