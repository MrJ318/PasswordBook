package com.jevon.passwordbook.viewmodel;

import android.content.ContentValues;
import android.database.Cursor;
import android.databinding.BindingAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.jevon.passwordbook.PasswordApplication;
import com.jevon.passwordbook.been.Password;
import com.jevon.passwordbook.utils.DatabaseHelper;

/**
 * @Author: Mr.J
 * @CreateDate: 2019/4/2 11:11
 */
public class InsertVM {

    public final Password password = new Password();

    //    保存按钮点击事件
    public void onClick() {
        DatabaseHelper databaseHelper = new DatabaseHelper(PasswordApplication.getContext());
        Cursor cursor = databaseHelper.query(password.getName());

        if (cursor.getCount() != 0) {
            Toast.makeText(PasswordApplication.getContext(), "该名称已存在！", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", password.getName());
        contentValues.put("id", password.getId());
        contentValues.put("password", password.getPsw());
        contentValues.put("note", password.getNote());
        if (databaseHelper.insert(contentValues) != -1) {
            password.setNull();
            Toast.makeText(PasswordApplication.getContext(), "保存成功！", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(PasswordApplication.getContext(), "保存失败，请重试！", Toast.LENGTH_SHORT).show();
        }
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
