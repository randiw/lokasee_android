package com.playing.lokasee.controller.request;

import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by randiwaranugraha on 6/5/15.
 */
public abstract class PostJSONRequest extends BaseJSONRequest {

    private HashMap<String, String> parameters;

    public PostJSONRequest(String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, listener, errorListener);
    }

    public PostJSONRequest(String url, HashMap<String, String> parameters, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        this(url, listener, errorListener);
        setParameters(parameters);
    }

    public void setParameters(HashMap<String, String> parameters) {
        this.parameters = parameters;
    }

    public HashMap<String, String> getParameters() {
        return parameters;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        if (parameters == null) {
            return super.getParams();
        }
        displayParams(parameters);
        return parameters;
    }
}