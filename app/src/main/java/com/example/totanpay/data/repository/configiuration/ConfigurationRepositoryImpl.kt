package com.example.totanpay.data.repository.configiuration

import android.content.Context
import android.util.Log
import com.example.totanpay.data.dao.TransactionLogDao
import com.example.totanpay.data.repository.DeviceRepository
import com.example.totanpay.data.repository.datasource.settings.ConnectionSettingsDataSource
import com.example.totanpay.data.repository.datasource.MainDataSource
import com.example.totanpay.data.repository.datasource.settings.MerchantLocalDataSource
import com.example.totanpay.data.repository.datasource.ResponseData
import com.example.totanpay.data.repository.datasource.configuration.LogonRemoteDataSource
import com.example.totanpay.data.repository.datasource.configuration.LogonRemoteDataSourceImpl
import com.example.totanpay.data.repository.datasource.configuration.TerminalInfoRemoteDataSource
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.example.totanpay.data.repository.datasource.report.ReportLocalDataSource
import com.example.totanpay.data.repository.datasource.settings.MacEnableLocalDataSource
import com.example.totanpay.data.repository.datasource.transaction.request.Apportionment
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.toResponseTransaction
import com.example.totanpay.data.repository.getAppVersion
import com.example.totanpay.data.util.isNetworkAvailable
import com.example.totanpay.data.util.toFormattedDate
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.jpos.iso.ISOUtil
import javax.inject.Inject

class ConfigurationRepositoryImpl @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val logonRemoteDataSource: LogonRemoteDataSource,
    private val terminalRemoteDataSource: TerminalInfoRemoteDataSource,
    private val connectionSettingsDataSource: ConnectionSettingsDataSource,
    private val merchantLocalDataSource: MerchantLocalDataSource,
    private val deviceRepository: DeviceRepository,
    private val dataSource: MainDataSource,
    private val macEnableLocalDataSource: MacEnableLocalDataSource,
    private val reportLocalDataSource: ReportLocalDataSource,
    private val context: Context
) : ConfigurationRepository {

    override suspend fun logon(): ResponseData<ResponseTransaction> {
        return withContext(ioDispatcher) {
            val logonTransactionResponse = logonRemoteDataSource.logon(
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
                terminalConnectionType = connectionSettingsDataSource.getTerminalConnectionType(),
                posConditionCode = connectionSettingsDataSource.getPosConditionCode(), enableMac =macEnableLocalDataSource.getEnableMac(),
                context = context
            )
            if (logonTransactionResponse is ResponseData.Success) {
                val previousTerminalId=merchantLocalDataSource.getTerminalId()
                merchantLocalDataSource.storeTerminalId((logonTransactionResponse.data as BaseTransactionResponse.LogonTransactionResponse).terminalId.trim())
                if(previousTerminalId!= logonTransactionResponse.data.terminalId.trim()){
                    reportLocalDataSource.clearReports()
                }
                Log.d("TAG", "logon: data->${logonTransactionResponse.data.dataKey}")
                Log.d("TAG", "logon: pin->${logonTransactionResponse.data.pinKey}")
                Log.d("TAG", "logon: mac->${logonTransactionResponse.data.macKey}")

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
                if (logonTransactionResponse.data!=null) {
                    if(!logonTransactionResponse.data!!.dateTimeOfServer.isNullOrEmpty())
                    deviceRepository.setDateTime(
                        logonTransactionResponse.data.dateTimeOfServer!!.toFormattedDate()
                    )
                }
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

    override suspend fun enableMac(isEnable: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun isEnableMac(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun init(): ResponseData<BaseTransactionResponse.InitTransactionResponse> {
        return withContext(ioDispatcher) {
            val terminalId = merchantLocalDataSource.getTerminalId()
            val initTransactionResponse = terminalRemoteDataSource.init(
                isNetworkAvailable = {
                    isNetworkAvailable(context)
                },
                terminalId = terminalId,
                serial = deviceRepository.getSerial(),
                appVersion = context.getAppVersion(),
                nii = connectionSettingsDataSource.getNii(),
                terminalLanguage = merchantLocalDataSource.getTerminalLanguage(),
                merchantId = "",
                acquiringInstitutionIdentificationCode = "",
                posConditionCode = connectionSettingsDataSource.getPosConditionCode(),
                terminalConnectionType = connectionSettingsDataSource.getTerminalConnectionType()
            )
            merchantLocalDataSource.storePosCode("")
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
                    val apportionments: MutableList<Apportionment> = mutableListOf()
                    for (i in 0..initTransactionResponse.data.accountMerchants!!.size - 1) {
                        apportionments.add(
                            Apportionment(
                                IBAN = initTransactionResponse.data.accountMerchants.get(i).number
                                    ?: "",
                                amount =
                                    if (i == 0)
                                        "100" else "0",
                                bankName = initTransactionResponse.data.accountMerchants.get(i).farsiBankName
                                    ?: "", englishBankName =initTransactionResponse.data.accountMerchants.get(i).englishBankName
                                    ?: ""
                            )
                        )
                    }
                    merchantLocalDataSource.storeApportionments(apportionments)
                }
                merchantLocalDataSource.storeMerchant(
                    merchantId = initTransactionResponse.data.merchantId.trim(),
                    merchantPhone = initTransactionResponse.data.merchantPhone?.trim() ?: "",
                    merchantName = initTransactionResponse.data.merchantName?.trim() ?: "",
                    englishMerchantName = initTransactionResponse.data.englishMerchantName?.trim() ?: ""

                )
            }
            else if(initTransactionResponse is ResponseData.Error){
                if(initTransactionResponse.data!=null)
                {
                    if (!initTransactionResponse.data.dateTimeOfServer.isNullOrEmpty()) {
                        deviceRepository.setDateTime(
                            initTransactionResponse.data.dateTimeOfServer!!.toFormattedDate()
                        )
                    }
                }
            }
            initTransactionResponse

        }
    }

}