package com.example.shop_app.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface cartDAO {
    @Insert
    void insertCart(CartRoom cart);

    @Update
    void updateCart(CartRoom cart);

    @Delete
    void deleteCart(CartRoom cart);

    @Query("SELECT * FROM cart")
    List<CartRoom> getAllCart();
}
