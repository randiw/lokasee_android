package com.playing.lokasee.activites;

import android.content.Intent;
import android.os.Bundle;

import com.playing.lokasee.R;
import com.playing.lokasee.presenter.LoginPresenter;

import butterknife.OnClick;

/**
 * Created by nabilla on 8/18/15.
 */
public class LoginActivity extends NucleusBaseActivity<LoginPresenter> {

    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setupLayout(R.layout.activity_login);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getPresenter().onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.btn_fb_login)
    public void loginFb() {
        getPresenter().loginFacebook();
    }
}