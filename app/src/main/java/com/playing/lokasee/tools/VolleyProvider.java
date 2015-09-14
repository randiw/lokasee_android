package com.playing.lokasee.tools;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.cache.plus.BitmapImageCache;
import com.android.volley.cache.plus.SimpleImageLoader;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

public class VolleyProvider {

    public static final String TAG = VolleyProvider.class.getSimpleName();

    private static RequestQueue requestQueue;
    private static RequestFuture requestFuture;
    private static SimpleImageLoader imageLoader;

    private VolleyProvider() {
    }

    public static void init(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        requestFuture = RequestFuture.newFuture();
        imageLoader = new SimpleImageLoader(requestQueue, BitmapImageCache.getInstance(null));
    }

    public static RequestQueue getRequestQueue() {
        if (requestQueue != null) {
            return requestQueue;
        } else {
            throw new IllegalStateException("RequestQueue not initialized");
        }
    }

    public static SimpleImageLoader getImageLoader() {
        if (imageLoader != null) {
            return imageLoader;
        } else {
            throw new IllegalStateException("ImageLoader not initialized");
        }
    }

    public static RequestFuture getRequestFuture() {
        if(requestFuture != null) {
            return requestFuture;
        } else {
            throw new IllegalStateException("RequestFuture not initialized");
        }
    }

    public static <T> void addToRequestQueue(Request<T> request, String tag) {
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(request);
    }

    public static void cancelPendingRequest(Object tag) {
        if(requestQueue != null) {
            requestQueue.cancelAll(tag);
        }
    }
}