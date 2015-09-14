package com.playing.lokasee.controller.request;

import com.android.volley.Response;
import com.playing.lokasee.R;
import com.playing.lokasee.controller.API;
import com.playing.lokasee.helper.UserData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by randi on 9/8/15.
 */
public class TokenRequest extends PostJSONRequest {

    public TokenRequest(String url, HashMap<String, String> parameters, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(url, parameters, listener, errorListener);
        if(!parameters.containsKey(API.Key.APIKEY)) {
            parameters.put(API.Key.APIKEY, getContext().getString(R.string.loket_apikey));
        }
        setParameters(parameters);
    }

    @Override
    protected void handleDataResponse(JSONObject json) throws JSONException {
        JSONObject data = json.getJSONObject("data");
        String token = data.getString("token");
        UserData.saveLoketToken(token);
    }
}