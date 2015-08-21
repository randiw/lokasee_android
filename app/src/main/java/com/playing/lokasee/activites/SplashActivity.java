package com.playing.lokasee.activites;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.playing.lokasee.R;
import com.playing.lokasee.helper.ParseHelper;
import com.playing.lokasee.helper.UserData;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.functions.Action1;

/**
 * Created by nabilla on 8/18/15.
 */
public class SplashActivity extends BaseActivity {

    private static final int SPLASH_TIME = 3000;
    private static final int SPLASH_INTERVAL = 500;
    private static final String tag = SplashActivity.class.getSimpleName();
    private boolean isFinishCountDown;
    private boolean isLocationRetrieved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupLayout(R.layout.activity_splash);

        FacebookSdk.sdkInitialize(getApplicationContext());

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
                Log.d(tag, "location response");
                isLocationRetrieved = true;
                if (location != null) {
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();

                    UserData.saveLocation(Double.toString(lat), Double.toString(lon));
                    if (ParseUser.getCurrentUser() != null) {
                        ParseHelper.getInstance().saveMyLocation(lat, lon, new ParseHelper.OnSaveParseObjectListener() {
                            @Override
                            public void onSaveParseObject(ParseObject parseObject) {

                            }

                            @Override
                            public void onError(ParseException pe) {

                            }
                        });
                    }
                }

                finishSplash();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.d(tag, "throwable " + throwable.getMessage());
                finishSplash();
            }
        });
    }

    private void finishSplash() {
        if (isFinishCountDown || isLocationRetrieved) {
            Intent intent;
            if (ParseUser.getCurrentUser() != null && AccessToken.getCurrentAccessToken() != null) {
                intent = new Intent(getApplicationContext(), MainActivity.class);
            } else {
                intent = new Intent(getApplicationContext(), LoginActivity.class);
            }

            startActivity(intent);
            finish();
        }
    }
}