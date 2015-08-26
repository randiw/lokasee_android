package com.playing.lokasee.activites;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.github.johnkil.print.PrintView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.playing.lokasee.R;
import com.playing.lokasee.User;
import com.playing.lokasee.events.UpdateLocationEvent;
import com.playing.lokasee.helper.BusProvider;
import com.playing.lokasee.helper.DataHelper;
import com.playing.lokasee.helper.ParseHelper;
import com.playing.lokasee.helper.UserData;
import com.playing.lokasee.repositories.UserRepository;
import com.squareup.otto.Subscribe;

import java.util.Hashtable;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements OnMapReadyCallback, View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Bind(R.id.drawer_layout) DrawerLayout drawerLayout;
    @Bind(R.id.side_drawer) LinearLayout sideDrawer;

    private MaterialMenuView materialMenu;
    private GoogleMap googleMap;
    private Marker myMarker;
    private Hashtable<String, Marker> markers;
    private double lat;
    private double lon;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupLayout(R.layout.activity_main);

        drawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                materialMenu.setTransformationOffset(MaterialMenuDrawable.AnimationState.BURGER_ARROW, drawerLayout.isDrawerOpen(sideDrawer) ? 2 - slideOffset : slideOffset);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                if (newState == DrawerLayout.STATE_IDLE) {
                    if (drawerLayout.isDrawerOpen(sideDrawer)) {
                        materialMenu.setState(MaterialMenuDrawable.IconState.ARROW);
                    } else {
                        materialMenu.setState(MaterialMenuDrawable.IconState.BURGER);
                    }
                }
                super.onDrawerStateChanged(newState);
            }
        });

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }
        // Register Event Bus to receive event
        // from Location Alarm
    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    protected View createActionBar(LayoutInflater inflater) {
        View actionbar = inflater.inflate(R.layout.actionbar, null);

        title = ButterKnife.findById(actionbar, R.id.title);
        title.setText(R.string.app_name);

        materialMenu = ButterKnife.findById(actionbar, R.id.menuIcon);
        materialMenu.setState(MaterialMenuDrawable.IconState.BURGER);
        materialMenu.setOnClickListener(this);

//        PrintView imgSearch = ButterKnife.findById(actionbar, R.id.action_search);
//        imgSearch.setOnClickListener(this);

//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = ButterKnife.findById(actionbar, R.id.action_testsearch);
        searchView.setOnClickListener(this);

        return actionbar;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menuIcon:
                if (drawerLayout.isDrawerOpen(sideDrawer)) {
                    drawerLayout.closeDrawer(sideDrawer);
                } else {
                    drawerLayout.openDrawer(sideDrawer);
                }
                break;
//
//            case R.id.action_search:
////                Intent i = new Intent(getApplicationContext(), SearchActivity.class);
////                startActivityForResult(i, 1);
//                Toast.makeText(getApplicationContext(), "search", Toast.LENGTH_SHORT).show();
//                break;
            case R.id.action_testsearch:
                if(title.getVisibility() == View.VISIBLE) {
                    title.setVisibility(View.GONE);
                    Log.e(TAG, "TES");
                }
//                Intent i = new Intent(getApplicationContext(), SearchActivity.class);
//                startActivityForResult(i, 1);
//                Toast.makeText(getApplicationContext(), "test search", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        lat = Double.parseDouble(UserData.getLatitude());
        lon = Double.parseDouble(UserData.getLongitude());

        setMyLocation(lat, lon, UserData.getName());
        retrieveMarkers();
    }

    private void setMyLocation(double latitude, double longitude, String name) {
        if (googleMap != null) {
            LatLng position = new LatLng(latitude, longitude);
            float zoom = 14.0f;

            if (myMarker == null) {
                myMarker = googleMap.addMarker(new MarkerOptions()
                        .position(position)
                        .title(name));
            } else {
                myMarker.setPosition(position);
            }

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, zoom));
        }
    }

    private void retrieveMarkers() {
        ParseHelper.getInstance().getAllUser(new ParseHelper.OnParseQueryListener() {
            @Override
            public void onParseQuery(List<ParseObject> parseObjectList) {
                updateMarker();
            }

            @Override
            public void onError(ParseException pe) {

            }
        });
    }

    private void updateMarker() {
        List<User> users = UserRepository.getAll();
        Log.e(TAG, "users size: " + users.size());
        if (users == null) {
            return;
        }

        if (markers == null) {
            markers = new Hashtable<>();
        }
        for (User user : users) {
            LatLng latLng = new LatLng(user.getLatitude(), user.getLongitude());
            if (!markers.contains(user.getObject_id())) {
                Log.e(TAG, "User Profile " + user.getUrl_prof_pic());
                Marker userMarker = googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(user.getName())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                markers.put(user.getObject_id(), userMarker);
            } else {
                Marker userMarker = markers.get(user.getObject_id());
                userMarker.setPosition(latLng);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                String objId = data.getStringExtra("objId");
                findLocationUser(objId);
            }
        }
    }

    private void findLocationUser(String objId) {
        User user = UserRepository.find(objId);
        LatLng userPos = new LatLng(user.getLatitude(), user.getLongitude());
        CameraPosition cameraPosition = new CameraPosition.Builder().target(userPos).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Subscribe
    public void onUpdateLocation(UpdateLocationEvent updateLocationEvent) {
        Log.i(TAG, updateLocationEvent.toString());
        if (updateLocationEvent.location != null) {
            if (googleMap != null && markers != null) {
                Location location = updateLocationEvent.location;
                retrieveMarkers();
                setMyLocation(location.getLatitude(), location.getLongitude(), null);
            }
        }
    }
}