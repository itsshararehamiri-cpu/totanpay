package com.example.totanpay.data.repository.datasource.settings

interface ConnectionSettingsDataSource {
    fun getNii(): String
    fun hasConnectionSettings(): Boolean
    fun getIP(): String
    fun getPort(): String
    fun saveConnectionSettings(ip: String, port: String, nii: String)
    fun getTerminalConnectionType(): String
    fun getTerminalType(): String
    fun getPOS(): String
    fun getPosConditionCode(): String
}