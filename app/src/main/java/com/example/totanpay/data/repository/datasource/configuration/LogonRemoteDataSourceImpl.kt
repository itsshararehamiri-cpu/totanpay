package com.example.totanpay.data.repository.datasource.configuration

import android.util.Log
import com.example.totanpay.R
import com.example.totanpay.data.ConnectionException
import com.example.totanpay.data.repository.datasource.ResponseData
import com.example.totanpay.data.repository.datasource.StanGenerator
import com.example.totanpay.data.repository.datasource.TotanPayException
import com.example.totanpay.data.repository.datasource.getDateOfTransaction
import com.example.totanpay.data.repository.datasource.getTimeOfTransaction
import com.example.totanpay.data.repository.datasource.settings.ConnectionSettingsDataSource
import com.example.totanpay.data.repository.datasource.transaction.IMacGenerator
import com.example.totanpay.data.repository.datasource.transaction.LogonTransaction
import com.example.totanpay.data.repository.datasource.transaction.connection.IConnection
import com.example.totanpay.data.repository.datasource.transaction.request.LogonTransactionRequest
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.FailedTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.toLogonTransactionResponse
import com.example.totanpay.data.repository.util.getExceptionFromResponseCode
import com.example.totanpay.data.repository.util.getResponseMessageFromResponseCode
import com.example.totanpay.data.util.toEnglishNumber
import org.jpos.transaction.Context
import javax.inject.Inject

class LogonRemoteDataSourceImpl @Inject constructor( private val macGenerator: IMacGenerator,
                                                     private val connection: IConnection,
                                                     private val stanGenerator: StanGenerator ,
                                                     connectionSettingsDataSource: ConnectionSettingsDataSource,
): LogonRemoteDataSource {
    init {
        if (connectionSettingsDataSource.hasConnectionSettings()) {
            connection.init(
                ip = connectionSettingsDataSource.getIP(),
                port = connectionSettingsDataSource.getPort().toInt(),
                nii = connectionSettingsDataSource.getNii()

            )
        }
    }
    override suspend fun logon(context: android.content.Context,
                               isNetworkAvailable: () -> Boolean,
                               isLoadSettings: () -> Boolean,
                               serial: String,
                               appVersion: String,
                               nii: String,
                               terminalLanguage: String,
                               terminalConnectionType: String,
                               posConditionCode: String, enableMac: Boolean): ResponseData<BaseTransactionResponse.LogonTransactionResponse> {
        if (isNetworkAvailable()) {
            if (isLoadSettings()) {
                val transaction = LogonTransaction(
                    LogonTransactionRequest(
                        stan = stanGenerator.generate().toString().toEnglishNumber().toInt(),
                        serial = serial.toEnglishNumber(),
                        appVersion = appVersion,
                        nii = nii.toEnglishNumber(),
                        date = getDateOfTransaction().toEnglishNumber(),
                        time = getTimeOfTransaction().toEnglishNumber(),
                        terminalLanguage = terminalLanguage.toEnglishNumber(),
                        terminalConnectionType = terminalConnectionType.toEnglishNumber(),
                        posConditionCode = posConditionCode.toEnglishNumber(), enableMac = enableMac
                    ), macGenerator, connection
                )
                val response = transaction.execute()
                return if (response is FailedTransactionResponse) {
                    ResponseData.Error(
                        data = response.toLogonTransactionResponse(
                            getResponseMessageFromResponseCode(
                                response.responseCode, response.responseMessage
                            )
                        ),
                        error = getExceptionFromResponseCode(
                            response.responseCode, response.responseMessage
                        )
                    )
                } else {
                    ResponseData.Success(data = response as BaseTransactionResponse.LogonTransactionResponse)
                }
            } else {
                return ResponseData.Error(
                    error = getLoadSettingsException(context)
                )
            }
        } else {
            return ResponseData.Error(
                error = TotanPayException(
                    messageError = ConnectionException.message,
                    type = TotanPayException.Type.DISCONNECT
                )
            )
        }
    }
    private fun getLoadSettingsException(context: android.content.Context,
    ): TotanPayException {
        return TotanPayException(
            messageError = R.string.there_are_no_connection_settings_please_go_to_connection_settings_section_and_configure_settings,
            TotanPayException.Type.LOAD_SETTINGS
        )

    }
}