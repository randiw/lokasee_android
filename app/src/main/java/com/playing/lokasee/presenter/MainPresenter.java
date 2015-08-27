package com.playing.lokasee.presenter;

import android.os.Bundle;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.playing.lokasee.activites.MainActivity;
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

    @Subscribe
    public void onUpdateLocation(UpdateLocationEvent updateLocationEvent) {
        if (updateLocationEvent.location != null) {
            getView().onUpdateLocation(updateLocationEvent.location);
            retrieveMarkers();
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