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
    public static final String LOKET_TOKEN = "loket_token";

    public static void saveFacebookLogin(String facebook_id, String name) {
        DataHelper.saveData(FACEBOOK_ID, facebook_id);
        DataHelper.saveData(NAME, name);
    }

    public static void saveFacebookProfPic(String url_prof_pic){
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

    public static void saveLoketToken(String loket_token) {
        DataHelper.saveData(LOKET_TOKEN, loket_token);
    }

    public static String getLoketToken() {
        String loket_token = DataHelper.getString(LOKET_TOKEN);
        return loket_token;
    }

    public static String getFacebookId() {
        String facebookId = DataHelper.getString(FACEBOOK_ID);
        return facebookId;
    }

    public static String getName() {
        String name = DataHelper.getString(NAME);
        return name;
    }

    public static String getUsername() {
        String username = DataHelper.getString(USERNAME);
        return username;
    }

    public static String getPassword() {
        String password = DataHelper.getString(PASSWORD);
        return password;
    }

    public static double getLatitude() {
        String latitude = DataHelper.getString(LATITUDE);
        if(latitude == null || latitude.length() == 0) {
            return 0.0;
        }

        return Double.parseDouble(latitude);
    }

    public static double getLongitude() {
        String longitude = DataHelper.getString(LONGITUDE);
        if(longitude == null || longitude.length() == 0) {
            return 0.0;
        }

        return Double.parseDouble(longitude);
    }

    public static String getParseObjectId() {
        String parseObjectId = DataHelper.getString(PARSE_OBJECT_ID);
        return parseObjectId;
    }

    public static String getFacebookProfilePicUrl() {
        String profilePic = DataHelper.getString(URL_PROF_PIC);
        return profilePic;
    }

    public static boolean isLogin() {
        boolean isLogin = false;

        if(DataHelper.getString(FACEBOOK_ID) != null) {
            isLogin = true;
        }

        return isLogin;
    }

    public static boolean clearUserData(){
        DataHelper.clearData();
        return DataHelper.getString(FACEBOOK_ID) == null;
    }
}