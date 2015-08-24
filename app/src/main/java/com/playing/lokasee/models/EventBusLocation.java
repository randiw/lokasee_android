package com.playing.lokasee.models;

import android.location.Location;

/**
 * Created by
 * Name : mexan.
 * Date : 8/21/2015.
 * Email : mexanjuadha17@gmail.com
 */
public class EventBusLocation {

    private static boolean statMapRefresh;
    private static Location location;


    public EventBusLocation(boolean statMapRefresh, Location location ){
        this.statMapRefresh = statMapRefresh;
        this.location = location;

    }

    public  static boolean isStatMapRefresh() {
        return statMapRefresh;
    }


    public static Location getLocation() {
        return location;
    }
}
