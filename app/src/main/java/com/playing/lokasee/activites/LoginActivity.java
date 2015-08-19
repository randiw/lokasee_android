package com.playing.lokasee.activites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.parse.ParseObject;
import com.playing.lokasee.R;

import java.util.ArrayList;
import com.facebook.*;

/**
 * Created by nabilla on 8/18/15.
 */
public class LoginActivity extends BaseActivity {

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
                if(profile!=null && isLoginFb()){
                    String idFb = profile.getId().toString();
                    String nama = profile.getName().toString();
                    SetSharedPrefs(LoginActivity.this, idFb, nama, "", "");
                    ArrayList<String> dataUser = new ArrayList<String>();
                    dataUser = GetSharedPrefs(LoginActivity.this);
                    Log.e(tag, "NAMA: "+dataUser.get(1).toString());

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
        callbackManager.onActivityResult(requestCode, resultCode, data);
        loginButton.setVisibility(View.GONE);
        Intent i = new Intent(getApplicationContext(), HomeMapsActivity.class);
        startActivity(i);
        finish();
    }
}
