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
import com.playing.lokasee.repositories.UserRepository;

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
    private ParseObject userLocation;

    public void signUp(String firstName, String lastName, String name, String url_prof_pic, final String facebookId, final OnLogParseListener onLogParseListener) {
        final String username = firstName.toLowerCase() + "." + lastName.toLowerCase();

        final ParseUser parseUser = new ParseUser();
        parseUser.setUsername(username);
        parseUser.setPassword(facebookId);
        parseUser.put(UserDao.Properties.Facebook_id.name, facebookId);
        parseUser.put(UserDao.Properties.Name.name, name);
        parseUser.put(UserDao.Properties.Url_prof_pic.name, url_prof_pic);
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

    public void updateUser(String firstName, String lastName, String name, String url_prof_pic, final String facebookId, final OnLogParseListener onLogParseListener){
        final String username = firstName.toLowerCase() + "." + lastName.toLowerCase();

        currentUser.setUsername(username);
        currentUser.setPassword(facebookId);
        currentUser.put(UserDao.Properties.Facebook_id.name, facebookId);
        currentUser.put(UserDao.Properties.Name.name, name);
        currentUser.put(UserDao.Properties.Url_prof_pic.name, url_prof_pic);
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    UserData.saveParseResponse(currentUser.getObjectId());
                    onLogParseListener.onSuccess(currentUser);
                } else {
                    Log.e(TAG, "update parseException " + e.getMessage());
                    onLogParseListener.onError(e);
                }
            }
        });
    }

    public void getAllUser(final OnParseQueryListener onParseQueryListener) {
        ParseQuery<ParseObject> locationQuery = new ParseQuery<ParseObject>(LOCATION);
        locationQuery.whereNotEqualTo(UserDao.Properties.Facebook_id.name, UserData.getFacebookId());
        locationQuery.orderByDescending("updatedAt");
        locationQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (ParseObject locationObject : list) {
                        String name = locationObject.getString(UserDao.Properties.Name.name);
                        String facebook_id = locationObject.getString(UserDao.Properties.Facebook_id.name);
                        double latitude = locationObject.getDouble(UserDao.Properties.Latitude.name);
                        double longitude = locationObject.getDouble(UserDao.Properties.Longitude.name);
                        String url_prof_pic = locationObject.getString(UserDao.Properties.Url_prof_pic.name);

                        User user = new User();
                        user.setObject_id(locationObject.getObjectId());
                        user.setName(name);
                        user.setFacebook_id(facebook_id);
                        user.setLatitude(latitude);
                        user.setLongitude(longitude);
                        user.setUrl_prof_pic(url_prof_pic);
                        UserRepository.save(user);
                    }

                    onParseQueryListener.onParseQuery(list);
                } else {
                    Log.e(TAG, "get list location parseException " + e.getMessage());
                    onParseQueryListener.onError(e);
                }
            }
        });
    }

    public void retrieveMyLocation(String facebookId, final OnParseQueryListener onParseQueryListener) {
        ParseQuery<ParseObject> locationQuery = new ParseQuery<ParseObject>(LOCATION);
        locationQuery.whereEqualTo(UserDao.Properties.Facebook_id.name, facebookId);
        locationQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if(e == null) {
                    if(list.size() > 0) {
                        ParseObject locationObject = list.get(0);
                        userLocation = locationObject;
                        onParseQueryListener.onParseQuery(list);
                    } else {
                        onParseQueryListener.onError(new ParseException(new Throwable("empty list")));
                    }

                } else {
                    Log.e(TAG, "retrieve my location parseException " + e.getMessage());
                    onParseQueryListener.onError(e);
                }
            }
        });
    }

    public void saveMyLocation(final double latitude, final double longitude, final OnSaveParseObjectListener onSaveParseObjectListener) {
        if(currentUser == null) {
            currentUser = ParseUser.getCurrentUser();
        }

        if(userLocation == null) {
            retrieveMyLocation(UserData.getFacebookId(), new OnParseQueryListener() {
                @Override
                public void onParseQuery(List<ParseObject> parseObjectList) {
                    saveMyLocation(latitude, longitude, onSaveParseObjectListener);
                }

                @Override
                public void onError(ParseException pe) {
                    userLocation = new ParseObject(LOCATION);
                    saveMyLocation(latitude, longitude, onSaveParseObjectListener);
                }
            });
        } else {
            userLocation.put(UserDao.TABLENAME.toLowerCase(), currentUser);
            userLocation.put(UserDao.Properties.Name.name, currentUser.get(UserDao.Properties.Name.name));
            userLocation.put(UserDao.Properties.Facebook_id.name, currentUser.get(UserDao.Properties.Facebook_id.name));
            userLocation.put(UserDao.Properties.Latitude.name, latitude);
            userLocation.put(UserDao.Properties.Longitude.name, longitude);
            userLocation.put(UserDao.Properties.Url_prof_pic.name, currentUser.get(UserDao.Properties.Url_prof_pic.name));

            userLocation.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        onSaveParseObjectListener.onSaveParseObject(userLocation);
                    } else {
                        Log.e(TAG, "save location parseException: " + e.getMessage());
                        onSaveParseObjectListener.onError(e);
                    }
                }
            });
        }
    }

    public void updateUser(String firstName, String lastName, String s, String id, OnLogParseListener onLogParseListener) {
    }

    public abstract interface OnParseListener {
        public void onError(ParseException pe);
    }

    public interface OnSaveParseObjectListener extends OnParseListener {
        public void onSaveParseObject(ParseObject parseObject);
    }

    public interface OnLogParseListener extends OnParseListener {
        public void onSuccess(ParseUser parseUser);
    }

    public interface OnParseQueryListener extends OnParseListener {
        public void onParseQuery(List<ParseObject> parseObjectList);
    }
}