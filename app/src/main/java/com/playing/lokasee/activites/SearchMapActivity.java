package com.playing.lokasee.activites;


import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.playing.lokasee.R;
import com.playing.lokasee.util.UtilStatic;

import java.security.interfaces.DSAPublicKey;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nabilla on 8/20/15.
 */
public class SearchMapActivity extends BaseActivity {

    @Bind(R.id.username)
    TextView username;
    private GoogleMap googleMap;
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_maps);

//        setActionBar("LokaSee - Search");

        ButterKnife.bind(this);
        Intent i = getIntent();
        pos = i.getIntExtra("pos", 0);
        username.setText(UtilStatic.users.get(pos).getName());
        initializeMap();
    }

    private void initializeMap() {
        if(googleMap == null){
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

            if(googleMap == null){
                Toast.makeText(getApplicationContext(), "Sorry! unable create maps", Toast.LENGTH_SHORT).show();
            }
        }else {
            LatLng user = new LatLng(UtilStatic.users.get(pos).getLat(),UtilStatic.users.get(pos).getLng());
            MarkerOptions marker = new MarkerOptions().position(user).title(UtilStatic.users.get(pos).getName());
            CameraPosition cameraPosition = new CameraPosition.Builder().target(user).zoom(12).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
            googleMap.addMarker(marker);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        initializeMap();
    }
}
