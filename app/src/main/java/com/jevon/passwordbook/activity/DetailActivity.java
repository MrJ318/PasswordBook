package com.jevon.passwordbook.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.jevon.passwordbook.R;
import com.jevon.passwordbook.been.Password;
import com.jevon.passwordbook.databinding.LayoutPasswordDetailBinding;
import com.jevon.passwordbook.utils.Jtoast;
import com.jevon.passwordbook.viewmodel.PasswordDetailVM;

/**
 * @Author: Mr.J
 * @CreateDate: 2019/4/2 17:02
 */
public class DetailActivity extends AppCompatActivity {

    private PasswordDetailVM vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutPasswordDetailBinding binding
                = DataBindingUtil.setContentView(this, R.layout.layout_password_detail);

        initBar(binding);

        //获取前activity的传值
        Intent intent = getIntent();
        Password password = (Password) intent.getSerializableExtra("item");

        //获取ViewModel实例，设置相关参数
        vm = PasswordDetailVM.getInstance(getApplicationContext());
        vm.setActivity(PasswordDetailVM.ACTIVITY_DETAIL);
        //将获取到的账号密码实体交给VM
        vm.setPassword(password);
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
            actionBar.setTitle("详情");
        }

        //设置名称不可更改
        binding.editDetailName.setFocusable(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.menu_edit:
                vm.setActivity(PasswordDetailVM.ACTIVITY_EDIT);
                break;
            case R.id.menu_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("确定要删除" + vm.getPassword().getName() + "吗?");
                builder.setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (vm.deleteData() > 0) {
                            Jtoast.show("删除成功");
                            finish();
                        }
                    }
                });
                builder.show();
                break;
        }
        return true;
    }

}
