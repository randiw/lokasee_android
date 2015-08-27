package com.playing.lokasee.repositories.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.playing.lokasee.DaoSession;
import com.playing.lokasee.UserDao;
import de.greenrobot.dao.DaoLog;

/**
 * Created by nabilla on 8/21/15.
 */
public class UserContentProvider extends ContentProvider {

    public static final String AUTHORITY = "com.playing.lokasee.repositories.provider.UserContentProvider";
    public static final String TABLE_NAME = UserDao.TABLENAME;
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
    private static final int USERS = 0;
    private static final int USERS_ID = 1;
    private static final String PK = UserDao.Properties.Id.columnName;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, TABLE_NAME, USERS);
        uriMatcher.addURI(AUTHORITY, TABLE_NAME + "/#", USERS);
    }

    private static DaoSession daoSession;

    public static void setDaoSession(DaoSession _daoSession){
        daoSession = _daoSession;
    }

    protected SQLiteDatabase getDatabase(){
        if(daoSession == null){
            throw new IllegalStateException("Dao Session must be set during content provider active");
        }
        return daoSession.getDatabase();
    }

    @Override
    public boolean onCreate() {
        DaoLog.d("Content provider started: " + CONTENT_URI);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        int uriType = uriMatcher.match(uri);
        switch (uriType){
            case USERS:
                queryBuilder.setTables(TABLE_NAME);
                break;
            case USERS_ID:
                queryBuilder.setTables(TABLE_NAME);
                queryBuilder.appendWhere(PK + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        Cursor cursor = queryBuilder.query(daoSession.getDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
