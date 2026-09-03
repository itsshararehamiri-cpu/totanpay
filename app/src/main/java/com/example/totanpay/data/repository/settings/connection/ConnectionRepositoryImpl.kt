package com.example.totanpay.data.repository.settings.connection

import com.example.totanpay.data.repository.datasource.MainDataSource
import com.example.totanpay.data.repository.datasource.settings.ConnectionSettingsDataSource
import javax.inject.Inject

class ConnectionRepositoryImpl @Inject constructor(
    private val connectionSettingsDataSource: ConnectionSettingsDataSource,
    private val mainDataSource: MainDataSource
) : ConnectionRepository {
    override fun confirmConnectionSettings(ip: String, port: String, nii: String) {
        connectionSettingsDataSource.saveConnectionSettings(ip, port, nii)
       mainDataSource.loadSettings()
    }


    override fun getIP(): String {
        return connectionSettingsDataSource.getIP()
    }

    override fun getNii(): String {
        return connectionSettingsDataSource.getNii()
    }

    override fun getPort(): String {
        return connectionSettingsDataSource.getPort()

    }
}