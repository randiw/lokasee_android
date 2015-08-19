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

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nabilla on 8/18/15.
 */
public class LoginActivity extends BaseActivity {

    private static final String tag = LoginActivity.class.getSimpleName();

    @Bind(R.id.login_button) LoginButton loginButton;
    CallbackManager callbackManager ;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        mContext = this;
        loginButton.setReadPermissions("public_profile");
        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(tag, "On Success");

                Profile profile = Profile.getCurrentProfile();
                if (profile != null) {
                    String idFb = profile.getId().toString();
                    String nama = profile.getName().toString();
                    SetSharedPrefs(LoginActivity.this, idFb, nama, "", "");
                    ArrayList<String> dataUser = new ArrayList<String>();
                    dataUser = GetSharedPrefs(LoginActivity.this);
                    Log.e(tag, "NAMA: " + dataUser.get(1).toString());
                    saveData(profile.getId().toString(), profile.getName().toString());

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


    private void saveData(final String userId, final String userName) {

        ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(mContext);

        locationProvider.getLastKnownLocation()
                .subscribe(new Action1<Location>() {
                    @Override
                    public void call(Location location) {

                        // if location detected then save data to parse

                        String lat = String.valueOf(location.getLatitude());
                        String lon = String.valueOf(location.getLongitude());

                        ParseObject testObject = new ParseObject("User");
                        testObject.put("userId", userId);
                        testObject.put("name", userName);
                        testObject.put("lat", lat);
                        testObject.put("long", lon);

                        testObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {

                                if (e == null) {

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
    }
}