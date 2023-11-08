package com.example.shop_app.dash.sellerdash;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shop_app.R;
import com.example.shop_app.adapter.ListCategoryAdapter;
import com.example.shop_app.adapter.ListCategoryAdapterSeller;
import com.example.shop_app.databinding.FragmentCategorySellerBinding;
import com.example.shop_app.model.Category;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragmentSeller extends Fragment {
    View view;
    FragmentCategorySellerBinding binding;
    List<Category> categoryList = new ArrayList<>();
    ListCategoryAdapterSeller listCategoryAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_category_seller, container,false);
        binding = FragmentCategorySellerBinding.inflate(getLayoutInflater());
        binding.toolbar.ivToolbarRight.setImageResource(R.drawable.icons8_add);
        viewClick();
        loadCategory();

        return  binding.getRoot();
    }

    private void loadCategory() {
        GridLayoutManager linearLayoutManager = new GridLayoutManager( getActivity(),2, RecyclerView.VERTICAL, false);
        binding.rcyCategory.setLayoutManager (linearLayoutManager);
        binding.rcyCategory.setHasFixedSize(true);
        listCategoryAdapter = new ListCategoryAdapterSeller(getActivity(),categoryList);
        binding.rcyCategory.setAdapter(listCategoryAdapter);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Categorys");
        myRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (categoryList!= null){
                    categoryList.clear();
                }
                for (DataSnapshot getSnapshot: dataSnapshot.getChildren()) {

                    Category category = getSnapshot.getValue(Category.class);
                    category.setImage(getSnapshot.child("url").getValue().toString());
                    category.setName(getSnapshot.child("name").getValue().toString());
                    categoryList.add(category);

                }
                listCategoryAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Get Fail Category", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void viewClick() {

    }
}
