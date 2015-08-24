package com.playing.lokasee.activites;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.google.android.gms.location.LocationRequest;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.playing.lokasee.R;
import com.playing.lokasee.helper.LocationManager;
import com.playing.lokasee.helper.ParseHelper;
import com.playing.lokasee.helper.UserData;

import java.util.concurrent.TimeUnit;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by nabilla on 8/18/15.
 */
public class SplashActivity extends BaseActivity {

    private static final int SPLASH_TIME = 3000;
    private static final int SPLASH_INTERVAL = 500;

    private boolean isFinishCountDown;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setupLayout(R.layout.activity_splash);
        mContext = this;
    }

    @Override
    protected void onStart() {
        super.onStart();
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
        LocationManager.checkLocation(mContext).subscribe(new Action1<Location>() {
            @Override
            public void call(Location location) {
                System.out.println(" throwable " + location);

                if (location != null) {
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();

                    System.out.println("lat on retrieve " + lat);

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
                finishSplash();
            }
        });
    }


    private void finishSplash() {
        if (isFinishCountDown) {
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