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
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.jevon.passwordbook.R;
import com.jevon.passwordbook.databinding.ActivityMainBinding;
import com.jevon.passwordbook.listener.MainListener;
import com.jevon.passwordbook.utils.Jtoast;
import com.jevon.passwordbook.viewmodel.MainViewModel;


public class MainActivity extends AppCompatActivity implements MainListener {

    private DrawerLayout drawerLayout;
    private MainViewModel viewModel;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        viewModel = new MainViewModel(this);
        mainBinding.setViewmodel(viewModel);

        /* recyclerView设置 */
        mainBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));

        searchView = mainBinding.searchView;
        searchView.setOnQueryTextListener(viewModel);


        //设置Toolbar
        setSupportActionBar(mainBinding.toolbarMain);

        drawerLayout = mainBinding.drawerlayout;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(Gravity.START);
        }
        return true;
    }

    @Override
    protected void onRestart() {
        viewModel.refreshData();
        searchView.clearFocus();
        searchView.onActionViewCollapsed();
        drawerLayout.closeDrawers();
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

    @Override
    public void onOperationComplete(String msg) {
        drawerLayout.closeDrawers();
        Jtoast.show(msg);
    }
}
