package com.example.totanpay.data.repository.datasource

import kotlinx.coroutines.flow.Flow

interface DeviceSettingsDataSource {
    fun setSoundPlaybackStatus(enable: Boolean)
    fun getSoundPlaybackStatus(): Boolean
    fun getThemeIsDark(): Flow<Boolean>?
    suspend fun setThemeIsDark(enable: Boolean)
}