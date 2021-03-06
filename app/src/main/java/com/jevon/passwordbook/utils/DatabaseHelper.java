package com.jevon.passwordbook.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jevon.passwordbook.AppApplication;

/**
 * @Author: Mr.J
 * @CreateDate: 2019/3/29 9:48
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "Password";

    private SQLiteDatabase db;

    public DatabaseHelper() {
        super(AppApplication.getInstance().getApplicationContext(), "PasswordData.db", null, 1);
        db = this.getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Password (name text,id text,password text,note text,str_key text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public long insert(ContentValues contentValues) {
//        db.execSQL("insert into Password (name,id,password,note) " +
//                "values ('" + name + "','" + id + "','" + password + "','" + note + "')");
        return db.insert(TABLE_NAME, null, contentValues);
    }

    public void delete() {
//        db.execSQL("delete from password");
        db.delete(TABLE_NAME, null, null);
    }

    public int delete(String name) {
//        db.execSQL("delete from password where name='" + name + "'");
        return db.delete(TABLE_NAME, "name=?", new String[]{name});
    }

    public int update(ContentValues contentValues, String name) {
        return db.update(TABLE_NAME, contentValues, "name=?", new String[]{name});
    }

    public Cursor query() {
        return db.query(TABLE_NAME, null, null, null, null, null, null);
    }

    public Cursor query(String name) {
        return db.query(TABLE_NAME, null, "name=?", new String[]{name}, null, null, null);
    }

    public void close() {
        db.close();
    }
}
