package com.example.shop_app.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

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


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class HomeFragment extends Fragment {
    private ImageSlider imageSlider;
    View view;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    Timer timer;
    int page_position = 0;
    private int dotscount;
    private ImageView[] dots;
    private Integer[] images = {R.drawable.slide, R.drawable.slide, R.drawable.slide, R.drawable.slide, R.drawable.slide};
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
        imageSlider = view.findViewById(R.id.imgSlider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel("https://cdn.shopify.com/s/files/1/0554/5879/1593/products/last_frame.png?v=1648751161", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://media.cnn.com/api/v1/images/stellar/prod/220818135836-ariana-grande-bodycare-lead-image-cnnu.jpg?c=original", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://cdn.shopify.com/s/files/1/0026/2022/7637/collections/master-navigation-image-2500x1750-shop-haircare-collection-1.jpg?v=1666334710", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://tutinminhdep.com/wp-content/uploads/2020/10/nhung-dung-cu-trang-diem-can-thiet.jpg", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://www.elleman.vn/wp-content/uploads/2020/04/01/174970/bia_nuoc-hoa-nam-gioi_0420.jpg", ScaleTypes.FIT));
        imageSlider.setImageList(slideModels,ScaleTypes.FIT);
        mapping();
        Category();
        SP();
        SPNew();
        return  view;
    }
    public void scheduleSlider() {

        final Handler handler = new Handler();

        final Runnable update = new Runnable() {
            public void run() {
                if (page_position == dotscount) {
                    page_position = 0;
                } else {
                    page_position = page_position + 1;
                }
                viewPager.setCurrentItem(page_position, true);
            }
        };

        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 1000, 4000);
    }



    public void Category(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getActivity(), RecyclerView.HORIZONTAL, false);
        rcyCategory.setLayoutManager (linearLayoutManager);
        rcyCategory.setHasFixedSize(true);

        listCategoryAdapter = new ListCategoryAdapter(getActivity(),categoryList);
        rcyCategory.setAdapter(listCategoryAdapter);

        categoryList.add(new Category(R.drawable.ic_1,"Fragrance"));
        categoryList.add(new Category(R.drawable.ic_2,"Bodycare"));
        categoryList.add(new Category(R.drawable.ic_3,"Haircare"));
        categoryList.add(new Category(R.drawable.ic_4,"Facial"));
        categoryList.add(new Category(R.drawable.ic_5,"Makeup"));
        categoryList.add(new Category(R.drawable.ic_6,"Medicine"));
        categoryList.add(new Category(R.drawable.ic_7,"Men"));
        categoryList.add(new Category(R.drawable.ic_8,"Others"));


    }
    public void SP(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getActivity(), RecyclerView.HORIZONTAL, false);
        rcyProduct.setLayoutManager (linearLayoutManager);
        rcyProduct.setHasFixedSize(true);

        listProductAdapter = new ListProductAdapter(getActivity(),productList);
        rcyProduct.setAdapter(listProductAdapter);
        productList.add(new Product(R.drawable.icon_hoa,"Vert Malachite","Rp 999.999","(999)"));
        productList.add(new Product(R.drawable.icon_hoa,"Vert Malachite","Rp 999.999","(999)"));
        productList.add(new Product(R.drawable.icon_hoa,"Vert Malachite","Rp 999.999","(999)"));
        productList.add(new Product(R.drawable.icon_hoa,"Vert Malachite","Rp 999.999","(999)"));
        productList.add(new Product(R.drawable.icon_hoa,"Vert Malachite","Rp 999.999","(999)"));
        productList.add(new Product(R.drawable.icon_hoa,"Vert Malachite","Rp 999.999","(999)"));



    }
    public void SPNew(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getActivity(), RecyclerView.VERTICAL, false);
        rcySpNew.setLayoutManager (linearLayoutManager);
        rcySpNew.setHasFixedSize(true);

        listSPNew = new ListSPNew(getActivity(),sanPhamList1);
        rcySpNew.setAdapter(listSPNew);
        sanPhamList1.add(new Product(R.drawable.fram1,"Essencia","Rp 999.999","(999)"));
        sanPhamList1.add(new Product(R.drawable.fram2,"Sauvage","Rp 999.999","(999)"));
        sanPhamList1.add(new Product(R.drawable.fram1,"Soothing","Rp 999.999","(999)"));

        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);

        rcySpNew.addItemDecoration(decoration);


    }

    public void mapping(){
        rcyCategory = view.findViewById(R.id.rcyCategory);
        rcyProduct = view.findViewById(R.id.rcyProduct);
        rcySpNew = view.findViewById(R.id.rcySpNew);
    }
}
