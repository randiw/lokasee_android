package com.playing.lokasee.activites;

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
import com.playing.lokasee.R;
import com.playing.lokasee.UserDao;
import com.playing.lokasee.helper.ParseHelper;
import com.playing.lokasee.helper.UserData;
import com.playing.lokasee.repositories.UserRepository;

import java.util.Arrays;

import butterknife.OnClick;

/**
 * Created by nabilla on 8/18/15.
 */
public class LoginActivity extends BaseActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setupLayout(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.btn_fb_login)
    public void loginFb() {
        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile"));
    }

    private void signUpParse(Profile profile) {
        ParseHelper.getInstance().signUp(profile.getFirstName(), profile.getLastName(), profile.getName(), profile.getProfilePictureUri(50, 50).toString(), profile.getId(), new ParseHelper.OnLogParseListener() {
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
                updateParse(profile);
                updateLocation();
            }

            @Override
            public void onError(ParseException pe) {
                signUpParse(profile);
            }
        });
    }

    public static void updateParse(Profile profile) {
        ParseHelper.getInstance().updateUser(profile.getFirstName(), profile.getLastName(), profile.getName(), profile.getProfilePictureUri(50, 50).toString(), profile.getId(), new ParseHelper.OnLogParseListener() {
            @Override
            public void onError(ParseException pe) {
                Log.e(TAG, "update parse: gagal");
            }

            @Override
            public void onSuccess(ParseUser parseUser) {
                Log.e(TAG, "SUKSES UPDATE parse");
            }
        });
    }

    private void updateLocation() {
        ParseHelper.getInstance().saveMyLocation(UserData.getLatitude(), UserData.getLongitude(), new ParseHelper.OnSaveParseObjectListener() {
            @Override
            public void onSaveParseObject(ParseObject parseObject) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }

            @Override
            public void onError(ParseException pe) {

            }
        });
    }
}