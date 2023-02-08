package com.example.shop_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

public class Realtime extends AppCompatActivity {

    EditText etText;
    Button btnAdd,btnDelete;
    TextView txtGet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime);

        Button btnGet = findViewById(R.id.btnGet);
        etText = findViewById(R.id.etText);
        txtGet = findViewById(R.id.txtGet);
        btnAdd = findViewById(R.id.btnAdd);
        btnDelete = findViewById(R.id.btnDelete);
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

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickDelete();
            }
        });
    }

    private void onClickDelete() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("chanel");
        myRef.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @androidx.annotation.NonNull DatabaseReference ref) {
                Toast.makeText(Realtime.this, "Xóa dữ liệu thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onClickData() {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("chanel");

        myRef.setValue(etText.getText().toString().trim(), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @androidx.annotation.NonNull DatabaseReference ref) {
                Toast.makeText(Realtime.this, "Cập nhập dữ liệu thành công", Toast.LENGTH_SHORT).show();
            }
        });


        DatabaseReference myRef1 = database.getReference("chanel1");
        myRef1.setValue(true);
        DatabaseReference myRef2 = database.getReference("chanel2");
        myRef2.setValue("Đạo 2");

    }
    private void ReadData(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("chanel");
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                txtGet.setText(value);
                Toast.makeText(Realtime.this, ""+value, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value

            }
        });
    }
}