package com.example.totanpay.data.repository.voucher

import android.content.Context
import com.example.totanpay.data.Operator
import com.example.totanpay.data.repository.DeviceRepository
import com.example.totanpay.data.repository.datasource.ResponseData
import com.example.totanpay.data.repository.datasource.log.LogLocalDataSource
import com.example.totanpay.data.repository.datasource.mask
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.example.totanpay.data.repository.datasource.settings.ConnectionSettingsDataSource
import com.example.totanpay.data.repository.datasource.settings.MerchantLocalDataSource
import com.example.totanpay.data.repository.datasource.transaction.api.toVoucherSuccessResponse
import com.example.totanpay.data.repository.datasource.transaction.api.toVoucherUnSuccessResponse
import com.example.totanpay.data.repository.datasource.voucher.VoucherRemoteDataSource
import com.example.totanpay.data.repository.getAppVersion
import com.example.totanpay.data.repository.settings.CurrentLanguageRepository
import com.example.totanpay.data.repository.util.extractPanFromTrack2
import com.example.totanpay.data.util.getProductCode
import com.example.totanpay.data.util.isNetworkAvailable
import com.example.totanpay.util.convertToTimestamp
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class VoucherRepositoryImpl @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val dataSource: VoucherRemoteDataSource,
    private val connectionSettingsDataSource: ConnectionSettingsDataSource,
    private val merchantLocalDataSource: MerchantLocalDataSource,
    private val deviceRepository: DeviceRepository,
    private val logLocalDataSource: LogLocalDataSource,
    private val languageRepository: CurrentLanguageRepository,

    private val context: Context
) : VoucherRepository {
    override suspend fun voucher(
        amount: String,
        pan: String,
        track2: String,
        pinBlock: String,
        operator: Operator
    ): ResponseData<ResponseTransaction> {
        return withContext(ioDispatcher) {
            val terminalId = merchantLocalDataSource.getTerminalId()
            val merchant = merchantLocalDataSource.getMerchant()
            val result = dataSource.voucher(
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
                productCode = getProductCode(amount = amount, operator.code.toString()),
                POS = connectionSettingsDataSource.getPOS(),
                posConditionCode = connectionSettingsDataSource.getPosConditionCode(),
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
                    result.data!!.toVoucherSuccessResponse(isFarsi = languageRepository.languageIsFarsi(),
                        merchant = merchant, terminalId = merchantLocalDataSource.getTerminalId(),
                        track2 = track2, operator = operator,posCode=merchantLocalDataSource.getPosCode()
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
                    data = result.data?.toVoucherUnSuccessResponse(
                        merchant,
                        merchantLocalDataSource.getTerminalId(),posCode=merchantLocalDataSource.getPosCode()
                    ), error = result.error
                )
            }
        }

    }
}