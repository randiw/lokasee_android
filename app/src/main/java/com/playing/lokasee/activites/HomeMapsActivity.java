package com.playing.lokasee.activites;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.playing.lokasee.R;
import com.playing.lokasee.util.GPSTracker;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by mexan on 8/18/15.
 */
public class HomeMapsActivity extends AppCompatActivity implements HomeMapsView, OnMapReadyCallback {


    private final GoogleMap gMap = null;
    private GPSTracker gpsTracker;
    private Context mContext;

    // Location variable
    private double latitude = 0;
    private double longitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        initView();


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void initView() {
        setContentView(R.layout.home_maps);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void mapsSetup() {


    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        LocationRequest request = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(5).setInterval(100);

        ReactiveLocationProvider loProv = new ReactiveLocationProvider(mContext);
        Subscription subScript = loProv.getUpdatedLocation(request).subscribe(new Action1<Location>() {
            @Override
            public void call(Location location) {

                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(location.getLatitude(), location.getLongitude()))
                        .title("Marker"));

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14));

                // Update current lat & lon to parse
                updateLocation(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
            }
        });
    }


    private void updateLocation(final String lat, final String lon) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");

        query.getInBackground("OI9plXpAj3", new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {

                if (e == null) {

                    parseObject.put("lat", lat);
                    parseObject.put("long", lon);
                    parseObject.saveInBackground();

                }
            }
        });
    }

}
