package com.hyphenate.easeui.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.lang.ref.WeakReference;

/**
 * 作者:王凤旭
 * 日期:2017/8/4
 * 项目:oa
 * 详述:
 */

public class FriendsInfoCacheSvc {

    private final SQLiteDatabase mDB;
    private static FriendsInfoCacheSvc instance   = null;
    public static  String              TABLE_NAME = "t_friend";


    private FriendsInfoCacheSvc(Context context) {
        WeakReference<Context> wr = new WeakReference<>(context); //单例中弱引用context对象，避免造成内存泄露   modify by lvdinghao 2017/9/4
        mDB = EaseUISqliteHelper.getInstance(wr.get()).getWritableDatabase();
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
        if (null != friends.getUserSex()) {
            contentValues.put(Friends.COLUMNNAME_SEX, friends.getUserSex());
        }
        if (null != friends.getUserPhone()) {
            contentValues.put(Friends.COLUMNNAME_PHONE, friends.getUserPhone());
        }
        if (null != friends.getUserPost()) {
            contentValues.put(Friends.COLUMNNAME_POST, friends.getUserPost());
        }
        if (null != friends.getUserDepartment()) {
            contentValues.put(Friends.COLUMNNAME_DEOARTMENT, friends.getUserDepartment());
        }
        if (null != friends.getUid()) {
            contentValues.put(Friends.COLUMNNAME_UID, friends.getUid());
        }
        if (null != friends.getUserEmail()) {
            contentValues.put(Friends.COLUMNNAME_EMAIL, friends.getUserEmail());
        }
        if (null != friends.getUserDepartmentId()) {
            contentValues.put(Friends.COLUMNNAME_DEOARTMENTId, friends.getUserDepartmentId());
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

    public void setPortrait(String portrait, String userId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Friends.COLUMNNAME_PORTRAIT, portrait);
        try {
            mDB.update(TABLE_NAME, contentValues, "user_id=?", new String[]{userId});
            contentValues.clear();
        } catch (Exception e) {
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
        return "";
    }

    public String getUserId(String userId) {
        try {
            Cursor cursor = mDB.query(TABLE_NAME, null, " user_id=?", new String[]{userId}, null, null, null);
            if (cursor.moveToFirst()) {
                String nickName = cursor.getString(cursor.getColumnIndex(Friends.COLUMNNAME_USERID));
                cursor.close();
                return nickName;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getDepartment(String userId) {
        Cursor cursor = mDB.query(TABLE_NAME, null, " user_id=?", new String[]{userId}, null, null, null);
        if (cursor.moveToFirst()) {
            String portrait = cursor.getString(cursor.getColumnIndex(Friends.COLUMNNAME_DEOARTMENT));
            cursor.close();
            return portrait;
        }
        return "";
    }

    public String getPost(String userId) {
        Cursor cursor = mDB.query(TABLE_NAME, null, " user_id=?", new String[]{userId}, null, null, null);
        if (cursor.moveToFirst()) {
            String portrait = cursor.getString(cursor.getColumnIndex(Friends.COLUMNNAME_POST));
            cursor.close();
            return portrait;
        }
        return "";
    }

    public String getSex(String userId) {
        Cursor cursor = mDB.query(TABLE_NAME, null, " user_id=?", new String[]{userId}, null, null, null);
        if (cursor.moveToFirst()) {
            String portrait = cursor.getString(cursor.getColumnIndex(Friends.COLUMNNAME_SEX));
            cursor.close();
            return portrait;
        }
        return "";
    }

    public String getPortrait(String userId) {
        Cursor cursor = mDB.query(TABLE_NAME, null, " user_id=?", new String[]{userId}, null, null, null);
        if (cursor.moveToFirst()) {
            String portrait = cursor.getString(cursor.getColumnIndex(Friends.COLUMNNAME_PORTRAIT));
            cursor.close();
            return portrait;
        }
        return "";
    }

    public String getPhone(String userId) {
        Cursor cursor = mDB.query(TABLE_NAME, null, " user_id=?", new String[]{userId}, null, null, null);
        if (cursor.moveToFirst()) {
            String portrait = cursor.getString(cursor.getColumnIndex(Friends.COLUMNNAME_PHONE));
            cursor.close();
            return portrait;
        }
        return "";
    }

    public String getEmail(String userId) {
        Cursor cursor = mDB.query(TABLE_NAME, null, " user_id=?", new String[]{userId}, null, null, null);
        if (cursor.moveToFirst()) {
            String portrait = cursor.getString(cursor.getColumnIndex(Friends.COLUMNNAME_EMAIL));
            cursor.close();
            return portrait;
        }
        return "";
    }

    public String getDepartmentId(String userId) {
        Cursor cursor = mDB.query(TABLE_NAME, null, " user_id=?", new String[]{userId}, null, null, null);
        if (cursor.moveToFirst()) {
            String portrait = cursor.getString(cursor.getColumnIndex(Friends.COLUMNNAME_DEOARTMENTId));
            cursor.close();
            return portrait;
        }
        return "";
    }

    public int getUid(String userCode) {
        Cursor cursor = mDB.query(TABLE_NAME, null, " user_id=?", new String[]{userCode}, null, null, null);
        if (cursor.moveToFirst()) {
            int uid = cursor.getInt(cursor.getColumnIndex(Friends.COLUMNNAME_UID));
            cursor.close();
            return uid;
        }
        return 0;
    }
}
