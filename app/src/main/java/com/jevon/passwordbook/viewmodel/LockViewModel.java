package com.jevon.passwordbook.viewmodel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.ObservableField;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.widget.Toast;

import com.jevon.passwordbook.activity.MainActivity;

import java.util.Objects;

/**
 *
 */
public class LockViewModel {

    private Activity mContext;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private CancellationSignal cancellationSignal;
    public final ObservableField<String> psw = new ObservableField<>();

    @SuppressLint("CommitPrefEdits")
    public LockViewModel(Activity mContext) {
        this.mContext = mContext;
        mSharedPreferences = mContext.getSharedPreferences("passwordbook", Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    /**
     * 获取运行状态
     * @return 是否已经设置密码
     */
    public boolean getStatus() {
        boolean status = mSharedPreferences.getBoolean("status", false);
        //启用指纹
        if (status) {
            FingerprintManagerCompat fingerprintManager = FingerprintManagerCompat.from(mContext);
            cancellationSignal = new CancellationSignal();
            fingerprintManager.authenticate(null, 0, cancellationSignal, new FingerCallback(mContext), null);
        }
        return status;
    }

    /**
     * 确认按钮点击事件
     * @param statu 运行状态
     */
    public void onPositiveClick(boolean statu) {
        String password;
        if (psw.get() == null) {
            password = "";
        } else {
            password = Objects.requireNonNull(psw.get()).trim();
        }
        if (!statu) {
            if (4 > password.length()) {
                Toast.makeText(mContext, "密码长度不能小于四位", Toast.LENGTH_SHORT).show();
                return;
            }
            mEditor.putString("password", password);
            mEditor.putBoolean("status", true);
            mEditor.commit();
            Toast.makeText(mContext, "密码设置成功，请牢记！", Toast.LENGTH_SHORT).show();

        } else {
            if (password.equals("") || !password.equals(mSharedPreferences.getString("password", ""))) {
                Toast.makeText(mContext, "密码错误！", Toast.LENGTH_LONG).show();
                return;
            }
        }
        mContext.startActivity(new Intent(mContext, MainActivity.class));
        cancellationSignal.cancel();
        mContext.finish();
    }

    /**
     * 取消按钮点击事件
     */
    public void onCancelClick() {
        cancellationSignal.cancel();
        mContext.finish();
    }

    public void onDestroy() {
        cancellationSignal.cancel();
    }
}
