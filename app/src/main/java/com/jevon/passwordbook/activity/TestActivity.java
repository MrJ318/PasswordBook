package com.jevon.passwordbook.activity;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jevon.passwordbook.R;
import com.jevon.passwordbook.databinding.ActivityTestBinding;
import com.jevon.passwordbook.utils.AesEncryptionUtils;

public class TestActivity extends AppCompatActivity {

    ActivityTestBinding binding;
    String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_test);
        binding.setActivity(this);


        binding.btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s = AesEncryptionUtils.createKey("123");
                binding.txt1.setText(s);
            }
        });
        binding.btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.txt2.setText(AesEncryptionUtils.encrypt(s, binding.editText.getText().toString().trim()));
            }
        });
        binding.btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.txt2.setText(AesEncryptionUtils.decrypt(s, binding.txt2.getText().toString().trim()));
            }
        });
    }


}
