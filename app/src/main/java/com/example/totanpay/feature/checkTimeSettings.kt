package com.example.totanpay.feature

import android.content.Context
import android.provider.Settings
import androidx.compose.ui.res.stringResource
import com.example.totanpay.R
import com.example.totanpay.util.TimeSettingsChecker

fun checkTimeSettings(context: Context): String? {
    return try {
        if (TimeSettingsChecker.isAutoDateTimeEnabled(context)) {
            context.getString(R.string.please_turn_off_automatic_date_and_time_setting)
        } else if (!TimeSettingsChecker.isTimeZoneTehran()) {
            context.getString(R.string.please_set_time_zone_to_tehran)
        } else {
            null
        }
    } catch (e: Settings.SettingNotFoundException) {
        e.printStackTrace()
        null
    }
}
