package pl.surecase.eu;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class MyDaoGenerator {

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(2, "com.playing.lokasee");

        Entity user = schema.addEntity("User");
        user.addIdProperty();
        user.addStringProperty("object_id");
        user.addStringProperty("facebook_id");
        user.addStringProperty("name");
        user.addDoubleProperty("latitude");
        user.addDoubleProperty("longitude");
        user.addStringProperty("url_prof_pic");

        Entity event = schema.addEntity("Event");
        event.addIdProperty();
        event.addIntProperty("event_id");
        event.addStringProperty("name");
        event.addStringProperty("image");
        event.addStringProperty("description");
        event.addStringProperty("rating");
        event.addStringProperty("type");
        event.addStringProperty("date");
        event.addStringProperty("lowest_available_price");
        event.addStringProperty("location");
        event.addStringProperty("status");

        Entity image = schema.addEntity("Image");
        image.addIdProperty();
        image.addIntProperty("image_id");
        image.addIntProperty("order");
        image.addStringProperty("path");
        image.addStringProperty("type");

        Property eventIdForImage = image.addLongProperty("event_id").notNull().getProperty();
        ToMany eventToImage = event.addToMany(image, eventIdForImage);
        eventToImage.setName("images");

        Entity location = schema.addEntity("Location");
        location.addIdProperty();
        location.addIntProperty("location_id");
        location.addStringProperty("name");
        location.addStringProperty("venue_name");
        location.addStringProperty("address");
        location.addStringProperty("district");
        location.addStringProperty("region");
        location.addStringProperty("province");
        location.addDoubleProperty("latitude");
        location.addDoubleProperty("longitude");

        Property eventIdForLocation = location.addLongProperty("event_id").notNull().getProperty();
        ToMany eventToLocation = event.addToMany(location, eventIdForLocation);
        eventToLocation.setName("locations");

        Entity schedule = schema.addEntity("Schedule");
        schedule.addIdProperty();
        schedule.addIntProperty("schedule_id");
        schedule.addStringProperty("date");
        schedule.addStringProperty("start_time");
        schedule.addStringProperty("finish_time");
        schedule.addIntProperty("location_id");

        Property eventIdForSchedule = schedule.addLongProperty("event_id").notNull().getProperty();
        ToMany eventToSchedule = event.addToMany(schedule, eventIdForSchedule);
        eventToSchedule.setName("schedules");

        Entity ticket = schema.addEntity("Ticket");
        ticket.addIdProperty();
        ticket.addIntProperty("ticket_id");
        ticket.addStringProperty("name");
        ticket.addStringProperty("description");
        ticket.addStringProperty("seatplan");
        ticket.addStringProperty("type");
        ticket.addLongProperty("price");
        ticket.addIntProperty("stock");

        Property scheduleIdForTicket = ticket.addLongProperty("schedule_id").notNull().getProperty();
        ToMany scheduleToTicket = schedule.addToMany(ticket, scheduleIdForTicket);
        scheduleToTicket.setName("tickets");

        new DaoGenerator().generateAll(schema, args[0]);
    }
}