package com.playing.lokasee.activites;

import android.os.Bundle;
import android.util.Log;

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
import com.playing.lokasee.helper.ParseHelper;
import com.playing.lokasee.helper.UserData;
import com.playing.lokasee.repositories.UserRepository;

import java.util.Hashtable;
import java.util.List;

public class MainActivity extends BaseActivity implements OnMapReadyCallback {

    private static final String TAG = MainActivity.class.getSimpleName();

    private GoogleMap googleMap;
    private Marker myMarker;
    private Hashtable<String, Marker> markers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupLayout(R.layout.activity_main);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        double lat = Double.parseDouble(UserData.getLatitude());
        double lon = Double.parseDouble(UserData.getLongitude());

        setMyLocation(lat, lon);
        retrieveMarkers();
    }

    private void setMyLocation(double latitude, double longitude) {
        if (googleMap != null) {
            LatLng position = new LatLng(latitude, longitude);
            float zoom = 14.0f;

            if (myMarker == null) {
                myMarker = googleMap.addMarker(new MarkerOptions()
                        .position(position)
                        .title("I am here"));
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
        if(users == null){
            return;
        }

        if(markers == null) {
            markers = new Hashtable<>();
        }
        for(User user : users) {
            LatLng latLng = new LatLng(user.getLatitude(), user.getLongitude());
            if(!markers.contains(user.getObject_id())) {
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
}