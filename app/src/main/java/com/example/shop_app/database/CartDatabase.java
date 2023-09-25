package com.example.shop_app.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {CartRoom.class}, version = 1)
public abstract class CartDatabase extends RoomDatabase {
    public abstract cartDAO cartDAO();

    private static CartDatabase instance;

    public static synchronized CartDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            CartDatabase.class, "app_database")
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}