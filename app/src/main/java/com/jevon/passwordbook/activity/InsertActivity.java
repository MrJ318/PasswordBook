package com.jevon.passwordbook.activity;

import android.databinding.DataBindingUtil;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jevon.passwordbook.R;
import com.jevon.passwordbook.databinding.ActivityInsertBinding;
import com.jevon.passwordbook.databinding.LayoutPasswordDetailBinding;
import com.jevon.passwordbook.viewmodel.InsertVM;

public class InsertActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutPasswordDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.layout_password_detail);

        initBar(binding);

        InsertVM insertVM = new InsertVM(this);
        binding.setItem(insertVM.password);
        binding.setInsertvm(insertVM);

    }

    private void initBar(LayoutPasswordDetailBinding binding) {
        //设置Toolbar
        setSupportActionBar(binding.toolbarDetail);
        //设置ToolBar左侧的图标
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("添加");
    }
}
