package com.example.totanpay.printutil;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;

public class StorageUtils {

    private final static String TAG = "StorageUtils===>";

    // 是否有外置存储卡
    public static boolean hasSd = false;

    public static StorageManager storageManager;

    public static String initSd(Context context) {
        storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        // 1000单位,个人测过的大部分是以1000为单位的,部分是1024,实际使用可以自己测试下
        float unit = 1000;
        try {
            Method getVolumes = StorageManager.class.getDeclaredMethod("getVolumes");
            List<Object> getVolumeInfo = (List<Object>) getVolumes.invoke(storageManager);
            if (null != getVolumeInfo && getVolumeInfo.size() > 0) {
                for (int i = 0; i < getVolumeInfo.size(); i++) {
                    Object obj = getVolumeInfo.get(i);
                    Field getType = obj.getClass().getField("type");
                    int type = getType.getInt(obj);
                    if (type == 0) {//TYPE_PUBLIC
                        //外置存储
                        Method isMountedReadable = obj.getClass().getDeclaredMethod("isMountedReadable");
                        boolean readable = (boolean) isMountedReadable.invoke(obj);
                        if (readable) {
                            hasSd = true;
                            Method file = obj.getClass().getDeclaredMethod("getPath");
                            File f = (File) file.invoke(obj);
                            //外置存储
                            String path = getPath(obj);
                            long total = f.getTotalSpace();
                            long free = f.getFreeSpace();
                            Log.e(TAG ,"外置:" + path + ",total:" + getUnit(total, unit) + ",free:" + getUnit(free, unit));
                            return path;
                        }
                    }
                }
                return "";
            }
        } catch (SecurityException e) {
            Log.e(TAG, "请检查权限");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String getPath(Object obj) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String sdPath = "";
        Method isMountedReadable = obj.getClass().getDeclaredMethod("isMountedReadable");
        boolean readable = (boolean) isMountedReadable.invoke(obj);
        if (readable) {
            Method file = obj.getClass().getDeclaredMethod("getPath");
            File f = (File) file.invoke(obj);
            sdPath = f.getPath();
        }
        return sdPath;
    }

    // 获取外置存储卡视频的大小
    public static long getVideoSize(Context context) {
        //获取ContentResolver实例
        long total = 0;
        ContentResolver resolver = context.getContentResolver();
        // 视频为例: 需要查询的内容->名称，大小，路径
        String[] querys = new String[]{MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.SIZE, MediaStore.Video.Media.DATA};
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        //开始查询，查询结果是一个cursor对象
        Cursor cursor = resolver.query(uri, querys, null, null, null);
        //若查询的Uri不存在，则cursor为空，所以要做非空判断
        if (cursor != null) {
            while (cursor.moveToNext()) {
                // 判断路径
                String displayName = cursor.getString(0);
                int size = cursor.getInt(1);
                String filePath = cursor.getString(2);
                total += size;

                Log.e(TAG ,"name:" + displayName + ",size:" + size + ",path:" + filePath);
            }
            //记得关闭cursor
            cursor.close();
            Log.e(TAG ,"video total size:" + total);
        }
        return total;
    }

    // 格式化SD卡以及挂载
    private static void format(String id) {
        try {
            Method format = StorageManager.class.getDeclaredMethod("format", String.class);
            Method mount = StorageManager.class.getDeclaredMethod("mount", new Class[]{String.class});
            format.invoke(storageManager, id);
            mount.invoke(storageManager, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final String[] units = {"B", "KB", "MB", "GB", "TB"};

    /**
     * 进制转换
     */
    public static String getUnit(float size, float base) {
        int index = 0;
        while (size > base && index < 4) {
            size = size / base;
            index++;
        }
        return String.format(Locale.getDefault(), " %.2f %s ", size, units[index]);
    }

}