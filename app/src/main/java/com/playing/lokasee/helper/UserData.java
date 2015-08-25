package com.playing.lokasee.helper;

import android.net.Uri;

import java.net.URI;

/**
 * Created by randi on 8/20/15.
 */
public class UserData {

    public static final String FACEBOOK_ID = "facebook_id";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String NAME = "name";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String URL_PROF_PIC = "url_prof_pic";
    public static final String PARSE_OBJECT_ID = "parse_object_id";

    public static void saveFacebookLogin(String facebook_id, String name) {
        DataHelper.saveData(FACEBOOK_ID, facebook_id);
        DataHelper.saveData(NAME, name);
    }

    public static void saveFacebookProfPic(String facebook_id, String url_prof_pic){
        DataHelper.saveData(FACEBOOK_ID, facebook_id);
        DataHelper.saveData(URL_PROF_PIC, url_prof_pic);
    }

    public static void saveParseCred(String username, String password) {
        DataHelper.saveData(USERNAME, username);
        DataHelper.saveData(PASSWORD, password);
    }

    public static void saveParseResponse(String parse_object_id) {
        DataHelper.saveData(PARSE_OBJECT_ID, parse_object_id);
    }

    public static void saveLocation(String latitude, String longitude) {
        DataHelper.saveData(LATITUDE, latitude);
        DataHelper.saveData(LONGITUDE, longitude);
    }

    public static String getFacebookId() {
        String facebookId = DataHelper.getString(FACEBOOK_ID);
        return facebookId;
    }

    public static String getUsername() {
        String username = DataHelper.getString(USERNAME);
        return username;
    }

    public static String getPassword() {
        String password = DataHelper.getString(PASSWORD);
        return password;
    }

    public static String getLatitude() {
        String lat = DataHelper.getString(LATITUDE);
        return lat;
    }

    public static String getLongitude() {
        String lon = DataHelper.getString(LONGITUDE);
        return lon;
    }

    public static String getParseObjectId() {
        String parseObjectId = DataHelper.getString(PARSE_OBJECT_ID);
        return parseObjectId;
    }

    public static boolean isLogin() {
        boolean isLogin = false;

        if(DataHelper.getString(FACEBOOK_ID) != null) {
            isLogin = true;
        }

        return isLogin;
    }
}