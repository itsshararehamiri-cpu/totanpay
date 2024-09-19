package com.example.totanpay.data.repository



import com.example.totanpay.data.repository.datasource.DeviceSettingsDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject


class DeviceSettingsRepositoryImpl @Inject constructor(
    private val deviceSettingsDataSource: DeviceSettingsDataSource, private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) : DeviceSettingsRepository {
    override fun setPlaybackStatusSound(enable: Boolean) {
       deviceSettingsDataSource.setSoundPlaybackStatus(enable)
    }
    override fun getPlaybackStatusSound(): Boolean {
       return deviceSettingsDataSource.getSoundPlaybackStatus()
    }
    override fun getThemeIsDark(): Flow<Boolean>? {
      return deviceSettingsDataSource.getThemeIsDark()

    }
    override suspend fun setThemeIsDark(enable: Boolean) {
       withContext(ioDispatcher){
           deviceSettingsDataSource.setThemeIsDark(enable)
       }
    }
}