package com.jevon.passwordbook.viewmodel;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.databinding.BindingAdapter;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.jevon.passwordbook.PasswordApplication;
import com.jevon.passwordbook.R;
import com.jevon.passwordbook.been.Password;
import com.jevon.passwordbook.utils.AesEncryptionUtils;
import com.jevon.passwordbook.utils.DatabaseHelper;

/**
 * @Author: Mr.J
 * @CreateDate: 2019/4/3 11:56
 */
public class PasswordDetailVM {

    /**
     * 操作状态标志
     */
    public static final int ACTIVITY_INSERT = 0;
    public static final int ACTIVITY_DETAIL = 1;
    public static final int ACTIVITY_EDIT = 2;

    /**
     * 空间状态标志
     */
    public final ObservableBoolean isEnable = new ObservableBoolean();
    public final ObservableInt isVisible = new ObservableInt();

    @SuppressLint("StaticFieldLeak")
    private static PasswordDetailVM passwordDetailVM;
    private Password password;
    private int activity;
    private ClipboardManager manager;
    private DatabaseHelper databaseHelper;
    private Context context;


    //    构造器
    private PasswordDetailVM() {
        context = PasswordApplication.getContext();
        //剪贴板服务
        manager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);

    }

    //    单例工厂
    public static PasswordDetailVM getInstance() {
        if (passwordDetailVM == null) {
            passwordDetailVM = new PasswordDetailVM();
        }
        return passwordDetailVM;
    }

    //    设置activity标记，设置控件显示状态
    public void setActivity(int activity) {
        this.activity = activity;
        if (activity == ACTIVITY_INSERT || activity == ACTIVITY_EDIT) {
            isEnable.set(true);
            isVisible.set(View.VISIBLE);
        } else if (activity == ACTIVITY_DETAIL) {
            isEnable.set(false);
            isVisible.set(View.INVISIBLE);
        }
    }

    public Password getPassword() {
        return password;
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    //    保存按钮点击事件
    public void onSaveBtnClick(View view) {
        if (activity == ACTIVITY_INSERT) {
            insertData();
        } else if (activity == ACTIVITY_EDIT) {
            updateData();
        }

        //收起软键盘
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    //    保存按钮--新增数据
    private void insertData() {

        databaseHelper = new DatabaseHelper(context);
        Cursor cursor = databaseHelper.query(password.getName());
        if (cursor.getCount() != 0) {
            Toast.makeText(context, "该名称已存在！", Toast.LENGTH_SHORT).show();
            return;
        }
        String key = AesEncryptionUtils.createKey(password.getPsw());
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", password.getName());
        contentValues.put("id", password.getId());
        contentValues.put("password", AesEncryptionUtils.encrypt(key, password.getPsw()));
        contentValues.put("note", password.getNote());
        contentValues.put("str_key", key);
        if (databaseHelper.insert(contentValues) > 0) {
            password.setNull();
            Toast.makeText(context, "保存成功！", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "保存失败，请重试！", Toast.LENGTH_SHORT).show();
        }
        databaseHelper.close();
    }

    //    保存按钮--更新数据
    private void updateData() {

        String key = AesEncryptionUtils.createKey(password.getPsw());
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", password.getName());
        contentValues.put("id", password.getId());
        contentValues.put("password", AesEncryptionUtils.encrypt(key, password.getPsw()));
        contentValues.put("note", password.getNote());
        contentValues.put("str_key", key);
        databaseHelper = new DatabaseHelper(context);
        if (databaseHelper.update(contentValues, password.getName()) > 0) {
            setActivity(ACTIVITY_DETAIL);
            Toast.makeText(context, "修改成功！", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "修改失败，请重试！", Toast.LENGTH_SHORT).show();
        }
        databaseHelper.close();
    }

    //    账号密码复制
    public void onCopyClick(View view) {
        //若处于编辑状态，则不进行复制
        if (isEnable.get()) {
            return;
        }
        ClipData clipData = null;
        switch (view.getId()) {
            case R.id.edit_detail_id:
                clipData = ClipData.newPlainText("a", password.getId());
                Toast.makeText(context, "账号已复制到剪贴板！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.edit_detail_password:
                clipData = ClipData.newPlainText("a", password.getPsw());
                Toast.makeText(context, "密码已复制到剪贴板！", Toast.LENGTH_SHORT).show();
                break;
        }
        if (clipData != null) {
            manager.setPrimaryClip(clipData);
        }
    }

    //    删除数据
    public int deleteData() {
        databaseHelper = new DatabaseHelper(context);
        int result = databaseHelper.delete(password.getName());
        databaseHelper.close();
        return result;
    }

    //    设置文字头像
    @BindingAdapter("imageText")
    public static void setImg(ImageView img, String imageText) {
        ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
        if (imageText == null || "".equals(imageText)) {
            imageText = "空";
        }
        TextDrawable textDrawable = TextDrawable.builder().beginConfig().toUpperCase().endConfig()
                .buildRound(imageText.substring(0, 1), colorGenerator.getColor(imageText));
        img.setImageDrawable(textDrawable);
    }


}
