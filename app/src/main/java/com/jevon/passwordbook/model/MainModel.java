package com.jevon.passwordbook.model;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jevon.passwordbook.PasswordApplication;
import com.jevon.passwordbook.been.Password;
import com.jevon.passwordbook.utils.AesEncryptionUtils;
import com.jevon.passwordbook.utils.DatabaseHelper;
import com.jevon.passwordbook.utils.FileUtils;
import com.jevon.passwordbook.utils.Jlog;
import com.jizhenhua.encryption.Encryption;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Mr.J
 * @CreateDate: 2019/3/29 11:21
 */
public class MainModel {

    private List<Password> allDataList;
    private List<Password> serachList;

    public MainModel() {
        allDataList = new ArrayList<>();
        serachList = new ArrayList<>();
    }

    //    获取全部数据
    public List<Password> getDataFromDB() {
        DatabaseHelper databaseHelper = new DatabaseHelper(PasswordApplication.getContext());
        allDataList.clear();

        Cursor cursor = databaseHelper.query();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String key = cursor.getString(cursor.getColumnIndex("str_key"));
            String securePsw = cursor.getString(cursor.getColumnIndex("password"));
            String psw = AesEncryptionUtils.decrypt(key, securePsw);
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String note = cursor.getString(cursor.getColumnIndex("note"));
            allDataList.add(new Password(name, id, psw, note));
        }
        databaseHelper.close();
        serachList.addAll(allDataList);
        return allDataList;
    }

    /**
     * 搜索方法
     *
     * @param item 搜索条件
     * @return 结果集
     */
    public List<Password> getDataByName(String item) {

        allDataList.clear();

        for (int i = 0; i < serachList.size(); i++) {
            if (serachList.get(i).getName().contains(item)) {
                allDataList.add(serachList.get(i));
            }
        }
        return serachList;
    }


    //    导入数据
    public int readData(String path) {

        //打开备份数据库
        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        Jlog.d(path);
        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.query(DatabaseHelper.TABLE_NAME, null,
                null, null, null, null, null);
        //查看是否有数据
        if (cursor.getCount() < 1) {
            sqLiteDatabase.close();
            return -1;
        }

        DatabaseHelper databaseHelper = new DatabaseHelper(PasswordApplication.getContext());
        int count = 0;
//        从备份数据库中逐条写入当前数据库
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            //查看当前是否已存在该项数据
            Cursor cursor1 = databaseHelper.query(cursor.getString(cursor.getColumnIndex("name")));
            if (cursor1.getCount() != 0) {
                continue;
            }

            ContentValues contentValues = new ContentValues();
            contentValues.put("name", cursor.getString(cursor.getColumnIndex("name")));
            contentValues.put("id", cursor.getString(cursor.getColumnIndex("id")));


            //老版本数据库
            if (cursor.getColumnIndex("str_key") < 0) {
                String oldPsw = cursor.getString(cursor.getColumnIndex("password"));
                String original = String.valueOf(new Encryption().getOriginal(oldPsw));
                String key = AesEncryptionUtils.createKey(original);
                contentValues.put("password", AesEncryptionUtils.encrypt(key, original));
                contentValues.put("str_key", key);
                contentValues.put("note", cursor.getString(cursor.getColumnIndex("notone")));
            } else {
                contentValues.put("password", cursor.getString(cursor.getColumnIndex("password")));
                contentValues.put("str_key", cursor.getString(cursor.getColumnIndex("str_key")));
                contentValues.put("note", cursor.getString(cursor.getColumnIndex("note")));
            }
            if (databaseHelper.insert(contentValues) > 0) {
                count++;
            }
        }
        databaseHelper.close();
        sqLiteDatabase.close();
        FileUtils.deleteCache(path);
        return count;
    }

}
