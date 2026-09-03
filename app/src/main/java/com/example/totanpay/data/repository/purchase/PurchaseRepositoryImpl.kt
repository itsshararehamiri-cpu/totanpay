package com.example.totanpay.data.repository.purchase

import android.content.Context
import com.example.totanpay.data.repository.DeviceRepository
import com.example.totanpay.data.repository.datasource.ResponseData
import com.example.totanpay.data.repository.datasource.log.LogLocalDataSource
import com.example.totanpay.data.repository.datasource.mask
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.example.totanpay.data.repository.datasource.purchase.PurchaseRemoteDataSource
import com.example.totanpay.data.repository.datasource.settings.ConnectionSettingsDataSource
import com.example.totanpay.data.repository.datasource.settings.MerchantLocalDataSource
import com.example.totanpay.data.repository.datasource.transaction.api.toPurchaseSuccessResponse
import com.example.totanpay.data.repository.datasource.transaction.api.toPurchaseUnSuccessResponse
import com.example.totanpay.data.repository.datasource.transaction.request.Apportionment
import com.example.totanpay.data.repository.getAppVersion
import com.example.totanpay.data.repository.settings.CurrentLanguageRepository
import com.example.totanpay.data.repository.util.extractPanFromTrack2
import com.example.totanpay.data.util.isNetworkAvailable
import com.example.totanpay.data.util.toEnglishNumber
import com.example.totanpay.util.convertToTimestamp
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PurchaseRepositoryImpl @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val dataSource: PurchaseRemoteDataSource,
    private val connectionSettingsDataSource: ConnectionSettingsDataSource,
    private val merchantLocalDataSource: MerchantLocalDataSource,
    private val deviceRepository: DeviceRepository,
    private val logLocalDataSource: LogLocalDataSource,
    private val languageRepository: CurrentLanguageRepository,
    private val context: Context
) : PurchaseRepository {
    override suspend fun purchase(
        amount: String,
        track2: String,
        pinBlock: String,
        purchaseId: String?
    ): ResponseData<ResponseTransaction> {
        return withContext(ioDispatcher) {
            val terminalId = merchantLocalDataSource.getTerminalId()
            val merchant = merchantLocalDataSource.getMerchant()
            val result = dataSource.
            purchase(
                isNetworkAvailable = {
                    isNetworkAvailable(context)
                },
                amount = amount,
                pan = extractPanFromTrack2(track2).mask(),
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
                POS = connectionSettingsDataSource.getPOS(),
                purchaseId = purchaseId,
                posConditionCode = connectionSettingsDataSource.getPosConditionCode(),
                apportionments = getApportionments(amount),
                logs = logLocalDataSource.getLogs()
            )
            if (result is ResponseData.Success) {
                logLocalDataSource.deleteAllLog()
                if (result.data != null) {
                    if (!(result.data!!.dateTimeOfServer.isNullOrEmpty())) {
                        deviceRepository.setDateTime(
                            convertToTimestamp(result.data.dateTimeOfServer!!).toString()
                        )

                    }
                    if (!result.data.posCode.isNullOrEmpty())
                        merchantLocalDataSource.storePosCode(result.data.posCode)
                }
                ResponseData.Success(
                    result.data!!.toPurchaseSuccessResponse(isFarsi=languageRepository.languageIsFarsi(),
                        merchant = merchant,
                        track2 = track2,
                        terminalId = terminalId
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
                    data = result.data?.toPurchaseUnSuccessResponse(
                        purchaseId,
                        merchant,
                        merchantLocalDataSource.getTerminalId(),posCode=merchantLocalDataSource.getPosCode()
                    ), error = result.error
                )
            }
        }
    }

    private fun getApportionments(amount: String): List<Apportionment>? {
        val apportionment: List<Apportionment> = merchantLocalDataSource.getApportionments()
            ?: return null
        var sum=0L
       for(i in 0 ..apportionment.size-2){
           apportionment.get(i).amount = (((if ( apportionment.get(i).amount.isNotEmpty())  apportionment.get(i).amount.toEnglishNumber()
               .toLong() else 0L) * amount.toEnglishNumber().toLong()) / 100).toString()
           sum+=apportionment.get(i).amount.toLong()
       }
        apportionment.get(apportionment.size-1).amount=(amount.toLong()-sum).toString()
        return apportionment
    }

}