package com.google.android.gms.location.sample.geofencing.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
interface FenceKeyDao {

    @Query("SELECT * FROM table_fence_keys")
    List<FenceKeysModel> getAll();

    @Delete
    void delete(FenceKeysModel model);

    @Insert
    void add(FenceKeysModel model);

    @Query("DELETE  from table_fence_keys where id=:id")
    void delete(int id);

    @Query("DELETE  from table_fence_keys where `key`=:key")
    void delete(String key);


    @Query("SELECT * from table_fence_keys where `key`=:key")
    List<FenceKeysModel> getFence(String key);
}
