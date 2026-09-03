package com.example.totanpay.data.repository.datasource.balance

import com.example.totanpay.data.ConnectionException
import com.example.totanpay.data.repository.datasource.ResponseData
import com.example.totanpay.data.repository.datasource.StanGenerator
import com.example.totanpay.data.repository.datasource.TotanPayException
import com.example.totanpay.data.repository.datasource.getDateOfTransaction
import com.example.totanpay.data.repository.datasource.getTimeOfTransaction
import com.example.totanpay.data.repository.datasource.settings.ConnectionSettingsDataSource
import com.example.totanpay.data.repository.datasource.settlereverse.SettleReverseRemoteDataSource
import com.example.totanpay.data.repository.datasource.transaction.BalanceTransaction
import com.example.totanpay.data.repository.datasource.transaction.IMacGenerator
import com.example.totanpay.data.repository.datasource.transaction.connection.IConnection
import com.example.totanpay.data.repository.datasource.transaction.request.BalanceTransactionRequest
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.FailedTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.toBalanceTransactionResponse
import com.example.totanpay.data.repository.util.getExceptionFromResponseCode
import com.example.totanpay.data.repository.util.getResponseMessageFromResponseCode
import javax.inject.Inject

class BalanceRemoteDataSourceImpl @Inject constructor(    private val macGenerator: IMacGenerator,
                                                          private val connection: IConnection,
                                                          private val stanGenerator: StanGenerator,
                                                          connectionSettingsDataSource:
                                                          ConnectionSettingsDataSource,
                                                          private val settleReverseRemoteDataSource: SettleReverseRemoteDataSource
): BalanceRemoteDataSource{
    init {
        if (connectionSettingsDataSource.hasConnectionSettings()) {
            connection.init(
                ip = connectionSettingsDataSource.getIP(),
                port = connectionSettingsDataSource.getPort().toInt(),
                nii = connectionSettingsDataSource.getNii()

            )
        }
    }
    override suspend fun balance(
        isNetworkAvailable: () -> Boolean,
        pan: String,
        terminalId: String,
        terminalLanguage: String,
        terminalConnectionType: String,
        terminalType: String,
        track2: String,
        pinBlock: String,
        merchantId: String,
        currency: String,
        pOS: String,
        serial: String,
        appVersion: String,
        nii: String,
        posConditionCode: String
    ): ResponseData<BaseTransactionResponse.BalanceTransactionResponse> {
        if (isNetworkAvailable()) {
            val transaction = BalanceTransaction(
                BalanceTransactionRequest(
                    pan = pan,
                    terminalId = terminalId,
                    terminalConnectionType = terminalConnectionType,
                    terminalType = terminalType,
                    terminalLanguage = terminalLanguage,
                    track2 = track2,
                    pinBlock = pinBlock,
                    merchantId = merchantId,
                    POS = pOS,
                    currency = currency,
                    stan =  stanGenerator.generate(),
                    serial = serial,
                    appVersion = appVersion,
                    nii = nii,
                    date = getDateOfTransaction(),
                    time = getTimeOfTransaction(),
                    posConditionCode = posConditionCode
                ), macGenerator,   sendTransactionInQueue = {
                    settleReverseRemoteDataSource.sendTransactionInQueue(
                        terminalLanguage = terminalLanguage,
                        terminalConnectionType = terminalConnectionType,
                        terminalType = terminalType
                    )
                },connection
            )
            val response = transaction.execute()
            return if (response is FailedTransactionResponse) ResponseData.Error(
                data = response.toBalanceTransactionResponse(
                    getResponseMessageFromResponseCode(
                        response.responseCode, response.responseMessage
                    )
                ),
                error = getExceptionFromResponseCode(
                    response.responseCode, response.responseMessage
                )
            )
            else {
                ResponseData.Success(data = response as BaseTransactionResponse.BalanceTransactionResponse)
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