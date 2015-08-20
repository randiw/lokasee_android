package com.playing.lokasee.activites;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.AccessToken;
import com.playing.lokasee.R;

import java.util.ArrayList;

/**
 * Created by nabilla on 8/19/15.
 */
public class BaseActivity extends AppCompatActivity {

    private static final String IdUser = "idUser";
    private static final String Name = "nameUser";
    private static final String LastLat = "lastLat";
    private static final String LastLong = "lastLong";
    ArrayList<String> dataUser = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void SetSharedPrefs(Context context, String idFb, String nama, String lastLat, String lastLong){
        SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(IdUser, idFb);
        editor.putString(Name, nama);
        editor.putString(LastLat, lastLat);
        editor.putString(LastLong, lastLong);
        editor.commit();
    }
    public ArrayList<String> GetSharedPrefs(Context context){
        dataUser.clear();
        SharedPreferences sharedPreferences = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        dataUser.add(sharedPreferences.getString(IdUser, ""));
        dataUser.add(sharedPreferences.getString(Name, ""));
        dataUser.add(sharedPreferences.getString(LastLat, ""));
        dataUser.add(sharedPreferences.getString(LastLong, ""));
        return dataUser;
    }

    public Boolean isLoginFb(){
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    public Boolean isLogin(Context context){
        String prefs = GetSharedPrefs(context).get(0).toString();
        if(isLoginFb())
            return true;
        else{
            return false;
        }
    }

    protected void setActionBar(String title){
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return super.onOptionsItemSelected(item);
    }
}
