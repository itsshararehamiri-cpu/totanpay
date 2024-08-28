package com.example.totanpay.data.repository.datasource

import android.device.sdk.BuildConfig
import android.util.Log
import com.example.totanpay.data.dao.TransactionLogDao
import com.example.totanpay.data.dao.TransactionQueueDao
import com.example.totanpay.data.entity.TransactionLogEntity
import com.example.totanpay.data.entity.TransactionQueueEntity
import com.example.totanpay.data.repository.datasource.model.Merchant
import com.example.totanpay.data.repository.datasource.transaction.BalanceTransaction
import com.example.totanpay.data.repository.datasource.transaction.BillInqueryTransaction
import com.example.totanpay.data.repository.datasource.transaction.BillPayTransaction
import com.example.totanpay.data.repository.datasource.transaction.GetKeyTransaction
import com.example.totanpay.data.repository.datasource.transaction.IMacGenerator
import com.example.totanpay.data.repository.datasource.transaction.InitTransaction
import com.example.totanpay.data.repository.datasource.transaction.LogonTransaction
import com.example.totanpay.data.repository.datasource.transaction.PurchaseTransaction
import com.example.totanpay.data.repository.datasource.transaction.SettlementReverseTransaction
import com.example.totanpay.data.repository.datasource.transaction.TopupTransaction
import com.example.totanpay.data.repository.datasource.transaction.TransactionType
import com.example.totanpay.data.repository.datasource.transaction.VoucherTransaction
import com.example.totanpay.data.repository.datasource.transaction.connection.IConnection
import com.example.totanpay.data.repository.datasource.transaction.request.BalanceTransactionRequest
import com.example.totanpay.data.repository.datasource.transaction.request.BillInqueryTransactionRequest
import com.example.totanpay.data.repository.datasource.transaction.request.BillPayTransactionRequest
import com.example.totanpay.data.repository.datasource.transaction.request.InitTransactionRequest
import com.example.totanpay.data.repository.datasource.transaction.request.LogonTransactionRequest
import com.example.totanpay.data.repository.datasource.transaction.request.PurchaseTransactionRequest
import com.example.totanpay.data.repository.datasource.transaction.request.SettlementReverseTransactionRequest
import com.example.totanpay.data.repository.datasource.transaction.request.TopupTransactionRequest
import com.example.totanpay.data.repository.datasource.transaction.request.VoucherTransactionRequest
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.FaildTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.toBalanceTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.toBillPayTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.toPurchaseTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.toTopupTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.toVoucherTransactionResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class MainDataSourceImpl @Inject constructor(
    private val macGenerator: IMacGenerator,
    private val iConnection: IConnection,
    private val totanPayPreference: TotanPayPreference,
    private val transactionLogDao: TransactionLogDao,
    private val transactionQueueDao: TransactionQueueDao) : MainDataSource {
    override suspend fun getKeyTransaction(
        serial: String,
        appVersion: String,
        nii: String, compressedPublicKey: String, hashCode: String
    ) {

        val transaction = GetKeyTransaction(compressedPublicKey, hashCode,
            dateTimeInGMT = g(),
            LogonTransactionRequest(
                stan = generateStan(),
                serial = serial,
                appVersion = appVersion,
                nii = nii,
                date = getDateOfTransaction(),
                time = getTimeOfTransaction()
            ),
            macGenerator,
            iConnection,
            saveTransactionLog = {},
            saveReverseData = {}, updateTransaction = {date, time,pan,cardIssuer,responseCode,amount->
                updateTransaction(date,time,pan,cardIssuer,responseCode,amount,null,null,null,null,null,null)

            })
        safeApiCall(isConnected = { true }) {
            transaction.execute() as BaseTransactionResponse.LogonTransactionResponse
        }
    }

    override suspend fun logon(
        serial: String,
        appVersion: String,
        nii: String
    ): ResponseData<BaseTransactionResponse.LogonTransactionResponse> {
        val transaction = LogonTransaction(LogonTransactionRequest(
            stan = generateStan(),
            serial = serial,
            appVersion = appVersion,
            nii = nii,
            date = getDateOfTransaction(),
            time = getTimeOfTransaction()
        ),
            macGenerator,
            iConnection,
            saveTransactionLog = {},
            saveReverseData = {})
        return safeApiCall(isConnected = { true }) {
            transaction.execute() as BaseTransactionResponse.LogonTransactionResponse
        }
    }

    override suspend fun init(
        terminalId: String,
        serial: String,
        appVersion: String,
        nii: String
    ): ResponseData<BaseTransactionResponse.InitTransactionResponse> {
        val transaction = InitTransaction(InitTransactionRequest(
            terminalId = terminalId,
            stan = generateStan(),
            serial = serial,
            appVersion = appVersion,
            nii = nii,
            date = getDateOfTransaction(),
            time = getTimeOfTransaction()
        ),
            macGenerator,
            iConnection,
            saveTransactionLog = {},
            saveReverseData = {})
        return safeApiCall(isConnected = { true }) {
            transaction.execute() as BaseTransactionResponse.InitTransactionResponse
        }

    }

    /*
    val pan:String,stan: Int, val terminalId:String,
                                     val terminalLanguage:String,
                                     val terminalConnectionType:String,
                                     val terminalType:String,
                                     val track2:String,
                                     val pinBlock:String,
                                     val merchantId:String,
                                     val currency:String,
                                     val POS:String,
                                     serial: String, appVersion: String, nii: String
     */
    override suspend fun balance(
        pan: String,
        terminalId: String,
        terminalLanguage: String,
        terminalConnectionType: String,
        terminalType: String,
        track2: String,
        pinBlock: String,
        merchantId: String,
        currency: String,
        POS: String,
        serial: String, appVersion: String, nii: String
    )
            : ResponseData<BaseTransactionResponse.BalanceTransactionResponse> {
        val transaction = BalanceTransaction(BalanceTransactionRequest(
            pan = pan, terminalId = "12010164",
            terminalConnectionType = terminalConnectionType,
            terminalType = terminalType,
            terminalLanguage = terminalLanguage,
            track2 = track2,
            pinBlock = pinBlock,
            merchantId = merchantId,
            POS = POS,
            currency = currency, stan = generateStan(),
            serial = "92261946156409",
            appVersion = "1.0.0",
            nii =nii,
            date = getDateOfTransaction(),
            time = getTimeOfTransaction()
        ),
            macGenerator,
            iConnection,
            saveReverseData = {})

//        return safeApiCall(isConnected = { true }) {
//            transaction.execute() as BalanceTransactionResponse
//        }
        val response = transaction.execute()
        return if (response is FaildTransactionResponse)
            ResponseData.Error(data = response.toBalanceTransactionResponse())
        else ResponseData.Success(data = response as BaseTransactionResponse.BalanceTransactionResponse)
    }

    override suspend fun purchase(
        amount: String,
        pan: String,
        terminalId: String,
        terminalLanguage: String,
        terminalConnectionType: String,
        terminalType: String,
        track2: String,
        pinBlock: String,
        merchantId: String,
        currency: String,
        POS: String,
        serial: String,
        appVersion: String,
        nii: String
    ): ResponseData<BaseTransactionResponse.PurchaseTransactionResponse> {
        val date = getDateOfTransaction()
        val time = getTimeOfTransaction()
        val stan = generateStan()
        val transaction = PurchaseTransaction(
            PurchaseTransactionRequest(
                pan = pan, terminalId = "12010164",
                terminalConnectionType = terminalConnectionType,
                terminalType = terminalType,
                terminalLanguage = terminalLanguage,
                track2 = track2,
                pinBlock = pinBlock,
                merchantId = merchantId,
                POS = POS,
                currency = currency, stan = stan,
                serial = "92261946156409",
                appVersion = "1.0.0",
                nii = "211", amount = amount,
                date = date,
                time = time
            ),
            macGenerator,
            iConnection,
            saveTransactionLog = {
                withContext(Dispatchers.IO){
                    transactionLogDao.insert(
                        TransactionLogEntity(
                            0,
                            it.processCode,
                            amount,
                            stan.toString(),
                            date,
                            time,
                            terminalId,
                            pan,
                            TransactionType.PURCHASE.tag, null, null, 0, null
                        )
                    )
                }
            },
            saveReverseData = {
                withContext(Dispatchers.IO) {
                    val transactionForInqueue = TransactionQueueEntity(
                        dateTime = "${date}-${time}",
                        date = date,
                        time = time,
                        status = 'R',
                        printed = false,
                        processingCode = it.processCode,
                        amount = amount,
                        merchantId = merchantId,
                        stan = stan.toString(),
                        rrn = "",
                        maskedPan = pan,
                        responseCode = null,
                        type = TransactionType.PURCHASE.tag,
                        issuer = "",
                        responseMsg = ""
                    )
                    println("hhhhhhhhhhhh=>${transactionForInqueue.toString()}")
                    transactionQueueDao.insert(
                        transactionForInqueue
                    )
                }
            }, sendTransactionInQueue = {
                sendTransactionInQueue()
            }, setStatusToSettle = { date, time ->

                withContext(Dispatchers.IO) {
                    //                    val transactionQueue =transactionQueueDao. getWithDateTime(date, time)
//                    transactionQueue!!.status = 'S'
                    transactionQueueDao.updateStatusByDateTime('S', date, time)
                }

            }, setStatusToReverse = { date, time ->

//                    val transactionQueue =transactionQueueDao. getWithDateTime(date, time)
//                    transactionQueue!!.status = 'S'
                withContext(Dispatchers.IO){
                    transactionQueueDao.updateStatusByDateTime('R', date, time)
                }
            }, updateTransaction = {date, time,pan,cardIssuer,responseCode,amount,rrn,
                                    serviceDesc, pinVoucher, serialVoucher,mobileNumber,operatorCode->
                updateTransaction(date,time,pan,cardIssuer,responseCode,amount,rrn,serviceDesc,pinVoucher,serialVoucher,mobileNumber,operatorCode)
            })
        val response = transaction.execute()
        println("ddddwddddddduy->${Gson().toJson(response)}")
        return if (response is FaildTransactionResponse)
            ResponseData.Error(data = response.toPurchaseTransactionResponse(), error = response.responseMessage)
        else ResponseData.Success(data = response as BaseTransactionResponse.PurchaseTransactionResponse)
    }

    override suspend fun voucher(
        amount: String,
        pan: String,
        terminalId: String,
        terminalLanguage: String,
        terminalConnectionType: String,
        terminalType: String,
        track2: String,
        pinBlock: String,
        merchantId: String,
        currency: String,
        POS: String,
        serial: String,
        appVersion: String,
        nii: String,
        requestDecryptData: (ByteArray?) -> ByteArray?
    ): ResponseData<BaseTransactionResponse.VoucherTransactionResponse> {
        val date = getDateOfTransaction()
        val time = getTimeOfTransaction()
        val stan = generateStan()
        val transaction = VoucherTransaction(
            VoucherTransactionRequest(
                pan = pan, terminalId = "12010164",
                terminalConnectionType = terminalConnectionType,
                terminalType = terminalType,
                terminalLanguage = terminalLanguage,
                track2 = track2,
                pinBlock = pinBlock,
                merchantId = merchantId,
                POS = POS,
                currency = currency, stan = stan,
                serial = "92261946156409",
                appVersion = "1.0.0",
                nii = "211", amount = "1000",
                date = date,
                time = time, productCode = "101"
            ),
            macGenerator,
            iConnection,
            saveTransactionLog = {
                withContext(Dispatchers.IO){
                    transactionLogDao.insert(
                        TransactionLogEntity(
                            0,
                            it.processCode,
                            amount,
                            stan.toString(),
                            date,
                            time,
                            terminalId,
                            pan,
                            TransactionType.PURCHASE.tag, null, null, null, null
                        )
                    )
                }
            },
            saveReverseData = {
                withContext(Dispatchers.IO){
                    val v = TransactionQueueEntity(
                        dateTime = "${date}-${time}",
                        date = date,
                        time = time,
                        status = 'R',
                        printed = false,
                        processingCode = it.processCode,
                        amount = amount,
                        merchantId = merchantId,
                        stan = stan.toString(),
                        rrn = "",
                        maskedPan = pan,
                        responseCode = null,
                        type = TransactionType.VOUCHER.tag,
                        issuer = "",
                        responseMsg = ""
                    )
                    println("jjjjjjjjjjj->${v.toString()}")
                    transactionQueueDao.insert(
                        v
                    )
                }
            }, requestDecryptData = {
                requestDecryptData(it)
            }, sendTransactionInQueue = {
                sendTransactionInQueue()
            }, updateTransaction = {date, time,pan,cardIssuer,responseCode,amount,rrn,
                                    serviceDesc, pinVoucher, serialVoucher,mobileNumber,operatorCode->
                updateTransaction(date,time,pan,cardIssuer,responseCode,amount,rrn,serviceDesc,pinVoucher,serialVoucher,mobileNumber,operatorCode)

            }, setStatusToSettle = {x,y->
                withContext(Dispatchers.IO) {
                    //                    val transactionQueue =transactionQueueDao. getWithDateTime(date, time)
//                    transactionQueue!!.status = 'S'
                    transactionQueueDao.updateStatusByDateTime('S', date, time)
                }
            })
//        return ResponseData.Success(
//            transaction.execute(
//            ) as VoucherTransactionResponse
//        )
        val response = transaction.execute()
        return if (response is FaildTransactionResponse)
            ResponseData.Error(data = response.toVoucherTransactionResponse(), error = response.responseMessage)
        else ResponseData.Success(data = response as BaseTransactionResponse.VoucherTransactionResponse)
    }
    override suspend fun topup(
        amount: String,
        mobile:String,
        productCode:String,
        pan: String,
        terminalId: String,
        terminalLanguage: String,
        terminalConnectionType: String,
        terminalType: String,
        track2: String,
        pinBlock: String,
        merchantId: String,
        currency: String,
        POS: String,
        serial: String,
        appVersion: String,
        nii: String,
        requestDecryptData: (ByteArray?) -> ByteArray?
    ): ResponseData<BaseTransactionResponse.TopupTransactionResponse> {
        val date = getDateOfTransaction()
        val time = getTimeOfTransaction()
        val stan = generateStan()
        val transaction = TopupTransaction(
            TopupTransactionRequest(
                pan = pan, terminalId = "12010164",
                terminalConnectionType = terminalConnectionType,
                terminalType = terminalType,
                terminalLanguage = terminalLanguage,
                track2 = track2,
                pinBlock = pinBlock,
                merchantId = merchantId,
                POS = POS,
                currency = currency, stan = stan,
                serial = "92261946156409",
                appVersion = "1.0.0",
                nii = "211", amount = amount,
                date = date,
                time = time, productCode = productCode, mobile = mobile
            ),
            macGenerator,
            iConnection,
            saveTransactionLog = {
                withContext(Dispatchers.IO){
                    transactionLogDao.insert(
                        TransactionLogEntity(
                            0,
                            it.processCode,
                            amount,
                            stan.toString(),
                            date,
                            time,
                            terminalId,
                            pan,
                            TransactionType.PURCHASE.tag, null, null, null, null
                        )
                    )
                }
            },
            saveReverseData = {
                withContext(Dispatchers.IO){
                    val v = TransactionQueueEntity(
                        dateTime = "${date}-${time}",
                        date = date,
                        time = time,
                        status = 'R',
                        printed = false,
                        processingCode = it.processCode,
                        amount = amount,
                        merchantId = merchantId,
                        stan = stan.toString(),
                        rrn = "",
                        maskedPan = pan,
                        responseCode = null,
                        type = TransactionType.VOUCHER.tag,
                        issuer = "",
                        responseMsg = ""
                    )
                    println("jjjjjjjjjjj->${v.toString()}")
                    transactionQueueDao.insert(
                        v
                    )
                }
            }, sendTransactionInQueue = {
                sendTransactionInQueue()
            }, updateTransaction = {date, time,pan,cardIssuer,responseCode,amount,rrn,
                                    serviceDesc, pinVoucher, serialVoucher,mobileNumber,operatorCode->
               updateTransaction(date,time,pan,cardIssuer,responseCode,amount,rrn,serviceDesc,pinVoucher,serialVoucher,mobileNumber,operatorCode)
            }, setStatusToSettle = {x,y->
                withContext(Dispatchers.IO) {
                    //                    val transactionQueue =transactionQueueDao. getWithDateTime(date, time)
//                    transactionQueue!!.status = 'S'
                    transactionQueueDao.updateStatusByDateTime('S', date, time)
                }
            })
//        return ResponseData.Success(
//            transaction.execute(
//            ) as VoucherTransactionResponse
//        )
        val response = transaction.execute()
        println("hhhhhhhhhhhhhfffdd->${Gson().toJson(response)}")
        val v= if (response is FaildTransactionResponse)
            ResponseData.Error(data = response.toTopupTransactionResponse(),error = response.responseMessage)
        else ResponseData.Success(data = response as BaseTransactionResponse.TopupTransactionResponse)
        println("hhhhhhhhhhhhhfffd->${Gson().toJson(v)}")
        return v
    }

    private suspend fun updateTransaction(
        date: String,
        time: String,
        pan: String,
        cardIssuer: String,
        responseCode: String,
        amount: String,
        rrn: String?,
        serviceDesc: String?,
        pinVoucher: String?,
        serialVoucher: String?,
        mobileNumber: String?,
        operatorCode: Int?
    ) {
        withContext(Dispatchers.IO){
            with(transactionLogDao) {
                val transaction = getByDateTime(date, time)
                transaction!!.rrn = rrn
                transaction.issuer = cardIssuer
                transaction.responseCode = responseCode.toInt()
                transaction.maskedPan=pan
                if(cardIssuer!=null)transaction.issuer=cardIssuer
                //   transaction.responseMsg = respMessage
                if(serviceDesc!=null)transaction.serviceDesc=serviceDesc
                if(pinVoucher!=null)transaction.pinVoucher=pinVoucher
                if(serialVoucher!=null)transaction.serialVoucher=serialVoucher
                if(mobileNumber!=null)transaction.mobileNumber=mobileNumber
                if(operatorCode!=null)transaction.operatorCode=operatorCode
                update(transaction)
            }
        }
    }

    override suspend fun billInquery(
        terminalId: String,
        terminalLanguage: String,
        terminalConnectionType: String,
        terminalType: String,
        merchantId: String,
        serial: String,
        appVersion: String,
        nii: String, billID: String, payId: String
    ): ResponseData<BaseTransactionResponse.BillInqueryTransactionResponse> {
        val stan = generateStan()
        val transaction = BillInqueryTransaction(
            BillInqueryTransactionRequest(
                terminalId = "12010164",
                terminalConnectionType = terminalConnectionType,
                terminalType = terminalType,
                terminalLanguage = terminalLanguage,
                merchantId = merchantId,
                stan = stan,
                serial = "92261946156409",
                appVersion = "1.0.0",
                nii = "211",
                date = getDateOfTransaction(),
                time = getTimeOfTransaction(),
                billId = billID, payId = payId
            ),
            macGenerator,
            iConnection,
            saveTransactionLog = {},
            saveReverseData = {}, sendTransactionInQueue = {
                sendTransactionInQueue()
            })
        return ResponseData.Success(
            transaction.execute(
            ) as BaseTransactionResponse.BillInqueryTransactionResponse
        )
    }

    override suspend fun billPay(
        amount: String,
        pan: String,
        track2: String,
        pinBlock: String,
        terminalId: String,
        terminalLanguage: String,
        terminalConnectionType: String,
        terminalType: String,
        merchantId: String,
        serial: String,
        appVersion: String,
        nii: String,
        billID: String,
        payId: String
    ): ResponseData<BaseTransactionResponse.BillPayTransactionResponse> {
        val date = getDateOfTransaction()
        val time = getTimeOfTransaction()
        val stan = generateStan()
        val transaction = BillPayTransaction(
            BillPayTransactionRequest
                (
                amount = amount,
                terminalId = "12010164",
                terminalConnectionType = terminalConnectionType,
                terminalType = terminalType,
                terminalLanguage = terminalLanguage,
                merchantId = merchantId,
                stan = stan,
                serial = "92261946156409",
                appVersion = BuildConfig.VERSION_NAME,
                nii = "211",
                date = date, time = time,
                billId = billID, payId = payId,
                pan = pan,
                track2 = track2,
                pinBlock = pinBlock,
                POS = "POS", currency = "", serviceDesc = ""
            ),
            macGenerator,
            iConnection,
            saveTransactionLog = {
                withContext(Dispatchers.IO){
                    transactionLogDao.insert(
                        TransactionLogEntity(
                            0,
                            it.processCode,
                            amount,
                            stan.toString(),
                            date,
                            time,
                            terminalId,
                            pan,
                            TransactionType.PURCHASE.tag, null, null, null, null
                        )
                    )
                }
            },
            saveReverseData = {
               withContext(Dispatchers.IO){
                   transactionQueueDao.insert(
                       TransactionQueueEntity(
                           dateTime = "${date}-${time}",
                           date = date,
                           time = time,
                           status = 'R',
                           printed = false,
                           processingCode = it.processCode,
                           amount = amount,
                           merchantId = merchantId,
                           stan = stan.toString(),
                           rrn = "",
                           maskedPan = pan,
                           responseCode = null,
                           type = TransactionType.BILL_PAY.tag,
                           issuer = "",
                           responseMsg = ""
                       )
                   )
               }
            }, sendTransactionInQueue = {
                sendTransactionInQueue()
            }, updateTransaction = {date, time,pan,cardIssuer,responseCode,amount,rrn,
                                    serviceDesc, pinVoucher, serialVoucher,mobileNumber,operatorCode->
                updateTransaction(date,time,pan,cardIssuer,responseCode,amount,rrn,serviceDesc,pinVoucher,serialVoucher,mobileNumber,operatorCode)

            }, setStatusToSettle = {x,y->  withContext(Dispatchers.IO) {
                //                    val transactionQueue =transactionQueueDao. getWithDateTime(date, time)
//                    transactionQueue!!.status = 'S'
                transactionQueueDao.updateStatusByDateTime('S', date, time)
            }})
//        return ResponseData.Success(
//            transaction.execute(
//            ) as BaseTransactionResponse.BillPayTransactionResponse
//        )
        val response = transaction.execute()
        return if (response is FaildTransactionResponse)
            ResponseData.Error(data = response.toBillPayTransactionResponse())
        else ResponseData.Success(data = response as BaseTransactionResponse.BillPayTransactionResponse)
    }

    override fun storeTerminalId(terminalId: String) {
        totanPayPreference.storeTerminalId(terminalId)
    }

    override fun storeMerchant(merchantId: String?, merchantPhone: String?, merchantName: String?) {
        Log.d(
            "TAG",
            "storeMerchant() called with: merchantId = $merchantId, merchantPhone = $merchantPhone, merchantName = $merchantName,"
        )
        totanPayPreference.storeMerchantName(merchantName)
        totanPayPreference.storeMerchantId(merchantId)
        totanPayPreference.storeMerchantPhone(merchantPhone)
    }

    private fun generateStan(): Int {
        var stan = totanPayPreference.getStan()
        stan++
        totanPayPreference.storeStan(stan)
        return stan
    }

    override fun getMerchant(): Merchant {
        println("sssssssssss->${totanPayPreference.getMerchantId()}")
        println("sssssssssss->${totanPayPreference.getMerchantPhone()}")
        println("sssssssssss->${totanPayPreference.getMerchantName()}")

        return Merchant(
            totanPayPreference.getMerchantId(),
            totanPayPreference.getMerchantPhone(),
            totanPayPreference.getMerchantName()
        )
    }

    override fun saveConnectionSettings(ip: String, port: String, nii: String) {
        totanPayPreference.storeConnectionSettings(ip, port, nii)
    }

    override fun getTerminalId(): String {
        return totanPayPreference.getTerminalId() ?: ""
    }

    override fun getLastTransaction(): TransactionLogEntity? {
        val lastTransaction =
            transactionLogDao.getLast(exceptType = TransactionType.BALANCE.tag)// TODO:
        println("lastTransaction=>${Gson().toJson(lastTransaction)}")
        return lastTransaction
    }

    override fun geTransactionBasedStan(stan: String): TransactionLogEntity? {
        val transaction = transactionLogDao.getByStan(stan = stan)// TODO:
        println("lastTransaction=>${Gson().toJson(transaction)}")
        return transaction
    }

    override fun getNii(): String {
      //  return totanPayPreference.getNii()
        return "211"
    }

    override fun getCurrency(): String {
        return "364"
    }

    override fun getTerminalConnectionType(): String {
        return "2"
    }

    override fun getTerminalType(): String {
        return "2"
    }
    override fun hasConnectionSettings(): Boolean {
//        return totanPayPreference.getNii().isNotEmpty() && totanPayPreference.getIP()
//            .isNotEmpty() &&
//                totanPayPreference.getPort().isNotEmpty()
        // TODO:
        return true
    }

    override fun getIP(): String {
        return totanPayPreference.getIP()
    }

    override fun getPort(): String {
        return totanPayPreference.getPort()
    }

    private suspend fun sendTransactionInQueue(): Boolean {
        return withContext(Dispatchers.IO){
            var transactionsInQueue = transactionQueueDao.getAll()
            if (!transactionsInQueue.isNullOrEmpty()) {
                transactionsInQueue.forEach {
                    val settlementReverseTransactionRequest = SettlementReverseTransactionRequest(
                        stan = it.stan.toInt(),
                        processingCode = it.processingCode,
                        status = it.status,
                        amount = it.amount,
                        currency = getCurrency(),
                        terminalConnectionType = getTerminalConnectionType(),
                        terminalType = getTerminalType(),
                        merchantId = it.merchantId,
                        date = it.date,
                        time = it.time,
                        nii = getNii(),
                        appVersion = BuildConfig.VERSION_NAME,
                        serial = "", terminalId = "12010164"
                    )
                    println("tytytytt->${Gson().toJson(settlementReverseTransactionRequest)}")
                    val transaction = SettlementReverseTransaction(settlementReverseTransactionRequest,
                        macGenerator,
                        iConnection,
                        saveTransactionLog = {},
                        saveReverseData = {}, updateTransaction = {date, time,pan,cardIssuer,responseCode,amount,rrn,
                                                                   serviceDesc, pinVoucher, serialVoucher,mobileNumber,operatorCode->
                            updateTransaction(date,time,pan,cardIssuer,responseCode,amount,rrn,serviceDesc,pinVoucher,serialVoucher,mobileNumber,operatorCode)
                        })
                    val response = transaction.execute()
                    if (response.responseCode != 80)
                        transactionQueueDao.deleteByDateTime(it.date, it.time)

                }
            }
            transactionsInQueue = transactionQueueDao.getAll()
            if (transactionsInQueue.isNullOrEmpty())
                 true
            else false
        }
    }

    override suspend fun settlementReverse() {
        sendTransactionInQueue()
    }
}


suspend fun <T> safeApiCall(
    isConnected: () -> Boolean,
    call: suspend () -> (T),
): ResponseData<T> {
    return withContext(Dispatchers.IO) {
        try {
//            if (!isConnected()) {
//                return@withContext Result.Error(message = ConnectionException.message)
//            }
            val result = call()
//            val commonParam = resultParam(result)
            val isSuccess = !(result is FaildTransactionResponse)
            if (isSuccess) {
                return@withContext ResponseData.Success(result)
            } else {
                return@withContext ResponseData.Error(
                    data = result,
                    //message = SwitchHelper.getSwitchError(commonParam.responseCode)
                )
            }
        } catch (exception: IOException) {
            return@withContext ResponseData.Error(data = null)
        } catch (exception: java.lang.Exception) {
            return@withContext ResponseData.Error(data = null)
        }
    }


}

private fun <T> T.isSuccess(): Boolean {
    return true
}
