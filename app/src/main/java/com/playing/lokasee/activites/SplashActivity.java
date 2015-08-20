package com.playing.lokasee.activites;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.facebook.AccessToken;
import com.playing.lokasee.R;
import com.playing.lokasee.helper.UserData;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.functions.Action1;

/**
 * Created by nabilla on 8/18/15.
 */
public class SplashActivity extends BaseActivity {

    private static final int SPLASH_TIME = 3000;
    private static final int SPLASH_INTERVAL = 500;

    private boolean isFinishCountDown;
    private boolean isLocationRetrieved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupLayout(R.layout.activity_splash);

        new CountDownTimer(SPLASH_TIME, SPLASH_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                isFinishCountDown = true;
                finishSplash();
            }
        }.start();

        retrieveLocation();
    }

    private void retrieveLocation() {
        ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(getApplicationContext());
        locationProvider.getLastKnownLocation().subscribe(new Action1<Location>() {
            @Override
            public void call(Location location) {
                isLocationRetrieved = true;
                if (location != null) {
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();

                    UserData.saveLocation(Double.toString(lat), Double.toString(lon));
                }
            }
        });
    }

    private void finishSplash() {
        if (isFinishCountDown && isLocationRetrieved) {
            Intent intent = null;
            if (UserData.isLogin() && AccessToken.getCurrentAccessToken() != null) {
                intent = new Intent(getApplicationContext(), MainActivity.class);
            } else {
                intent = new Intent(getApplicationContext(), LoginActivity.class);
            }

            startActivity(intent);
            finish();
        }
    }
}