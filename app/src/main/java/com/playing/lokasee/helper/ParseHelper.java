package com.playing.lokasee.helper;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.playing.lokasee.User;
import com.playing.lokasee.UserDao;

import java.util.List;

/**
 * Created by randi on 8/20/15.
 */
public class ParseHelper {

    private static final String TAG = ParseHelper.class.getSimpleName();

    public static final String LOCATION = "Location";

    private static ParseHelper instance = null;

    public static ParseHelper getInstance() {
        if (instance == null) {
            instance = new ParseHelper();
        }
        return instance;
    }

    private ParseUser currentUser;

    public void signUp(String firstName, String lastName, String name, final String facebookId, final OnLogParseListener onLogParseListener) {
        final String username = firstName.toLowerCase() + "." + lastName.toLowerCase();

        final ParseUser parseUser = new ParseUser();
        parseUser.setUsername(username);
        parseUser.setPassword(facebookId);
        parseUser.put(UserDao.Properties.Facebook_id.name, facebookId);
        parseUser.put(UserDao.Properties.Name.name, name);

        parseUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    currentUser = ParseUser.getCurrentUser();
                    UserData.saveParseResponse(currentUser.getObjectId());
                    onLogParseListener.onSuccess(currentUser);
                } else {
                    Log.e(TAG, "signUp parseException " + e.getMessage());
                    onLogParseListener.onError(e);
                }
            }
        });
    }

    public void login(String firstName, String lastName, final String facebookId, final OnLogParseListener onLogParseListener) {
        final String username = firstName.toLowerCase() + "." + lastName.toLowerCase();

        ParseUser.logInInBackground(username, facebookId, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (e == null) {
                    currentUser = parseUser;
                    UserData.saveParseResponse(currentUser.getObjectId());
                    onLogParseListener.onSuccess(currentUser);
                } else {
                    Log.e(TAG, "login parseException " + e.getMessage());
                    onLogParseListener.onError(e);
                }
            }
        });
    }

    public void getAllUser() {
        ParseQuery<ParseObject> locationQuery = new ParseQuery<ParseObject>(LOCATION);
        locationQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (ParseObject locationObject : list) {
                        User user = new User();

                    }
                } else {
                    Log.e(TAG, "get list location parseException " + e.getMessage());
                }
            }
        });
    }

    public void saveMyLocation(final double latitude, final double longitude, final OnSaveParseObjectListener onSaveParseObjectListener) {
        final ParseObject locationObject = new ParseObject(LOCATION);
        locationObject.put(UserDao.TABLENAME.toLowerCase(), currentUser);
        locationObject.put(UserDao.Properties.Name.name, currentUser.get(UserDao.Properties.Name.name));
        locationObject.put(UserDao.Properties.Facebook_id.name, currentUser.get(UserDao.Properties.Facebook_id.name));
        locationObject.put(UserDao.Properties.Latitude.name, latitude);
        locationObject.put(UserDao.Properties.Longitude.name, longitude);

        locationObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    onSaveParseObjectListener.onSaveParseObject(locationObject);
                } else {
                    Log.e(TAG, "save location parseException: " + e.getMessage());
                }
            }
        });
    }

    public interface OnSaveParseObjectListener {
        public void onSaveParseObject(ParseObject parseObject);
    }

    public interface OnLogParseListener {
        public void onSuccess(ParseUser parseUser);

        public void onError(ParseException pe);
    }
}