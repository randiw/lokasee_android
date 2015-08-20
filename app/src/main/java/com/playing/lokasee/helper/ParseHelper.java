package com.playing.lokasee.helper;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

/**
 * Created by randi on 8/20/15.
 */
public class ParseHelper {

    private static final String TAG = ParseHelper.class.getSimpleName();

    public static final String USER = "User";
    public static final String USER_FACEBOOK_ID = "facebook_id";
    public static final String USER_NAME = "name";

    public static final String LOCATION = "Location";

    private static ParseHelper instance = null;

    public static ParseHelper getInstance() {
        if(instance == null) {
            instance = new ParseHelper();
        }
        return instance;
    }

    public static void saveMyUser(String facebookId, String name, final OnSaveParseObjectListener onSaveParseObjectListener) {
        final ParseObject userObject = new ParseObject(USER);
        userObject.put(USER_FACEBOOK_ID, facebookId);
        userObject.put(USER_NAME, name);

        userObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {
                    UserData.saveParseResponse(userObject.getObjectId());
                    onSaveParseObjectListener.onSaveParseObject(userObject);
                } else {
                    Log.e(TAG, "save user parseException " + e.getMessage());
                }
            }
        });
    }

    public interface OnSaveParseObjectListener {
        public void onSaveParseObject(ParseObject parseObject);
    }
}