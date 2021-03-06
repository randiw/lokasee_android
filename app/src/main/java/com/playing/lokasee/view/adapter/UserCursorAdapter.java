package com.playing.lokasee.view.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.playing.lokasee.R;
import com.playing.lokasee.User;
import com.playing.lokasee.UserDao;
import com.playing.lokasee.repositories.UserRepository;
import com.playing.lokasee.tools.RepoTools;
import com.playing.lokasee.tools.RoundImage;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nabilla on 8/21/15.
 */
public class UserCursorAdapter extends CursorAdapter {

    private static String tag = UserCursorAdapter.class.getSimpleName();

    public UserCursorAdapter(Context context) {
        super(context, null, false);
    }

    @Override
    public User getItem(int position) {
        Cursor cursor = getCursor();
        if(!cursor.moveToPosition(position)){
            return null;
        }
        return UserRepository.create(cursor);
    }

    @Override
    public int getCount() {
        Cursor cursor = getCursor();
        if(cursor == null) {
            return 0;
        }
        return cursor.getCount();
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
        String profpic = RepoTools.getString(cursor, UserDao.Properties.Url_prof_pic.columnName);

        holder.user.setText(name);
        Glide.with(context).load(profpic).transform(new RoundImage(context)).into(holder.imageUser);
    }

    static class ViewHolder {

        @Bind(R.id.user) TextView user;
        @Bind(R.id.imageUser) ImageView imageUser;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}