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
        if(customActionBar != null) {
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

        setAlarmShort();
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);

        setAlarmLong();
    }

    @Override
    protected void onStart() {
        super.onStart();

        setAlarmShort();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        setAlarmLong();
    }

    private void setAlarmShort() {
        if (UserRepository.isDataExist()) {

            if(locationAlarm == null){
                locationAlarm = new LocationAlarm();
            }

            locationAlarm.cancelAlarm(getApplicationContext());
            locationAlarm.setAlarm(getApplicationContext(),  TimeUnit.MINUTES.toMillis(1));
        }
    }

    private void setAlarmLong() {
        if (UserRepository.isDataExist()) {

            if(locationAlarm == null){
                locationAlarm = new LocationAlarm();
            }

            locationAlarm.cancelAlarm(getApplicationContext());
            locationAlarm.setAlarm(getApplicationContext(), TimeUnit.HOURS.toMillis(1));
        }
    }

    protected void setFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}