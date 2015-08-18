package com.playing.lokasee;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

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
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_friends"));
        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            private ProfileTracker mProfilTracker;

            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(tag, "On Success");
                Profile profile = Profile.getCurrentProfile();
                if(profile!=null){
                    Log.e(tag, profile.getFirstName());
                    Log.e(tag, profile.getLastName());
                    Log.e(tag, profile.getId());
                    Log.e(tag,profile.getName());
                    Log.e(tag,profile.getLinkUri().toString());

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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(callbackManager.onActivityResult(requestCode, resultCode,data))
            return;
    }
}
