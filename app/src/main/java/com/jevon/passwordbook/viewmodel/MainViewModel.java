package com.jevon.passwordbook.viewmodel;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.ObservableField;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;

import com.jevon.passwordbook.R;
import com.jevon.passwordbook.activity.AboutActivity;
import com.jevon.passwordbook.activity.DetailActivity;
import com.jevon.passwordbook.activity.InsertActivity;
import com.jevon.passwordbook.activity.MainActivity;
import com.jevon.passwordbook.adapter.MainListAdapter;
import com.jevon.passwordbook.been.Password;
import com.jevon.passwordbook.model.MainModel;
import com.jevon.passwordbook.utils.DatabaseHelper;
import com.jevon.passwordbook.utils.FileUtils;
import com.jevon.passwordbook.utils.Jtoast;
import com.jevon.passwordbook.utils.SharedPreferenceUtils;
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

    private Activity mainActivity;
    private MainModel mainModel;
    private List<Password> list;
    private MainListAdapter adapter;
    public final ObservableField<Integer> listSize = new ObservableField<>();


    public MainViewModel(MainActivity activity) {
        this.mainActivity = activity;
        mainModel = new MainModel();
    }

    /**
     * 控件点击事件
     *
     * @param view 控件
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void buttonClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                mainActivity.startActivity(new Intent(mainActivity, InsertActivity.class));
//                refreshData();
                break;
            case R.id.layout_clear_data:
                deleteAllData();
                break;
            case R.id.layout_backup:
                requestPermission(view);
                break;
            case R.id.layout_import:
                requestPermission(view);
                break;
            case R.id.layout_modify_password:
                change();
                break;
            case R.id.layout_about:
                mainActivity.startActivity(new Intent(mainActivity, AboutActivity.class));
                break;
        }
    }

    /**
     * 获取从数据库查询到的数据，生成adapter
     *
     * @return adapter
     */
    public MainListAdapter getAdapter() {
        list = mainModel.getDataFromDB();
        adapter = new MainListAdapter(this, list);
        listSize.set(list.size());
        return adapter;
    }

    /**
     * 刷新数据
     */
    public void refreshData() {
        list = mainModel.getDataFromDB();
        adapter.notifyDataSetChanged();
        listSize.set(list.size());
    }

    /**
     * RecyclerView item点击事件
     *
     * @param position item id
     */
    public void onRecyclerViewItemClick(int position) {
        Intent intent = new Intent(mainActivity, DetailActivity.class);
        intent.putExtra("item", list.get(position));
        mainActivity.startActivity(intent);
    }


    /**
     * 清除所有数据
     */
    private void deleteAllData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setMessage("该操作会清除全部数据，确认继续吗？");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseHelper db = new DatabaseHelper();
                db.delete();
                db.close();
                refreshData();
                Jtoast.show("清除成功！");

            }
        });
        builder.show();
    }


    //    权限申请
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermission(View view) {
        if (mainActivity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            switch (view.getId()) {
                case R.id.layout_backup:
                    mainActivity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESTCODE_PERMISSION_BACKPACK);
                    break;
                case R.id.layout_import:
                    mainActivity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESTCODE_PERMISSION_IMPORTS);
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
        filePicker.withActivity(mainActivity);
        filePicker.withRequestCode(requestNum);
        filePicker.withIconStyle(Constant.ICON_STYLE_BLUE);//图标风格
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

    /**
     * 备份数据
     *
     * @param path 备份路径
     */
    public void backups(String path) {
        try {
            FileUtils.copyDatabase(mainActivity.getApplicationContext(), path);
            Jtoast.show("备份成功！");
        } catch (IOException e) {
            e.printStackTrace();
            Jtoast.show("备份出错！\n" + e.getMessage());
        }
    }

    /**
     * 导入数据
     *
     * @param path 导入路径
     */
    public void imports(String path) {

        int result = mainModel.readData(path);
        if (result > -1) {
            Jtoast.show("成功导入" + result + "条数据。");
        } else {
            Jtoast.show("未读取到数据！");
        }
    }

    /**
     * 修改密码
     */
    private void change() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(mainActivity).inflate(R.layout.dialog_modify_password, null);
        builder.setTitle("修改密码");
        builder.setView(view);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.show();

        final TextInputEditText editOldPsw = view.findViewById(R.id.edit_old_password);
        final TextInputEditText editNewPsw = view.findViewById(R.id.edit_new_password);
        final TextInputEditText editConfirm = view.findViewById(R.id.edit_confirm);
        final TextInputLayout layout1 = view.findViewById(R.id.TextInputLayout1);
        final TextInputLayout layout2 = view.findViewById(R.id.TextInputLayout2);
        final TextInputLayout layout3 = view.findViewById(R.id.TextInputLayout3);

        editOldPsw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layout1.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editNewPsw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layout2.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layout2.setErrorEnabled(false);
                layout3.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        view.findViewById(R.id.btn_dialog_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        view.findViewById(R.id.btn_dialog_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferenceUtils sh = new SharedPreferenceUtils(mainActivity.getApplicationContext());
                String oldpsw = "";
                String newpsw = "";
                String conpsw = "";
                if (editOldPsw.getText() != null) {
                    oldpsw = editOldPsw.getText().toString().trim();
                }

                if (editNewPsw.getText() != null) {
                    newpsw = editNewPsw.getText().toString().trim();
                }

                if (editConfirm.getText() != null) {
                    conpsw = editConfirm.getText().toString().trim();
                }


                if (!oldpsw.equals(sh.getPassword())) {
                    layout1.setError("密码不正确");
                    return;
                }
                if (newpsw.length() < 4) {
                    layout2.setError("密码长度不能小于4位");
                    return;
                }
                if (!newpsw.equals(conpsw)) {
                    layout2.setError("两次密码不一致");
                    layout3.setError("两次密码不一致");
                    return;
                }
                sh.putPassword(conpsw);
                dialog.cancel();
                Jtoast.show("密码修改成功");
            }
        });

    }


    /**
     * 根据名称查询指定条目
     *
     * @param name 查询名称
     */
    public void searchByName(String name) {
        if (name == null) {
            return;
        }
        list = mainModel.getDataByName(name);
        adapter.notifyDataSetChanged();
        listSize.set(list.size());
    }


}
