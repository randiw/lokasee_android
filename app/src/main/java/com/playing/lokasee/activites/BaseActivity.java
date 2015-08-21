package com.playing.lokasee.activites;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.facebook.appevents.AppEventsLogger;

import butterknife.ButterKnife;

/**
 * Created by nabilla on 8/19/15.
 */
public abstract class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar();
    }

    protected void setupLayout(@LayoutRes int layout) {
        setContentView(layout);
        ButterKnife.bind(this);
    }

    private void initActionBar() {
        View customActionBar = createActionBar(LayoutInflater.from(getApplicationContext()));
        initActionBar(customActionBar);
    }

    protected void initActionBar(View customActionBar) {
        if(customActionBar != null) {
            ActionBar actionBar = getActionBar();
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(customActionBar);
        }
    }

    protected View createActionBar(LayoutInflater inflater) {
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    protected void setFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}