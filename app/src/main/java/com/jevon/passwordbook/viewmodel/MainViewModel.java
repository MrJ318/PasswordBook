package com.jevon.passwordbook.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.jevon.passwordbook.activity.DetailActivity;
import com.jevon.passwordbook.activity.InsertActivity;
import com.jevon.passwordbook.adapter.MainListAdapter;
import com.jevon.passwordbook.been.Password;
import com.jevon.passwordbook.model.MainModel;

import java.util.List;

/**
 * @Author: Mr.J
 * @CreateDate: 2019/3/28 16:33
 */
public class MainViewModel {

    private Activity activity;
    private MainModel mainModel;
    private List<Password> list;
    private MainListAdapter adapter;

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
    }

    /**
     * 查看是否有数据
     *
     * @return 数据个数
     */
    public int getListSize() {
        return list.size();
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
        startActivity(InsertActivity.class);
    }


    //    清除数据
    public void deleteAllData() {
        Toast.makeText(activity, "2", Toast.LENGTH_SHORT).show();
    }


    //    备份数据
    public void backups() {
        Toast.makeText(activity, "3", Toast.LENGTH_SHORT).show();
    }


    //    导入数据
    public void imports() {
        Toast.makeText(activity, "4", Toast.LENGTH_SHORT).show();
    }


    //    修改密码
    public void change() {
        Toast.makeText(activity, "5", Toast.LENGTH_SHORT).show();
    }


    //    关于
    public void about() {
        Toast.makeText(activity, "6", Toast.LENGTH_SHORT).show();
    }

    private void startActivity(Class c) {
        activity.startActivity(new Intent(activity, c));
    }


}
