package com.jevon.passwordbook.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: Mr.J
 * @CreateDate: 2019/4/10 12:15
 */
public class FileUtils {
    private static final String DATA_PATH = "/data/data/com.jevon.passwordbook/databases";

    public static void copyDatabase(String path) throws IOException {

        //获取数据目录下的数据库文件
        File[] files = new File(DATA_PATH).listFiles();

        File dir = new File(path + "/" + getDate());
        dir.mkdir();
        for (File souceFile : files) {
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
    }

    private static String getDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HHmmss");
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }
}
