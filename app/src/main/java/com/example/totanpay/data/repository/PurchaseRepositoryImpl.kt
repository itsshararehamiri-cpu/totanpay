package com.example.totanpay.data.repository

import android.content.Context
import com.example.totanpay.data.repository.datasource.ConnectionSettingsDataSource
import com.example.totanpay.data.repository.datasource.MainDataSource
import com.example.totanpay.data.repository.datasource.MerchantLocalDataSource
import com.example.totanpay.data.repository.datasource.ResponseData
import com.example.totanpay.data.repository.datasource.mask
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.example.totanpay.data.repository.datasource.transaction.api.toPurchaseSuccessResponse
import com.example.totanpay.data.repository.datasource.transaction.api.toPurchaseUnSuccessResponse
import com.example.totanpay.data.repository.datasource.transaction.request.Apportionment
import com.example.totanpay.data.repository.util.extractPanFromTrack2
import com.example.totanpay.data.util.isNetworkAvailable
import com.example.totanpay.data.util.toEnglishNumber
import com.example.totanpay.util.convertToTimestamp
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PurchaseRepositoryImpl @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val dataSource: MainDataSource,
    private val connectionSettingsDataSource: ConnectionSettingsDataSource,
    private val merchantLocalDataSource: MerchantLocalDataSource,
    private val deviceRepository: DeviceRepository,
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
            val result = dataSource.purchase(
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
                apportionments = getApportionments(amount)
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
                    result.data!!.toPurchaseSuccessResponse(
                        merchant = merchant,
                        track2 = track2,
                        terminalId = terminalId
                    )
                )
            } else {
                ResponseData.Error(
                    data = result.data?.toPurchaseUnSuccessResponse(
                        purchaseId,
                        merchant,
                        merchantLocalDataSource.getTerminalId()
                    ), error = result.error
                )
            }
        }
    }

    private fun getApportionments(amount: String): List<Apportionment>? {
        val apportionment: List<Apportionment> = merchantLocalDataSource.getApportionments()
            ?: return null
        apportionment.forEach {
            it.amount = (((if(it.amount.isNotEmpty())it.amount.toEnglishNumber().toLong() else 0L) * amount.toEnglishNumber().toLong()) / 100).toString()
        }
        return apportionment
    }
}