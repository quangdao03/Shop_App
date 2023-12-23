package com.example.shop_app.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shop_app.R;
import com.example.shop_app.utils.CustomToast;
import com.example.shop_app.utils.SystemUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {
    TextView tvTitleToolbar;
    ImageView ivToolbarLeft,ivToolbarRight,imgShowPassword,showpassNew;
    EditText edt_PasswordNew,edt_PasswordOld;
    Button btn_ChangePass;
    private ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SystemUtil.setLocale(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_changepassword);
        mapping();
        getSupportActionBar().hide();
        tvTitleToolbar.setText(getString(R.string.change_pass));
        ivToolbarLeft.setImageResource(R.drawable.ic_left);
        ivToolbarRight.setVisibility(View.GONE);
        ivToolbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getString(R.string.please_wait));
        progressDialog.setCanceledOnTouchOutside(false);
        btn_ChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPass = edt_PasswordOld.getText().toString().trim();
                String newPass = edt_PasswordNew.getText().toString().trim();
                if (TextUtils.isEmpty(oldPass)){
                    Toast.makeText(ChangePassword.this, ""+getText(R.string.enter_pass), Toast.LENGTH_SHORT).show();
                    edt_PasswordOld.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(newPass)){
                    edt_PasswordNew.requestFocus();
                    Toast.makeText(ChangePassword.this, ""+getText(R.string.pass_must), Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
                updatePassword(oldPass,newPass);
            }
        });
        imgShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId()==R.id.imgShowPassword){

                    if(edt_PasswordOld.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                        //Show Password
                        imgShowPassword.setImageResource(R.drawable.ic_show);
                        edt_PasswordOld.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }
                    else{
                        imgShowPassword.setImageResource(R.drawable.ic_showpass);
                        //Hide Password
                        edt_PasswordOld.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    }
                }
            }
        });
        showpassNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId()==R.id.showpassNew){

                    if(edt_PasswordNew.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                        //Show Password
                        showpassNew.setImageResource(R.drawable.ic_show);
                        edt_PasswordNew.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }
                    else{
                        showpassNew.setImageResource(R.drawable.ic_showpass);
                        //Hide Password
                        edt_PasswordNew.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    }
                }
            }
        });

    }

    private void updatePassword(String oldPass, String newPass) {
        progressDialog.show();
        progressDialog.setMessage(getText(R.string.update_pass));
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        AuthCredential authCredential = EmailAuthProvider.getCredential(firebaseUser.getEmail(),oldPass);
        firebaseUser.reauthenticate(authCredential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        firebaseUser.updatePassword(newPass)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        progressDialog.dismiss();
                                        Toast.makeText(ChangePassword.this, ""+getText(R.string.update_pass_success), Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(ChangePassword.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(ChangePassword.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void mapping() {
        tvTitleToolbar = findViewById(R.id.tvTitleToolbar);
        ivToolbarLeft = findViewById(R.id.ivToolbarLeft);
        ivToolbarRight = findViewById(R.id.ivToolbarRight);
        btn_ChangePass = findViewById(R.id.btn_ChangePass);
        edt_PasswordNew = findViewById(R.id.edt_PasswordNew);
        edt_PasswordOld = findViewById(R.id.edt_PasswordOld);
        imgShowPassword = findViewById(R.id.imgShowPassword);
        showpassNew = findViewById(R.id.showpassNew);
    }
}
