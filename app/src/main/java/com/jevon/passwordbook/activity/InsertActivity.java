package com.jevon.passwordbook.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.jevon.passwordbook.R;
import com.jevon.passwordbook.been.Password;
import com.jevon.passwordbook.databinding.LayoutPasswordDetailBinding;
import com.jevon.passwordbook.viewmodel.PasswordDetailVM;

public class InsertActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutPasswordDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.layout_password_detail);

        initBar(binding);

        PasswordDetailVM vm = PasswordDetailVM.getInstance();
        vm.setActivity(PasswordDetailVM.ACTIVITY_INSERT);
        vm.setPassword(new Password());
        binding.setItem(vm.getPassword());
        binding.setVm(vm);

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
