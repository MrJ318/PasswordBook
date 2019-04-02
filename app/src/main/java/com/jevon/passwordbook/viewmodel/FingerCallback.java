package com.jevon.passwordbook.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.widget.Toast;

import com.jevon.passwordbook.MainActivity;

public class FingerCallback extends FingerprintManagerCompat.AuthenticationCallback {

    private Activity context;

    FingerCallback(Activity activity) {
        context = activity;
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        super.onAuthenticationError(errMsgId, errString);

        //errMagId = 5  取消监听
        //errMagId = 5  错误次数过多，30s后重试
        if (errMsgId != 5) {
            Toast.makeText(context, errString, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        super.onAuthenticationHelp(helpMsgId, helpString);
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);
        context.startActivity(new Intent(context, MainActivity.class));
        context.finish();
    }

    @Override
    public void onAuthenticationFailed() {
        super.onAuthenticationFailed();
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(100);
    }
}
