package com.example.totanpay.data.repository.bill

import android.content.Context
import com.example.totanpay.data.repository.DeviceRepository
import com.example.totanpay.data.repository.datasource.ResponseData
import com.example.totanpay.data.repository.datasource.bill.BillRemoteDataSource
import com.example.totanpay.data.repository.datasource.log.LogLocalDataSource
import com.example.totanpay.data.repository.datasource.mask
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.example.totanpay.data.repository.datasource.settings.ConnectionSettingsDataSource
import com.example.totanpay.data.repository.datasource.settings.MerchantLocalDataSource
import com.example.totanpay.data.repository.datasource.transaction.api.toBillInquirySuccessResponse
import com.example.totanpay.data.repository.datasource.transaction.api.toBillInquiryUnSuccessResponse
import com.example.totanpay.data.repository.datasource.transaction.api.toBillSuccessResponse
import com.example.totanpay.data.repository.datasource.transaction.api.toBillUnSuccessResponse
import com.example.totanpay.data.repository.getAppVersion
import com.example.totanpay.data.repository.util.extractPanFromTrack2
import com.example.totanpay.data.util.isNetworkAvailable
import com.example.totanpay.util.convertToTimestamp
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BillRepositoryImpl @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val dataSource: BillRemoteDataSource,
    private val connectionSettingsDataSource: ConnectionSettingsDataSource,
    private val merchantLocalDataSource: MerchantLocalDataSource,
    private val deviceRepository: DeviceRepository,
    private val logLocalDataSource: LogLocalDataSource,
    private val context: Context
) : BillRepository {
    override suspend fun billPay(
        billID: String,
        payId: String,
        amount: String,
        track2: String,
        pinBlock: String, serviceDesc: String?
    ): ResponseData<ResponseTransaction> {
        return withContext(ioDispatcher) {
            val terminalId = merchantLocalDataSource.getTerminalId()
            val merchant = merchantLocalDataSource.getMerchant()
            val result = dataSource.billPay(
                isNetworkAvailable = {
                    isNetworkAvailable(context)
                },
                terminalId = terminalId,
                serial = deviceRepository.getSerial(),
                appVersion = context.getAppVersion(),
                nii = connectionSettingsDataSource.getNii(),
                terminalLanguage = merchantLocalDataSource.getTerminalLanguage(),
                terminalConnectionType = connectionSettingsDataSource.getTerminalConnectionType(),
                terminalType = connectionSettingsDataSource.getTerminalType(),
                merchantId = merchant.merchantId ?: "",
                billID = billID,
                payId = payId,
                amount = amount,
                pinBlock = pinBlock,
                track2 = track2,
                pan = extractPanFromTrack2(track2).mask(),
                currency = merchantLocalDataSource.getCurrency(),
                serviceDesc = serviceDesc,
                posConditionCode = connectionSettingsDataSource.getPosConditionCode(),
                pOS = connectionSettingsDataSource.getPOS(),
                logs =logLocalDataSource.getLogs()
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
                    result.data!!.toBillSuccessResponse(
                        merchant = merchant,
                        terminalId = merchantLocalDataSource.getTerminalId(),
                        track2 = track2,
                        amount = amount,
                        payId = payId,
                        billID = billID,
                        serviceDesc = serviceDesc ?: "",posCode=merchantLocalDataSource.getPosCode()
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
                    data = result.data?.toBillUnSuccessResponse(
                        merchant,
                        merchantLocalDataSource.getTerminalId(),posCode=merchantLocalDataSource.getPosCode()
                    ), error = result.error
                )
            }
        }

    }

    override suspend fun billInquiry(
        billID: String,
        payId: String
    ): ResponseData<ResponseTransaction> {
        return withContext(ioDispatcher) {
            val terminalId = merchantLocalDataSource.getTerminalId()
            val merchant = merchantLocalDataSource.getMerchant()
            val result = dataSource.billInquiry(
                isNetworkAvailable = {
                    isNetworkAvailable(context)
                },
                terminalId = terminalId,
                serial = deviceRepository.getSerial(),
                appVersion = context.getAppVersion(),
                nii = connectionSettingsDataSource.getNii(),
                terminalLanguage = merchantLocalDataSource.getTerminalLanguage(),
                terminalConnectionType = connectionSettingsDataSource.getTerminalConnectionType(),
                terminalType = connectionSettingsDataSource.getTerminalType(),
                merchantId = merchant.merchantId ?: "",
                billID = billID,
                payId = payId,
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
                    result.data!!.toBillInquirySuccessResponse(
                        merchant = merchant,
                        billID = billID,
                        payId = payId,
                        terminalId = terminalId,posCode=merchantLocalDataSource.getPosCode()
                    )
                )
            } else {
                if (!(result.data!!.dateTimeOfServer.isNullOrEmpty())) {
                    deviceRepository.setDateTime(
                        convertToTimestamp(result.data.dateTimeOfServer!!).toString()
                    )

                }
                ResponseData.Error(
                    data = result.data!!.toBillInquiryUnSuccessResponse(
                        merchant = merchant,
                        terminalId = terminalId,posCode=merchantLocalDataSource.getPosCode()
                    )
                )
            }

        }
    }
}