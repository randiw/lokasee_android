package com.playing.lokasee.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.playing.lokasee.presenter.BasePresenterFragment;

import butterknife.ButterKnife;
import nucleus.view.NucleusFragment;

/**
 * Created by bils on 8/30/2015.
 */
public abstract class NucleusBaseFragment<T extends BasePresenterFragment> extends NucleusFragment<T> {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = setupLayout(inflater, container);
        ButterKnife.bind(this, view);
        return view;
    }

    protected abstract View setupLayout(LayoutInflater inflater, ViewGroup container);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}