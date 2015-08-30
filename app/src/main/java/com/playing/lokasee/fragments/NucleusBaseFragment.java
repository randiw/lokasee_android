package com.playing.lokasee.fragments;

import android.os.Bundle;

import com.playing.lokasee.presenter.BasePresenterFragment;

import nucleus.view.NucleusFragment;
import nucleus.view.NucleusSupportFragment;

/**
 * Created by bils on 8/30/2015.
 */
public class NucleusBaseFragment<T extends BasePresenterFragment> extends NucleusFragment<T> {

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
    }
}
