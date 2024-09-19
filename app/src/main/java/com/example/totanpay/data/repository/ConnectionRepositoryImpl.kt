package com.example.totanpay.data.repository

import com.example.totanpay.data.repository.datasource.ConnectionSettingsDataSource
import javax.inject.Inject


class ConnectionRepositoryImpl @Inject constructor(
    private val connectionSettingsDataSource: ConnectionSettingsDataSource

) : ConnectionRepository {
    override fun confirmConnectionSettings(ip: String, port: String, nii: String) {
        connectionSettingsDataSource.saveConnectionSettings(ip, port, nii)
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