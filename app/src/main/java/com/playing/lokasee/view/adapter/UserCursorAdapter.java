package com.playing.lokasee.view.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.playing.lokasee.R;
import com.playing.lokasee.UserDao;
import com.playing.lokasee.tools.RepoTools;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nabilla on 8/21/15.
 */
public class UserCursorAdapter extends CursorAdapter {

    public UserCursorAdapter(Context context) {
        super(context, null, false);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View customListView = LayoutInflater.from(context).inflate(R.layout.activity_search_detil, null);

        ViewHolder holder = new ViewHolder(customListView);
        customListView.setTag(holder);

        return customListView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        String name = RepoTools.getString(cursor, UserDao.Properties.Name.columnName);
        holder.user.setText(name);
    }

    static class ViewHolder {

        @Bind(R.id.user) TextView user;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}