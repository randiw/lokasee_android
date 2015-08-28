package com.playing.lokasee.presenter;

import android.content.Context;
import android.os.Bundle;

import com.facebook.appevents.AppEventsLogger;
import com.playing.lokasee.LokaseeApplication;
import com.playing.lokasee.activites.NucleusBaseActivity;
import com.playing.lokasee.receiver.LocationAlarm;

import java.util.concurrent.TimeUnit;

import nucleus.presenter.RxPresenter;

/**
 * Created by randi on 8/27/15.
 */
public abstract class BasePresenter<T extends NucleusBaseActivity> extends RxPresenter<T> {

    private LocationAlarm locationAlarm;
    private Context context;

    private enum Alarm {
        SHORT(TimeUnit.SECONDS, 3),
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
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        context = LokaseeApplication.getInstance().getApplicationContext();
    }

    @Override
    protected void onTakeView(T t) {
        super.onTakeView(t);
        AppEventsLogger.activateApp(context);
        startAlarm(Alarm.SHORT);
    }

    @Override
    protected void onDropView() {
        super.onDropView();
        AppEventsLogger.deactivateApp(context);
        startAlarm(Alarm.LONG);
    }

    private void startAlarm(Alarm alarm) {
        if (locationAlarm == null) {
            locationAlarm = new LocationAlarm();
        }
        locationAlarm.cancelAlarm(context);
        locationAlarm.setAlarm(getView().getApplicationContext(), alarm.time());
    }

    protected Context getContext() {
        return context;
    }
}