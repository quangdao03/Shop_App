package com.example.shop_app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.shop_app.R;

public class FavoriteFragment extends Fragment {
    TextView tvTitleToolbar;
    ImageView ivToolbarLeft,ivToolbarRight;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.fragment_favorite, container,false);
        init();
        ivToolbarLeft.setVisibility(View.GONE);
        ivToolbarRight.setVisibility(View.GONE);
        tvTitleToolbar.setText("WishList");



        return view;
    }

    private void init() {
        tvTitleToolbar = view.findViewById(R.id.tvTitleToolbar);
        ivToolbarLeft = view.findViewById(R.id.ivToolbarLeft);
        ivToolbarRight = view.findViewById(R.id.ivToolbarRight);
    }
}
