package com.playing.lokasee.util;

import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Created by mexan on 8/18/15.
 */
public class UtilFunction {

    // Google Play Service Validation
    public static boolean isGooglePlayServicesAvailable(Context mContext) {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mContext);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
//            GooglePlayServicesUtil.getErrorDialog(status, mContext, 0).show();
            return false;
        }
    }
}
