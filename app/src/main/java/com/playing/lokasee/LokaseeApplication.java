package com.playing.lokasee;

import android.app.Activity;
import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.playing.lokasee.activites.MainActivity;
import com.playing.lokasee.activites.SplashActivity;
import com.playing.lokasee.helper.DataHelper;

/**
 * Created by mexan on 8/18/15.
 */
public class LokaseeApplication extends Application {

    private static LokaseeApplication instance;

    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        DataHelper.init(instance);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setupParse();

        setupDatabase();

    }

    public static synchronized LokaseeApplication getInstance() {
        return instance;
    }

    private void setupParse() {
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_id));
    }

    private void setupDatabase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "lokasee-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }


}