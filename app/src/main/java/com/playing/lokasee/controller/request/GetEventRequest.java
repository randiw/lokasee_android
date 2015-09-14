package com.playing.lokasee.controller.request;

import com.android.volley.Response;
import com.google.gson.reflect.TypeToken;
import com.playing.lokasee.Event;
import com.playing.lokasee.repositories.EventRepository;

import java.util.List;
import java.util.Map;

/**
 * Created by randi on 9/10/15.
 */
public class GetEventRequest extends GetRequest<List<Event>> {

    private static final String TAG = GetEventRequest.class.getSimpleName();

    public GetEventRequest(String url, Map<String, String> headers, Response.Listener<List<Event>> successListener, Response.ErrorListener listener) {
        super(url, headers, new TypeToken<List<Event>>() {}.getType(), successListener, listener);
    }

    @Override
    protected void handleDataResponse(List<Event> events) {
        EventRepository.save(events);
    }
}