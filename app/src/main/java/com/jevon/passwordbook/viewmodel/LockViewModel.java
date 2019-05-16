package com.jevon.passwordbook.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;

import com.jevon.passwordbook.activity.MainActivity;
import com.jevon.passwordbook.utils.Jtoast;
import com.jevon.passwordbook.utils.SharedPreferenceUtils;

import java.util.Objects;

public class LockViewModel {

    private Activity mContext;
    private SharedPreferenceUtils mSharedPreferences;
    private CancellationSignal cancellationSignal;
    public final ObservableField<String> psw = new ObservableField<>();
    public final ObservableBoolean status = new ObservableBoolean();

    public LockViewModel(Activity mContext) {
        this.mContext = mContext;
        mSharedPreferences = new SharedPreferenceUtils(mContext.getApplicationContext());
        status.set(mSharedPreferences.getStatus());

        if (status.get()) {
            FingerprintManagerCompat fingerprintManager = FingerprintManagerCompat.from(mContext);
            cancellationSignal = new CancellationSignal();
            fingerprintManager.authenticate(null, 0, cancellationSignal, new FingerCallback(mContext), null);
        }
    }


    /**
     * 确认按钮点击事件
     */
    public void onPositiveClick() {
        String password;
        if (psw.get() == null) {
            password = "";
        } else {
            password = Objects.requireNonNull(psw.get()).trim();
        }
        if (!status.get()) {
            if (4 > password.length()) {
                Jtoast.show("密码长度不能小于四位");
                return;
            }
            mSharedPreferences.putPassword(password);
            mSharedPreferences.putStatus(true);
            Jtoast.show("密码设置成功，请牢记！");

        } else {
            if (password.equals("") || !password.equals(mSharedPreferences.getPassword())) {
                Jtoast.show("密码错误！");
                return;
            }
        }
        mContext.startActivity(new Intent(mContext, MainActivity.class));
        mContext.finish();
    }

    /**
     * 取消按钮点击事件
     */
    public void onCancelClick() {
        mContext.finish();
    }

    public void onDestroy() {
        if (cancellationSignal != null) {
            cancellationSignal.cancel();
        }
    }
}
