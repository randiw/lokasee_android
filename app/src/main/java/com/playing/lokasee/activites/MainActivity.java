package com.playing.lokasee.activites;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.side_drawer)
    LinearLayout sideDrawer;
    @Bind(R.id.search)
    Button searchButton;

    private MaterialMenuView materialMenu;
    private GoogleMap googleMap;
    private Marker myMarker;
    private Hashtable<String, Marker> markers;
    private double lat;
    private double lon;
    private Context mContext;

    // View for custom Marker
    private View marker;
    private LinearLayout linMarker;
    private ImageView imgProf;
    private MarkerOptions markerOptions;

    private ImageView imgProfSide;
    private TextView txtNameSide;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupLayout(R.layout.activity_main);

        mContext = this;

        drawerLayout.setDrawerListener(lsDrawerListener);

        renderViewSide();

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        searchButton.setOnClickListener(lsSearch);



    }

    @Override
    protected View createActionBar(LayoutInflater inflater) {
        View actionbar = inflater.inflate(R.layout.actionbar, null);

        TextView title = ButterKnife.findById(actionbar, R.id.title);
        title.setText(R.string.app_name);

        materialMenu = ButterKnife.findById(actionbar, R.id.menuIcon);
        materialMenu.setState(MaterialMenuDrawable.IconState.BURGER);
        materialMenu.setOnClickListener(this);

        PrintView imgRefresh = ButterKnife.findById(actionbar, R.id.action_refresh);

        imgRefresh.setOnClickListener(this);


        return actionbar;
    }

    private void renderViewSide(){
        imgProfSide = ButterKnife.findById(drawerLayout, R.id.img_prof_side);
        txtNameSide = ButterKnife.findById(drawerLayout, R.id.txt_name_side);

        Glide.with(mContext).load(DataHelper.getString(UserData.URL_PROF_PIC)).into(imgProfSide);
        txtNameSide.setText(DataHelper.getString(UserData.NAME));
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
                Log.i(TAG, "Mymarker" + myMarker);
                createMarker(mContext, position, name, null, DataHelper.getString(UserData.URL_PROF_PIC));

            } else {

                myMarker.setPosition(position);
            }

            if (googleMap.getCameraPosition().zoom < zoom) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, zoom));
            } else {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(googleMap.getCameraPosition().target, googleMap.getCameraPosition().zoom));
            }

            Log.i(TAG, "Zoom Level" + googleMap.getCameraPosition().zoom);
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

    private void createMarker(final Context mContext, final LatLng latLng, final String name, final String objId, final String uriPhoto) {

        Glide.with(mContext).load(uriPhoto).asBitmap().into(new BitmapImageViewTarget(imgProf) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                super.onResourceReady(resource, glideAnimation);

                imgProf.setImageBitmap(resource);

                // if image already downloaded then draw marker using custom view
                markerOptions.position(latLng).title(name).icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromView(linMarker, mContext)));

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
            LatLng latLng = new LatLng(user.getLatitude(), user.getLongitude());

            Log.i(TAG, user.getName());

            if (!markers.contains(user.getObject_id())) {

                createMarker(mContext, latLng, user.getName(), user.getObject_id(), user.getUrl_prof_pic());

            } else {
                Marker userMarker = markers.get(user.getObject_id());
                userMarker.setPosition(latLng);
            }
        }
    }

    public static Bitmap getBitmapFromView(View view, Context mContext) {

        // Create display metric
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        // Set LayoutParams (Height and Width) from view
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();

        // Concert bitmap from view
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
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
                lat = location.getLatitude();
                lon = location.getLongitude();
                setMyLocation(lat, lon, null);
            }
        }
    }

    /* #  LISTENER FUNCTION # */

    View.OnClickListener lsSearch = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(i);
        }
    };

    DrawerLayout.SimpleDrawerListener lsDrawerListener = new DrawerLayout.SimpleDrawerListener() {
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
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_refresh:
                if (googleMap != null && markers != null) {
                    Log.i(TAG, "Clear Map");
                    retrieveMarkers();
                    setMyLocation(lat, lon, DataHelper.getString("name"));
                }
                break;

            case R.id.menuIcon:
                if (drawerLayout.isDrawerOpen(sideDrawer)) {
                    drawerLayout.closeDrawer(sideDrawer);
                } else {
                    drawerLayout.openDrawer(sideDrawer);
                }
                break;
        }
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

    @Override
    protected void onStop() {
        super.onStop();

    }


}