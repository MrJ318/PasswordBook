package com.jevon.passwordbook.viewmodel;

import android.app.Activity;
import android.content.ContentValues;
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

    private Activity activity;
    public final Password password = new Password();

    public InsertVM(Activity activity) {
        this.activity = activity;
    }

    //    保存按钮点击事件
    public void onClick() {
        Toast.makeText(PasswordApplication.getContext(), "" + password.getName(), Toast.LENGTH_SHORT).show();
        DatabaseHelper databaseHelper = new DatabaseHelper(PasswordApplication.getContext());
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

    //    生成随机密码
    public void generatePwd() {

    }


}
