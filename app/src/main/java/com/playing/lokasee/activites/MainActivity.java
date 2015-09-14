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
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.playing.lokasee.R;
import com.playing.lokasee.User;
import com.playing.lokasee.fragments.SearchFragment;
import com.playing.lokasee.helper.BusProvider;
import com.playing.lokasee.helper.MarkerHelper;
import com.playing.lokasee.helper.UserData;
import com.playing.lokasee.presenter.MainPresenter;
import com.playing.lokasee.repositories.UserRepository;
import com.playing.lokasee.tools.RoundImage;

import java.util.Hashtable;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nucleus.factory.RequiresPresenter;

@RequiresPresenter(MainPresenter.class)
public class MainActivity extends NucleusBaseActivity<MainPresenter> implements OnMapReadyCallback {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Bind(R.id.drawer_layout) DrawerLayout drawerLayout;
    @Bind(R.id.side_drawer) FrameLayout sideDrawer;
    @Bind(R.id.img_prof_side) ImageView profilePicture;
    @Bind(R.id.txt_name_side) TextView profileName;
    @Bind(R.id.loket) LinearLayout loket;
    @Bind(R.id.frame_search) FrameLayout frameSearch;

    private MaterialMenuView materialMenu;
    private TextView title;

    private GoogleMap googleMap;
    private Marker myMarker;
    private Hashtable<String, Marker> markers;

    SearchFragment searchFragment;

    private SearchView searchView;
    private View marker;
    private LinearLayout linMarker;
    private ImageView imgProf;
    private MarkerOptions markerOptions;

    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupLayout(R.layout.activity_main);

        searchFragment = (SearchFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_search);

        frameSearch.setVisibility(View.GONE);

        searchView.setOnQueryTextListener(searchFragment);

        Glide.with(getApplicationContext()).load(UserData.getFacebookProfilePicUrl()).transform(new RoundImage(getApplicationContext())).into(profilePicture);
        profileName.setText(UserData.getName());

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
    }

    @Override
    protected View createActionBar(LayoutInflater inflater) {
        View actionbar = inflater.inflate(R.layout.actionbar, null);

        title = ButterKnife.findById(actionbar, R.id.title);
        title.setText(R.string.app_name);

        materialMenu = ButterKnife.findById(actionbar, R.id.menuIcon);
        materialMenu.setState(MaterialMenuDrawable.IconState.BURGER);
        materialMenu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(sideDrawer)) {
                    drawerLayout.closeDrawer(sideDrawer);
                    searchView.setVisibility(View.VISIBLE);
                } else {
                    if (!searchView.isIconified()) {
                        closeActionBar();
                    } else {
                        drawerLayout.openDrawer(sideDrawer);
                        searchView.setVisibility(View.GONE);
                    }
                }
            }
        });

        searchView = ButterKnife.findById(actionbar, R.id.action_testsearch);
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialMenu.setState(MaterialMenuDrawable.IconState.ARROW);
                title.setVisibility(View.GONE);

                frameSearch.setVisibility(View.VISIBLE);
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                closeActionBar();
                return false;
            }
        });

        return actionbar;
    }

    private void setUpMarker() {
        markerOptions = new MarkerOptions();
        marker = LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_marker, null);
        linMarker = ButterKnife.findById(marker, R.id.lin_custom_marker);
        imgProf = ButterKnife.findById(marker, R.id.prof_img);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        setUpMarker();
        setMyLocation(UserData.getLatitude(), UserData.getLongitude(), UserData.getName());
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

    private void recursiveMarker(final List<User> users) {
        if (i < users.size()) {
            final String name = users.get(i).getName();
            final String objId = users.get(i).getObject_id();
            String uriPhoto = users.get(i).getUrl_prof_pic();
            final LatLng position = new LatLng(users.get(i).getLatitude(), users.get(i).getLongitude());

            if (!markers.containsKey(objId)) {
                Glide.with(getApplicationContext()).load(uriPhoto).asBitmap().into(new BitmapImageViewTarget(imgProf) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        super.onResourceReady(resource, glideAnimation);
                        imgProf.setImageBitmap(resource);

                        // if image already downloaded then draw marker using custom view
                        markerOptions.position(position).title(name).icon(BitmapDescriptorFactory.fromBitmap(MarkerHelper.getBitmapFromView(linMarker, MainActivity.this)));
                        Log.d(TAG, "Create marker " + name + " latitude: " + position.latitude + " longitude: " + position.longitude);

                        Marker userMarker = googleMap.addMarker(markerOptions);
                        markers.put(objId, userMarker);

                        i++;
                        recursiveMarker(users);
                    }
                });
            } else {
                Log.d(TAG, "Update marker " + name + " latitude: " + position.latitude + " longitude: " + position.longitude);

                Marker userMarker = markers.get(objId);
                userMarker.setPosition(position);

                i++;
                recursiveMarker(users);
            }
        }
    }

    public void updateMarker() {
        List<User> users = UserRepository.getAll();
        if (users == null) {
            return;
        }

        if (markers == null) {
            markers = new Hashtable<>();
        }
       recursiveMarker(users);
    }

    private void closeActionBar() {
        materialMenu.setState(MaterialMenuDrawable.IconState.BURGER);
        title.setVisibility(View.VISIBLE);

        searchView.isIconfiedByDefault();
        searchView.onActionViewCollapsed();
        searchView.setQuery("", false);

        View view = this.getCurrentFocus();
        if( view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        hideFragment();
    }

    private void hideFragment() {
        if(frameSearch.getVisibility() == View.VISIBLE) {
            frameSearch.setVisibility(View.GONE);
        }
    }

    public void getUserMapLocation(User user) {
        if(user != null) {
            closeActionBar();

            LatLng userPos = new LatLng(user.getLatitude(), user.getLongitude());
            CameraPosition cameraPosition = new CameraPosition.Builder().target(userPos).zoom(12).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    public void onUpdateLocation(Location location) {
        if (location != null) {
            if (googleMap != null && markers != null) {
                // Reset count location
                i = 0;
                setMyLocation(location.getLatitude(), location.getLongitude(), null);
            }
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

    @OnClick(R.id.loket)
    public void openLoketMenu() {
        getPresenter().openLoket();
    }

    @OnClick(R.id.gotix)
    public void openGoTixMenu() {
        getPresenter().openGotix();
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(sideDrawer)) {
            drawerLayout.closeDrawer(sideDrawer);
            materialMenu.setState(MaterialMenuDrawable.IconState.ARROW);
        }
        if(!searchView.isIconified()) {
            closeActionBar();
        }
        else {
            super.onBackPressed();
        }
    }
}