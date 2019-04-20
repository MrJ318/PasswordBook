package com.jevon.passwordbook.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.jevon.passwordbook.R;
import com.jevon.passwordbook.databinding.ActivityMainBinding;
import com.jevon.passwordbook.viewmodel.MainViewModel;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        viewModel = new MainViewModel(this);
        mainBinding.setViewmodel(viewModel);

        /* recyclerView设置 */
        mainBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));

        //设置Toolbar
        setSupportActionBar(mainBinding.toolbarMain);
        //设置ToolBar左侧的图标
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
            actionBar.setTitle("密码本（测试版）");
        }

        drawerLayout = mainBinding.drawerlayout;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(Gravity.START);
                break;
        }
        return true;
    }

    @Override
    protected void onRestart() {
        viewModel.refreshData();
        super.onRestart();
    }

    //    权限申请回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MainViewModel.REQUESTCODE_PERMISSION_BACKPACK:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    viewModel.chooesFile(MainViewModel.REQUESTCODE_CHOOESFILE_DIR);
                } else {
                    Toast.makeText(this, "权限被拒绝！", Toast.LENGTH_SHORT).show();
                }
                break;
            case MainViewModel.REQUESTCODE_PERMISSION_IMPORTS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    viewModel.chooesFile(MainViewModel.REQUESTCODE_CHOOESFILE_FILE);
                } else {
                    Toast.makeText(this, "权限被拒绝！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    //    文件选择回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            switch (requestCode) {
                case MainViewModel.REQUESTCODE_CHOOESFILE_DIR:
                    viewModel.backups(data.getStringExtra("path"));
                    break;
                case MainViewModel.REQUESTCODE_CHOOESFILE_FILE:
                    viewModel.imports(data.getStringArrayListExtra("paths").get(0));
                    break;
            }
        }
    }
}
