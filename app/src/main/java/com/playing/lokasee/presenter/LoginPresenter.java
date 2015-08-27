package com.playing.lokasee.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.playing.lokasee.activites.LoginActivity;
import com.playing.lokasee.activites.MainActivity;
import com.playing.lokasee.helper.ParseHelper;
import com.playing.lokasee.helper.UserData;

import java.util.Arrays;

/**
 * Created by randi on 8/27/15.
 */
public class LoginPresenter extends BasePresenter<LoginActivity> {

    private static final String TAG = LoginPresenter.class.getSimpleName();

    private CallbackManager callbackManager;
    private LoginManager loginManager;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        callbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void loginFacebook() {
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Profile profile = Profile.getCurrentProfile();
                if (profile != null) {
                    UserData.saveFacebookLogin(profile.getId(), profile.getName());
                    UserData.saveFacebookProfPic(profile.getProfilePictureUri(50, 50).toString());
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
        loginManager.logInWithReadPermissions(getView(), Arrays.asList("public_profile"));
    }

    private void signUpParse(Profile profile) {
        ParseHelper.getInstance().signUp(profile.getFirstName(), profile.getLastName(), profile.getName(), profile.getProfilePictureUri(50, 50).toString(), profile.getId(), new ParseHelper.OnLogParseListener() {
            @Override
            public void onSuccess(ParseUser parseUser) {
                updateLocation();
            }

            @Override
            public void onError(ParseException pe) {
                Log.e(TAG, "signUp ParseException: " + pe.getMessage());
            }
        });
    }

    private void loginParse(final Profile profile) {
        ParseHelper.getInstance().login(profile.getFirstName(), profile.getLastName(), profile.getId(), new ParseHelper.OnLogParseListener() {
            @Override
            public void onSuccess(ParseUser parseUser) {
                updateParse(profile);
                updateLocation();
            }

            @Override
            public void onError(ParseException pe) {
                signUpParse(profile);
            }
        });
    }

    private void updateParse(Profile profile) {
        ParseHelper.getInstance().updateUser(profile.getFirstName(), profile.getLastName(), profile.getName(), profile.getProfilePictureUri(50, 50).toString(), profile.getId(), new ParseHelper.OnLogParseListener() {
            @Override
            public void onError(ParseException pe) {
                Log.e(TAG, "update parseException " + pe.getMessage());
            }

            @Override
            public void onSuccess(ParseUser parseUser) {
            }
        });
    }

    private void updateLocation() {
        ParseHelper.getInstance().saveMyLocation(UserData.getLatitude(), UserData.getLongitude(), new ParseHelper.OnSaveParseObjectListener() {
            @Override
            public void onSaveParseObject(ParseObject parseObject) {
                getView().startActivity(new Intent(getView(), MainActivity.class));
                getView().finish();
            }

            @Override
            public void onError(ParseException pe) {
                Log.e(TAG, "updateLocation ParseException: " + pe.getMessage());
            }
        });
    }
}