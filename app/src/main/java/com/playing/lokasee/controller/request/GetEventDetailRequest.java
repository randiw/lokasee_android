package com.playing.lokasee.controller.request;

import com.android.volley.Response;
import com.playing.lokasee.Event;
import com.playing.lokasee.repositories.EventRepository;

import java.util.Map;

/**
 * Created by randi on 9/10/15.
 */
public class GetEventDetailRequest extends GetRequest<Event> {

    private static final String TAG = GetEventDetailRequest.class.getSimpleName();

    public GetEventDetailRequest(String url, Map<String, String> headers, Response.Listener<Event> successListener, Response.ErrorListener listener) {
        super(url, headers, Event.class, successListener, listener);
    }

    @Override
    protected void handleDataResponse(Event event) {
        EventRepository.save(event);
    }
}