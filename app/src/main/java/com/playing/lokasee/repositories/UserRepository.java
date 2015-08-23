package com.playing.lokasee.repositories;

import com.playing.lokasee.LokaseeApplication;
import com.playing.lokasee.User;
import com.playing.lokasee.UserDao;
import com.playing.lokasee.tools.RepoTools;

import java.util.List;

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

    private static UserDao getUserDao() {
        return LokaseeApplication.getInstance().getDaoSession().getUserDao();
    }

    public static boolean isDataExist(){

        return LokaseeApplication.getInstance().getDaoSession().getUserDao().count() > 0;
    }
}