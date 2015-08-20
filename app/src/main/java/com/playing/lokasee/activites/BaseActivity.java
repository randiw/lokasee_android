package com.playing.lokasee.activites;

import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentActivity;

import com.facebook.appevents.AppEventsLogger;

import butterknife.ButterKnife;

/**
 * Created by nabilla on 8/19/15.
 */
public abstract class BaseActivity extends FragmentActivity {

    protected void setupLayout(@LayoutRes int layout) {
        setContentView(layout);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }
}