package com.google.android.gms.location.sample.geofencing.db;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "table_fence_keys")
public class FenceKeysModel {
    @PrimaryKey(autoGenerate = true)
    public int id = 0;

    private String key;
    private double lat = 0;
    private double lng = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
