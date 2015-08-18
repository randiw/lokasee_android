package com.playing.lokasee;

import android.app.Application;

import com.parse.Parse;
import com.playing.lokasee.util.UtilStatic;

/**
 * Created by mexan on 8/18/15.
 */
public class LokaseeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_id));
    }
}
