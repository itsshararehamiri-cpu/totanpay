package com.example.totanpay.data.repository.balance

import android.content.Context
import com.example.totanpay.data.repository.DeviceRepository
import com.example.totanpay.data.repository.datasource.ResponseData
import com.example.totanpay.data.repository.datasource.balance.BalanceRemoteDataSource
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.example.totanpay.data.repository.datasource.settings.ConnectionSettingsDataSource
import com.example.totanpay.data.repository.datasource.settings.MerchantLocalDataSource
import com.example.totanpay.data.repository.datasource.transaction.api.toBalanceSuccessResponse
import com.example.totanpay.data.repository.datasource.transaction.api.toBalanceUnSuccessResponse
import com.example.totanpay.data.repository.getAppVersion
import com.example.totanpay.data.repository.settings.CurrentLanguageRepository
import com.example.totanpay.data.repository.util.extractPanFromTrack2
import com.example.totanpay.data.util.isNetworkAvailable
import com.example.totanpay.util.convertToTimestamp
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BalanceRepositoryImpl @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val dataSource: BalanceRemoteDataSource,
    private val connectionSettingsDataSource: ConnectionSettingsDataSource,
    private val merchantLocalDataSource: MerchantLocalDataSource,
    private val deviceRepository: DeviceRepository,
    private val languageRepository: CurrentLanguageRepository,
    private val context: Context
) : BalanceRepository {
    override suspend fun balance(
        track2: String,
        pinBlock: String
    ): ResponseData<ResponseTransaction> {
        return withContext(ioDispatcher) {
            val terminalId = merchantLocalDataSource.getTerminalId()
            val merchant = merchantLocalDataSource.getMerchant()
            val result = dataSource.balance(
                isNetworkAvailable = {
                    isNetworkAvailable(context)
                },
                pan = extractPanFromTrack2(track2),
                terminalId = terminalId,
                serial = deviceRepository.getSerial(),
                appVersion = context.getAppVersion(),
                nii = connectionSettingsDataSource.getNii(),
                terminalLanguage = merchantLocalDataSource.getTerminalLanguage(),
                terminalConnectionType = connectionSettingsDataSource.getTerminalConnectionType(),
                terminalType = connectionSettingsDataSource.getTerminalType(),
                track2 = track2,
                pinBlock = pinBlock,
                merchantId = merchant.merchantId ?: "",
                currency = merchantLocalDataSource.getCurrency(),
                pOS = connectionSettingsDataSource.getPOS(),
                posConditionCode = connectionSettingsDataSource.getPosConditionCode()
            )
            if (result is ResponseData.Success) {
                if (result.data != null) {
                    if (!(result.data!!.dateTimeOfServer.isNullOrEmpty())) {
                        deviceRepository.setDateTime(
                            convertToTimestamp(result.data.dateTimeOfServer!!).toString()
                        )

                    }
                }
                ResponseData.Success(
                    result.data!!.toBalanceSuccessResponse(
                        isFarsi = languageRepository.languageIsFarsi(),
                        track2 = track2,
                        terminalId = merchantLocalDataSource.getTerminalId(),
                        merchant = merchant, posCode = merchantLocalDataSource.getPosCode()
                    )
                )
            } else {
                if (result.data != null) {
                    if (!result.data!!.dateTimeOfServer.isNullOrEmpty())
                        deviceRepository.setDateTime(
                            convertToTimestamp(result.data.dateTimeOfServer!!).toString()
                        )

                }
                ResponseData.Error(
                    data = result.data?.toBalanceUnSuccessResponse(
                        merchant = merchant,
                        terminalId = merchantLocalDataSource.getTerminalId(),
                        track2 = track2,
                        posCode = merchantLocalDataSource.getPosCode()
                    ), error = result.error
                )
            }

        }
    }
}