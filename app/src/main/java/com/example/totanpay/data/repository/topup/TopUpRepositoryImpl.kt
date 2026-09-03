package com.example.totanpay.data.repository.topup

import android.content.Context
import com.example.totanpay.data.Operator
import com.example.totanpay.data.repository.DeviceRepository
import com.example.totanpay.data.repository.datasource.ResponseData
import com.example.totanpay.data.repository.datasource.log.LogLocalDataSource
import com.example.totanpay.data.repository.datasource.mask
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.example.totanpay.data.repository.datasource.settings.ConnectionSettingsDataSource
import com.example.totanpay.data.repository.datasource.settings.LocalTaxDataSource
import com.example.totanpay.data.repository.datasource.settings.MerchantLocalDataSource
import com.example.totanpay.data.repository.datasource.topup.TopUpRemoteDataSource
import com.example.totanpay.data.repository.datasource.transaction.api.toTopUpSuccessResponse
import com.example.totanpay.data.repository.datasource.transaction.api.toTopUpUnSuccessResponse
import com.example.totanpay.data.repository.getAppVersion
import com.example.totanpay.data.repository.settings.CurrentLanguageRepository
import com.example.totanpay.data.repository.util.extractPanFromTrack2
import com.example.totanpay.data.util.isNetworkAvailable
import com.example.totanpay.data.util.toEnglishNumber
import com.example.totanpay.util.convertToTimestamp
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TopUpRepositoryImpl @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val dataSource: TopUpRemoteDataSource,
    private val connectionSettingsDataSource: ConnectionSettingsDataSource,
    private val merchantLocalDataSource: MerchantLocalDataSource,
    private val deviceRepository: DeviceRepository,
    private val context: Context,
    private val localTaxDataSource: LocalTaxDataSource,
    private val logLocalDataSource: LogLocalDataSource,
    private val languageRepository: CurrentLanguageRepository
    ) : TopUpRepository {

    override suspend fun topUp(
        amount: String,
        mobile: String,
        operator: Operator,
        track2: String,
        pinBlock: String
    ): ResponseData<ResponseTransaction> {
        return withContext(ioDispatcher) {
            val terminalId = merchantLocalDataSource.getTerminalId()
            val merchant = merchantLocalDataSource.getMerchant()
            val result = dataSource.topUp(
                isNetworkAvailable = {
                    isNetworkAvailable(context)
                },
                amount = getAmountWithTax(amount.toEnglishNumber(), operator),
                mobile = mobile.toEnglishNumber(),
                productCode = operator.code.toString().toEnglishNumber(),
                pan = extractPanFromTrack2(track2).mask().toEnglishNumber(),
                terminalId = terminalId.toEnglishNumber(),
                serial = deviceRepository.getSerial().toEnglishNumber(),
                appVersion = context.getAppVersion().toEnglishNumber(),
                nii = connectionSettingsDataSource.getNii().toEnglishNumber(),
                terminalLanguage = merchantLocalDataSource.getTerminalLanguage().toEnglishNumber(),
                terminalConnectionType = connectionSettingsDataSource.getTerminalConnectionType()
                    .toEnglishNumber(),
                terminalType = connectionSettingsDataSource.getTerminalType().toEnglishNumber(),
                track2 = track2.toEnglishNumber(),
                pinBlock = pinBlock.toEnglishNumber(),
                merchantId = merchant.merchantId ?: "".toEnglishNumber(),
                currency = merchantLocalDataSource.getCurrency().toEnglishNumber(),
                POS = connectionSettingsDataSource.getPOS().toEnglishNumber(),
                posConditionCode = connectionSettingsDataSource.getPosConditionCode()
                    .toEnglishNumber(),
                logs =logLocalDataSource.getLogs(),
                requestDecryptData = { deviceRepository.requestDecryptData(data = it) }
            )
            if (result is ResponseData.Success) {
                logLocalDataSource.deleteAllLog()
                if (result.data != null) {
                    if (!(result.data!!.dateTimeOfServer.isNullOrEmpty())) {
                        deviceRepository.setDateTime(
                            convertToTimestamp(result.data.dateTimeOfServer!!).toString()
                        )
                    }
                }
                ResponseData.Success(
                    result.data!!.toTopUpSuccessResponse(isFarsi=languageRepository.languageIsFarsi(),
                        merchant = merchant,
                        terminalId = terminalId,
                        track2 = track2,
                        mobile = mobile,posCode=merchantLocalDataSource.getPosCode()
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
                    data = result.data?.toTopUpUnSuccessResponse(
                        merchant,
                        merchantLocalDataSource.getTerminalId(),posCode=merchantLocalDataSource.getPosCode()
                    ), error = result.error
                )
            }
        }
    }
    override fun getAmountWithTax(amount: String, operator: Operator): String {
        val tax=localTaxDataSource.getTax()
        var tempTax="0".toDouble()
        if(tax.isNotEmpty())
            tempTax="1".toDouble()+(tax.toDouble()/100)
        return if (operator.code == 11) (amount.toEnglishNumber().toLong() * tempTax).toLong().toString() else amount

    }

    override fun getTaxForCharge():String {
        return localTaxDataSource.getTax()
    }
}