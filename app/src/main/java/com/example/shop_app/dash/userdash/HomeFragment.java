package com.example.shop_app.dash.userdash;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
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
import com.example.shop_app.activity.ChatActivityUser;
import com.example.shop_app.activity.LoginActivity;
import com.example.shop_app.activity.MainActivity;
import com.example.shop_app.activity.MainActivitySeller;
import com.example.shop_app.activity.Product_All;
import com.example.shop_app.adapter.ListCategoryAdapter;
import com.example.shop_app.adapter.ListProductNew;
import com.example.shop_app.adapter.ProductAdapter;
import com.example.shop_app.model.Category;
import com.example.shop_app.model.Product;

import com.example.shop_app.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;



import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;


public class HomeFragment extends Fragment {
    private ImageSlider imageSlider;
    View view;
    ViewPager viewPager;
    Timer timer;
    ImageView ivToolbarLeft,ivToolbarRight,ivToolbarRight_message;
    TextView tvTitleToolbar,tv_ViewAll;
    private FirebaseAuth firebaseAuth;
    int page_position = 0;
    RecyclerView rcyCategory;
    ListCategoryAdapter listCategoryAdapter;
    List<Category> categoryList = new ArrayList<>();

    RecyclerView rcyProduct,rcySpNew;
    ProductAdapter listProductAdapter;
    List<Product> productList = new ArrayList<>();
    List<Product> productNewList = new ArrayList<>();
    ListProductNew listProductNew;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container,false);
        mapping();
        firebaseAuth = FirebaseAuth.getInstance();
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
        ProductHome();
        ProductNew();
        tv_ViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Product_All.class));
            }
        });
        ivToolbarRight_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ChatActivityUser.class));
            }
        });
        checkUserType();
        getObjectUser();
        return  view;
    }





    public void Category(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getActivity(), RecyclerView.HORIZONTAL, false);
        rcyCategory.setLayoutManager (linearLayoutManager);
        rcyCategory.setHasFixedSize(true);
        listCategoryAdapter = new ListCategoryAdapter(getActivity(),categoryList);
        rcyCategory.setAdapter(listCategoryAdapter);


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
    public void ProductHome(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getActivity(), RecyclerView.HORIZONTAL, false);
        rcyProduct.setLayoutManager (linearLayoutManager);
        rcyProduct.setHasFixedSize(true);

        listProductAdapter = new ProductAdapter(getActivity(),productList);
        rcyProduct.setAdapter(listProductAdapter);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myProdcut = database.getReference("Product");
        Query query = myProdcut.orderByChild("rate").startAfter(3);
        query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (productList != null){
                    productList.clear();
                }

                for (DataSnapshot getData : dataSnapshot.getChildren()){
//                    Product product = getData.getValue(Product.class);
                    Product product = new Product();
                    product.setUrl(getData.child("image").getValue().toString());
                    product.setName(getData.child("name").getValue().toString());
                    product.setPrice(getData.child("price").getValue().toString()+" $");
                    String quantity = "";
                    quantity = getData.child("quantity").getValue().toString();
                    product.setQuantity("("+quantity+")");
                    Log.d("AAA",""+getData);
                    productList.add(product);

                }
                listProductAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void ProductNew(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getActivity(), RecyclerView.VERTICAL, false);
        rcySpNew.setLayoutManager (linearLayoutManager);
        rcySpNew.setHasFixedSize(true);

        listProductNew = new ListProductNew(getActivity(),productNewList);
        rcySpNew.setAdapter(listProductNew);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);

        rcySpNew.addItemDecoration(decoration);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myProdcut = database.getReference("Product");

        //Lọc 2 phẩn tử đầu tiên của bảng dùng Query và limit
        Query query = myProdcut.limitToFirst(2);
//        Query query1 = myProdcut.child("uid").equalTo(firebaseAuth.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (productNewList != null){
                    productNewList.clear();
                }

                for (DataSnapshot getData : dataSnapshot.getChildren()){
//                    Product product = getData.getValue(Product.class);
                    Product product = new Product();
                    product.setUrl(getData.child("image").getValue().toString());
                    product.setName(getData.child("name").getValue().toString());
                    product.setPrice(getData.child("price").getValue().toString()+ " $");
                    String quantity = "";
                    quantity = getData.child("quantity").getValue().toString();
                    product.setQuantity("("+quantity+")");
                    productNewList.add(product);

                }
                listProductNew.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void checkUserType() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.orderByChild("uid")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()){
                            String accountType = ""+ds.child("accountType").getValue();
                            if (accountType.equals("Seller")){
                                Utils.SELLERID = ds.getKey();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
    private void getObjectUser() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            String name = ""+dataSnapshot.child("name").getValue();
                            Utils.Name = name;
                            String image = ""+dataSnapshot.child("profileImage").getValue();
                            Utils.Image = image;
                        }
                    }

                    @Override
                    public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                    }
                });
    }



    public void mapping(){
        imageSlider = view.findViewById(R.id.imgSlider);
        ivToolbarLeft = view.findViewById(R.id.ivToolbarLeft);
        ivToolbarRight = view.findViewById(R.id.ivToolbarRight);
        tvTitleToolbar = view.findViewById(R.id.tvTitleToolbar);
        rcyCategory = view.findViewById(R.id.rcyCategory);
        rcyProduct = view.findViewById(R.id.rcyProduct);
        rcySpNew = view.findViewById(R.id.rcySpNew);
        tv_ViewAll = view.findViewById(R.id.tv_ViewAll);
        ivToolbarRight_message = view.findViewById(R.id.ivToolbarRight_message);
    }
}
