package com.example.shop_app.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shop_app.R;
import com.example.shop_app.utils.LocaleHelper;
import com.example.shop_app.utils.SystemUtil;

import java.util.Locale;

public class ChangeLanguage extends AppCompatActivity {
    TextView tvTitleToolbar;
    ImageView ivToolbarLeft,ivToolbarRight;
    Button btn_Vie,btn_En;
    Context context;
    Resources resources;
    String codeLang;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_language);
        mapping();
        getSupportActionBar().hide();
        tvTitleToolbar.setText(R.string.chang_language);
        codeLang = Locale.getDefault().getLanguage();
        btn_Vie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                context = LocaleHelper.setLocale(ChangeLanguage.this, "vi");
//                resources = context.getResources();
//                btn_Vie.setText(resources.getString(R.string.vie));
                SystemUtil.saveLocale(getBaseContext(), "vi");
                dialog();
            }
        });
        btn_En.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                context = LocaleHelper.setLocale(ChangeLanguage.this, "en-rGB");
//                resources = context.getResources();
                SystemUtil.saveLocale(getBaseContext(), "en");
                dialog();
            }
        });
        ivToolbarLeft.setImageResource(R.drawable.ic_left);
        ivToolbarRight.setVisibility(View.GONE);
        ivToolbarLeft.setOnClickListener(view -> {
            onBackPressed();
            finish();
        });
    }

    private void dialog(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_dialog);
        dialog.getWindow().getAttributes().windowAnimations = androidx.appcompat.R.style.Animation_AppCompat_DropDownUp;
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        TextView message = dialog.findViewById(R.id.message);
        TextView btnAction = dialog.findViewById(R.id.btnAction);
        message.setText(R.string.confirm_success);
        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChangeLanguage.this,MainActivity.class));
                finishAffinity();
            }
        });
        dialog.show();
    }
    private void mapping() {
        tvTitleToolbar = findViewById(R.id.tvTitleToolbar);
        ivToolbarLeft = findViewById(R.id.ivToolbarLeft);
        ivToolbarRight = findViewById(R.id.ivToolbarRight);
        btn_Vie = findViewById(R.id.btn_Vie);
        btn_En = findViewById(R.id.btn_En);

    }
}
