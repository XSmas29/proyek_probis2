package com.example.proyek_mobcomp;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.proyek_mobcomp.classFolder.cCart;
import com.example.proyek_mobcomp.daoFolder.CartDao;

@Database(entities = {cCart.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CartDao cartDao();

    public static AppDatabase instance;
    public static synchronized AppDatabase getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,
                    "myitemDB")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
