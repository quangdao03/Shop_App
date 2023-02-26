package com.example.shop_app.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shop_app.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    TextView tvTitleToolbar;
    ImageView ivToolbarLeft,ivToolbarRight;
    EditText edt_Email;
    Button btn_forgotPass;

    FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mapping();
        getSupportActionBar().hide();
        ivToolbarLeft.setImageResource(R.drawable.ic_left);
        ivToolbarRight.setVisibility(View.GONE);
        ivToolbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        tvTitleToolbar.setText(getText(R.string.forgot_pass));
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getText(R.string.please_wait));
        progressDialog.setCanceledOnTouchOutside(false);
        btn_forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recoverPassword();
            }
        });
    }
    private Boolean validateEmail(){
        String val = edt_Email.getText().toString();
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
    public String email = "";
    private void recoverPassword(){
        if (!validateEmail()){
            return;
        }
        email = edt_Email.getText().toString().trim();

        progressDialog.setMessage(getText(R.string.send_pass));
        progressDialog.show();

        firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(ForgotPassword.this, ""+getText(R.string.send_pass_success), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(ForgotPassword.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void mapping() {
        tvTitleToolbar = findViewById(R.id.tvTitleToolbar);
        ivToolbarLeft = findViewById(R.id.ivToolbarLeft);
        ivToolbarRight = findViewById(R.id.ivToolbarRight);
        edt_Email = findViewById(R.id.edt_Email);
        btn_forgotPass = findViewById(R.id.btn_forgotPass);
    }
}