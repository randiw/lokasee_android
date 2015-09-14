package com.playing.lokasee.controller;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.RequestFuture;
import com.playing.lokasee.Event;
import com.playing.lokasee.Schedule;
import com.playing.lokasee.controller.request.BaseJSONRequest;
import com.playing.lokasee.controller.request.GetEventDetailRequest;
import com.playing.lokasee.controller.request.GetEventRequest;
import com.playing.lokasee.controller.request.TokenRequest;
import com.playing.lokasee.tools.Connect;
import com.playing.lokasee.tools.VolleyProvider;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by randi on 9/7/15.
 */
public class API {

    public static final String TAG = API.class.getSimpleName();

    private static final String URL_LOKET_DEVELOPMENT = "http://api.sandbox.loket.com/v1/";
    private static final String URL_GOTIX_MOCK_SERVER = "http://private-8c162-gotixapidocumentationv1.apiary-mock.com/v1/";

    public static Observable<List<Event>> getEventOnSale() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("status", "on_sale");

        String url = getUrlWithParameters(Action.EVENT, parameters);

        RequestFuture<List<Event>> future = VolleyProvider.getRequestFuture();
        GetEventRequest getEventRequest = new GetEventRequest(url, null, future, future);

        Connect connect = new Connect();
        connect.execute(getEventRequest, Action.EVENT);

        return Observable.from(future, Schedulers.newThread());
    }

    public static Observable<List<Event>> getEventComingSoon() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("status", "coming_soon");

        String url = getUrlWithParameters(Action.EVENT, parameters);
        RequestFuture<List<Event>> future = VolleyProvider.getRequestFuture();

        Connect connect = new Connect();
        connect.execute(new GetEventRequest(url, null, future, future), Action.EVENT);

        return Observable.from(future, Schedulers.newThread());
    }

    public static Observable<Event> getEventDetail(int event_id) {
        String url = getUrl(Action.EVENT) + "/" + event_id;
        RequestFuture<Event> future = VolleyProvider.getRequestFuture();

        Connect connect = new Connect();
        connect.execute(new GetEventDetailRequest(url, null, future, future), Action.EVENT);

        return Observable.from(future, Schedulers.newThread());
    }

    public static void post(String action, HashMap<String, String> parameters, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        String url = getUrl(action);

        if(parameters == null) {
            parameters = new HashMap<>();
        }

        BaseJSONRequest request = null;
        if(Action.REQUEST_TOKEN.equals(action)) {
            request = new TokenRequest(url, parameters, responseListener, errorListener);
        }

        Connect connect = new Connect();
        connect.execute(request);
    }

    public static String getUrl(String action) {
        String url = URL_GOTIX_MOCK_SERVER;
        return url + action;
    }

    public static String getUrlWithParameters(String action, HashMap<String, String> parameters) {
        String url = getUrl(action);
        url += expandParameter(parameters);
        return url;
    }

    public static String expandParameter(HashMap<String, String> parameters) {
        StringBuilder builder = new StringBuilder();

        builder.append("?");
        int total = parameters.size();
        int i = 0;
        for(String key : parameters.keySet()) {
            String value = parameters.get(key);
            builder.append(key + "=" + value);
            i++;
            if(i < total) {
                builder.append("&");
            }
        }

        return builder.toString();
    }

    public interface Action {
        public static final String REQUEST_TOKEN = "login";
        public static final String EVENT = "events";
    }

    public interface Key {
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String APIKEY = "APIKEY";
    }
}