package com.playing.lokasee;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.playing.lokasee.helper.DataHelper;

/**
 * Created by mexan on 8/18/15.
 */
public class LokaseeApplication extends Application {

    public static final String TAG = LokaseeApplication.class.getSimpleName();

    private static LokaseeApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        DataHelper.init(instance);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setupParse();
    }

    public static synchronized LokaseeApplication getInstance() {
        return instance;
    }

    private void setupParse() {
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_id));
    }
}