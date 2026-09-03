package com.example.totanpay.data.repository.settings.device_settings

import kotlinx.coroutines.flow.Flow

interface DeviceSettingsRepository {
    fun setPlaybackStatusSound(enable:Boolean)
    fun getPlaybackStatusSound():Boolean
    fun getThemeIsDark(): Flow<Boolean>
 suspend   fun setThemeIsDark(enable:Boolean)
}