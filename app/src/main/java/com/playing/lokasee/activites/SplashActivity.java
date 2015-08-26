package com.playing.lokasee.activites;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.norbsoft.typefacehelper.TypefaceHelper;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.playing.lokasee.R;
import com.playing.lokasee.helper.LocationManager;
import com.playing.lokasee.helper.ParseHelper;
import com.playing.lokasee.helper.FontLibHelper;
import com.playing.lokasee.helper.UserData;

import butterknife.Bind;
import rx.Scheduler;
import rx.functions.Action1;

/**
 * Created by nabilla on 8/18/15.
 */
public class SplashActivity extends BaseActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    private static final int SPLASH_TIME = 3000;
    private static final int SPLASH_INTERVAL = 500;

    private boolean isFinishCountDown;

    @Bind(R.id.txt_icon_lokasee) TextView txtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setupLayout(R.layout.activity_splash);

        TypefaceHelper.typeface(txtTitle, FontLibHelper.getHarbaraFace());
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
        LocationManager.checkLocation(getApplicationContext()).subscribe(new Action1<Location>() {
            @Override
            public void call(Location location) {
                if (location != null) {
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();

                    System.out.println("Latitude" + lat);
                    System.out.println("Longitude" + lon);


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
                } else {
                    Log.i(TAG, "location null");
                }
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