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

import com.bumptech.glide.Glide;
import com.example.shop_app.R;
import com.example.shop_app.databinding.ActivityEditProductSellerBinding;
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

public class EditProductSeller extends AppCompatActivity {
    private String productId;

    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;

    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;

    private String[] cameraPermissions;
    private String[] storagePermissions;

    private Uri image_uri;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    List<Category> categoryList = new ArrayList<>();
    ActivityEditProductSellerBinding binding;
    String url;
    String id;
    private String productTitle, productDescription, productCategory, productQuantity,
            originalPrice, creator, variant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        binding = ActivityEditProductSellerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Vui lòng đợi");
        progressDialog.setCanceledOnTouchOutside(false);
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        firebaseAuth = FirebaseAuth.getInstance();
        productId = getIntent().getStringExtra("productId");
        loadProductDetails();
        loadCategory();
        binding.toolbar.ivToolbarLeft.setImageResource(R.drawable.ic_left);
        hideView(binding.toolbar.ivToolbarRight);
        binding.toolbar.tvTitleToolbar.setText("Cập nhật sản phẩm");
        binding.toolbar.ivToolbarLeft.setOnClickListener(view -> {
            onBackPressed();
            finish();
        });
        binding.profileIv.setOnClickListener(view -> {
            showImagePickDialog();
        });
        binding.category.setOnClickListener(view -> {
            CategoryPickDialog();
        });
        binding.addProductBtn.setOnClickListener(view -> {
            inputData();
        });
    }

    private void loadCategory() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Categorys");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Category category = ds.getValue(Category.class);
                    categoryList.add(category);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadProductDetails() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Product");
        reference.child(productId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        binding.name.setText(dataSnapshot.child("name").getValue().toString());
                        url = dataSnapshot.child("image").getValue().toString();
                        Glide.with(getApplicationContext()).load(url).into(binding.profileIv);
                        binding.brand.setText(dataSnapshot.child("creator").getValue().toString());
                        binding.tvVariant.setText(dataSnapshot.child("variant").getValue().toString());
                        binding.desc.setText(dataSnapshot.child("desc").getValue().toString());
                        binding.priceEdt.setText(dataSnapshot.child("price").getValue().toString());
                        binding.quantity.setText(dataSnapshot.child("quantity").getValue().toString());
                        binding.category.setText(dataSnapshot.child("category").getValue().toString());
                        id = dataSnapshot.child("id").getValue().toString();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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
    private void CategoryPickDialog() {
        String[] categoriesArray = new String[categoryList.size()];
        for (int i = 0; i < categoryList.size(); i++) {
            categoriesArray[i] = categoryList.get(i).getName();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(EditProductSeller.this);
        builder.setTitle("Chọn danh mục")
                .setItems(categoriesArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String category = categoriesArray[which];

                        binding.category.setText(category);
                    }
                })
                .show();
    }
    private void inputData() {
        if (!validateName() | !validateDescription() | !validateQuantity() | !validatePrice() | !validateVariant() | !validateCreator()) {
            return;
        }
        productTitle = binding.name.getText().toString().trim();
        productDescription = binding.desc.getText().toString().trim();
        productCategory = binding.category.getText().toString().trim();
        productQuantity = binding.quantity.getText().toString().trim();
        originalPrice = binding.priceEdt.getText().toString().trim();
        creator = binding.brand.getText().toString().trim();
        variant = binding.tvVariant.getText().toString().trim();

        updateProduct();
    }

    private void updateProduct() {
        progressDialog.setMessage("Đang cập nhật sản phẩm");
        progressDialog.show();

        if (image_uri == null){
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("name",""+productTitle);
            hashMap.put("desc",""+productDescription);
            hashMap.put("category",""+productCategory);
            hashMap.put("quantity",""+productQuantity);
            hashMap.put("price",""+originalPrice);
            hashMap.put("creator",""+creator);
            hashMap.put("variant",""+variant);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Product");
            reference.child(productId).updateChildren(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            progressDialog.dismiss();
                            Toast.makeText(EditProductSeller.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(EditProductSeller.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else {
            String filePathAndName = "product_images/" + "" + productId;

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

                                hashMap.put("name",""+productTitle);
                                hashMap.put("desc",""+productDescription);
                                hashMap.put("category",""+productCategory);
                                hashMap.put("quantity",""+productQuantity);
                                hashMap.put("price",""+originalPrice);
                                hashMap.put("creator",""+creator);
                                hashMap.put("variant",""+variant);
                                hashMap.put("image",""+downloadImageUri);

                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Product");
                                reference.child(productId).updateChildren(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                progressDialog.dismiss();
                                                Toast.makeText(EditProductSeller.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                Toast.makeText(EditProductSeller.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditProductSeller.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private Boolean validateName() {
        String val = binding.name.getText().toString();
        if (val.isEmpty()) {
            binding.name.setError("Vui lòng nhập đầy đủ thông tin");
            binding.name.requestFocus();
            return false;
        } else {
            binding.name.setError(null);
            return true;
        }
    }

    private Boolean validateVariant() {
        String val = binding.tvVariant.getText().toString();
        if (val.isEmpty()) {
            binding.tvVariant.setError("Vui lòng nhập đầy đủ thông tin");
            binding.tvVariant.requestFocus();
            return false;
        } else {
            binding.tvVariant.setError(null);
            return true;
        }
    }

    private Boolean validateCreator() {
        String val = binding.brand.getText().toString();
        if (val.isEmpty()) {
            binding.brand.setError("Vui lòng nhập đầy đủ thông tin");
            binding.brand.requestFocus();
            return false;
        } else {
            binding.brand.setError(null);
            return true;
        }
    }

    private Boolean validateDescription() {
        String val = binding.desc.getText().toString();
        if (val.isEmpty()) {
            binding.desc.setError("Vui lòng nhập đầy đủ thông tin");
            binding.desc.requestFocus();
            return false;
        } else {
            binding.desc.setError(null);
            return true;
        }
    }

    private Boolean validateQuantity() {
        String val = binding.quantity.getText().toString();
        if (val.isEmpty()) {
            binding.quantity.setError("Vui lòng nhập đầy đủ thông tin");
            binding.quantity.requestFocus();
            return false;
        } else {
            binding.quantity.setError(null);

            return true;
        }
    }

    private Boolean validatePrice() {
        String val = binding.priceEdt.getText().toString();
        if (val.isEmpty()) {
            binding.priceEdt.setError("Vui lòng nhập đầy đủ thông tin");
            binding.priceEdt.requestFocus();
            return false;
        } else {
            binding.priceEdt.setError(null);
            return true;
        }
    }
}