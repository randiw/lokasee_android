package com.playing.lokasee.activites;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.playing.lokasee.R;

import java.util.ArrayList;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.functions.Action1;

/**
 * Created by nabilla on 8/18/15.
 */
public class LoginActivity extends BaseActivity {

    private static final String tag = LoginActivity.class.getSimpleName();

    LoginButton loginButton;
    CallbackManager callbackManager;


    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = this;

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile");
        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {

                Profile profile = Profile.getCurrentProfile();
                if (profile != null && isLoginFb()) {

                    if (profile != null) {

                        // if login isSuccess save profile data to parse
                        saveData(profile);
                    }
                }

            }

            @Override
            public void onCancel() {
                Log.v("facebook-onCancel", "cancelled");
            }

            @Override
            public void onError(FacebookException e) {
                Log.v("facebook-onError", e.getMessage());
            }

        });
    }


    private void saveData(final Profile profile) {

        ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(mContext);

        // Get location of use
        locationProvider.getLastKnownLocation()
                .subscribe(new Action1<Location>() {
                    @Override
                    public void call(Location location) {

                        String lat = String.valueOf(location.getLatitude());
                        String lon = String.valueOf(location.getLongitude());

                        // if location detected then save data to parse
                        final ParseObject testObject = new ParseObject("User");
                        testObject.put("fbId", profile.getId());
                        testObject.put("name", profile.getName());
                        testObject.put("lat", lat);
                        testObject.put("long", lon);

                        testObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {

                                // if saving data to parse success then
                                // set data to pref and intent to maps
                                if (e == null) {

                                    SetSharedPrefs(LoginActivity.this, testObject.getObjectId() , profile.getId(), profile.getName(), "", "");

                                    Intent i = new Intent(getApplicationContext(), HomeMapsActivity.class);
                                    startActivity(i);
                                    finish();

                                } else {

                                    e.printStackTrace();

                                }
                            }
                        });
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        loginButton.setVisibility(View.GONE);
        Intent i = new Intent(getApplicationContext(), HomeMapsActivity.class);
        startActivity(i);
        finish();
    }
}