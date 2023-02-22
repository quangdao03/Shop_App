package com.example.shop_app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.shop_app.activity.LoginActivity;
import com.example.shop_app.activity.MainActivity;
import com.example.shop_app.R;
import com.example.shop_app.utils.DatalocalManager;


public class OnboardingFragment3 extends Fragment {

private Button btnstart,btn_login;
private View mView;

    public OnboardingFragment3() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.onboading1, container, false);
        btnstart = mView.findViewById(R.id.started);
        btn_login = mView.findViewById(R.id.btn_login);
        btnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                DatalocalManager.setFirstInstalled(true);

                getActivity().startActivity(intent);
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                DatalocalManager.setFirstInstalled(true);

                getActivity().startActivity(intent);
            }
        });

        return mView;
    }
}