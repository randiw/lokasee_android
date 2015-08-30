package com.playing.lokasee.fragments;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.playing.lokasee.R;
import com.playing.lokasee.User;
import com.playing.lokasee.UserDao;
import com.playing.lokasee.helper.BusProvider;
import com.playing.lokasee.presenter.SearchPresenter;
import com.playing.lokasee.repositories.provider.UserContentProvider;
import com.playing.lokasee.view.adapter.UserCursorAdapter;

import nucleus.factory.RequiresPresenter;

/**
 * Created by nabilla on 8/27/15.
 */
@RequiresPresenter(SearchPresenter.class)
public class SearchFragment extends NucleusBaseFragment<SearchPresenter> implements LoaderManager.LoaderCallbacks<Cursor>, SearchView.OnQueryTextListener {

    private static String TAG = SearchFragment.class.getSimpleName();
    private static final int LIST_ID = 11;

    private ListView listviewUser;
    private String name = null;
    private UserCursorAdapter adapter;

    public static SearchFragment newInstance() {
        SearchFragment searchFragment = new SearchFragment();
        return searchFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        listviewUser = (ListView) rootView.findViewById(R.id.listUser);

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
    public void onViewCreated(View view, Bundle savedStateInstance) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (LIST_ID == id) {
            String selection = null;
            if(name != null){
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.i(TAG, newText);
        name = newText;
        getLoaderManager().restartLoader(LIST_ID, null, SearchFragment.this);
        return false;
    }
}