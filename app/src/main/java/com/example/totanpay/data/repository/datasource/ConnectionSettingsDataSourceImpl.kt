package com.example.totanpay.data.repository.datasource

import javax.inject.Inject


class ConnectionSettingsDataSourceImpl @Inject constructor(private val totanPayPreference:TotanPayPreference
) : ConnectionSettingsDataSource {
    override fun getNii(): String {
          return totanPayPreference.getNii()
    }
    override fun hasConnectionSettings(): Boolean {
        return totanPayPreference.getNii().isNotEmpty() && totanPayPreference.getIP()
            .isNotEmpty() &&
                totanPayPreference.getPort().isNotEmpty()
    }
    override fun getIP(): String {
          return totanPayPreference.getIP()

    }
    override fun getPort(): String {
        return totanPayPreference.getPort()
    }
    override fun saveConnectionSettings(ip: String, port: String, nii: String) {
        totanPayPreference.storeConnectionSettings(ip, port, nii)
    }

    override  fun getTerminalConnectionType(): String {
        return "2"
    }

    override fun getTerminalType(): String {
        return "2"
    }

    override   fun getPosConditionCode(): String {
        return "00"
    }


    override  fun getPOS(): String = "021"
}