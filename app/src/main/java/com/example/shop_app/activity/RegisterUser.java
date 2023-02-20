package com.example.shop_app.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shop_app.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class RegisterUser extends AppCompatActivity {

    EditText edt_Username,edt_Email,edt_Password,edt_Phone, edt_Address;

    TextView txt_Login;
    ImageView img_User,img_Register;
    private String[] locationPermissions;
    private String[] cameraPermissions;
    private String[] storagePermissions;

    private static final int CAMERA_REQUEST_CODE = 101;
    private static final int STORAGE_REQUEST_CODE = 102;
    private static final int IMAGE_PICK_GALLERY_CODE = 103;
    private static final int IMAGE_PICK_CAMERA_CODE = 104;

    private Uri image_uri;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        init();
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Vui lòng đợi");
        progressDialog.setCanceledOnTouchOutside(false);
        img_User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePickDialog();
            }
        });
        img_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputData();
            }
        });
        txt_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterUser.this,LoginActivity.class));
            }
        });




    }


    private String name, address, email, phone, password;
    private Boolean validateEmail() {
        String val = edt_Email.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (val.isEmpty()) {
            edt_Email.setError("Vui lòng nhập đầy đủ thông tin");
            edt_Email.requestFocus();
            return false;
        } else if (!val.matches(emailPattern)) {
            edt_Email.setError("Email không hợp lệ");
            return false;
        } else {
            edt_Email.setError(null);
            return true;
        }
    }


    private Boolean validateName() {
        String val = edt_Username.getText().toString();
        if (val.isEmpty()) {
            edt_Username.setError("Vui lòng nhập đầy đủ thông tin");
            edt_Username.requestFocus();
            return false;
        } else {
            edt_Username.setError(null);
            return true;
        }
    }
    private Boolean validateAddress() {
        String val = edt_Address.getText().toString();
        if ( val.isEmpty()) {
            edt_Address.setError("Vui lòng nhập đầy đủ thông tin");
            edt_Address.requestFocus();
            return false;

        } else {
            edt_Address.setError(null);
            return true;
        }
    }
    private Boolean validatePhone() {
        String val = edt_Phone.getText().toString();
        if (val.isEmpty()) {
            edt_Phone.setError("Vui lòng nhập đầy đủ thông tin");
            edt_Phone.requestFocus();
            return false;
        } else {
            edt_Phone.setError(null);
            return true;
        }
    }
    private Boolean validatePassword() {
        String val = edt_Password.getText().toString();
        String passwordVal = "^" +
                "(?=.*[@#$%^&+=])" +     // at least 1 special character
                "(?=\\S+$)" +            // no white spaces
                ".{6,}" +                // at least 4 characters
                "$";
        if (val.isEmpty()) {
            edt_Password.setError("Vui lòng nhập đầy đủ thông tin");
            edt_Password.requestFocus();
            return false;
        } else if (!val.matches(passwordVal)) {
            edt_Password.setError("Mật khẩu không hợp lệ \n" +
                    "Ít nhất 6 ký tự và có ít nhất 1 ký tự đặc biệt");
            return false;
        } else {
            edt_Password.setError(null);
            return true;
        }
    }
    private void inputData() {
        if (!validateName() | !validateEmail() | !validatePhone() | !validatePassword() | !validateAddress()) {
            return;
        }
        name = edt_Username.getText().toString().trim();
        address = edt_Address.getText().toString().trim();
        email = edt_Email.getText().toString().trim();
        phone = edt_Phone.getText().toString().trim();
        password = edt_Password.getText().toString().trim();
        createAccount();
    }
    private void createAccount() {
        progressDialog.setMessage("Đang tạo tài khoản");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        saveFireDatabase();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@androidx.annotation.NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterUser.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
    private void saveFireDatabase() {
        String timestamp = "" + System.currentTimeMillis();
        if (image_uri == null) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("uid", "" + firebaseAuth.getUid());
            hashMap.put("email", "" + email);
            hashMap.put("name", "" + name);
            hashMap.put("phone", "" + phone);
            hashMap.put("address", "" + address);
            hashMap.put("timestamp", "" + timestamp);
            hashMap.put("accountType", "User");
            hashMap.put("online", "true");
            hashMap.put("profileImage", "");

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(firebaseAuth.getUid()).setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(RegisterUser.this, "Tạo tài khoản thành công", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterUser.this, MainActivity.class));
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@androidx.annotation.NonNull Exception e) {
                            Toast.makeText(RegisterUser.this, "Tạo tài khoản thất bại", Toast.LENGTH_SHORT).show();
                        }
                    });

        } else {

            String filePathAndName = "profile_images/" + "" + firebaseAuth.getUid();

            StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
            storageReference.putFile(image_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful()) ;
                            Uri downloadImageUri = uriTask.getResult();

                            if (uriTask.isSuccessful()) {
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("uid", "" + firebaseAuth.getUid());
                                hashMap.put("email", "" + email);
                                hashMap.put("name", "" + name);
                                hashMap.put("phone", "" + phone);
                                hashMap.put("address", "" + address);
                                hashMap.put("timestamp", "" + timestamp);
                                hashMap.put("accountType", "User");
                                hashMap.put("online", "true");
                                hashMap.put("profileImage", "" + downloadImageUri);

                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                                reference.child(firebaseAuth.getUid()).setValue(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(RegisterUser.this, "Tạo thành công", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(RegisterUser.this, MainActivity.class));
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@androidx.annotation.NonNull Exception e) {
                                                Toast.makeText(RegisterUser.this, "Tạo thất bại", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@androidx.annotation.NonNull Exception e) {
                            Toast.makeText(RegisterUser.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("AAA",e.getMessage());
                        }
                    });
        }
    }

    private void showImagePickDialog() {
        String[] options = {"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chose Image")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            if (checkCameraPermission()) {
                                ImagePickFromCamera();
                            } else {
                                requestCameraPermission();
                            }
                        } else {
                            if (checkStoragePermission()) {
                                ImagePickFromGallery();
                            } else {
                                requestStoragePermission();
                            }
                        }
                    }
                }).show();
    }
    private void ImagePickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private boolean checkStoragePermission(){
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;

    }
    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }
    private void ImagePickFromCamera() {

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_Image Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Image Description");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }
    private boolean checkCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;

    }
    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){

            case CAMERA_REQUEST_CODE:{
                if (grantResults.length>0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted){
                        ImagePickFromCamera();
                    }
                    else{
                        Toast.makeText(this, "Camera permission is necessary.....", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE:{
                if (grantResults.length>0){
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted){
                        ImagePickFromGallery();
                    }
                    else{
                        Toast.makeText(this, "Storage permission is necessary.....", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode == IMAGE_PICK_GALLERY_CODE){
                image_uri = data.getData();
                img_User.setImageURI(image_uri);
            }else if (requestCode == IMAGE_PICK_CAMERA_CODE){
                img_User.setImageURI(image_uri);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    private void  init(){
        edt_Username = findViewById(R.id.edt_UserName);
        edt_Email = findViewById(R.id.edt_Email);
        edt_Password = findViewById(R.id.edt_Password);
        edt_Phone = findViewById(R.id.edt_Phone);
        edt_Address = findViewById(R.id.edt_Address);
        img_User = findViewById(R.id.img_User);
        img_Register = findViewById(R.id.img_Register);
        txt_Login = findViewById(R.id.txt_Login);
    }
}