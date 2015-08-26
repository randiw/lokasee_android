package com.playing.lokasee.activites;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
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
import com.playing.lokasee.UserDao;
import com.playing.lokasee.events.UpdateLocationEvent;
import com.playing.lokasee.helper.BusProvider;
import com.playing.lokasee.helper.MarkerHelper;
import com.playing.lokasee.helper.ParseHelper;
import com.playing.lokasee.helper.UserData;
import com.playing.lokasee.repositories.UserRepository;
import com.squareup.otto.Subscribe;

import java.util.Hashtable;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements OnMapReadyCallback {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Bind(R.id.drawer_layout) DrawerLayout drawerLayout;
    @Bind(R.id.side_drawer) LinearLayout sideDrawer;
    @Bind(R.id.search) Button searchButton;

    private MaterialMenuView materialMenu;
    private GoogleMap googleMap;
    private Marker myMarker;
    private Hashtable<String, Marker> markers;

    private View marker;
    private LinearLayout linMarker;
    private ImageView imgProf;
    private MarkerOptions markerOptions;


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

    @Override
    protected View createActionBar(LayoutInflater inflater) {
        View actionbar = inflater.inflate(R.layout.actionbar, null);

        TextView title = ButterKnife.findById(actionbar, R.id.title);
        title.setText(R.string.app_name);

        materialMenu = ButterKnife.findById(actionbar, R.id.menuIcon);
        materialMenu.setState(MaterialMenuDrawable.IconState.BURGER);
        materialMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(sideDrawer)) {
                    drawerLayout.closeDrawer(sideDrawer);
                } else {
                    drawerLayout.openDrawer(sideDrawer);
                }
            }
        });

        PrintView refresh = ButterKnife.findById(actionbar, R.id.action_refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (googleMap != null && markers != null) {
                    retrieveMarkers();
                }
            }
        });

        return actionbar;
    }

    private void setUpMarker() {
        markerOptions = new MarkerOptions();
        marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);
        linMarker = ButterKnife.findById(marker, R.id.lin_custom_marker);
        imgProf = ButterKnife.findById(marker, R.id.prof_img);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        setUpMarker();

        setMyLocation(UserData.getLatitude(), UserData.getLongitude(), UserData.getName());
        retrieveMarkers();
    }

    private void setMyLocation(double latitude, double longitude, String name) {
        if (googleMap != null) {
            LatLng position = new LatLng(latitude, longitude);
            float zoom = 14.0f;

            if (myMarker == null) {
                createMarker(position, name, null, UserData.getFacebookProfilePicUrl());
            } else {
                myMarker.setPosition(position);
            }

            if (googleMap.getCameraPosition().zoom < zoom) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, zoom));
            } else {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(googleMap.getCameraPosition().target, googleMap.getCameraPosition().zoom));
            }
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

    private void createMarker(final LatLng latLng, final String name, final String objId, final String uriPhoto) {
        Glide.with(getApplicationContext()).load(uriPhoto).asBitmap().into(new BitmapImageViewTarget(imgProf) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                super.onResourceReady(resource, glideAnimation);
                imgProf.setImageBitmap(resource);

                // if image already downloaded then draw marker using custom view
                markerOptions.position(latLng).title(name).icon(BitmapDescriptorFactory.fromBitmap(MarkerHelper.getBitmapFromView(linMarker, MainActivity.this)));
                Log.d(TAG, "Create marker " + name + " latitude: " + latLng.latitude + " longitude: " + latLng.longitude);

                if (objId == null) {
                    myMarker = googleMap.addMarker(markerOptions);
                } else {
                    Marker userMarker = googleMap.addMarker(markerOptions);
                    markers.put(objId, userMarker);
                }

            }
        });
    }

    private void updateMarker() {
        List<User> users = UserRepository.getAll();
        if (users == null) {
            return;
        }

        if (markers == null) {
            markers = new Hashtable<>();
        }

        for (User user : users) {
            Log.d(TAG, "users " + user.getName());
            LatLng latLng = new LatLng(user.getLatitude(), user.getLongitude());
            if (!markers.contains(user.getObject_id())) {
                createMarker(latLng, user.getName(), user.getObject_id(), user.getUrl_prof_pic());
            } else {
                Marker userMarker = markers.get(user.getObject_id());
                userMarker.setPosition(latLng);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String objId = data.getStringExtra(UserDao.Properties.Object_id.name);
                Log.d(TAG, "objecId: " + objId);
                findLocationUser(objId);
            }
        }
    }

    private void findLocationUser(String objId) {
        User user = UserRepository.find(objId);
        Log.d(TAG, "User: " + user.getName() + " latitude: " + user.getLatitude() + " longitude: " + user.getLongitude());

        LatLng userPos = new LatLng(user.getLatitude(), user.getLongitude());
        CameraPosition cameraPosition = new CameraPosition.Builder().target(userPos).zoom(12).build();
//        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Subscribe
    public void onUpdateLocation(UpdateLocationEvent updateLocationEvent) {
        if (updateLocationEvent.location != null) {
            if (googleMap != null && markers != null) {
                Location location = updateLocationEvent.location;
                retrieveMarkers();
                setMyLocation(location.getLatitude(), location.getLongitude(), null);
            }
        }
    }

    @OnClick(R.id.search)
    public void searchUser(View view) {
        startActivityForResult(new Intent(getApplicationContext(), SearchActivity.class), 1);
    }

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
}