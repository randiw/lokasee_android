package com.playing.lokasee.activites;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.playing.lokasee.R;
import com.playing.lokasee.User;
import com.playing.lokasee.UserDao;
import com.playing.lokasee.view.adapter.UserContentProvider;
import com.playing.lokasee.view.adapter.UserCursorAdapter;

import com.playing.lokasee.DaoMaster;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * Created by nabilla on 8/19/15.
 */
public class SearchActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = SearchActivity.class.getSimpleName();
    private static final int LIST_ID = 42;

    @Bind(R.id.listUser) ListView listviewUser;
    @Bind(R.id.searchEdit) EditText searchEdit;
    UserCursorAdapter adapter;
    String textSearch;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        adapter = new UserCursorAdapter(getApplicationContext());
        listviewUser.setAdapter(adapter);

        getLoaderManager().initLoader(LIST_ID, null, this);

        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(s)){
                    textSearch = null;
                }else{
                    textSearch = s.toString();
                }
                getLoaderManager().restartLoader(LIST_ID, null, SearchActivity.this);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnItemClick(R.id.listUser)
    public void clickUser(int position) {
        User user = adapter.getItem(position);
        Intent intent = new Intent(getApplicationContext(), SearchMapActivity.class);
        intent.putExtra("objId", user.getObject_id());
        startActivity(intent);
        Log.d(TAG, "Name: " + user.getName() + " objectId: " + user.getObject_id());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if (LIST_ID != id) {
            return null;
        } else {
            if(textSearch == null)
                return new CursorLoader(SearchActivity.this, UserContentProvider.CONTENT_URI, null, null, null, null);
            else {
                String selection = UserDao.Properties.Name.columnName + " like '%" + textSearch.toString() + "%'";
                return new CursorLoader(SearchActivity.this, UserContentProvider.CONTENT_URI, null, selection, null, null);
            }
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == LIST_ID) {
            adapter.swapCursor(data);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (loader.getId() == LIST_ID) {
            adapter.swapCursor(null);
            adapter.notifyDataSetChanged();
        }
    }
}
