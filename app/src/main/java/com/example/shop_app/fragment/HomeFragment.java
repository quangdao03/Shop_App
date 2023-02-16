package com.example.shop_app.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.shop_app.R;
import com.example.shop_app.adapter.ListCategoryAdapter;
import com.example.shop_app.adapter.ListProductAdapter;
import com.example.shop_app.adapter.ListSPNew;
import com.example.shop_app.model.Category;
import com.example.shop_app.model.Product;
import com.example.shop_app.model.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class HomeFragment extends Fragment {
    private ImageSlider imageSlider;
    View view;
    ViewPager viewPager;
    Timer timer;
    ImageView ivToolbarLeft,ivToolbarRight;
    TextView tvTitleToolbar;
    int page_position = 0;
    RecyclerView rcyCategory;
    ListCategoryAdapter listCategoryAdapter;
    List<Category> categoryList = new ArrayList<>();

    RecyclerView rcyProduct,rcySpNew;
    ListProductAdapter listProductAdapter;
    List<Product> productList = new ArrayList<>();
    List<Product> sanPhamList1 = new ArrayList<>();
    ListSPNew listSPNew;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container,false);
        mapping();
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("Slider")
               .addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               for (DataSnapshot dataSnapshot:snapshot.getChildren())
                   slideModels.add(new SlideModel(dataSnapshot.child("url").getValue().toString(),ScaleTypes.FIT));
               imageSlider.setImageList(slideModels,ScaleTypes.FIT);
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {
               Toast.makeText(requireContext(), "Fail Slider", Toast.LENGTH_SHORT).show();
           }
        });

        ivToolbarLeft.setVisibility(View.GONE);
        tvTitleToolbar.setText(R.string.app_home);

        Category();
        SP();
        SPNew();
        return  view;
    }



    public void Category(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getActivity(), RecyclerView.HORIZONTAL, false);
        rcyCategory.setLayoutManager (linearLayoutManager);
        rcyCategory.setHasFixedSize(true);
        listCategoryAdapter = new ListCategoryAdapter(getActivity(),categoryList);
        rcyCategory.setAdapter(listCategoryAdapter);


            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Category");
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
    public void SP(){
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getActivity(), RecyclerView.HORIZONTAL, false);
//        rcyProduct.setLayoutManager (linearLayoutManager);
//        rcyProduct.setHasFixedSize(true);
//
//        listProductAdapter = new ListProductAdapter(getActivity(),productList);
//        rcyProduct.setAdapter(listProductAdapter);
//        productList.add(new Product(1,R.drawable.icon_hoa,"Vert Malachite","Rp 999.999","(999)"));
//        productList.add(new Product(2,R.drawable.icon_hoa,"Vert Malachite","Rp 999.999","(999)"));
//        productList.add(new Product(3,R.drawable.icon_hoa,"Vert Malachite","Rp 999.999","(999)"));
//        productList.add(new Product(4,R.drawable.icon_hoa,"Vert Malachite","Rp 999.999","(999)"));
//        productList.add(new Product(5,R.drawable.icon_hoa,"Vert Malachite","Rp 999.999","(999)"));
//        productList.add(new Product(6,R.drawable.icon_hoa,"Vert Malachite","Rp 999.999","(999)"));

    }
    public void SPNew(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getActivity(), RecyclerView.VERTICAL, false);
        rcySpNew.setLayoutManager (linearLayoutManager);
        rcySpNew.setHasFixedSize(true);

        listSPNew = new ListSPNew(getActivity(),sanPhamList1);
        rcySpNew.setAdapter(listSPNew);
//        sanPhamList1.add(new Product(1,R.drawable.fram1,"Essencia","Rp 999.999","(999)"));
//        sanPhamList1.add(new Product(2,R.drawable.fram2,"Sauvage","Rp 999.999","(999)"));
//        sanPhamList1.add(new Product(3,R.drawable.fram1,"Soothing","Rp 999.999","(999)"));

        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);

        rcySpNew.addItemDecoration(decoration);


    }

    public void mapping(){
        imageSlider = view.findViewById(R.id.imgSlider);
        ivToolbarLeft = view.findViewById(R.id.ivToolbarLeft);
        ivToolbarRight = view.findViewById(R.id.ivToolbarRight);
        tvTitleToolbar = view.findViewById(R.id.tvTitleToolbar);
        rcyCategory = view.findViewById(R.id.rcyCategory);
        rcyProduct = view.findViewById(R.id.rcyProduct);
        rcySpNew = view.findViewById(R.id.rcySpNew);
    }
}
