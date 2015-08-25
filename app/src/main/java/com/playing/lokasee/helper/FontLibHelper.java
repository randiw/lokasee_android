package com.playing.lokasee.helper;

import android.content.Context;
import android.graphics.Typeface;

import com.norbsoft.typefacehelper.TypefaceCollection;
import com.norbsoft.typefacehelper.TypefaceHelper;
import com.playing.lokasee.R;

/**
 * Created by mexan on 8/24/15.
 */
public class FontLibHelper {

    private static TypefaceCollection harabaraFace;

    public static void initFace(Context context) {
        harabaraFace = new TypefaceCollection.Builder()
                .set(Typeface.NORMAL, Typeface.createFromAsset(context.getAssets(), context.getString(R.string.Harabara)))
                .create();

        TypefaceHelper.init(harabaraFace);
    }

    public static TypefaceCollection getHarbaraFace() {
        return harabaraFace;
    }
}