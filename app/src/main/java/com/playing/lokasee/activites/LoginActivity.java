package com.playing.lokasee.activites;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInstaller;
import android.os.Bundle;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.parse.ParseObject;
import com.playing.lokasee.R;

import java.util.Arrays;

/**
 * Created by nabilla on 8/18/15.
 */
public class LoginActivity extends Activity {

    private static final String tag = LoginActivity.class.getSimpleName();

    LoginButton loginButton;
    CallbackManager callbackManager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile");
        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(tag, "On Success");
                Profile profile = Profile.getCurrentProfile();
                if(profile!=null){
                    testParse(profile.getId().toString(), profile.getName().toString());

                    Intent i = new Intent(getApplicationContext(), HomeMapsActivity.class);
                    startActivity(i);
                    finish();
                }
            }

            @Override
            public void onCancel() {
                // App code
                Log.v("facebook-onCancel", "cancelled");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.v("facebook-onError", exception.getMessage());
            }
        });
    }

    private void testParse(String userId, String userName){
        ParseObject testObject = new ParseObject("User");
        testObject.put("userId", userId);
        testObject.put("name", userName);
        testObject.put("lat", "-6.2330249");
        testObject.put("long", "106.8119497");
        testObject.saveInBackground();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(callbackManager.onActivityResult(requestCode, resultCode,data))
            return;
    }
}
