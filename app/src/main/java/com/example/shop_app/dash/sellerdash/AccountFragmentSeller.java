package com.example.shop_app.dash.sellerdash;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.shop_app.R;
import com.example.shop_app.activity.About;
import com.example.shop_app.activity.EditUser;
import com.example.shop_app.activity.LoginActivity;
import com.example.shop_app.activity.Setting;
import com.example.shop_app.activity.StatisticalActivity;
import com.example.shop_app.databinding.FragmentAccountSellerBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AccountFragmentSeller extends Fragment {
    View view;
    LinearLayout ll_logout, ll_Address, ln_Statistical;
    TextView tvTitleToolbar, txt_username, txt_userphone, txt_email;
    ImageView ivToolbarLeft, ivToolbarRight, userImage_ImageView;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    FragmentAccountSellerBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAccountSellerBinding.inflate(getLayoutInflater());
        view = inflater.inflate(R.layout.fragment_account_seller, container, false);
        init();
        ivToolbarLeft.setVisibility(View.GONE);
        ivToolbarRight.setImageResource(R.drawable.icon_settings__style);
        tvTitleToolbar.setText(getString(R.string.profile));
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle(getString(R.string.please_wait));
        progressDialog.setCanceledOnTouchOutside(false);
        checkUser();
        ll_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });
        ln_Statistical.setOnClickListener(view1 -> {
            startActivity(new Intent(getContext(), StatisticalActivity.class));
        });
        binding.llAddress.setOnClickListener(view1 -> {
            startActivity(new Intent(requireContext(), EditUser.class));
        });
        binding.llSetting.setOnClickListener(view1 -> {
            startActivity(new Intent(requireContext(), Setting.class));
        });
        binding.toolbar.ivToolbarRight.setOnClickListener(view1 -> {
            startActivity(new Intent(requireContext(), Setting.class));
        });
        binding.lnAbout.setOnClickListener(view1 -> {
            startActivity(new Intent(requireContext(), About.class));
        });
        return binding.getRoot();
    }

    private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            requireActivity().finishAffinity();
        } else {
            loadMyInfo();
        }
    }

    private void loadMyInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String name = "" + dataSnapshot.child("name").getValue();
                            String email = "" + dataSnapshot.child("email").getValue();
                            String phone = "" + dataSnapshot.child("phone").getValue();
                            String profileImage = "" + dataSnapshot.child("profileImage").getValue();
                            String accountType = "" + dataSnapshot.child("accountType").getValue();

                            txt_username.setText(name);
                            txt_email.setText(email);
                            txt_userphone.setText(phone);
                            try {
                                Glide.with(getActivity()).load(profileImage).placeholder(R.drawable.profile).into(userImage_ImageView);
                            } catch (Exception e) {
                                userImage_ImageView.setImageResource(R.drawable.profile);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void logOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setMessage(getString(R.string.log_out));

        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                makeMeOffline();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void makeMeOffline() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("online", "false");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        firebaseAuth.signOut();
                        checkUser();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void init() {
        ln_Statistical = binding.lnStatistical;
        ll_logout = binding.llLogout;
        ll_Address = binding.llAddress;
        tvTitleToolbar = binding.toolbar.tvTitleToolbar;
        ivToolbarLeft = binding.toolbar.ivToolbarLeft;
        ivToolbarRight = binding.toolbar.ivToolbarRight;
        txt_username = binding.txtUsername;
        txt_email = binding.txtEmail;
        txt_userphone = binding.txtUserphone;
        userImage_ImageView = binding.userImageImageView;
    }
}
