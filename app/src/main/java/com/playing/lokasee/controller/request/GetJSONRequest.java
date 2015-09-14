package com.playing.lokasee.controller.request;

import com.android.volley.Response;

import org.json.JSONObject;

/**
 * Created by randiwaranugraha on 6/7/15.
 */
public abstract class GetJSONRequest extends BaseJSONRequest {

    public GetJSONRequest(String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, listener, errorListener);
    }

}