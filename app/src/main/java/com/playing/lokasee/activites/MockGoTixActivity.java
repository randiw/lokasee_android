package com.playing.lokasee.activites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.playing.lokasee.R;
import com.playing.lokasee.presenter.MockGoTixPresenter;

import butterknife.ButterKnife;
import nucleus.factory.RequiresPresenter;

/**
 * Created by randi on 9/10/15.
 */
@RequiresPresenter(MockGoTixPresenter.class)
public class MockGoTixActivity extends NucleusBaseActivity<MockGoTixPresenter> {

    private MaterialMenuView materialMenu;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupLayout(R.layout.activity_mock_gotix);
    }

    @Override
    protected View createActionBar(LayoutInflater inflater) {
        View actionbar = inflater.inflate(R.layout.actionbar, null);

        title = ButterKnife.findById(actionbar, R.id.title);
        title.setText("GO-TIX");

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
}