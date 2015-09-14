package com.playing.lokasee.controller.request;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.ParseError;
import com.android.volley.error.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.playing.lokasee.LokaseeApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by randiwaranugraha on 6/7/15.
 */
public abstract class BaseJSONRequest extends Request<JSONObject> {

    private static final String TAG = BaseJSONRequest.class.getSimpleName();

    private Response.Listener<JSONObject> listener;
    private Context context;

    public BaseJSONRequest(int method, String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = listener;
        context = LokaseeApplication.getInstance().getApplicationContext();
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            Log.d(TAG, json.toString());

            JSONObject object = new JSONObject(json);

            int status = object.getInt("status");
            String message = object.getString("message");
            if(status > 100 && status < 300) {
                JSONObject data = object.getJSONObject("data");
                handleDataResponse(data);
                return Response.success(data, HttpHeaderParser.parseCacheHeaders(response));
            }

            return Response.error(new VolleyError(response, new Throwable(message)));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        } catch (JSONException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

    protected abstract void handleDataResponse(JSONObject json) throws JSONException;

    @Override
    protected void deliverResponse(JSONObject jsonObject) {
        if (listener != null) {
            listener.onResponse(jsonObject);
        }
    }

    public Context getContext() {
        return context;
    }

    protected void displayParams(Map<String, String> parameters) {
        for(String key : parameters.keySet()) {
            String value = parameters.get(key);
            Log.d(TAG, key + " : " + value);
        }
    }
}