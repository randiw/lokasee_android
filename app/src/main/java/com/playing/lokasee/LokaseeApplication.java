package com.playing.lokasee;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.github.johnkil.print.PrintConfig;
import com.parse.Parse;
import com.playing.lokasee.helper.DataHelper;
import com.playing.lokasee.helper.FontLibHelper;
import com.playing.lokasee.view.adapter.UserContentProvider;

import io.fabric.sdk.android.Fabric;

/**
 * Created by mexan on 8/18/15.
 */
public class LokaseeApplication extends Application {

    private static LokaseeApplication instance;

    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        instance = this;

        DataHelper.init(instance);

        PrintConfig.initDefault(getAssets(), getString(R.string.MaterialIconFont));
        FacebookSdk.sdkInitialize(getApplicationContext());
        setupParse();

        setupDatabase();

        FontLibHelper.initFace(this);
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

        UserContentProvider.setDaoSession(daoSession);
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}