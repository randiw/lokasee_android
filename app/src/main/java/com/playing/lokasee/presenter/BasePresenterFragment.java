package com.playing.lokasee.presenter;

import android.content.Context;
import android.os.Bundle;

import com.playing.lokasee.LokaseeApplication;
import com.playing.lokasee.fragments.NucleusBaseFragment;

import nucleus.presenter.RxPresenter;

/**
 * Created by bils on 8/30/2015.
 */
public class BasePresenterFragment<T extends NucleusBaseFragment> extends RxPresenter<T> {

    private Context context;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        context = LokaseeApplication.getInstance().getApplicationContext();
    }

    @Override
    protected void onTakeView(T t) {
        super.onTakeView(t);
    }

    @Override
    protected void onDropView() {
        super.onDropView();
    }

    protected Context getContext() {
        return context;
    }
}
