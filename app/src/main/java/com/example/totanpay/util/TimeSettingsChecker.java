package com.example.totanpay.util;

import android.provider.Settings;
import android.content.Context;
import android.util.Log;


import com.google.gson.Gson;

import java.util.TimeZone;

public class TimeSettingsChecker {

    public static boolean isAutoDateTimeEnabled(Context context) {
        try {
            return Settings.Global.getInt(
                context.getContentResolver(),
                Settings.Global.AUTO_TIME
            ) == 1;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isAutoTimeZoneEnabled(Context context) {
        try {
            return Settings.Global.getInt(
                context.getContentResolver(),
                Settings.Global.AUTO_TIME_ZONE
            ) == 1;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean isTimeZoneTehran() {
        TimeZone timeZone = TimeZone.getDefault();
        String timeZoneId = timeZone.getID();
        return "Asia/Tehran".equals(timeZoneId) || timeZoneId.contains("Tehran") || timeZoneId.contains("GMT+03:30");


    }
}
