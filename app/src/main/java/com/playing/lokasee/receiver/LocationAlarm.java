package com.playing.lokasee.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.playing.lokasee.R;
import com.playing.lokasee.helper.BusProvider;
import com.playing.lokasee.helper.LocationManager;
import com.playing.lokasee.helper.ParseHelper;
import com.playing.lokasee.helper.UserData;
import com.playing.lokasee.models.EventBusLocation;
import com.squareup.otto.Produce;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.functions.Action1;

/**
 * Created by mexan on 8/21/15.
 */
public class LocationAlarm extends BroadcastReceiver {

    private static final String TAG = LocationAlarm.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        detectLocation(context);
    }

    public void setAlarm(Context context, long durationTime) {

        BusProvider.getInstance().register(context);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, LocationAlarm.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);

        // run after duration seconds
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), durationTime, pi);
    }

    public void cancelAlarm(Context mContext) {

        BusProvider.getInstance().unregister(mContext);

        Intent intent = new Intent(mContext, LocationAlarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(mContext, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    private void detectLocation(final Context mContext) {

        LocationManager.checkLocation(mContext).subscribe(new Action1<Location>() {
            @Override
            public void call(Location location) {
                if (location != null) {

                    UserData.saveLocation(Double.toString(location.getLatitude()), Double.toString(location.getLongitude()));
                    updateLocation(location.getLatitude(), location.getLongitude());
                    // If updating location success
                    // then broadcast to activity to refresh maps
                    BusProvider.getInstance().post(new EventBusLocation(true, location));
                } else {

                    Log.e(TAG, mContext.getString(R.string.error_message_location));
                    // If updating location failed
                    // then broadcast to activity to don't refresh maps
                    BusProvider.getInstance().post(new EventBusLocation(false, null));
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });
    }


    private void updateLocation(Double lat, Double lon) {
        ParseHelper.getInstance().saveMyLocation(lat, lon, new ParseHelper.OnSaveParseObjectListener() {
            @Override
            public void onSaveParseObject(ParseObject parseObject) {
            }

            @Override
            public void onError(ParseException pe) {
                pe.printStackTrace();
            }
        });
    }
}
