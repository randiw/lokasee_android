package com.playing.lokasee.controller.request;

import com.android.volley.Response;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by randi on 9/11/15.
 */
public abstract class GetRequest<T> extends BaseAPIRequest<T> {

    public GetRequest(String url, Map<String, String> headers, Type type, Response.Listener<T> successListener, Response.ErrorListener listener) {
        super(Method.GET, url, headers, type, successListener, listener);
    }
}