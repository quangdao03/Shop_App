package com.example.shop_app.activity;

import static com.example.shop_app.utils.Utility.hideView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.shop_app.R;
import com.example.shop_app.databinding.ActivityAddProductSellerBinding;
import com.example.shop_app.model.Category;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddProductSeller extends AppCompatActivity {

    ActivityAddProductSellerBinding binding;

    private Uri image_uri;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;

    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;
    private String[] cameraPermissions;
    private String[] storagePermissions;

    List<Category> categoryList = new ArrayList<>();

    private String productTitle, productDescription, productCategory, productQuantity,
            originalPrice ,creator,variant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        binding = ActivityAddProductSellerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Vui lòng đợi");
        progressDialog.setCanceledOnTouchOutside(false);
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        firebaseAuth = FirebaseAuth.getInstance();
        loadCategory();

        viewClick();
    }

    private void  loadCategory(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Categorys");
        reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        categoryList.clear();
                        for (DataSnapshot ds: snapshot.getChildren()){
                            Category category = ds.getValue(Category.class);
                            categoryList.add(category);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    private void  CategoryPickDialog(){
        String[] categoriesArray = new String[categoryList.size()];
        for (int i = 0; i < categoryList.size(); i++){
            categoriesArray[i] = categoryList.get(i).getName();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(AddProductSeller.this);
        builder.setTitle("Chọn danh mục")
                .setItems(categoriesArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String category = categoriesArray[which];

                        binding.categoryEdt.setText(category);
                    }
                })
                .show();
    }
    private void viewClick() {
        binding.toolbar.ivToolbarLeft.setImageResource(R.drawable.ic_left);
        hideView(binding.toolbar.ivToolbarRight);
        binding.toolbar.tvTitleToolbar.setText("Add product");
        binding.toolbar.ivToolbarLeft.setOnClickListener(view -> {
            onBackPressed();
            finish();
        });
        binding.profileIv.setOnClickListener(view -> {
            showImagePickDialog();
        });
        binding.categoryEdt.setOnClickListener(view -> {
            CategoryPickDialog();
        });
        binding.addProductBtn.setOnClickListener(view -> {
            inputData();
        });
    }
    private void inputData() {
        if (!validateName() | !validateDescription() | !validateQuantity() | !validatePrice() |!validateVariant() |!validateCreator()){
            return;
        }
        productTitle = binding.titleEdt.getEditText().getText().toString().trim();
        productDescription = binding.descriptionEdt.getEditText().getText().toString().trim();
        productCategory = binding.categoryEdt.getText().toString().trim();
        productQuantity = binding.quantityEdt.getEditText().getText().toString().trim();
        originalPrice = binding.priceEdt.getEditText().getText().toString().trim();
        creator = binding.titleEdtCreator.getEditText().getText().toString().trim();
        variant = binding.tvVariant.getEditText().getText().toString().trim();

        addProduct();
    }
    private void addProduct() {
        progressDialog.setMessage("Đang thêm sản phẩm");
        progressDialog.show();
        String timestamp = ""+System.currentTimeMillis();
        if (image_uri == null){
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("id",""+timestamp);
            hashMap.put("name",""+productTitle);
            hashMap.put("image","");
            hashMap.put("desc",""+productDescription);
            hashMap.put("category",""+productCategory);
            hashMap.put("quantity",""+productQuantity);
            hashMap.put("price",""+originalPrice);
            hashMap.put("timestamp",""+timestamp);
            hashMap.put("creator",""+creator);
            hashMap.put("variant",variant);
            hashMap.put("favourite",false);
            hashMap.put("rate",0);
            hashMap.put("uid",""+firebaseAuth.getUid());


            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Product");
            reference.child(timestamp).setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            progressDialog.dismiss();
                            Toast.makeText(AddProductSeller.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
//                            clearData();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddProductSeller.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else {
            String filePathAndName = "product_image/" + "" + firebaseAuth.getUid();

            StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
            storageReference.putFile(image_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful());
                            Uri downloadImageUri = uriTask.getResult();
                            if (uriTask.isSuccessful()){
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("id",""+timestamp);
                                hashMap.put("name",""+productTitle);
                                hashMap.put("image",""+downloadImageUri);
                                hashMap.put("desc",""+productDescription);
                                hashMap.put("category",""+productCategory);
                                hashMap.put("quantity",""+productQuantity);
                                hashMap.put("price",""+originalPrice);
                                hashMap.put("timestamp",""+timestamp);
                                hashMap.put("creator",""+creator);
                                hashMap.put("variant",variant);
                                hashMap.put("favourite",false);
                                hashMap.put("rate",0);
                                hashMap.put("uid",""+firebaseAuth.getUid());

                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Product");
                                reference.child(timestamp).setValue(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                progressDialog.dismiss();
                                                Toast.makeText(AddProductSeller.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
//                                                clearData();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                Toast.makeText(AddProductSeller.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddProductSeller.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    private Boolean validateName(){
        String val = binding.titleEdt.getEditText().getText().toString();
        if (val.isEmpty()){
            binding.titleEdt.setError("Vui lòng nhập đầy đủ thông tin");
            binding.titleEdt.requestFocus();
            return false;
        }else {
            binding.titleEdt.setError(null);
            binding.titleEdt.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validateVariant(){
        String val = binding.tvVariant.getEditText().getText().toString();
        if (val.isEmpty()){
            binding.tvVariant.setError("Vui lòng nhập đầy đủ thông tin");
            binding.tvVariant.requestFocus();
            return false;
        }else {
            binding.tvVariant.setError(null);
            binding.tvVariant.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validateCreator(){
        String val = binding.titleEdtCreator.getEditText().getText().toString();
        if (val.isEmpty()){
            binding.titleEdtCreator.setError("Vui lòng nhập đầy đủ thông tin");
            binding.titleEdtCreator.requestFocus();
            return false;
        }else {
            binding.titleEdtCreator.setError(null);
            binding.titleEdtCreator.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validateDescription(){
        String val = binding.descriptionEdt.getEditText().getText().toString();
        if (val.isEmpty()){
            binding.descriptionEdt.setError("Vui lòng nhập đầy đủ thông tin");
            binding.descriptionEdt.requestFocus();
            return false;
        }else {
            binding.descriptionEdt.setError(null);
            binding.descriptionEdt.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validateQuantity(){
        String val = binding.quantityEdt.getEditText().getText().toString();
        if (val.isEmpty()){
            binding.quantityEdt.setError("Vui lòng nhập đầy đủ thông tin");
            binding.quantityEdt.requestFocus();
            return false;
        }else {
            binding.quantityEdt.setError(null);
            binding.quantityEdt.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validatePrice(){
        String val = binding.priceEdt.getEditText().getText().toString();
        if (val.isEmpty()){
            binding.priceEdt.setError("Vui lòng nhập đầy đủ thông tin");
            binding.priceEdt.requestFocus();
            return false;
        }else {
            binding.priceEdt.setError(null);
            binding.priceEdt.setErrorEnabled(false);
            return true;
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

    private void ImagePickFromCamera() {

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_Image Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Image Description");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;

    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;

    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted) {
                        ImagePickFromCamera();
                    } else {
                        Toast.makeText(this, "Camera permission is necessary.....", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        ImagePickFromGallery();
                    } else {
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
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                image_uri = data.getData();
                binding.profileIv.setImageURI(image_uri);
            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                binding.profileIv.setImageURI(image_uri);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}