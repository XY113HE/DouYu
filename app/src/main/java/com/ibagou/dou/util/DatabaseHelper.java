package com.ibagou.dou.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by liumingyu on 2018/6/5.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="douyu.db";
    private static final int SCHEMA_VERSION=1;
    private static DatabaseHelper instance;
    private SQLiteDatabase db;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
        db = getWritableDatabase();
    }

    public static DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            synchronized(DatabaseHelper.class) {
                if (instance == null) {
                    instance = new DatabaseHelper(context);
                }
            }
        }
        return instance;
    }

    @Override
    public void close(){
        super.close();
        instance = null;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE like_info (_id INTEGER PRIMARY KEY, isLike TEXT, editTime INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion==1 && newVersion==2) {//升级判断,如果再升级就要再加两个判断,从1到3,从2到3

        }
    }

    public boolean getIsLike(String id){
        String args[] = {id+""};
        StringBuffer buf = new StringBuffer("SELECT isLike FROM like_info ");
        Cursor cursor = db.rawQuery(buf.toString() + "WHERE _id=?", args);
        String isLike = "0";
        if(cursor.moveToFirst()){
            isLike = cursor.getString(cursor.getColumnIndex("isLike"));
        }
        cursor.close();
        return isLike.equals("1");
    }

    public void setIsLike(String id, String isLike){
        Cursor cursor = db.rawQuery("SELECT isLike FROM like_info WHERE _id=?", new String[]{id+""});
        ContentValues cv = new ContentValues();
        cv.put("isLike", isLike);
        cv.put("editTime", System.currentTimeMillis());
        if(cursor.moveToFirst()){

            db.update("like_info", cv, "_id=?", new String[]{id+""});
        }else{
            cv.put("_id", id);
            long num = db.insert("like_info", "_id", cv);
        }
        cursor.close();
    }
}
