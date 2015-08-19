package com.playing.lokasee.activites;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.playing.lokasee.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by mexan on 8/18/15.
 */
public class HomeMapsActivity extends AppCompatActivity implements HomeMapsView, OnMapReadyCallback {


    private Context mContext;
    private Subscription subCript;
    private Double lat, lng;
    private ArrayList<Marker> mMarkers;
    private GoogleMap gMap;


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

    private void drawMarker(final GoogleMap nMap) {
        mMarkers = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {

                System.out.println("Size " + list.size());

                for (int i = 0; i < list.size(); i++) {

                    Double lat = Double.valueOf(list.get(i).getString("lat"));
                    Double lon = Double.valueOf(list.get(i).getString("long"));

                    mMarkers.add(nMap.addMarker(new MarkerOptions()
                            .position(new LatLng(lat, lon))
                            .title("Marker")));

                }

            }
        });


    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(mContext);

        locationProvider.getLastKnownLocation()
                .subscribe(new Action1<Location>() {
                    @Override
                    public void call(Location location) {


                        lat = location.getLatitude();
                        lng = location.getLongitude();

                        // if location detected show marker
                        setupMaps(googleMap, lat, lng);

                    }
                });
    }


    private void setupMaps(GoogleMap googleMap, Double lat, Double lon){

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lon))
                .title("Marker"));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 14));

        // Update current lat & lon to parse
        updateLocation(lat, lon, googleMap);
    }


    private void updateLocation(final Double lat, final Double lon, final GoogleMap googleMap) {

        final String latitude = lat.toString();
        final String longitude = lon.toString();


        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");

        query.getInBackground("OI9plXpAj3", new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {

                if (e == null) {

                    parseObject.put("lat", latitude);
                    parseObject.put("long", longitude);
                    parseObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {

                            if (e == null) {

                                Log.i(getLocalClassName(), "Successs");

                                drawMarker(googleMap);


                            } else {

                                e.printStackTrace();
                            }
                        }
                    });

                } else {

                    e.printStackTrace();

                }
            }
        });
    }

    public boolean onKeyDown(int keycode, KeyEvent event){
        if(keycode==KeyEvent.KEYCODE_BACK){
            moveTaskToBack(true);
        }
        return super.onKeyDown(keycode,event);
    }

}
