package com.playing.lokasee.fragments;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.playing.lokasee.R;
import com.playing.lokasee.User;
import com.playing.lokasee.UserDao;
import com.playing.lokasee.helper.BusProvider;
import com.playing.lokasee.repositories.provider.UserContentProvider;
import com.playing.lokasee.view.adapter.UserCursorAdapter;

/**
 * Created by nabilla on 8/27/15.
 */
public class SearchFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static String TAG = SearchFragment.class.getSimpleName();

    private static final int LIST_ID = 11;

    private String name = null;
    private UserCursorAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        ListView listviewUser = (ListView) rootView.findViewById(R.id.listUser);

        adapter = new UserCursorAdapter(getActivity());
        listviewUser.setAdapter(adapter);
        listviewUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = adapter.getItem(position);
                BusProvider.getInstance().post(user);
            }
        });
        getLoaderManager().initLoader(LIST_ID, null, SearchFragment.this);
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (LIST_ID == id) {
            String selection = null;
            if (name != null) {
                selection = UserDao.Properties.Name.columnName + " like '%" + name + "%'";
            }
            return new CursorLoader(getActivity(), UserContentProvider.CONTENT_URI, null, selection, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (LIST_ID == loader.getId()) {
            adapter.swapCursor(data);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (LIST_ID == loader.getId()) {
            adapter.swapCursor(null);
            adapter.notifyDataSetChanged();
        }
    }

    public void search(String key) {
        name = key;
        getLoaderManager().restartLoader(LIST_ID, null, SearchFragment.this);
    }
}