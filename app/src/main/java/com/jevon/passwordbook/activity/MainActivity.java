package com.jevon.passwordbook.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.MenuItem;

import com.jevon.passwordbook.R;
import com.jevon.passwordbook.databinding.ActivityMainBinding;
import com.jevon.passwordbook.viewmodel.MainViewModel;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private MainViewModel viewModel;
    private int startTag = 0;

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
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
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
    protected void onStart() {
        if (startTag > 0) {
            viewModel.refreshData();
        }
        startTag++;
        super.onStart();
    }
}
