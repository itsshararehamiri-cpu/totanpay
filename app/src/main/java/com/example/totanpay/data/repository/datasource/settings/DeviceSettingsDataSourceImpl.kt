package com.example.totanpay.data.repository.datasource.settings


import com.example.totanpay.data.dao.LightModeDao
import com.example.totanpay.data.entity.LightModeEntity
import com.example.totanpay.data.repository.datasource.TotanPayPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class DeviceSettingsDataSourceImpl @Inject constructor(private val totanPayPreference: TotanPayPreference, private val lightModeDao: LightModeDao) : DeviceSettingsDataSource {
    override fun getSoundPlaybackStatus(): Boolean {
        return totanPayPreference.getPlaybackStatusSound()
    }
    override fun setSoundPlaybackStatus(enable: Boolean) {
        totanPayPreference.setPlaybackStatusSound(enable)
    }
    override fun getThemeIsDark(): Flow<Boolean> {
        return lightModeDao.getFlow().map {
            it?.mode?:false
        }
    }
    override suspend fun setThemeIsDark(enable: Boolean) {
        if (lightModeDao.get() == null)
            lightModeDao.insert(LightModeEntity(1, enable))
        else lightModeDao.update(LightModeEntity(1, enable))
    }
}