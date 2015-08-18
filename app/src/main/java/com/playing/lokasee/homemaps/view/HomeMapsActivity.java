package com.playing.lokasee.homemaps.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.playing.lokasee.R;
import com.playing.lokasee.util.GPSTracker;
import com.playing.lokasee.util.UtilFunction;

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
        gpsTracker = new GPSTracker(mContext);

        // if gps not enabled then show alert to user
        // if gps enabled get location of user
        if(!gpsTracker.canGetLocation()){

            gpsTracker.showSettingsAlert();

        }else{

            // Get location user
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title("Marker"));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 14));
    }
}
