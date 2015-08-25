package com.playing.lokasee.repositories;

import android.database.Cursor;

import com.playing.lokasee.LokaseeApplication;
import com.playing.lokasee.User;
import com.playing.lokasee.UserDao;
import com.playing.lokasee.tools.RepoTools;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by randi on 8/20/15.
 */
public class UserRepository {

    public static void save(User user) {
        User oldUser = find(user.getObject_id());
        if(oldUser != null) {
            user.setId(oldUser.getId());
        }

        getUserDao().insertOrReplace(user);
    }

    public static User find(String objectId) {
        List<User> users = getUserDao().queryBuilder().where(UserDao.Properties.Object_id.eq(objectId)).limit(1).list();
        if(!RepoTools.isRowAvailable(users)) {
            return null;
        }

        User user = users.get(0);
        return user;
    }

    public static User findByFacebook(String facebookId) {
        List<User> users = getUserDao().queryBuilder().where(UserDao.Properties.Facebook_id.eq(facebookId)).limit(1).list();
        if(!RepoTools.isRowAvailable(users)) {
            return null;
        }

        User user = users.get(0);
        return user;
    }

    public static List<User> getAll() {
        List<User> users = getUserDao().queryBuilder().list();
        if(!RepoTools.isRowAvailable(users)) {
            return null;
        }

        return users;
    }

    public static User create(Cursor cursor) {
        long id = RepoTools.getLong(cursor, UserDao.Properties.Id.columnName);
        String object_id = RepoTools.getString(cursor, UserDao.Properties.Object_id.columnName);
        String facebook_id = RepoTools.getString(cursor, UserDao.Properties.Facebook_id.columnName);
        String name = RepoTools.getString(cursor, UserDao.Properties.Name.columnName);
        double latitude = RepoTools.getDouble(cursor, UserDao.Properties.Latitude.columnName);
        double longitude = RepoTools.getDouble(cursor, UserDao.Properties.Longitude.columnName);
        String url_prof_pic = RepoTools.getString(cursor, UserDao.Properties.Url_prof_pic.columnName);

        User user = new User(id, object_id, facebook_id, name, latitude, longitude, url_prof_pic);
        return user;
    }

    private static UserDao getUserDao() {
        return LokaseeApplication.getInstance().getDaoSession().getUserDao();
    }
}