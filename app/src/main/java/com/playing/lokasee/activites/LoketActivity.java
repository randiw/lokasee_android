package com.playing.lokasee.activites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.playing.lokasee.R;
import com.playing.lokasee.presenter.LoketPresenter;

import butterknife.ButterKnife;
import nucleus.factory.RequiresPresenter;

/**
 * Created by randi on 9/8/15.
 */
@RequiresPresenter(LoketPresenter.class)
public class LoketActivity extends NucleusBaseActivity<LoketPresenter> {

    private MaterialMenuView materialMenu;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupLayout(R.layout.activity_loket);
    }

    @Override
    protected View createActionBar(LayoutInflater inflater) {
        View actionbar = inflater.inflate(R.layout.actionbar, null);

        title = ButterKnife.findById(actionbar, R.id.title);
        title.setText("Loket.com");

        materialMenu = ButterKnife.findById(actionbar, R.id.menuIcon);
        materialMenu.setState(MaterialMenuDrawable.IconState.ARROW);
        materialMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        return actionbar;
    }

    public void startProgressDialog() {

    }

    public void dismissProgressDialog() {

    }
}
