package com.jevon.passwordbook.model;

import android.content.Context;
import android.database.Cursor;

import com.jevon.passwordbook.been.Password;
import com.jevon.passwordbook.utils.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Mr.J
 * @CreateDate: 2019/3/29 11:21
 */
public class MainModel {

    private Context context;
    private List<Password> list;

    public MainModel(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    public List<Password> getAllData() {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        list.clear();

        Cursor cursor = databaseHelper.query();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String psw = cursor.getString(cursor.getColumnIndex("password"));
            String note = cursor.getString(cursor.getColumnIndex("note"));
            list.add(new Password(name, id, psw, note));
        }
        return list;
    }
}
