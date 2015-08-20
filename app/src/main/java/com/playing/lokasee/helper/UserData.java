package com.playing.lokasee.helper;

/**
 * Created by randi on 8/20/15.
 */
public class UserData {

    public static final String ID_USER = "idUser";
    public static final String NAME = "nameUser";
    public static final String LAST_LAT = "lastLat";
    public static final String LAST_LON = "lastLong";
    public static final String OBJECT_ID = "objectId";

    public static void saveFacebookLogin(String fbId, String name) {
        DataHelper.saveData(ID_USER, fbId);
        DataHelper.saveData(NAME, name);
    }

    public static void saveParseResponse(String objectId) {
        DataHelper.saveData(OBJECT_ID, objectId);
    }

    public static void saveLocation(String lat, String lon) {
        DataHelper.saveData(LAST_LAT, lat);
        DataHelper.saveData(LAST_LON, lon);
    }

    public static String getLastLat() {
        String lat = DataHelper.getString(LAST_LAT);
        return lat;
    }

    public static String getLastLon() {
        String lon = DataHelper.getString(LAST_LON);
        return lon;
    }

    public static boolean isLogin() {
        boolean isLogin = false;

        if(DataHelper.getString(ID_USER) != null) {
            isLogin = true;
        }

        return isLogin;
    }
}