package com.jevon.passwordbook.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.jevon.passwordbook.R;
import com.jevon.passwordbook.databinding.ActivityAboutBinding;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityAboutBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_about);

        //设置Toolbar
        setSupportActionBar(binding.toolbarAbout);
        //设置ToolBar左侧的图标
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //设置显示版本号
        try {
            PackageInfo pack = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            String versonname = pack.versionName;
            binding.textVersion.setText(String.format(getResources().getString(R.string.version), versonname));
        } catch (PackageManager.NameNotFoundException e) {
            binding.textVersion.setVisibility(View.INVISIBLE);
            e.printStackTrace();
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
