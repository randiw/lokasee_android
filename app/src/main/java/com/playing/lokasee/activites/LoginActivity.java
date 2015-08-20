package com.playing.lokasee.activites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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
import com.playing.lokasee.helper.UserData;

import butterknife.Bind;

/**
 * Created by nabilla on 8/18/15.
 */
public class LoginActivity extends BaseActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    @Bind(R.id.login_button) LoginButton loginButton;

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupLayout(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();

        loginButton.setReadPermissions("public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Profile profile = Profile.getCurrentProfile();
                if (profile != null) {
                    UserData.saveFacebookLogin(profile.getId(), profile.getName());
                    saveData(profile);
                }
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebookLogin cancelled");
            }

            @Override
            public void onError(FacebookException e) {
                Log.e(TAG, "facebookException: " + e.getMessage());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void saveData(final Profile profile) {
        final ParseObject userObject = new ParseObject("User");
        userObject.put("fbId", profile.getId());
        userObject.put("name", profile.getName());
        userObject.put("lat", UserData.getLastLat());
        userObject.put("long", UserData.getLastLon());

        userObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    UserData.saveParseResponse(userObject.getObjectId());

                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Log.e(TAG, "parseException: " + e.getMessage());
                }
            }
        });
    }
}