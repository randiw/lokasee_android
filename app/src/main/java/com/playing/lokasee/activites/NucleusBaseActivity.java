package com.playing.lokasee.activites;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.playing.lokasee.presenter.BasePresenter;

import butterknife.ButterKnife;
import nucleus.view.NucleusFragmentActivity;

/**
 * Created by randi on 8/27/15.
 */
public abstract class NucleusBaseActivity<T extends BasePresenter> extends NucleusFragmentActivity<T> {

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
        if (customActionBar != null) {
            ActionBar actionBar = getActionBar();
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(customActionBar);
        }
    }

    protected View createActionBar(LayoutInflater inflater) {
        return null;
    }

    protected void setFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}