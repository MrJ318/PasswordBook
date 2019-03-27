package com.jevon.passwordbook.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jevon.passwordbook.R;
import com.jevon.passwordbook.databinding.ActivityLockBinding;
import com.jevon.passwordbook.viewmodel.LockViewModel;

public class LockActivity extends AppCompatActivity {

    private LockViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLockBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_lock);

        viewModel = new LockViewModel(this);
        mBinding.setViewModel(viewModel);
    }

    @Override
    protected void onDestroy() {
        viewModel.onDestroy();
        super.onDestroy();
    }
}
