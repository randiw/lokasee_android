package com.playing.lokasee.activites;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.playing.lokasee.R;
import com.playing.lokasee.helper.UserData;

public class MainActivity extends BaseActivity implements OnMapReadyCallback {

    private static final String TAG = MainActivity.class.getSimpleName();

    private GoogleMap googleMap;
    private Marker myMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupLayout(R.layout.activity_main);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        double lat = Double.parseDouble(UserData.getLastLat());
        double lon = Double.parseDouble(UserData.getLastLon());

        setMyLocation(lat, lon);
    }

    private void setMyLocation(double latitude, double longitude) {
        if (googleMap != null) {
            LatLng position = new LatLng(latitude, longitude);
            float zoom = 14.0f;

            if (myMarker == null) {
                myMarker = googleMap.addMarker(new MarkerOptions()
                        .position(position)
                        .title("I am here"));
            } else {
                myMarker.setPosition(position);
            }

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, zoom));
        }
    }
}