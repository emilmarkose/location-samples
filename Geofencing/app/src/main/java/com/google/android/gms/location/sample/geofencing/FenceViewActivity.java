package com.google.android.gms.location.sample.geofencing;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.sample.geofencing.db.DbUtility;
import com.google.android.gms.location.sample.geofencing.db.FenceKeysModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FenceViewActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DbUtility utility = new DbUtility();
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Marker userMarker;
    private boolean showMenu = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_fence);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        showMenu = getCallingActivity() != null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setZoomGesturesEnabled(true);
        loadAllFences();
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (userMarker != null) {
                    userMarker.remove();
                }
                String title = "lat: " + latLng.latitude + " lng: " + latLng.longitude;
                MarkerOptions options = new MarkerOptions()
                        .position(latLng)
                        .title(title)
                        .draggable(true);

                userMarker = mMap.addMarker(options);
            }
        });

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {
                LatLng latLng = marker.getPosition();
                String title = "lat: " + latLng.latitude + " lng: " + latLng.longitude;
                userMarker.setTitle(title);
                userMarker.showInfoWindow();
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                LatLng latLng = marker.getPosition();
                String title = "lat: " + latLng.latitude + " lng: " + latLng.longitude;
                userMarker.setTitle(title);
                userMarker.showInfoWindow();
                mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
            }
        });
    }

    private void loadAllFences() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                final List<FenceKeysModel> geofences = utility.getGeofences(FenceViewActivity.this);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        drawFences(geofences);
                    }
                });
            }
        });
    }

    private void drawFence(FenceKeysModel geofence) {
        LatLng latLng = new LatLng(geofence.getLat(), geofence.getLng());
        mMap.addMarker(new MarkerOptions()
                .title(geofence.getKey())
                .position(latLng));
        mMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(geofence.getRadius())
                .strokeColor(Color.BLUE)
                .strokeWidth(1F)
                .fillColor(0x22FF6347));
    }

    private void drawFences(List<FenceKeysModel> geofences) {
        for (FenceKeysModel geofence : geofences) {
            drawFence(geofence);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (showMenu) {
            getMenuInflater().inflate(R.menu.map_options, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.useLocation) {
            setActivityResult();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setActivityResult() {
        Intent intent = new Intent();
        if (userMarker == null) {
            Toast.makeText(this, "Long press on the map to select a location", Toast.LENGTH_SHORT).show();
            return;
        }
        LatLng position = userMarker.getPosition();
        intent.putExtra("lat", position.latitude);
        intent.putExtra("lng", position.longitude);
        setResult(RESULT_OK, intent);
        finish();
    }
}
