package com.playing.lokasee.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.playing.lokasee.User;
import com.playing.lokasee.activites.LoketActivity;
import com.playing.lokasee.activites.MainActivity;
import com.playing.lokasee.activites.MockGoTixActivity;
import com.playing.lokasee.events.ReturnSearchEvent;
import com.playing.lokasee.events.UpdateLocationEvent;
import com.playing.lokasee.helper.BusProvider;
import com.playing.lokasee.helper.ParseHelper;
import com.squareup.otto.Subscribe;

import java.util.List;

/**
 * Created by randi on 8/27/15.
 */
public class MainPresenter extends BasePresenter<MainActivity> {

    private static final String TAG = MainPresenter.class.getSimpleName();

    private void retrieveMarkers() {
        ParseHelper.getInstance().getAllUser(new ParseHelper.OnParseQueryListener() {
            @Override
            public void onParseQuery(List<ParseObject> parseObjectList) {
                getView().updateMarker();
            }

            @Override
            public void onError(ParseException pe) {
                Log.e(TAG, "parse query user exception: " + pe.getMessage());
            }
        });
    }

    public void openLoket() {
        getView().startActivity(new Intent(getView(), LoketActivity.class));
    }

    public void openGotix() {
        getView().startActivity(new Intent(getView(), MockGoTixActivity.class));
    }

    @Subscribe
    public void onUpdateLocation(UpdateLocationEvent updateLocationEvent) {
        if (updateLocationEvent.location != null) {
            getView().onUpdateLocation(updateLocationEvent.location);
            retrieveMarkers();
        }
    }

    @Subscribe
    public void onReturnSearchEvent(ReturnSearchEvent returnSearchEvent){
        if(returnSearchEvent != null) {
            getView().getUserMapLocation(returnSearchEvent.user);
        }
    }

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        BusProvider.getInstance().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }
}