package com.playing.lokasee;

import android.app.Application;

import com.parse.Parse;
import com.playing.lokasee.util.UtilStatic;

/**
 * Created by mexan on 8/18/15.
 */
public class LokaseeApplication extends Application implements UtilStatic {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, PARSE_APP_ID, PARSE_ClIENT_ID);
    }
}
