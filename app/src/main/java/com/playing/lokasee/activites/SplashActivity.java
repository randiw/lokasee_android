package com.playing.lokasee.activites;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookSdk;
import com.playing.lokasee.R;

/**
 * Created by nabilla on 8/18/15.
 */
public class SplashActivity extends BaseActivity{

    private static int SPLASH_TIME = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        FacebookSdk.sdkInitialize(getApplicationContext());
        updateWithToken(AccessToken.getCurrentAccessToken());

        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
                updateWithToken(newAccessToken);
            }
        };
    }

    private void updateWithToken(AccessToken currentAccessToken){
        if(currentAccessToken != null){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(SplashActivity.this, HomeMapsActivity.class);
                    startActivity(i);
                    finish();
                }
            }, SPLASH_TIME);
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE).edit().clear().commit();
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }, SPLASH_TIME);
        }
    }
}
