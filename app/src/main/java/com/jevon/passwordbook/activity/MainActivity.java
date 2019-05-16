package com.jevon.passwordbook.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.MenuItem;

import com.jevon.passwordbook.R;
import com.jevon.passwordbook.databinding.ActivityMainBinding;
import com.jevon.passwordbook.utils.Jtoast;
import com.jevon.passwordbook.viewmodel.MainViewModel;


public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, SearchView.OnQueryTextListener {

    private ActivityMainBinding mainBinding;
    private DrawerLayout drawerLayout;
    private MainViewModel viewModel;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initView();
    }

    private void initView() {

        viewModel = new MainViewModel(this);
        mainBinding.setVm(viewModel);
        //Toolbar
        setSupportActionBar(mainBinding.toolbarMain);
        // RecyclerView
        mainBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // SearchView
        searchView = mainBinding.searchView;
        searchView.setOnQueryTextListener(this);

        // SwipeRefreshLayout
        mainBinding.layoutRefresh.setOnRefreshListener(this);

        // DrawerLayout
        drawerLayout = mainBinding.drawerlayout;
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        viewModel.refreshData();
        mainBinding.layoutRefresh.setRefreshing(false);
        Jtoast.show("刷新成功！");
    }

    /**
     * 搜索框文本提交
     *
     * @param query 搜索文本
     * @return true
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        viewModel.searchByName(query);
        return true;
    }

    /**
     * 搜索框文本改变
     *
     * @param newText 新文本
     * @return true
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        viewModel.searchByName(newText);
        return true;
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
        //可以收起SearchView视图
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
                    Jtoast.show("权限被拒绝！");
                }
                break;
            case MainViewModel.REQUESTCODE_PERMISSION_IMPORTS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    viewModel.chooesFile(MainViewModel.REQUESTCODE_CHOOESFILE_FILE);
                } else {
                    Jtoast.show("权限被拒绝！");
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
