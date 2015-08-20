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
import com.parse.ParseUser;
import com.playing.lokasee.R;
import com.playing.lokasee.helper.ParseHelper;
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
                    loginParse(profile);
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

    private void signUpParse(Profile profile) {
        ParseHelper.getInstance().signUp(profile.getFirstName(), profile.getLastName(), profile.getName(), profile.getId(), new ParseHelper.OnLogParseListener() {
            @Override
            public void onSuccess(ParseUser parseUser) {
                updateLocation();
            }

            @Override
            public void onError(ParseException pe) {

            }
        });
    }

    private void loginParse(final Profile profile) {
        ParseHelper.getInstance().login(profile.getFirstName(), profile.getLastName(), profile.getId(), new ParseHelper.OnLogParseListener() {
            @Override
            public void onSuccess(ParseUser parseUser) {
                updateLocation();
            }

            @Override
            public void onError(ParseException pe) {
                signUpParse(profile);
            }
        });
    }

    private void updateLocation() {
        double latitude = Double.parseDouble(UserData.getLatitude());
        double longitude = Double.parseDouble(UserData.getLongitude());

        ParseHelper.getInstance().saveMyLocation(latitude, longitude, new ParseHelper.OnSaveParseObjectListener() {
            @Override
            public void onSaveParseObject(ParseObject parseObject) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
    }
}