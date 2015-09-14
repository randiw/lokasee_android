package com.playing.lokasee.presenter;

import android.os.Bundle;
import android.util.Log;

import com.playing.lokasee.Event;
import com.playing.lokasee.activites.MockGoTixActivity;
import com.playing.lokasee.controller.API;

import java.util.List;

import rx.Subscriber;

/**
 * Created by randi on 9/10/15.
 */
public class MockGoTixPresenter extends BasePresenter<MockGoTixActivity> {

    private static final String TAG = MockGoTixPresenter.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        getEventOnSale();
        getEventDetail(1);
    }

    public void getEventOnSale() {
        API.getEventOnSale().subscribe(new Subscriber<List<Event>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Event> events) {
                for (Event event : events) {
                    Log.d(TAG, "event id: " + event.getId());
                }
            }
        });
    }

    public void getEventDetail(int event_id) {
        API.getEventDetail(event_id).subscribe(new Subscriber<Event>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Event event) {

            }
        });
    }
}