package com.playing.lokasee.helper;

import android.content.Context;
import android.graphics.Typeface;

import com.norbsoft.typefacehelper.TypefaceCollection;
import com.norbsoft.typefacehelper.TypefaceHelper;

/**
 * Created by mexan on 8/24/15.
 */
public class TypeFaceHelper {

    private static  TypefaceCollection mHarbaraFace;


    public static void initFace(Context mContext) {

        mHarbaraFace = new TypefaceCollection.Builder()
                                .set(Typeface.NORMAL, Typeface.createFromAsset(mContext.getAssets(), "fonts/harabara_font.otf"))
                                .create();

        TypefaceHelper.init(mHarbaraFace);
    }


    public static TypefaceCollection getHarbaraFace(){
        return  mHarbaraFace;
    }

}
