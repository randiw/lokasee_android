package com.playing.lokasee.tools;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.request.StringRequest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by randiwaranugraha on 6/4/15.
 */
public class Connect {

    public static final String TAG = Connect.class.getSimpleName();

    private int method;
    private String url;
    private HashMap<String, String> headers;
    private HashMap<String, String> parameters;
    private Response.Listener<String> responseListener;
    private Response.ErrorListener errorListener;

    /**
     * Connect using Volley request, need to provide Request implementation
     * class on calling execute
     */
    public Connect() {
    }

    /**
     * Connect using Volley, method GET
     *
     * @param url              Target URL
     * @param responseListener Response success listener
     * @param errorListener    Response error listener
     */
    public Connect(String url, Response.Listener<String> responseListener, Response.ErrorListener errorListener) {
        initRequest(Request.Method.GET, url, null, null, responseListener, errorListener);
    }

    /**
     * Connect using Volley, method GET with request Headers
     *
     * @param url              Target URL
     * @param requestHeaders   Request Headers
     * @param responseListener Response success listener
     * @param errorListener    Response error listener
     */
    public Connect(String url, HashMap<String, String> requestHeaders, Response.Listener<String> responseListener, Response.ErrorListener errorListener) {
        initRequest(Request.Method.GET, url, requestHeaders, null, responseListener, errorListener);
    }

    /**
     * Connect using Volley, method POST with request Headers and POST
     * parameters
     *
     * @param url              Target URL
     * @param requestHeaders   Request Headers
     * @param postParameters   POST parameters
     * @param responseListener Response success listener
     * @param errorListener    Response error listener
     */
    public Connect(String url, HashMap<String, String> requestHeaders, HashMap<String, String> postParameters, Response.Listener<String> responseListener, Response.ErrorListener errorListener) {
        initRequest(Request.Method.POST, url, requestHeaders, postParameters, responseListener, errorListener);
    }

    private void initRequest(int method, String url, HashMap<String, String> requestHeaders, HashMap<String, String> postParameters, Response.Listener<String> responseListener, Response.ErrorListener errorListener) {
        this.method = method;
        this.url = url;
        headers = requestHeaders;
        parameters = postParameters;
        this.responseListener = responseListener;
        this.errorListener = errorListener;

        print(parameters);
    }

    public void execute() {
        StringRequest stringRequest = new StringRequest(method, url, responseListener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (parameters == null) {
                    return super.getParams();
                }
                return parameters;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (headers == null) {
                    return super.getHeaders();
                }
                return headers;
            }
        };

        execute(stringRequest);
    }

    public void execute(Request<?> request) {
        execute(request, null);
    }

    public void execute(Request<?> request, String tag) {
        Log.d(TAG, "execute " + request.getUrl());
        request.setRetryPolicy(new DefaultRetryPolicy(0, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleyProvider.addToRequestQueue(request, tag);
    }

    public static String encoder(String str) {
        URI uri;
        try {
            uri = new URI(str.replace(" ", "%20"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return str;
        }
        return uri.toString();
    }

    private void print(HashMap<String, String> params) {
        StringBuilder builder = new StringBuilder();
        for (String key : params.keySet()) {
            String value = params.get(key);
            builder.append(key + " " + value + "\n");
        }
        Log.d(TAG, "print params " + builder.toString());
    }
}