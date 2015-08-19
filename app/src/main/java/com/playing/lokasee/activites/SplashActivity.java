package com.playing.lokasee.activites;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

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
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                if(isLogin(getApplicationContext())&& isLoginFb()) {
                    Intent i = new Intent(getApplicationContext(), HomeMapsActivity.class);
                    startActivity(i);
                }else{
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(i);
                }
                finish();
            }
        }, SPLASH_TIME);
    }
}
