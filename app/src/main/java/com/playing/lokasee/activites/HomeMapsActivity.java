package com.playing.lokasee.activites;

import android.content.Context;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.playing.lokasee.R;
import com.playing.lokasee.model.User;

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
public class HomeMapsActivity extends BaseActivity implements HomeMapsView, OnMapReadyCallback {


    private Context mContext;
    private Subscription subCript;
    private Double lat, lng;
    private ArrayList<Marker> mMarkers;
    private GoogleMap gMap;
    private ArrayList<String> userData;
    int num;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        initView();
        initData();
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

    private void initData(){
        userData = GetSharedPrefs(mContext);
    }

    private void drawMarker(final GoogleMap nMap) {
        mMarkers = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Location");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {

                if (e == null) {

                    for (int i = 0; i < list.size(); i++) {

                        Double lat = (Double) list.get(i).getNumber("latitude");
                        Double lon = (Double) list.get(i).getNumber("longitude");

                        mMarkers.add(nMap.addMarker(new MarkerOptions()
                                .position(new LatLng(lat, lon))
                                .title(list.get(i).getString("fbId"))));
                    }
                }

            }
        });
    }



    private void isDataExists(final String userId, final Double lat, final Double lng){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Location");
        query.whereEqualTo("fbId", dataUser.get(0));
        query.countInBackground(new CountCallback() {
            @Override
            public void done(int i, ParseException e) {

                Log.i(getLocalClassName(), String.valueOf(i));

                if (e == null) {

                    boolean isExist = i > 0;
                    saveData(isExist, userId, lat, lng);

                }


            }
        });
    }

    private void saveData(boolean isExist, String userId,  final Double lat, final Double lng){

        if(!isExist){

            ParseObject dataObj = new ParseObject("Location");
            dataObj.put("fbId", dataUser.get(0));
            dataObj.put("latitude", lat);
            dataObj.put("longitude", lng);
            dataObj.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    // If done let's draw marker
                    drawMarker(gMap);
                }
            });

        }else{

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Location");
            query.whereEqualTo("fbId", userId);
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {

                    parseObject.put("latitude", lat);
                    parseObject.put("longitude", lng);
                    parseObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {

                            drawMarker(gMap);
                        }
                    });

                }
            });



        }

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


        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 14));

        gMap = googleMap;

        // Update current lat & lon to parse
        isDataExists(dataUser.get(0), lat, lon);
    }


    private void updateLocation(final Double lat, final Double lon, final GoogleMap googleMap) {

        final String latitude = lat.toString();
        final String longitude = lon.toString();


        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");

        query.getInBackground(userData.get(4), new GetCallback<ParseObject>() {
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

}
