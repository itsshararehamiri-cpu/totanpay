package com.example.totanpay.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionUtil {
    public static final String[] Permissions = new String[]{
            Manifest.permission.ACCESS_WIFI_STATE
    };
    public static List<String> getRequestPermissionList(Context context, String[] permissions) {
        List<String> reequestPermissionCount = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                reequestPermissionCount.add(permission);
            }
        }
        return reequestPermissionCount;
    }

}
