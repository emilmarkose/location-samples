package com.google.android.gms.location.sample.geofencing.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = FenceKeysModel.class, version = 1, exportSchema = false)
abstract class AppDataBase extends RoomDatabase {

    public abstract FenceKeyDao fenceDao();

    private static AppDataBase INSTANCE;

    public static AppDataBase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, AppDataBase.class, "geofence-db")
                    .fallbackToDestructiveMigration().build();
        }
        return INSTANCE;
    }

    public static void clearDatabase() {
        INSTANCE.clearAllTables();
    }
}
