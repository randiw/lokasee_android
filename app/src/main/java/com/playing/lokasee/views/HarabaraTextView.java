package com.playing.lokasee.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import com.playing.lokasee.R;

/**
 * Created by RETRO on 22/08/2015.
 */
public class HarabaraTextView extends TextView {
    public HarabaraTextView(Context context) {
        super(context);
        init(null);
    }

    public HarabaraTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public HarabaraTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HarabaraTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs!=null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.harabara_mais);
            String fontName = a.getString(R.styleable.harabara_mais_harabara_mais);
            if (fontName!=null) {
                Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), fontName);
                setTypeface(myTypeface);
            }
            a.recycle();
        }
    }
}
