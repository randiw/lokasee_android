package com.playing.lokasee.presenter;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import com.facebook.AccessToken;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.playing.lokasee.activites.LoginActivity;
import com.playing.lokasee.activites.MainActivity;
import com.playing.lokasee.activites.SplashActivity;
import com.playing.lokasee.helper.LocationManager;
import com.playing.lokasee.helper.ParseHelper;
import com.playing.lokasee.helper.UserData;

import rx.functions.Action1;

/**
 * Created by randi on 8/27/15.
 */
public class SplashPresenter extends BasePresenter<SplashActivity> {

    private static final String TAG = SplashPresenter.class.getSimpleName();

    private static final int SPLASH_TIME = 3000;
    private static final int SPLASH_INTERVAL = 500;
    private static boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        new CountDownTimer(SPLASH_TIME, SPLASH_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                finishSplash();
            }
        }.start();

        retrieveLocation();
    }

    private void retrieveLocation() {
        LocationManager.checkLocation(getContext()).subscribe(new Action1<Location>() {
            @Override
            public void call(Location location) {

                if (location != null && !isRunning) {
                    isRunning = true;
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();

                    UserData.saveLocation(Double.toString(lat), Double.toString(lon));
                    if (ParseUser.getCurrentUser() != null) {
                        ParseHelper.getInstance().saveMyLocation(lat, lon, new ParseHelper.OnSaveParseObjectListener() {
                            @Override
                            public void onSaveParseObject(ParseObject parseObject) {
                                isRunning = false;
                            }

                            @Override
                            public void onError(ParseException pe) {
                                isRunning = false;
                            }
                        });
                    }
                } else {
                    Log.i(TAG, "location null");
                }

                Log.i(TAG, "retrieveLocation()");
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                finishSplash();
            }
        });
    }

    private void finishSplash() {
        Intent intent;

        if (ParseUser.getCurrentUser() != null && AccessToken.getCurrentAccessToken() != null) {
            intent = new Intent(getView(), MainActivity.class);
        } else {
            intent = new Intent(getView(), LoginActivity.class);
        }

        getView().startActivity(intent);
        getView().finish();
    }
}