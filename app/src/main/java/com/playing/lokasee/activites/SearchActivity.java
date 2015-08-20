package com.playing.lokasee.activites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.playing.lokasee.R;
import com.playing.lokasee.model.User;
import com.playing.lokasee.util.UtilStatic;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nabilla on 8/19/15.
 */
public class SearchActivity extends BaseActivity {

    @Bind(R.id.listUser)
    ListView listviewUser;
    private static final String tag = SearchActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_search);

        setActionBar("LokaSee - Search");

        ButterKnife.bind(this);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    UtilStatic.users.clear();
                    for (int i = 0; i < list.size(); i++) {
                        String userId = (String) list.get(i).getString("userId");
                        String name = (String) list.get(i).getString("name");
                        //Double lat = Double.parseDouble(list.get(i).getString("lat").toString());
                        //Double lng = Double.parseDouble(list.get(i).getString("long").toString());
                        UtilStatic.users.add(new User(userId, name, -6.0453, 7.4535));
                    }
                    //Log.e(tag, UtilStatic.users.get(1).getLat().toString());
                    UserAdapter adapter = new UserAdapter(getApplicationContext(), UtilStatic.users);
                    listviewUser.setAdapter(adapter);
                    listviewUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent i = new Intent(getApplicationContext(), SearchMapActivity.class);
                            i.putExtra("pos", position);
                            startActivity(i);
                        }
                    });
                }
            }
        });

    }

    public  class UserAdapter extends BaseAdapter{
        List<User> userList = new ArrayList<>();
        Context context;

        public UserAdapter(Context context, List<User> users) {
            this.context = context;
            this.userList = users;
        }

        @Override
        public int getCount() {
            return userList.size();
        }

        @Override
        public Object getItem(int position) {
            return userList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        class ViewHolder{
//            @Bind(R.id.user)
//            TextView username;
            TextView username;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(convertView != null)
                holder = (ViewHolder) convertView.getTag();
            else {
                convertView = mInflater.inflate(R.layout.activity_search_detil, parent,false);
                holder = new ViewHolder();
                //ButterKnife.bind(this, convertView);
                holder.username = (TextView) convertView.findViewById(R.id.user);
                convertView.setTag(holder);
            }
            User user = (User) getItem(position);
            Log.e(tag, user.getName().toString());
            holder.username.setText(user.getName().toString());
            return convertView;
        }
    }

}
