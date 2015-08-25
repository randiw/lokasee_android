package com.playing.lokasee.events;

import android.location.Location;

/**
 * Created by
 * Name : mexan.
 * Date : 8/21/2015.
 * Email : mexanjuadha17@gmail.com
 */
public class UpdateLocationEvent {

    public final Location location;

    public UpdateLocationEvent(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Location latitude: " + location.getLatitude() + ", longitude: " + location.getLongitude());
        return builder.toString();
    }
}