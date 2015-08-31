package com.playing.lokasee.events;

import com.playing.lokasee.User;

/**
 * Created by randi on 8/31/15.
 */
public class ReturnSearchEvent {

    public final User user;

    public ReturnSearchEvent(User user) {
        this.user = user;
    }
}