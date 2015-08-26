package com.playing.lokasee.helper;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.LocationRequest;

import java.util.concurrent.TimeUnit;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by RETRO on 23/08/2015.
 */
public class LocationManager {

    private static final int NUM_UPDATES_INTERVAL = 5;
    private static final int INTERVAL = 100;
    private static final int LOCATION_TIMEOUT = 2;


    public static Observable<Location> checkLocation(Context mContext) {
        ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(mContext);

        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(NUM_UPDATES_INTERVAL)
                .setInterval(INTERVAL);

        Observable<Location> locationObservable =  locationProvider.getLastKnownLocation()
                .timeout(LOCATION_TIMEOUT, TimeUnit.SECONDS, Observable.just((Location) null))
                .concatWith(locationProvider.getUpdatedLocation(locationRequest))
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread());

        return locationObservable;
    }
}