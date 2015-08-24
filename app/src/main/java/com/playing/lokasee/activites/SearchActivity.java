package com.playing.lokasee.activites;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.playing.lokasee.DaoMaster;
import com.playing.lokasee.LokaseeApplication;
import com.playing.lokasee.R;
import com.playing.lokasee.User;
import com.playing.lokasee.UserDao;
import com.playing.lokasee.helper.ParseHelper;
import com.playing.lokasee.repositories.UserRepository;
import com.playing.lokasee.view.adapter.UserAdapter;
import com.playing.lokasee.view.adapter.UserContentProvider;
import com.playing.lokasee.view.adapter.UserCursorAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Handler;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nabilla on 8/19/15.
 */
public class SearchActivity extends BaseActivity {

    @Bind(R.id.listUser)
    ListView listviewUser;
    @Bind(R.id.searchEdit)
    EditText searchEdit;
    private Cursor cursor;
    private static final String tag = SearchActivity.class.getSimpleName();
    UserCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "lokasee-db", null);
        cursor = helper.getWritableDatabase().rawQuery("SELECT * from USER", null);

        adapter = new UserCursorAdapter(getApplicationContext(), cursor);
        listviewUser.setAdapter(adapter);

        listviewUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor itemCursor = (Cursor) SearchActivity.this.listviewUser.getItemAtPosition(i);
                String objId = itemCursor.getString(1);
                Intent intent = new Intent(getApplicationContext(), SearchMapActivity.class);
                intent.putExtra("objId", objId);
                startActivity(intent);
            }
        });
    }
}
