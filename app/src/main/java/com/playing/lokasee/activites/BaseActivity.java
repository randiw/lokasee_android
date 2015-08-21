package com.playing.lokasee.activites;

import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.facebook.appevents.AppEventsLogger;
import com.playing.lokasee.User;
import com.playing.lokasee.helper.UserData;
import com.playing.lokasee.receiver.LocationAlarm;

import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;

/**
 * Created by nabilla on 8/19/15.
 */
public abstract class BaseActivity extends FragmentActivity {

    private LocationAlarm locationAlarm;
    private static int ALARM_SHORT = 30000; // 30 Second
    private static int ALARM_LONG = 60000; // 1 Minutes


    protected void setupLayout(@LayoutRes int layout) {
        setContentView(layout);
        ButterKnife.bind(this);
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

        if (UserData.isLogin()) {

            if(locationAlarm == null){
                locationAlarm = new LocationAlarm();
            }

            locationAlarm.cancelAlarm(getApplicationContext());
            locationAlarm.setAlarm(getApplicationContext(), (int) TimeUnit.MINUTES.toMillis(15));
        }
    }

    private void setAlarmLong() {
        if (UserData.isLogin()) {

            if(locationAlarm == null){
                locationAlarm = new LocationAlarm();
            }

            locationAlarm.cancelAlarm(getApplicationContext());
            locationAlarm.setAlarm(getApplicationContext(), TimeUnit.HOURS.toMillis(1));
        }
    }
}