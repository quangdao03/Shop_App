package com.example.shop_app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    public static final String DATABASE_NAME = "Db_Cart";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "DB_CART";
    public static final String COLUMN_ID = "Cart_ID";
    public static final String COLUMN_PID = "productID";
    public static final String COLUMN_IMA = "image";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_CEATOR = "creator";
    public static final String COLUMN_VARIANT = "variant";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_PRICEEACH = "pricefinal";
    public static final String COLUMN_QUANTITY = "quantity";

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE "+ TABLE_NAME +
                " ("+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PID + " TEXT, " +
                COLUMN_IMA + " TEXT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_CEATOR + " TEXT, " +
                COLUMN_VARIANT + " TEXT, " +
                COLUMN_PRICE + " TEXT, " +
                COLUMN_PRICEEACH + " TEXT, " +
                COLUMN_QUANTITY + " TEXT);";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
    public void addCart(String productID,String image, String name, String creator, String variant, String price, String priceEach, String quantity){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PID, productID);
        cv.put(COLUMN_IMA, image);
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_CEATOR, creator);
        cv.put(COLUMN_VARIANT, variant);
        cv.put(COLUMN_PRICE, price);
        cv.put(COLUMN_PRICEEACH, priceEach);
        cv.put(COLUMN_QUANTITY, quantity);

        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1 ){
            Toast.makeText(context, "Thêm thất bại", Toast.LENGTH_SHORT).show();
        }else {
            Log.d("Cart","Thêm thành công vào giỏ hàng");
        }
    }

    public void updateData(String row_id, String productID, String image, String name, String creator, String variant, String price, String priceEach, String quantity){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PID, productID);
        cv.put(COLUMN_IMA, image);
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_CEATOR, creator);
        cv.put(COLUMN_VARIANT, variant);
        cv.put(COLUMN_PRICE, price);
        cv.put(COLUMN_PRICEEACH, priceEach);
        cv.put(COLUMN_QUANTITY, quantity);

        db.update(TABLE_NAME, cv,"Cart_ID=? AND productID=?", new String[]{row_id ,productID});


    }
    public void deleteData(String productID){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "productID=?", new String[]{productID} );

    }
    public void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME , null, null);

    }
    public Cursor readAllData(){
        String sql = "SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(sql, null);
        }
        return cursor;
    }

    public Cursor updateDataBase(String sql){
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(sql, null);
    }


}
