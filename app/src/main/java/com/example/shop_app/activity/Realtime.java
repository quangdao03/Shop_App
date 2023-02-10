package com.example.shop_app.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shop_app.R;
import com.example.shop_app.adapter.RealtimeAdapter;
import com.example.shop_app.model.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Realtime extends AppCompatActivity {

    EditText etText,etTextName;
    Button btnAdd,btnDelete,btnUpdate,btnUser;
    TextView txtGet;
    RecyclerView rcyUser;

    private  List<User> mListUser = new ArrayList<>();

    RealtimeAdapter adapter;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime);
        Button btnGet = findViewById(R.id.btnGet);
        etText = findViewById(R.id.etText);
        etTextName = findViewById(R.id.etTextName);
        txtGet = findViewById(R.id.txtGet);
        btnAdd = findViewById(R.id.btnAdd);
        btnDelete = findViewById(R.id.btnDelete);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnUser = findViewById(R.id.btnUser);
        rcyUser = findViewById(R.id.rcyUser);

        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        rcyUser.setLayoutManager(linearLayout);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        rcyUser.addItemDecoration(dividerItemDecoration);

        adapter = new RealtimeAdapter(mListUser, new RealtimeAdapter.iClickListener() {
            @Override
            public void onClickUpdateItem(User user) {
                openDialogUpdateItem(user);
            }

            @Override
            public void onClickDeleteItem(User user) {
                onClickDeletUser(user);
            }
        });
        rcyUser.setAdapter(adapter);

        getListUserRealtimercy();
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
        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = Integer.parseInt(etText.getText().toString().trim());
                String name = etTextName.getText().toString().trim();
                User user = new User(id,name);
                onClickAddUser(user);

//                onClickAddAllUser();
            }
        });
    }




    private void getListUserRealtimercy() {
        // My top posts by number of stars
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("list_users");



        //cach 1
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (mListUser!= null){
//                    mListUser.clear();
//                }
//                for (DataSnapshot getSnapshot: dataSnapshot.getChildren()) {
//                    User user = getSnapshot.getValue(User.class);
//                    mListUser.add(user);
//
//                }
//                adapter.notifyDataSetChanged();
//
//                Toast.makeText(Realtime.this, "Get Sucess", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Getting Post failed, log a message
//                Toast.makeText(Realtime.this, "Get Fail", Toast.LENGTH_SHORT).show();
//                // ...
//            }
//        });

        //cách 2
        Query query = myRef.orderByKey();
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@androidx.annotation.NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User user = snapshot.getValue(User.class);
                if (user != null){
                    mListUser.add(user);


                    // sắp xếp giảm dần
//                    mListUser.add(0,user);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(@androidx.annotation.NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User user = snapshot.getValue(User.class);
                if (user == null || mListUser == null || mListUser.isEmpty()){
                    return;
                }
                for (int i = 0; i < mListUser.size(); i++){
                    if (user.getId() == mListUser.get(i).getId()){
                        mListUser.set(i,user);
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@androidx.annotation.NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);
                if (user == null || mListUser == null || mListUser.isEmpty()){
                    return;
                }
                for (int i = 0; i < mListUser.size(); i++){
                    if (user.getId() == mListUser.get(i).getId()){
                        mListUser.remove(mListUser.get(i));
                        break;
                    }
                }
                adapter.notifyDataSetChanged();



            }

            @Override
            public void onChildMoved(@androidx.annotation.NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

            }
        });

    }


    //update user của list_users
    private void openDialogUpdateItem(User user) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_item_updateuser);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        Button btnUpdate = dialog.findViewById(R.id.btn_Update);
        Button btnCancel = dialog.findViewById(R.id.btn_Cancel);
        EditText edt_Update = dialog.findViewById(R.id.edt_Update);

        edt_Update.setText(user.getName());

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("list_users");
                String newName = edt_Update.getText().toString().trim();
                user.setName(newName);
                myRef.child(String.valueOf(user.getId())).updateChildren(user.toMap(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @androidx.annotation.NonNull DatabaseReference ref) {
                        dialog.dismiss();
                    }
                });

            }
        });


        dialog.show();
    }


    //Xóa 1 user của list_users
    private void onClickDeletUser(User user) {

        new AlertDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setMessage("Ban co chac chan xoa khong")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("list_users");
                        myRef.child(String.valueOf(user.getId())).removeValue(new DatabaseReference.CompletionListener() {
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

    private void onClickAddUser(User user) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("list_users");
        String pathObject = String.valueOf(user.getId());
        myRef.child(pathObject).setValue(user);
    }

    private void onClickAddAllUser(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("list_users");
        List<User> list = new ArrayList<>();

        list.add(new User(1,"Quang Dao1"));
        list.add(new User(2,"Quang Dao2"));
        list.add(new User(3,"Quang Dao3"));
        myRef.setValue(list);
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