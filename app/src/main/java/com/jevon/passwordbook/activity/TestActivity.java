package com.jevon.passwordbook.activity;

import android.database.DatabaseUtils;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jevon.passwordbook.R;
import com.jevon.passwordbook.databinding.ActivityTestBinding;
import com.jevon.passwordbook.utils.Jlog;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTestBinding activityTestBinding = DataBindingUtil.setContentView(this, R.layout.activity_test);

        activityTestBinding.setActivity(this);

    }


    public void onClick() {
        Jlog.d("testttt");
    }
}
