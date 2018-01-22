package com.hyphenate.easeui.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class EaseUISqliteHelper extends SQLiteOpenHelper {

    private static EaseUISqliteHelper instance = null;
    private static final String DATABASE_NAME = "contacts.db";
    private static final int DATABASE_VERSION = 2;

    public EaseUISqliteHelper(Context context) {
        super(context.getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
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
        try {
            Log.i("updateLog", "数据库更新了！");
            if (!isFieldExist(sqLiteDatabase, FriendsInfoCacheSvc.TABLE_NAME, "uid")) {
                String sql = "alter table " + FriendsInfoCacheSvc.TABLE_NAME + " add uid text";
                sqLiteDatabase.execSQL(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    private void createTable(SQLiteDatabase db) {
        //创建t_friend表
        db.execSQL("create table " + FriendsInfoCacheSvc.TABLE_NAME + " (id integer PRIMARY KEY," +
                "user_id text collate nocase,nickname text,portrait text,userSex text,uid text,userPhone text,userPost text,userDepartment text,userEmail text,userDepartmentId text)");

    }

    /**
     * 判断某表里某字段是否存在
     *
     * @param db
     * @param tableName
     * @param fieldName
     * @return
     */
    private boolean isFieldExist(SQLiteDatabase db, String tableName, String fieldName) {
        String queryStr = "select sql from sqlite_master where type = 'table' and name = '%s'";
        queryStr = String.format(queryStr, tableName);
        Cursor c = db.rawQuery(queryStr, null);
        String tableCreateSql = null;
        try {
            if (c != null && c.moveToFirst()) {
                tableCreateSql = c.getString(c.getColumnIndex("sql"));
            }
        } finally {
            if (c != null)
                c.close();
        }
        if (tableCreateSql != null && tableCreateSql.contains(fieldName))
            return true;
        return false;
    }
}
