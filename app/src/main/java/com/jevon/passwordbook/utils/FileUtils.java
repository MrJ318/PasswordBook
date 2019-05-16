package com.jevon.passwordbook.utils;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @Author: Mr.J
 * @CreateDate: 2019/4/10 12:15
 */
public class FileUtils {

    /**
     * 备份数据库文件到外部存储
     *
     * @param path 存储外部路径
     * @throws IOException io异常
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void copyDatabase(Context context, String path) throws IOException {

        File souceFile = context.getDatabasePath("PasswordData.db");
        File dir = new File(path + "/" + getDate());
        dir.mkdir();
        //创建新文件
        File newFile = new File(dir, souceFile.getName());
        if (newFile.exists()) {
            newFile.createNewFile();
        }
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(newFile));
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(souceFile));
        byte[] bytes = new byte[1024];
        int len;
        while ((len = bis.read(bytes)) != -1) {
            bos.write(bytes, 0, len);
        }
        bos.flush();
        bis.close();
        bos.close();
    }

    /**
     * 删除数据库产生的缓存
     *
     * @param path 缓存路径
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void deleteCache(String path) {
        File shm = new File(path + "-shm");
        File wal = new File(path + "-wal");
        if (shm.exists()) {
            shm.delete();

        }
        if (wal.exists()) {
            wal.delete();
        }
    }

    /**
     * 获取当前日期
     *
     * @return 当前日期
     */
    private static String getDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HHmmss", Locale.CHINA);
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }
}
