package com.playing.lokasee.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.playing.lokasee.R;
import com.playing.lokasee.User;
import com.playing.lokasee.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by nabilla on 8/21/15.
 */
public class UserAdapter extends BaseAdapter{
    List<User> users;
    List<User> search;
    Context context;

    public UserAdapter(Context context, List<User> users){
        this.users = users;
        this.context = context;
        search = new ArrayList<>();
        search.addAll(users);
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder{
        TextView username;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView != null){
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = mInflater.inflate(R.layout.activity_search_detil, parent, false);
            holder = new ViewHolder();
            holder.username = (TextView) convertView.findViewById(R.id.user);
            convertView.setTag(holder);
        }
        User user = (User) getItem(position);
        holder.username.setText(user.getName().toString());
        return convertView;
    }

    public void filter(String text) {
        text = text.toLowerCase(Locale.getDefault());
        users.clear();
        if (text.length() == 0) {
            users.addAll(search);
        } else {
            for (User usr : search) {
                if (usr.getName().toLowerCase(Locale.getDefault()).contains(text)) {
                    users.add(usr);
                }
            }
            notifyDataSetChanged();
        }

    }
}
