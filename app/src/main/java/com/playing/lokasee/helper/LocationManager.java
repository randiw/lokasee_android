package com.playing.lokasee.helper;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.LocationRequest;


import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;

/**
 * Created by RETRO on 23/08/2015.
 */
public class LocationManager {

    private static final int NUM_UPDATES_INTERVAL = 5;
    private static final int INTERVAL = 100;

    public static Observable<Location> checkLocation(Context mContext) {
        ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(mContext);

        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(NUM_UPDATES_INTERVAL)
                .setInterval(INTERVAL);

        return locationProvider.getUpdatedLocation(locationRequest);
    }
}
