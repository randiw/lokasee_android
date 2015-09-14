package com.playing.lokasee.presenter;

import android.os.Bundle;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.playing.lokasee.activites.LoketActivity;
import com.playing.lokasee.controller.API;
import com.playing.lokasee.helper.UserData;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by randi on 9/8/15.
 */
public class LoketPresenter extends BasePresenter<LoketActivity> {

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        requestToken();
    }

    public void requestToken() {
        if(UserData.getLoketToken() == null || UserData.getLoketToken().isEmpty()) {
            getView().startProgressDialog();

            HashMap<String, String> parameters = new HashMap<>();
            parameters.put(API.Key.USERNAME, "gojek");
            parameters.put(API.Key.PASSWORD, "123123");

            API.post(API.Action.REQUEST_TOKEN, parameters, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    getView().dismissProgressDialog();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    getView().dismissProgressDialog();

                }
            });
        } else {

        }
    }

    public void getListTicket() {

    }
}