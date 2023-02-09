package com.example.shop_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Realtime extends AppCompatActivity {

    EditText etText;
    Button btnAdd,btnDelete,btnUpdate;
    TextView txtGet;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime);

        Button btnGet = findViewById(R.id.btnGet);
        etText = findViewById(R.id.etText);
        txtGet = findViewById(R.id.txtGet);
        btnAdd = findViewById(R.id.btnAdd);
        btnDelete = findViewById(R.id.btnDelete);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickData();
            }
        });

        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReadData();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickUpdate();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickDelete();
            }
        });
    }

    private void onClickUpdate() {
        // cach 1 update data
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("my_map");
//
//        Map<String,Boolean> map = new HashMap<>();
//        map.put("1",false);
//        map.put("2",false);
//        map.put("3",false);
//        map.put("4",false);
//
//        myRef.setValue(map, new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(@Nullable DatabaseError error, @androidx.annotation.NonNull DatabaseReference ref) {
//                Toast.makeText(Realtime.this, "Cập nhập dữ liệu thành công", Toast.LENGTH_SHORT).show();
//            }
//        });

        //cach 2 update du lieu tung object
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("my_map");


//        myRef.setValue(true, new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(@Nullable DatabaseError error, @androidx.annotation.NonNull DatabaseReference ref) {
//                Toast.makeText(Realtime.this, "Cập nhập dữ liệu thành công", Toast.LENGTH_SHORT).show();
//            }
//        });

//        myRef.child("2").setValue(true, new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(@Nullable DatabaseError error, @androidx.annotation.NonNull DatabaseReference ref) {
//                Toast.makeText(Realtime.this, "Cập nhập dữ liệu thành công", Toast.LENGTH_SHORT).show();
//            }
//        });



        // cach 3 update du lieu nhieu item
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("my_map");
        Map<String,Object> mapUpdate = new HashMap<>();
        mapUpdate.put("1",false);
        mapUpdate.put("2",false);

        myRef.updateChildren(mapUpdate, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @androidx.annotation.NonNull DatabaseReference ref) {
                Toast.makeText(Realtime.this, "Cập nhập dữ liệu thành công", Toast.LENGTH_SHORT).show();
            }
        });





    }

    private void onClickDelete() {


//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("chanel");
//        myRef.removeValue(new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(@Nullable DatabaseError error, @androidx.annotation.NonNull DatabaseReference ref) {
//                Toast.makeText(Realtime.this, "Xóa dữ liệu thành công", Toast.LENGTH_SHORT).show();
//            }
//        });

        new AlertDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setMessage("Ban co chac chan xoa khong")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("my_map/1");


                        myRef.removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @androidx.annotation.NonNull DatabaseReference ref) {
                                Toast.makeText(Realtime.this, "Xóa dữ liệu thành công", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Khong",null)
                .show();

    }

    private void onClickData() {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("my_map");

        Map<String,Boolean> map = new HashMap<>();
        map.put("1",true);
        map.put("2",false);
        map.put("3",true);
        map.put("4",false);

        myRef.setValue(map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @androidx.annotation.NonNull DatabaseReference ref) {
                Toast.makeText(Realtime.this, "Cập nhập dữ liệu thành công", Toast.LENGTH_SHORT).show();
            }
        });


//        myRef.setValue(etText.getText().toString().trim(), new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(@Nullable DatabaseError error, @androidx.annotation.NonNull DatabaseReference ref) {
//                Toast.makeText(Realtime.this, "Cập nhập dữ liệu thành công", Toast.LENGTH_SHORT).show();
//            }
//        });



//        DatabaseReference myRef1 = database.getReference("chanel1");
//        myRef1.setValue(true);
//        DatabaseReference myRef2 = database.getReference("chanel2");
//        myRef2.setValue("Đạo 2");

    }
    private void ReadData(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("my_map");
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Map<String,Boolean> mapResult = new HashMap<>();
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    String key = dataSnapshot1.getKey();
                    Boolean value = dataSnapshot1.getValue(Boolean.class);
                    mapResult.put(key,value);
                }
                                txtGet.setText(mapResult.toString());

//                String value = dataSnapshot.getValue(String.class);
//                txtGet.setText(value);
//                Toast.makeText(Realtime.this, ""+value, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value

            }
        });
    }
}