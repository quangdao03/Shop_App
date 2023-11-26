package com.example.shop_app.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shop_app.R;
import com.example.shop_app.utils.Utils;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    ImageView img_Login,imgShowPassword;

    TextView txt_Create,forgotPass;
    EditText edt_Email,edt_Password;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private String email, password;
    Boolean ishowpass = false;
    RelativeLayout ll_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        edt_Email.setText("quangdao5320@gmail.com");
        edt_Password.setText("123456@");
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getText(R.string.please_wait));
        progressDialog.setCanceledOnTouchOutside(false);
        getSupportActionBar().hide();
        img_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });


        txt_Create.setPaintFlags(txt_Create.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txt_Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(LoginActivity.this,RegisterUser.class));
            }
        });

        imgShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId()==R.id.imgShowPassword){

                    if(edt_Password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                        //Show Password
                        imgShowPassword.setImageResource(R.drawable.ic_show);
                        edt_Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }
                    else{
                        imgShowPassword.setImageResource(R.drawable.ic_showpass);
                        //Hide Password
                        edt_Password.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    }
                }
            }
        });
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotPassword.class));
            }
        });
        ll_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideSoftKeyboard(LoginActivity.this);
            }
        });
    }
    private Boolean validateEmail(){
        String val = edt_Email.getText().toString().trim();
        if (val.isEmpty()){
            edt_Email.setError(getText(R.string.please_info));
            edt_Email.requestFocus();
            return false;
        }
        else {
            edt_Email.setError(null);
            return true;
        }
    }
    private Boolean validatePassword(){
        String val = edt_Password.getText().toString();

        if (val.isEmpty()){
            edt_Password.setError(getText(R.string.please_info));

            edt_Password.requestFocus();
            return false;
        }else {
            edt_Password.setError(null);
            return true;
        }
    }

    private void loginUser() {
        if (!validateEmail()|!validatePassword()){
            return;
        }
        email = edt_Email.getText().toString().trim();
        password = edt_Password.getText().toString().trim();
        progressDialog.setMessage(getText(R.string.login_user));
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        makeMeOnline();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void makeMeOnline() {
        progressDialog.setMessage(getText(R.string.login_user_success));
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("online", "true");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        checkUserType();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void checkUserType() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()){
                            String accountType = ""+ds.child("accountType").getValue();
                            if (accountType.equals("User")){
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            }else {
                                startActivity(new Intent(LoginActivity.this,MainActivitySeller.class));
                            }
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(LoginActivity.this, "Fail", Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void init(){
        img_Login = findViewById(R.id.img_Login);
        edt_Email = findViewById(R.id.edt_Email);
        edt_Password = findViewById(R.id.edt_Password);
        txt_Create = findViewById(R.id.txt_Create);
        imgShowPassword = findViewById(R.id.imgShowPassword);
        forgotPass = findViewById(R.id.forgotPass);
        ll_login = findViewById(R.id.ll_login);
    }
}