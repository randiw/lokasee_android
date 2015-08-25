package com.playing.lokasee.events;

/**
 * Created by randi on 8/25/15.
 */
public class ErrorEvent {

    public final String message;

    public ErrorEvent(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ErrorEvent message " + message);
        return builder.toString();
    }
}