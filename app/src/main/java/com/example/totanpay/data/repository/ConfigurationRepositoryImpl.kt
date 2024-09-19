package com.example.totanpay.data.repository

import android.content.Context
import com.example.totanpay.data.repository.datasource.ConnectionSettingsDataSource
import com.example.totanpay.data.repository.datasource.MainDataSource
import com.example.totanpay.data.repository.datasource.MerchantLocalDataSource
import com.example.totanpay.data.repository.datasource.ResponseData
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.toResponseTransaction
import com.example.totanpay.data.util.isNetworkAvailable
import com.example.totanpay.data.util.toFormattedDate
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.jpos.iso.ISOUtil
import javax.inject.Inject


class ConfigurationRepositoryImpl @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val dataSource: MainDataSource,
    private val connectionSettingsDataSource: ConnectionSettingsDataSource,
    private val merchantLocalDataSource: MerchantLocalDataSource,
    private val deviceRepository: DeviceRepository,
    private val context: Context
) : ConfigurationRepository {
    override suspend fun logon(): ResponseData<ResponseTransaction> {
        return withContext(ioDispatcher) {
            val logonTransactionResponse = dataSource.logon(
                isNetworkAvailable = {
                    isNetworkAvailable(context)
                },
                isLoadSettings = {
                    connectionSettingsDataSource.hasConnectionSettings()
                },
                serial = deviceRepository.getSerial(),
                appVersion = context.getAppVersion(),
                nii = connectionSettingsDataSource.getNii(),
                terminalLanguage = merchantLocalDataSource.getTerminalLanguage(),
                terminalConnectionType =connectionSettingsDataSource. getTerminalConnectionType(),
                posConditionCode = connectionSettingsDataSource. getPosConditionCode())
            if (logonTransactionResponse is ResponseData.Success) {
                merchantLocalDataSource.storeTerminalId((logonTransactionResponse.data as BaseTransactionResponse.LogonTransactionResponse).terminalId.trim())
                deviceRepository.writeDataKey(ISOUtil.hexString(logonTransactionResponse.data.dataKey))
                deviceRepository.writePinKey(ISOUtil.hexString(logonTransactionResponse.data.pinKey))
                deviceRepository.writeMacKey(ISOUtil.hexString(logonTransactionResponse.data.macKey))
                if (!logonTransactionResponse.data.dateTimeOfServer.isNullOrEmpty()) {
                    deviceRepository.setDateTime(
                        logonTransactionResponse.data.dateTimeOfServer.toFormattedDate()
                    )
                }
                ResponseData.Success(logonTransactionResponse.data.toResponseTransaction())
            } else {
                ResponseData.Error(error = logonTransactionResponse.error)
            }
        }
    }

    override suspend fun configuration(): ResponseData<BaseTransactionResponse.InitTransactionResponse> {
        return withContext(ioDispatcher) {
            dataSource.loadSettings()
            val logonResult = logon()
            if (logonResult is ResponseData.Success) {
                val initResult = init()
                if (initResult is ResponseData.Success) {
                    initResult
                } else {
                    ResponseData.Error(initResult.error)
                }
            } else {
                ResponseData.Error(logonResult.error)
            }

        }
    }

    override suspend fun init(): ResponseData<BaseTransactionResponse.InitTransactionResponse> {
        return withContext(ioDispatcher) {
            val terminalId = merchantLocalDataSource.getTerminalId()
            val initTransactionResponse = dataSource.init(
                isNetworkAvailable = {
                    isNetworkAvailable(context)
                },
                terminalId = terminalId,
                serial = deviceRepository.getSerial(),
                appVersion = context.getAppVersion(),
                nii = connectionSettingsDataSource.getNii(),
                terminalLanguage =merchantLocalDataSource. getTerminalLanguage(),
                merchantId = "",
                acquiringInstitutionIdentificationCode = "",
                posConditionCode = connectionSettingsDataSource. getPosConditionCode(),
                terminalConnectionType =connectionSettingsDataSource.  getTerminalConnectionType()
            )
            if (initTransactionResponse is ResponseData.Success) {
                if (initTransactionResponse.data != null) {
                    if (!(initTransactionResponse.data.dateTimeOfServer.isNullOrEmpty())) {
//                        deviceRepository.setDateTime(
//                            initTransactionResponse.data.dateTimeOfServer
//                        )

                    }
                }
                if (!(initTransactionResponse.data as BaseTransactionResponse.InitTransactionResponse).accountMerchants.isNullOrEmpty()) {
                    merchantLocalDataSource.storeAllAccountMerchants(
                        initTransactionResponse.data.accountMerchants!!
                    )

                }
                merchantLocalDataSource.storeMerchant(
                    initTransactionResponse.data.merchantId.trim(),
                    initTransactionResponse.data.merchantPhone?.trim() ?: "",
                    initTransactionResponse.data.merchantName?.trim() ?: ""
                )
            }
            initTransactionResponse

        }
    }
}