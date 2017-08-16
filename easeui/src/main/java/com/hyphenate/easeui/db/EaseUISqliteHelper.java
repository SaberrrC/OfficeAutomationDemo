package com.hyphenate.easeui.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EaseUISqliteHelper extends SQLiteOpenHelper {

    private static EaseUISqliteHelper instance = null;
    private static final String DATABASE_NAME = "contacts.db";
    private static final int DATABASE_VERSION = 1;

    public EaseUISqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static EaseUISqliteHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (EaseUISqliteHelper.class) {
                instance = new EaseUISqliteHelper(context);
            }
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createTable(sqLiteDatabase);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }


    private void createTable(SQLiteDatabase db) {
        //创建t_friend表
        db.execSQL("create table " + FriendsInfoCacheSvc.TABLE_NAME + " (id integer PRIMARY KEY," +
                "user_id text collate nocase,nickname text,portrait text,userSex text,userPhone text,userPost text,userDepartment text,userEmail text)");

    }

}
