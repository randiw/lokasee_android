package com.playing.lokasee.activites;

import android.os.Bundle;
import android.widget.TextView;

import com.norbsoft.typefacehelper.TypefaceHelper;
import com.playing.lokasee.R;
import com.playing.lokasee.helper.FontLibHelper;
import com.playing.lokasee.presenter.SplashPresenter;

import butterknife.Bind;
import nucleus.factory.RequiresPresenter;

/**
 * Created by nabilla on 8/18/15.
 */
@RequiresPresenter(SplashPresenter.class)
public class SplashActivity extends NucleusBaseActivity<SplashPresenter> {

    private static final String TAG = SplashActivity.class.getSimpleName();

    @Bind(R.id.txt_icon_lokasee) TextView txtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setupLayout(R.layout.activity_splash);

        TypefaceHelper.typeface(txtTitle, FontLibHelper.getHarbaraFace());
    }
}