package com.jevon.passwordbook.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.jevon.passwordbook.R;
import com.jevon.passwordbook.databinding.LayoutPasswordDetailBinding;
import com.jevon.passwordbook.viewmodel.InsertVM;

public class InsertActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutPasswordDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.layout_password_detail);

        initBar(binding);

        InsertVM insertVM = new InsertVM();
        binding.setItem(insertVM.password);
        binding.setInsertvm(insertVM);

    }

    private void initBar(LayoutPasswordDetailBinding binding) {
        //设置Toolbar
        setSupportActionBar(binding.toolbarDetail);
        //设置ToolBar左侧的图标
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("添加");
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return true;
    }
}
