package com.playing.lokasee.activites;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.facebook.appevents.AppEventsLogger;
import com.playing.lokasee.UserDao;
import com.playing.lokasee.helper.UserData;
import com.playing.lokasee.receiver.LocationAlarm;
import com.playing.lokasee.repositories.UserRepository;

import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;

/**
 * Created by nabilla on 8/19/15.
 */
public abstract class BaseActivity extends FragmentActivity {

    private LocationAlarm locationAlarm;

    private enum Alarm {
        SHORT(TimeUnit.MINUTES, 5),
        LONG(TimeUnit.HOURS, 1);

        private TimeUnit timeUnit;
        private int timeValue;

        private Alarm(TimeUnit timeUnit, int timeValue) {
            this.timeUnit = timeUnit;
            this.timeValue = timeValue;
        }

        public long time() {
            return timeUnit.toMillis(timeValue);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar();
    }

    protected void setupLayout(@LayoutRes int layout) {
        setContentView(layout);
        ButterKnife.bind(this);
    }

    private void initActionBar() {
        View customActionBar = createActionBar(LayoutInflater.from(getApplicationContext()));
        initActionBar(customActionBar);
    }

    protected void initActionBar(View customActionBar) {

        if (customActionBar != null) {
            ActionBar actionBar = getActionBar();
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(customActionBar);
        }

    }

    protected View createActionBar(LayoutInflater inflater) {
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
        startAlarm(Alarm.SHORT);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
        startAlarm(Alarm.LONG);
    }

    private void startAlarm(Alarm alarm) {
        if(locationAlarm == null) {
            locationAlarm = new LocationAlarm();
        }
        locationAlarm.cancelAlarm(getApplicationContext());
        locationAlarm.setAlarm(getApplicationContext(), alarm.time());
    }

    protected void setFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}