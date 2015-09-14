package com.playing.lokasee.tools;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.ParseError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by randi on 9/11/15.
 */
public abstract class BaseRequest<T> extends Request<T> {

    private final Map<String, String> headers;
    private final Response.Listener<T> successListener;
    private final Type type;
    private final Gson gson;

    public BaseRequest(int method, String url, Map<String, String> headers, Type type, Response.Listener<T> successListener, Response.ErrorListener listener) {
        super(method, url, listener);
        this.headers = headers;
        this.gson = gsonHandler(new GsonBuilder().setPrettyPrinting()).create();
        this.type = type;
        this.successListener = successListener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            String json = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
            T type = gson.fromJson(json, this.type);
            handleDataResponse(type);

            return Response.success(type, HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

    protected abstract void handleDataResponse(T type);

    protected abstract GsonBuilder gsonHandler(GsonBuilder builder);

    @Override
    protected void deliverResponse(T t) {
        if(successListener != null) {
            successListener.onResponse(t);
        }
    }
}