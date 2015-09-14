package com.playing.lokasee;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.playing.lokasee.Event;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table EVENT.
*/
public class EventDao extends AbstractDao<Event, Long> {

    public static final String TABLENAME = "EVENT";

    /**
     * Properties of entity Event.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Event_id = new Property(1, Integer.class, "event_id", false, "EVENT_ID");
        public final static Property Name = new Property(2, String.class, "name", false, "NAME");
        public final static Property Image = new Property(3, String.class, "image", false, "IMAGE");
        public final static Property Description = new Property(4, String.class, "description", false, "DESCRIPTION");
        public final static Property Rating = new Property(5, String.class, "rating", false, "RATING");
        public final static Property Type = new Property(6, String.class, "type", false, "TYPE");
        public final static Property Date = new Property(7, String.class, "date", false, "DATE");
        public final static Property Lowest_available_price = new Property(8, String.class, "lowest_available_price", false, "LOWEST_AVAILABLE_PRICE");
        public final static Property Location = new Property(9, String.class, "location", false, "LOCATION");
        public final static Property Status = new Property(10, String.class, "status", false, "STATUS");
    };

    private DaoSession daoSession;


    public EventDao(DaoConfig config) {
        super(config);
    }
    
    public EventDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'EVENT' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'EVENT_ID' INTEGER," + // 1: event_id
                "'NAME' TEXT," + // 2: name
                "'IMAGE' TEXT," + // 3: image
                "'DESCRIPTION' TEXT," + // 4: description
                "'RATING' TEXT," + // 5: rating
                "'TYPE' TEXT," + // 6: type
                "'DATE' TEXT," + // 7: date
                "'LOWEST_AVAILABLE_PRICE' TEXT," + // 8: lowest_available_price
                "'LOCATION' TEXT," + // 9: location
                "'STATUS' TEXT);"); // 10: status
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'EVENT'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Event entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer event_id = entity.getEvent_id();
        if (event_id != null) {
            stmt.bindLong(2, event_id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String image = entity.getImage();
        if (image != null) {
            stmt.bindString(4, image);
        }
 
        String description = entity.getDescription();
        if (description != null) {
            stmt.bindString(5, description);
        }
 
        String rating = entity.getRating();
        if (rating != null) {
            stmt.bindString(6, rating);
        }
 
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(7, type);
        }
 
        String date = entity.getDate();
        if (date != null) {
            stmt.bindString(8, date);
        }
 
        String lowest_available_price = entity.getLowest_available_price();
        if (lowest_available_price != null) {
            stmt.bindString(9, lowest_available_price);
        }
 
        String location = entity.getLocation();
        if (location != null) {
            stmt.bindString(10, location);
        }
 
        String status = entity.getStatus();
        if (status != null) {
            stmt.bindString(11, status);
        }
    }

    @Override
    protected void attachEntity(Event entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Event readEntity(Cursor cursor, int offset) {
        Event entity = new Event( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // event_id
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // image
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // description
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // rating
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // type
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // date
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // lowest_available_price
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // location
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10) // status
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Event entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setEvent_id(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setImage(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setDescription(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setRating(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setType(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setDate(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setLowest_available_price(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setLocation(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setStatus(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Event entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Event entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
