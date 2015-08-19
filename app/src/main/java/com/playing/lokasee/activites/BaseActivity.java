package com.playing.lokasee.activites;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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
    private static final String objectId = "objectId";

    ArrayList<String> dataUser = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void SetSharedPrefs(Context context, String objId, String idFb, String nama, String lastLat, String lastLong){
        SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(IdUser, idFb);
        editor.putString(Name, nama);
        editor.putString(LastLat, lastLat);
        editor.putString(LastLong, lastLong);
        editor.putString(objectId, objId);
        editor.commit();
    }
    public ArrayList<String> GetSharedPrefs(Context context){
        dataUser.clear();
        SharedPreferences sharedPreferences = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        dataUser.add(sharedPreferences.getString(IdUser, ""));
        dataUser.add(sharedPreferences.getString(Name, ""));
        dataUser.add(sharedPreferences.getString(LastLat, ""));
        dataUser.add(sharedPreferences.getString(LastLong, ""));
        dataUser.add(sharedPreferences.getString(objectId, ""));

        return dataUser;
    }

    public Boolean isLoginFb(){
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    public Boolean isLogin(Context context){
        String prefs = GetSharedPrefs(context).get(0).toString();
        if(!prefs.equals("") && isLoginFb())
            return true;
        else{
            context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE).edit().clear().commit();
            return false;
        }
    }
}
