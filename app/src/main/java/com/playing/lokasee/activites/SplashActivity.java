package com.playing.lokasee.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.facebook.FacebookSdk;
import com.playing.lokasee.R;

/**
 * Created by nabilla on 8/18/15.
 */
public class SplashActivity extends Activity{

    private static int SPLASH_TIME = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // Initialize the SDK before executing any other operations,
        // especially, if you're using Facebook UI elements.
        FacebookSdk.sdkInitialize(getApplicationContext());
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
// After 4 seconds this will run and starts main activity
        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(i);

// close this activity
                finish();
            }
        }, SPLASH_TIME);
    }
}
