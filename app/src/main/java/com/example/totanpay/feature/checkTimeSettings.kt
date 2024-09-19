package com.example.totanpay.feature

import android.content.Context
import android.provider.Settings
import com.example.totanpay.util.TimeSettingsChecker

fun checkTimeSettings(context: Context): String? {
    return try {
        if (TimeSettingsChecker.isAutoDateTimeEnabled(context)) {
            "لطفاً تنظیم خودکار تاریخ و ساعت را خاموش کنید."
        } else if (!TimeSettingsChecker.isTimeZoneTehran()) {
            "لطفاً منطقه زمانی را روی تهران تنظیم کنید."
        } else {
            null
        }
    } catch (e: Settings.SettingNotFoundException) {
        e.printStackTrace()
        null
    }
}
