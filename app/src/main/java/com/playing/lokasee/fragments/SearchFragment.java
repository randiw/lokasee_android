package com.playing.lokasee.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import com.playing.lokasee.R;
import com.playing.lokasee.User;
import com.playing.lokasee.UserDao;
import com.playing.lokasee.events.ReturnSearchEvent;
import com.playing.lokasee.helper.BusProvider;
import com.playing.lokasee.presenter.SearchPresenter;
import com.playing.lokasee.repositories.provider.UserContentProvider;
import com.playing.lokasee.view.adapter.UserCursorAdapter;

import butterknife.Bind;
import butterknife.OnItemClick;
import nucleus.factory.RequiresPresenter;

/**
 * Created by nabilla on 8/27/15.
 */
@RequiresPresenter(SearchPresenter.class)
public class SearchFragment extends NucleusBaseFragment<SearchPresenter> implements LoaderManager.LoaderCallbacks<Cursor>, SearchView.OnQueryTextListener {

    private static String TAG = SearchFragment.class.getSimpleName();
    private static final int LIST_ID = 11;

    @Bind(R.id.listUser) ListView listviewUser;

    private String name = null;
    private UserCursorAdapter adapter;

    public static SearchFragment newInstance() {
        SearchFragment searchFragment = new SearchFragment();
        return searchFragment;
    }

    public SearchFragment(){}

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
    }

    @Override
    protected View setupLayout(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedStateInstance) {
        super.onActivityCreated(savedStateInstance);
        adapter = new UserCursorAdapter(getActivity());
        listviewUser.setAdapter(adapter);

        getLoaderManager().initLoader(LIST_ID, null, SearchFragment.this);
    }

    @OnItemClick(R.id.listUser)
    public void onClickUser(int position) {
        User user = adapter.getItem(position);
        BusProvider.getInstance().post(new ReturnSearchEvent(user));
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