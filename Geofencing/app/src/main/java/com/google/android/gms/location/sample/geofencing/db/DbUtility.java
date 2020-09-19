package com.google.android.gms.location.sample.geofencing.db;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class DbUtility {

    private FenceKeyDao fenceKeyDao;

    public List<FenceKeysModel> getGeofences(Context context) {
        ArrayList<String> keys = new ArrayList<>();
        FenceKeyDao fenceDao = getFenceDao(context);
        return fenceDao.getAll();
    }

    public void addGeofence(Context context, String key, double lat, double lng) {
        FenceKeyDao fenceDao = getFenceDao(context);
        if (fenceDao.getFence(key).isEmpty()) {
            FenceKeysModel model = new FenceKeysModel();
            model.setKey(key);
            model.setLat(lat);
            model.setLng(lng);
            fenceDao.add(model);
        }
    }

    public void removeGeofence(Context context, String key) {
        FenceKeyDao fenceDao = getFenceDao(context);
        fenceDao.delete(key);
    }

    private FenceKeyDao getFenceDao(Context context) {
        if (fenceKeyDao == null) {
            AppDataBase instance = AppDataBase.getInstance(context);
            fenceKeyDao = instance.fenceDao();
        }
        return fenceKeyDao;
    }

}
