package com.playing.lokasee.activites;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.playing.lokasee.R;
import com.playing.lokasee.util.GPSTracker;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.functions.Action1;

/**
 * Created by mexan on 8/18/15.
 */
public class HomeMapsActivity extends AppCompatActivity implements HomeMapsView, OnMapReadyCallback {

    private GoogleMap gMap;
    private GPSTracker gpsTracker;
    private Context mContext;

    // Location variable
    private double latitude = 0;
    private double longitude = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext =  this;
        initView();
        mapsSetup();
    }


    @Override
    public void initView() {
        setContentView(R.layout.home_maps);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void mapsSetup() {

        ReactiveLocationProvider reactiveLocationProvider = new ReactiveLocationProvider(mContext);
        reactiveLocationProvider.getLastKnownLocation().subscribe(new Action1<Location>() {
            @Override
            public void call(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title("Marker"));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 14));
    }
}
