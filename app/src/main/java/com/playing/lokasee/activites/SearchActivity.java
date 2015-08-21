package com.playing.lokasee.activites;

import android.content.Context;
import android.content.Intent;
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
import com.playing.lokasee.R;
import com.playing.lokasee.User;
import com.playing.lokasee.repositories.UserRepository;
import com.playing.lokasee.view.adapter.UserAdapter;
import com.playing.lokasee.view.adapter.UserCursorAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    private static final String tag = SearchActivity.class.getSimpleName();
    UserCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        adapter = new UserCursorAdapter(getApplicationContext());
//        adapter.notifyDataSetChanged();
//        listviewUser.setAdapter(adapter);
//        listviewUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent i = new Intent(getApplicationContext(), SearchMapActivity.class);
//                User user1 = (User) adapter.getItem(position);
//                i.putExtra("objId", user1.getObject_id());
//                startActivity(i);
//            }
//        });

        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = searchEdit.getText().toString().toLowerCase(Locale.getDefault());
//                adapter.filter(text);
                adapter.notifyDataSetInvalidated();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Log.e(tag, "masuk");

    }

//    public  class UserAdapter extends BaseAdapter{
//        List<User> search;
//        Context context;
//
//        public UserAdapter(Context context, List<User> users) {
//            this.context = context;
//            userList = users;
//            this.search = new ArrayList<>();
//            search.addAll(users);
//        }
//
//        @Override
//        public int getCount() {
//            return userList.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return userList.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        public void filter(String text) {
//            text = text.toLowerCase(Locale.getDefault());
//            userList.clear();
//            if(text.length()==0){
//                userList.addAll(search);
//            }else {
//                for(User usr : search){
//                    if(usr.getName().toLowerCase(Locale.getDefault()).contains(text)){
//                        userList.add(usr);
//                    }
//                }
//                notifyDataSetChanged();
//            }
//        }
//
//        class ViewHolder{
////            @Bind(R.id.user)
////            TextView username;
//            TextView username;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ViewHolder holder;
//            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            if(convertView != null)
//                holder = (ViewHolder) convertView.getTag();
//            else {
//                convertView = mInflater.inflate(R.layout.activity_search_detil, parent,false);
//                holder = new ViewHolder();
//                //ButterKnife.bind(this, convertView);
//                holder.username = (TextView) convertView.findViewById(R.id.user);
//                convertView.setTag(holder);
//            }
//            User user = (User) getItem(position);
//            Log.e(tag, user.getName().toString());
//            holder.username.setText(user.getName().toString());
//            return convertView;
//        }
//    }

    @Override
    public void onResume(){
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}
