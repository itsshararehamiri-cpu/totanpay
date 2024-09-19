package com.example.totanpay.printutil;

import android.graphics.Bitmap;
import android.os.Environment;

import com.urovo.sdk.utils.DateUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class SignFiles {
    public static String signPhotoPath = Environment.getExternalStorageDirectory()
            + "/urovosdkLibs/printImg/";

    /*
     * 在内存中创建文件夹
     */
    public static String createSignFileDir() {
        String photoPath = signPhotoPath;
        File pohoFiles = new File(photoPath);
        if (!pohoFiles.exists()) {
            pohoFiles.mkdirs();
        }
        String cmd = "chmod 777 " + pohoFiles.getAbsolutePath();
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return photoPath;
    }

    public static String createSignFile(Bitmap bitmap) {
        ByteArrayOutputStream baos = null;
        String _path = null;
        try {
            _path = SignFiles.createSignFileDir() + "/"
                    + DateUtil.getDateTime7(new Date()) + ".jpeg";
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] photoBytes = baos.toByteArray();
            if (photoBytes != null) {
                new FileOutputStream(new File(_path)).write(photoBytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return _path;
    }

    public static void deleteAll(File f) {
        // 文件
        if (f.isFile()) {
            f.delete();
        } else { // 文件夹
            // 获得当前文件夹下的所有子文件和子文件夹
            File f1[] = f.listFiles();
            // 循环处理每个对象
            int len = f1.length;
            for (int i = 0; i < len; i++) {
                // 递归调用，处理每个文件对象
                deleteAll(f1[i]);
            }
            // 删除当前文件夹
            f.delete();
        }
    }
}
