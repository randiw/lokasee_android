package com.playing.lokasee.activites;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialmenu.MaterialMenu;
import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuIcon;
import com.balysv.materialmenu.MaterialMenuView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.playing.lokasee.R;
import com.playing.lokasee.User;
import com.playing.lokasee.helper.BusProvider;
import com.playing.lokasee.helper.DataHelper;
import com.playing.lokasee.helper.LocationManager;
import com.playing.lokasee.helper.ParseHelper;
import com.playing.lokasee.helper.UserData;
import com.playing.lokasee.models.EventBusLocation;
import com.playing.lokasee.receiver.LocationAlarm;
import com.playing.lokasee.repositories.UserRepository;
import com.squareup.otto.Subscribe;

import java.util.Hashtable;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;

public class MainActivity extends BaseActivity implements OnMapReadyCallback, View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.side_drawer)
    LinearLayout sideDrawer;

    private TextView title;
    private MaterialMenuView materialMenu;
    private GoogleMap googleMap;
    private Marker myMarker;
    private Hashtable<String, Marker> markers;
    private double lat;
    private double lon;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupLayout(R.layout.activity_main);

        mContext = this;

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

        // Register Event Bus to receive event
        // from Location Alarm
        BusProvider.getInstance().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Unregister Event Bus if application closed
        BusProvider.getInstance().unregister(this);
    }

    @Override
    protected View createActionBar(LayoutInflater inflater) {
        View actionbar = inflater.inflate(R.layout.actionbar, null);

        title = ButterKnife.findById(actionbar, R.id.title);
        title.setText(R.string.app_name);

        materialMenu = ButterKnife.findById(actionbar, R.id.menuIcon);
        materialMenu.setState(MaterialMenuDrawable.IconState.BURGER);


        ImageButton imgRefresh = ButterKnife.findById(actionbar, R.id.action_refresh);
        imgRefresh.setOnClickListener(lsRefresh);

        materialMenu.setOnClickListener(this);
        return actionbar;
    }

    View.OnClickListener lsRefresh = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (googleMap != null && markers != null) {

                LocationManager.checkLocation(mContext).subscribe(new Action1<Location>() {
                    @Override
                    public void call(Location location) {

                        Log.i(TAG, "latitude" + location.getLatitude());
                        Log.i(TAG, "longitude" + location.getLongitude());

                        setMyLocation(location.getLatitude(), location.getLongitude(), DataHelper.getString("name"));
                        retrieveMarkers();

                        ParseHelper.getInstance().saveMyLocation(location.getLatitude(), location.getLongitude(), new ParseHelper.OnSaveParseObjectListener() {
                            @Override
                            public void onSaveParseObject(ParseObject parseObject) {

                            }

                            @Override
                            public void onError(ParseException pe) {

                            }
                        });

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                        throwable.printStackTrace();

                        Log.i(TAG, "Error");


                    }
                });
            }
        }
    };


    @Override
    public void onClick(View v) {
        if (drawerLayout.isDrawerOpen(sideDrawer)) {
            drawerLayout.closeDrawer(sideDrawer);
        } else {
            drawerLayout.openDrawer(sideDrawer);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        lat = Double.parseDouble(UserData.getLatitude());
        lon = Double.parseDouble(UserData.getLongitude());

        setMyLocation(lat, lon, DataHelper.getString("name"));
        retrieveMarkers();
    }

    private void setMyLocation(double latitude, double longitude, String name) {



        if (googleMap != null) {
            LatLng position = new LatLng(latitude, longitude);
            float zoom = 14.0f;

            if (myMarker == null) {
//                myMarker = googleMap.addMarker(createMarker(mContext, position, name));


                createMarker(mContext, position, name);

            } else {
                myMarker.setPosition(position);
            }

            if (googleMap.getCameraPosition().zoom < zoom) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, zoom));
            } else {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, googleMap.getCameraPosition().zoom));
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

    private MarkerOptions createMarker(final Context mContext, final LatLng latLng, final String name) {

        final MarkerOptions markerOption = new MarkerOptions();

        View marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);
        final LinearLayout linMarker = ButterKnife.findById(marker, R.id.lin_custom_marker);
        final ImageView imgProf = ButterKnife.findById(marker, R.id.prof_img);
        Glide.with(mContext).load("https://avatars2.githubusercontent.com/u/1239461?v=3&s=460").asBitmap().into(new BitmapImageViewTarget(imgProf) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                super.onResourceReady(resource, glideAnimation);
                imgProf.setImageBitmap(resource);
                markerOption.position(latLng).title(name).icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromView(linMarker, mContext)));

                myMarker = googleMap.addMarker(markerOption);

            }
        });

        return markerOption;
    }

    private MarkerOptions createMarker2(final Context mContext, final LatLng latLng, final String name, final String objId) {

        final MarkerOptions markerOption = new MarkerOptions();
        View marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);
        final LinearLayout linMarker = ButterKnife.findById(marker, R.id.lin_custom_marker);
        final ImageView imgProf = ButterKnife.findById(marker, R.id.prof_img);
        Glide.with(mContext).load("https://avatars2.githubusercontent.com/u/1239461?v=3&s=460").asBitmap().into(new BitmapImageViewTarget(imgProf) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                super.onResourceReady(resource, glideAnimation);
                imgProf.setImageBitmap(resource);
                markerOption.position(latLng).title(name).icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromView(linMarker, mContext)));

                Marker userMarker = googleMap.addMarker(markerOption);

                markers.put(objId, userMarker);


            }
        });

        return markerOption;
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
            if (!markers.contains(user.getObject_id())) {
                createMarker2(mContext, latLng, user.getName(), user.getObject_id());
            } else {
                Marker userMarker = markers.get(user.getObject_id());
                userMarker.setPosition(latLng);
            }
        }
    }

    public static Bitmap getBitmapFromView(View view, Context mContext) {
        // Convert a view to bitmap
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    @Subscribe
    public void onEventBusLocation(EventBusLocation eventBusLocation) {
        Log.i(TAG, String.valueOf(eventBusLocation.getLocation().getLatitude()));
        try {
            if (eventBusLocation.isStatMapRefresh()) {
                if (googleMap != null && markers != null) {
                    Log.i(TAG, "Clear Map");
                    retrieveMarkers();
                    setMyLocation(eventBusLocation.getLocation().getLatitude(), eventBusLocation.getLocation().getLongitude(), DataHelper.getString("name"));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}