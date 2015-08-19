package com.playing.lokasee.activites;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.playing.lokasee.R;
import com.playing.lokasee.model.User;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by nabilla on 8/19/15.
 */
public class SearchActivity extends Activity {

    @Bind(R.id.listUser)
    ListView listUser;
    List<User> users = new ArrayList<>();
    private static final String tag = SearchActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_search);

        DecimalFormat df = new DecimalFormat();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                    if(e==null){
                        for (int i = 0; i < list.size(); i++) {
                            String userId = (String) list.get(i).getString("userId");
                            String name = (String) list.get(i).getString("name");
                            Double lat = Double.parseDouble(list.get(i).getString("lat").toString());
                            Double lng = Double.parseDouble(list.get(i).getString("long").toString());
                            users.add(new User(userId, name, lat, lng));
                        }
                    }
            }
        });
        Log.e(tag, users.toString());
    }

}
