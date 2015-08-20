package com.playing.lokasee.activites;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

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
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.playing.lokasee.R;
import com.playing.lokasee.helper.DataHelper;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.daogenerator.DaoGenerator;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by mexan on 8/18/15.
 */
public class HomeMapsActivity extends BaseActivity implements OnMapReadyCallback {

    private static final String TAG = HomeMapsActivity.class.getSimpleName();

    private GoogleMap googleMap;
    private Double lat, lng;
    private ArrayList<Marker> mMarkers;
    private String userId;
    private String objectId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupLayout(R.layout.home_maps);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initData();
    }

    private void initData() {
        userId = DataHelper.getString("userId");
        objectId = DataHelper.getString("objectId");
    }

    private void configDb(){


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


    private void isDataExists(final String userId, final Double lat, final Double lng) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Location");
        query.whereEqualTo("fbId", userId);
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

    private void saveData(boolean isExist, String userId, final Double lat, final Double lng) {

        if (!isExist) {
            ParseQuery<ParseObject> userQuery = new ParseQuery<ParseObject>("User");
            userQuery.getInBackground(objectId, new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    ParseObject dataObj = new ParseObject("Location");

                    dataObj.put("fbId", userId);
                    dataObj.put("latitude", lat);
                    dataObj.put("longitude", lng);
                    dataObj.put("userRelational", parseObject);
                    dataObj.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            // If done let's draw marker
                            drawMarker(googleMap);
                        }
                    });
                }
            });



        } else {

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

                            drawMarker(googleMap);
                        }
                    });

                }
            });
        }

    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;

        ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(getApplicationContext());
        locationProvider.getLastKnownLocation().subscribe(new Action1<Location>() {
            @Override
            public void call(Location location) {
                lat = location.getLatitude();
                lng = location.getLongitude();

                // if location detected show marker
                setupMaps(lat, lng);
            }
        });
    }


    private void setupMaps(Double lat, Double lon) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 14));

        // Update current lat & lon to parse
        isDataExists(userId, lat, lon);
    }
}


