package com.playing.lokasee.activites;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.facebook.login.LoginManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.playing.lokasee.R;
import com.playing.lokasee.User;
import com.playing.lokasee.fragments.SearchFragment;
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
    @Bind(R.id.side_drawer) LinearLayout sideDrawer;
    @Bind(R.id.img_prof_side) ImageView profilePicture;
    @Bind(R.id.txt_name_side) TextView profileName;

    private MaterialMenuView materialMenu;
    private TextView title;

    private GoogleMap googleMap;
    private Marker myMarker;
    private Hashtable<String, Marker> markers;

    private SearchFragment sf;
    private SearchFragment searchFrag;
    private Boolean flagSearch = false;
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

        searchFrag = SearchFragment.newInstance();
        searchView.setOnQueryTextListener(searchFrag);

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

                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.frameLayout, searchFrag, "tag");
                ft.setTransition(ft.TRANSIT_FRAGMENT_OPEN);
                ft.addToBackStack(null);
                ft.commit();
                Log.e(TAG, "search listener");
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
                recursiveMarker(null, false, position, name, UserData.getFacebookProfilePicUrl());
            } else {
                myMarker.setPosition(position);
                Log.d(TAG, "Update one marker " + name + " latitude: " + position.latitude + " longitude: " + position.longitude);
            }

            if (googleMap.getCameraPosition().zoom < zoom) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, zoom));
            } else {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(googleMap.getCameraPosition().target, googleMap.getCameraPosition().zoom));
            }
        }
    }

    private void recursiveMarker(final List<User> users, boolean isRecursive, LatLng locCurent, String nameCurrent, String urlPhoto) {
        if (isRecursive) {
            if (i < users.size()) {
                String uriPhoto = users.get(i).getUrl_prof_pic();
                final String name = users.get(i).getName();
                final String objId = users.get(i).getObject_id();
                final LatLng position = new LatLng(users.get(i).getLatitude(), users.get(i).getLongitude());

                if (!markers.containsKey(objId)) {
                    loadImage(users, uriPhoto, name, position, objId);
                } else {
                    Log.d(TAG, "Update many marker " + name + " latitude: " + position.latitude + " longitude: " + position.longitude);

                    Marker userMarker = markers.get(objId);
                    userMarker.setPosition(position);

                    i++;
                    recursiveMarker(users, true, null, null, null);
                }
            }
        } else {
            loadImage(null, urlPhoto, nameCurrent, locCurent, null);
        }
    }

    private void loadImage(final List<User> users, String uriPhoto, final String name, final LatLng position, final String objId) {
        Glide.with(getApplicationContext()).load(uriPhoto).asBitmap().into(new BitmapImageViewTarget(imgProf) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                super.onResourceReady(resource, glideAnimation);

                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(MarkerHelper.getBitmapFromView(linMarker, MainActivity.this));
                imgProf.setImageBitmap(resource);
                markerOptions.position(position).title(name).icon(bitmapDescriptor);

                if (objId != null) {
                    // if image already downloaded then draw marker using custom view
                    Marker userMarker = googleMap.addMarker(markerOptions);
                    markers.put(objId, userMarker);
                    Log.d(TAG, "Create many marker " + name + " latitude: " + position.latitude + " longitude: " + position.longitude);

                    i++;
                    recursiveMarker(users, true, null, null, null);
                } else {
                    myMarker = googleMap.addMarker(markerOptions);
                    Log.d(TAG, "Create one marker " + name + " latitude: " + position.latitude + " longitude: " + position.longitude);
                }
            }
        });
    }

    public void updateMarker() {
        List<User> users = UserRepository.getAll();
        if (users == null) {
            return;
        }

        if (markers == null) {
            markers = new Hashtable<>();
        }
        recursiveMarker(users, true, null, null, null);
    }

    private void closeActionBar() {
        materialMenu.setState(MaterialMenuDrawable.IconState.BURGER);
        title.setVisibility(View.VISIBLE);

        searchView.isIconfiedByDefault();
        searchView.onActionViewCollapsed();
        searchView.setQuery("", false);

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        removeFragment();
    }

    private void removeFragment() {
        FragmentManager fm1 = getFragmentManager();
        FragmentTransaction ft1 = fm1.beginTransaction();
        sf = (SearchFragment) fm1.findFragmentByTag("tag");
        ft1.remove(sf);
        if (flagSearch == true) {
            ft1.remove(searchFrag);
        }
        ft1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        ft1.commit();
    }

    public void getUserMapLocation(User user) {
        if (user != null) {
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

    @OnClick(R.id.rel_logout)
    public void logOut() {
        LoginManager.getInstance().logOut();
        UserData.clearUserData();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(sideDrawer)) {
            drawerLayout.closeDrawer(sideDrawer);
            materialMenu.setState(MaterialMenuDrawable.IconState.ARROW);
        }
        if (!searchView.isIconified()) {
            closeActionBar();
        } else {
            super.onBackPressed();
        }
    }
}