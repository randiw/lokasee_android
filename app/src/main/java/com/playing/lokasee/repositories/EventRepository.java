package com.playing.lokasee.repositories;

import com.playing.lokasee.Event;
import com.playing.lokasee.EventDao;
import com.playing.lokasee.LokaseeApplication;
import com.playing.lokasee.tools.RepoTools;

import java.util.List;

/**
 * Created by randi on 9/10/15.
 */
public class EventRepository {

    public static void save(Event event) {
        Event oldEvent = find(event.getEvent_id());
        if (oldEvent != null) {
            event.setId(oldEvent.getId());
        }

        getEventDao().insertOrReplace(event);
    }

    public static void save(List<Event> events) {
        for (Event event : events) {
            save(event);
        }
    }

    public static Event find(int event_id) {
        List<Event> events = getEventDao().queryBuilder().where(EventDao.Properties.Event_id.eq(event_id)).limit(1).list();
        if (!RepoTools.isRowAvailable(events)) {
            return null;
        }

        Event event = events.get(0);
        return event;
    }

    public static List<Event> getAll() {
        List<Event> events = getEventDao().queryBuilder().list();
        if (!RepoTools.isRowAvailable(events)) {
            return null;
        }

        return events;
    }

    private static EventDao getEventDao() {
        return LokaseeApplication.getInstance().getDaoSession().getEventDao();
    }
}