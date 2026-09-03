package com.example.totanpay.data.repository.datasource.configuration

import android.util.Log
import com.example.totanpay.data.ConnectionException
import com.example.totanpay.data.repository.datasource.ResponseData
import com.example.totanpay.data.repository.datasource.StanGenerator
import com.example.totanpay.data.repository.datasource.TotanPayException
import com.example.totanpay.data.repository.datasource.generateDateTimeInGMT
import com.example.totanpay.data.repository.datasource.getDateOfTransaction
import com.example.totanpay.data.repository.datasource.getTimeOfTransaction
import com.example.totanpay.data.repository.datasource.settings.ConnectionSettingsDataSource
import com.example.totanpay.data.repository.datasource.transaction.IMacGenerator
import com.example.totanpay.data.repository.datasource.transaction.InitTransaction
import com.example.totanpay.data.repository.datasource.transaction.connection.IConnection
import com.example.totanpay.data.repository.datasource.transaction.request.InitTransactionRequest
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.FailedTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.toInitTransactionResponse
import com.example.totanpay.data.repository.util.getExceptionFromResponseCode
import com.example.totanpay.data.repository.util.getResponseMessageFromResponseCode
import javax.inject.Inject

class TerminalInfoRemoteDataSourceImpl @Inject constructor(private val macGenerator: IMacGenerator,
                                                           private val connection: IConnection,
                                                           private val stanGenerator: StanGenerator,
                                                           connectionSettingsDataSource: ConnectionSettingsDataSource,
): TerminalInfoRemoteDataSource {
    init {
        if (connectionSettingsDataSource.hasConnectionSettings()) {
            connection.init(
                ip = connectionSettingsDataSource.getIP(),
                port = connectionSettingsDataSource.getPort().toInt(),
                nii = connectionSettingsDataSource.getNii()

            )
        }
    }

    override suspend fun init(
        isNetworkAvailable: () -> Boolean,
        terminalId: String,
        serial: String,
        appVersion: String,
        nii: String,
        terminalLanguage: String,
        acquiringInstitutionIdentificationCode: String,
        merchantId: String,
        posConditionCode: String,
        terminalConnectionType: String
    ): ResponseData<BaseTransactionResponse.InitTransactionResponse> {
        if (isNetworkAvailable()) {
            val transaction = InitTransaction(
                InitTransactionRequest(
                    terminalId = terminalId,
                    stan = stanGenerator.generate(),
                    serial = serial,
                    appVersion = appVersion,
                    nii = nii,
                    date = getDateOfTransaction(),
                    time = getTimeOfTransaction(),
                    dateTimeInGMT = generateDateTimeInGMT(),
                    terminalLanguage = terminalLanguage,
                    acquiringInstitutionIdentificationCode = acquiringInstitutionIdentificationCode,
                    merchantId = merchantId,
                    posConditionCode = posConditionCode,
                    terminalConnectionType = terminalConnectionType
                ), macGenerator,
                connection
            )
            val response = transaction.execute()
            return if (response is FailedTransactionResponse) {
                ResponseData.Error(
                    data = response.toInitTransactionResponse(
                        getResponseMessageFromResponseCode(
                            response.responseCode, response.responseMessage
                        )
                    ),
                    error = getExceptionFromResponseCode(
                        response.responseCode, response.responseMessage
                    )
                )
            }
            else {
                ResponseData.Success(data = response as BaseTransactionResponse.InitTransactionResponse)
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
}