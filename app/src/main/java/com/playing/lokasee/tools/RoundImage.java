package com.playing.lokasee.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by nabilla on 8/26/15.
 */
public class RoundImage extends BitmapTransformation {
    public RoundImage(Context context) {
        super(context);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return circleCrop(pool, toTransform);
    }

    private Bitmap circleCrop(BitmapPool pool, Bitmap toTransform) {
        if(toTransform == null)
            return null;

        int size = Math.min(toTransform.getWidth(), toTransform.getHeight());
        int x = (toTransform.getWidth() - size) / 2;
        int y = (toTransform.getHeight() - size) / 2;

        Bitmap kotak = Bitmap.createBitmap(toTransform,x,y, size,size);
        Bitmap hasil = pool.get(size, size, Bitmap.Config.ARGB_8888);
        if(hasil == null){
            hasil = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(hasil);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(kotak, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);
        return hasil;
    }

    @Override
    public String getId() {
        return getClass().getName();
    }
}
