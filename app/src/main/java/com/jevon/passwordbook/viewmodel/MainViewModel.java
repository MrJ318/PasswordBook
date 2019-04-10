package com.jevon.passwordbook.viewmodel;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.ObservableField;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.jevon.passwordbook.R;
import com.jevon.passwordbook.activity.DetailActivity;
import com.jevon.passwordbook.activity.InsertActivity;
import com.jevon.passwordbook.adapter.MainListAdapter;
import com.jevon.passwordbook.been.Password;
import com.jevon.passwordbook.model.MainModel;
import com.jevon.passwordbook.utils.DatabaseHelper;
import com.jevon.passwordbook.utils.FileUtils;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;

import java.io.IOException;
import java.util.List;

/**
 * @Author: Mr.J
 * @CreateDate: 2019/3/28 16:33
 */
public class MainViewModel {

    public static final int REQUESTCODE_PERMISSION_BACKPACK = 1000;
    public static final int REQUESTCODE_PERMISSION_IMPORTS = 1001;
    public static final int REQUESTCODE_CHOOESFILE_DIR = 1002;
    public static final int REQUESTCODE_CHOOESFILE_FILE = 1003;


    private Activity activity;
    private MainModel mainModel;
    private List<Password> list;
    private MainListAdapter adapter;
    public final ObservableField<Integer> listSize = new ObservableField<>();

    public MainViewModel(Activity activity) {
        this.activity = activity;
        mainModel = new MainModel(this.activity);
    }

    /**
     * 获取从数据库查询到的数据，生成adapter
     *
     * @return adapter
     */
    public MainListAdapter getAdapter() {
        list = mainModel.getAllData();
        adapter = new MainListAdapter(this, list);
        return adapter;
    }

    //    刷新数据
    public void refreshData() {
        list = mainModel.getAllData();
        adapter.notifyDataSetChanged();
        listSize.set(list.size());
    }

    /**
     * RecyclerView item点击事件
     *
     * @param possition item id
     */
    public void onRecyclerViewItemClick(int possition) {
        Intent intent = new Intent(activity, DetailActivity.class);
        intent.putExtra("item", list.get(possition));
        activity.startActivity(intent);
    }

    //    添加
    public void addItem() {
        activity.startActivity(new Intent(activity, InsertActivity.class));
    }

    //    清除数据
    public void deleteAllData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("确定要清空全部数据吗?");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new DatabaseHelper(activity).delete();
                refreshData();
                Toast.makeText(activity, "操作成功！", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    //    权限申请
    public void requestPermission(View view) {
        if (activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            switch (view.getId()) {
                case R.id.layout_backup:
                    activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESTCODE_PERMISSION_BACKPACK);
                    break;
                case R.id.layout_import:
                    activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESTCODE_PERMISSION_IMPORTS);
                    break;
            }
        } else {
            switch (view.getId()) {
                case R.id.layout_backup:
                    chooesFile(REQUESTCODE_CHOOESFILE_DIR);
                    break;
                case R.id.layout_import:
                    chooesFile(REQUESTCODE_CHOOESFILE_FILE);
                    break;
            }
        }
    }

    //    文件选择
    public void chooesFile(int requestNum) {
        LFilePicker filePicker = new LFilePicker();
        filePicker.withActivity(activity);
        filePicker.withRequestCode(requestNum);
        filePicker.withIconStyle(Constant.ICON_STYLE_BLUE);//图标风格
        filePicker.withStartPath("" + Environment.getExternalStorageDirectory());//指定初始显示路径
        filePicker.withMutilyMode(false);//是否多选

        if (requestNum == REQUESTCODE_CHOOESFILE_FILE) {
            filePicker.withTitle("文件选择");
            filePicker.withChooseMode(true);//true(默认)为选择文件，false为选择文件夹
        } else {
            filePicker.withTitle("选择备份路径");
            filePicker.withChooseMode(false);//true(默认)为选择文件，false为选择文件夹
        }
        filePicker.start();
    }

    //    备份数据
    public void backups(String path) {
        try {
            FileUtils.copyDatabase(path);
            Toast.makeText(activity, "备份成功！", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(activity, "备份出错！\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //    导入数据
    public void imports(String path) {

        int result = mainModel.readData(path);
        if (result > -1) {
            Toast.makeText(activity, "成功导入" + result + "条数据。", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, "未读取到数据！", Toast.LENGTH_SHORT).show();
        }
    }

    //    修改密码
    public void change() {
        Toast.makeText(activity, "5", Toast.LENGTH_SHORT).show();
    }

    //    关于
    public void about() {
        Toast.makeText(activity, "6", Toast.LENGTH_SHORT).show();
    }

}
