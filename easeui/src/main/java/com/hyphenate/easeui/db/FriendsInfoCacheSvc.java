package com.hyphenate.easeui.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 作者:王凤旭
 * 日期:2017/8/4
 * 项目:oa
 * 详述:
 */

public class FriendsInfoCacheSvc {

    private final SQLiteDatabase mDB;
    private static FriendsInfoCacheSvc instance = null;
    public static String TABLE_NAME = "t_friend";

    private FriendsInfoCacheSvc(Context context) {
        mDB = EaseUISqliteHelper.getInstance(context).getWritableDatabase();
    }

    public static FriendsInfoCacheSvc getInstance(Context context) {
        if (instance == null) {
            instance = new FriendsInfoCacheSvc(context);
        }
        return instance;
    }


    /**
     * @param uuid 数据库中是否有该uuid好友
     * @return
     */
    private boolean queryIfIDExists(String uuid) {
        Cursor cursor = mDB.query(TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(cursor.getColumnIndex(Friends.COLUMNNAME_USERID)).equals(uuid)) {
                    return true;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        return false;
    }


    /**
     * @param friends 添加或者更新好友信息
     */
    public boolean addOrUpdateFriends(Friends friends) {

        ContentValues contentValues = new ContentValues();
        if (null != friends.getUser_id()) {
            contentValues.put(Friends.COLUMNNAME_USERID, friends.getUser_id());
        }
        if (null != friends.getNickname()) {
            contentValues.put(Friends.COLUMNNAME_NICKNAME, friends.getNickname());
        }
        if (null != friends.getPortrait()) {
            contentValues.put(Friends.COLUMNNAME_PORTRAIT, friends.getPortrait());
        }

        if (queryIfIDExists(friends.getUser_id())) {

            mDB.update(TABLE_NAME, contentValues, "user_id=?", new String[]{friends.getUser_id()});
            contentValues.clear();
            return true;
        } else {
            mDB.beginTransaction();
            try {
                mDB.insert(TABLE_NAME, null, contentValues);
                contentValues.clear();
                mDB.setTransactionSuccessful();
            } catch (Exception e) {
                return false;
            } finally {
                mDB.endTransaction();
                return true;
            }
        }

    }

    public String getNickName(String userId) {
        try {
            Cursor cursor = mDB.query(TABLE_NAME, null, " user_id=?", new String[]{userId}, null, null, null);
            if (cursor.moveToFirst()) {
                String nickName = cursor.getString(cursor.getColumnIndex(Friends.COLUMNNAME_NICKNAME));
                cursor.close();
                return nickName;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getPortrait(String userId) {
        Cursor cursor = mDB.query(TABLE_NAME, null, " user_id=?", new String[]{userId}, null, null, null);
        if (cursor.moveToFirst()) {
            String portrait = cursor.getString(cursor.getColumnIndex(Friends.COLUMNNAME_PORTRAIT));
            cursor.close();
            return portrait;
        }
        return null;
    }


}
