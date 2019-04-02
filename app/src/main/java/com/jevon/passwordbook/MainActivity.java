package com.jevon.passwordbook;

import android.databinding.DataBindingUtil;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.jevon.passwordbook.adapter.MainListAdapter;
import com.jevon.passwordbook.databinding.ActivityMainBinding;
import com.jevon.passwordbook.listener.RecyclerViewClickListener;
import com.jevon.passwordbook.utils.Jlog;
import com.jevon.passwordbook.viewmodel.MainViewModel;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        viewModel = new MainViewModel(this);
        mainBinding.setViewmodel(viewModel);

        /* recyclerView设置 */
        recyclerView = mainBinding.recyclerview;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        MainListAdapter adapter = viewModel.getAdapter();
//        recyclerView.setAdapter(adapter);

        //设置Toolbar
        setSupportActionBar(mainBinding.toolbarMain);
        //设置ToolBar左侧的图标
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.menu);

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
}
