package com.example.totanpay.printutil;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionUtil {

    private static final String TAG = "PermissionUtil";

    public static final String[] Permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_PHONE_NUMBERS,
            Manifest.permission.READ_SMS,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_WIFI_STATE
    };

    /**
     * 获取需要去申请权限的权限列表
     *
     * @param permissions
     * @return
     */
    public static List<String> getRequestPermissionList(Context context, String... permissions) {
        List<String> reequestPermissionCount = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "checkSelfPermission false:" + permission);
                reequestPermissionCount.add(permission);
            }
        }
        return reequestPermissionCount;
    }

}
