package com.example.totanpay.data.repository


import android.content.Context
import android.device.sdk.BuildConfig
import android.graphics.Bitmap
import android.util.Log
import com.example.totanpay.data.Operator
import com.example.totanpay.data.repository.datasource.ConnectionSettingsDataSource
import com.example.totanpay.data.repository.datasource.MainDataSource
import com.example.totanpay.data.repository.datasource.ResponseData
import com.example.totanpay.data.repository.datasource.generateSHA1
import com.example.totanpay.data.repository.datasource.mask
import com.example.totanpay.data.repository.datasource.model.Merchant
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.example.totanpay.data.repository.datasource.parseSharedKey
import com.example.totanpay.data.repository.datasource.rki.ECC
import com.example.totanpay.data.repository.datasource.rki.ECCOperations
import com.example.totanpay.data.repository.datasource.rki.ECCParallel
import com.example.totanpay.data.repository.datasource.rki.ECCPoint
import com.example.totanpay.data.repository.datasource.transaction.TransactionType
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.toResponseTransaction
import com.example.totanpay.data.util.formatTime
import com.urovo.sdk.utils.BytesUtil
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jpos.iso.ISOUtil
import java.math.BigInteger
import java.util.concurrent.ExecutionException
import java.util.concurrent.Future
import javax.inject.Inject


class MainRepositoryImpl @Inject constructor(
    private val ioDispatcher:CoroutineDispatcher,
    private val coroutineScope: CoroutineScope,
    private val dataSource: MainDataSource,
    private val device: IDevice,
    private val connectionSettingsDataSource: ConnectionSettingsDataSource
) : MainRepository {
    override suspend fun getKeyTransaction(otp: String): ResponseData<ResponseTransaction> {
        return withContext(ioDispatcher) {
            val hashedOtp = generateSHA1(otp.toByteArray())
            val posPairKey = generatePairKey()// TODO: handle
            val keyTransactionResponse = dataSource.getKeyTransaction(
                serial = device.getSerial(),
                appVersion = BuildConfig.VERSION_NAME,
                nii = connectionSettingsDataSource.getNii(),
                compressedPOSPublicKey = posPairKey.second,
                hashedOtp = hashedOtp,
                terminalLanguage = getTerminalLanguage()
            ) { master, mac, encryptedToken ->
                decryptKeys(master, mac, encryptedToken, posPairKey)
            }
            if (keyTransactionResponse is ResponseData.Success) {
                dataSource.storeTerminalId(keyTransactionResponse.data!!.terminalId?:"")
                dataSource.storeAcquiringInstitutionIdentificationCode(keyTransactionResponse.data!!.acquiringInstitutionIdentificationCode?:"")
                val confirmMasterKeyResult = dataSource.confirmMasterKey(
                    serial = device.getSerial(),
                    appVersion = BuildConfig.VERSION_NAME,
                    nii = connectionSettingsDataSource.getNii(),
                    terminalLanguage = getTerminalLanguage(),
                    hashedMasterToken = generateSHA1(
                        keyTransactionResponse.data!!.decryptedToken!!.let {
                            ISOUtil.hex2byte(it)
                        }
                    ),
                    acquiringInstitutionIdentificationCode = keyTransactionResponse.data.acquiringInstitutionIdentificationCode
                        ?: "",
                    terminalId = keyTransactionResponse.data.terminalId ?: "",
                )
                if (confirmMasterKeyResult is ResponseData.Success) {
                    val workingKeyExchangeResult = dataSource.workingKeyExchange(
                        serial = device.getSerial(),
                        appVersion = BuildConfig.VERSION_NAME,
                        nii = connectionSettingsDataSource.getNii(),
                        terminalLanguage = getTerminalLanguage(), terminalId = "",
                        hashedMasterToken = generateSHA1(
                            keyTransactionResponse.data.decryptedToken!!.let { ISOUtil.hex2byte(it)
                            }
                        )
                    )
                    if (workingKeyExchangeResult is ResponseData.Success) {
                        if (workingKeyExchangeResult.data != null) {
                            device.writeDataKey(ISOUtil.hex2byte(workingKeyExchangeResult.data.dataKey))
                            device.writePinKey(ISOUtil.hex2byte(workingKeyExchangeResult.data!!.pinKey))
                          device.writeMacKey(ISOUtil.hex2byte(workingKeyExchangeResult.data.macKey))
                        }
                        ResponseData.Success(workingKeyExchangeResult.data!!.toResponseTransaction())
                    } else {
                        ResponseData.Error(workingKeyExchangeResult.error)

                    }
                } else {
                    ResponseData.Error(confirmMasterKeyResult.error)
                }
            } else {
                ResponseData.Error(keyTransactionResponse.error)
            }
        }
    }

    private fun getTerminalLanguage(): String {
        return "0"
    }

    private fun decryptKeys(
        master: String,
        mac: String,
        encryptedToken: String,
        posPairKey: Pair<BigInteger, String>
    ): Map<String, String> {
        try {
            var parsedMasterKey: String? = ""
            var parsedMacKey: String? = ""
            val receivedMasterPoint =
                ECCOperations.decompressPoint(master)//pnaMessage.compressedMaster
            val receivedMacPoint = ECCOperations.decompressPoint(mac)//pnaMessage.compressedMac
            val masterSharedKey =
                ECC.generateSharedKey(posPairKey.first, receivedMasterPoint).toString(16)
            val macSharedKey =
                ECC.generateSharedKey(posPairKey.first, receivedMacPoint).toString(16)
            parsedMasterKey = parseSharedKey(masterSharedKey.substring(masterSharedKey.length-32))
            parsedMacKey = parseSharedKey(macSharedKey.substring(macSharedKey.length- 32))
            device.writeMasterKey(ISOUtil.hex2byte(parsedMasterKey))
            val decryptedToken = device.decrypt(ISOUtil.hex2byte(encryptedToken))
            val macEnc = device.encrypt(ISOUtil.hex2byte(parsedMacKey))
            device.writeMacKey(macEnc!!)
            val map = mutableMapOf<String, String>()
            map["mac"] = parsedMacKey
            map["master"] = parsedMasterKey
            map["decryptedToken"] = ISOUtil.hexString(decryptedToken)
            return map
        } catch (e: Exception) {
            println("cause->${e.cause}")
            println("message->${e.message}")
            e.printStackTrace()
        }
        val map = mutableMapOf<String, String>()
        return map
    }

    @Throws
    private fun generatePairKey(): Pair<BigInteger, String> {
        val eccParallel = ECCParallel(Runtime.getRuntime().availableProcessors())
        try {
            val privateKey: BigInteger
            val publicKey: ECCPoint
            val keyPair: Array<BigInteger>
            val futureKeyPair: Future<Array<BigInteger>> = eccParallel.generateKeyPairAsync()
            keyPair = futureKeyPair.get()
            privateKey = keyPair[0]
            publicKey = ECCPoint(keyPair[1], keyPair[2])
            val compressedPOSPublicKey: String = ECCOperations.compressPoint(publicKey)//.toHexString()
            return Pair(privateKey, compressedPOSPublicKey)
        } catch (e: InterruptedException) {
            e.printStackTrace()
            throw InterruptedException()
        } catch (e: ExecutionException) {
            e.printStackTrace()
            throw InterruptedException()
            //   throw  Exception(message = e.message, cause = e.cause)// TODO:
        } finally {
            eccParallel.shutdown()
        }
    }

    override suspend fun keyInjection() {
        dataSource.loadSettings()
        //withContext(ioDispatcher) {
        device.writeMasterKey(BytesUtil.hexString2Bytes("15EF543B6AD4EC94A3BE012D9D581EFA"))
        val macEnc =
            device.encrypt(BytesUtil.hexString2Bytes("15EF543B6AD4EC94A3BE012D9D581EFA"))
        device.writeMacKey(macEnc!!, 2)
        getKeyTransaction("314932")//

//                    device.writeMasterKey(BytesUtil.hexString2Bytes("3272A9DB5E07D848337D00C275CCC58A"))
//        val macEnc =
//                device.encrypt(BytesUtil.hexString2Bytes("3272A9DB5E07D848337D00C275CCC58A"))
//       device.writeMacKey(macEnc!!)
        //    }
    }

    override suspend fun logon(): ResponseData<ResponseTransaction> {
        return withContext(ioDispatcher) {
            if (connectionSettingsDataSource.hasConnectionSettings()) {
                val logonTransactionResponse = dataSource.logon(
                    serial = device.getSerial(),
                    appVersion = BuildConfig.VERSION_NAME,
                    nii = connectionSettingsDataSource.getNii(),
                    terminalLanguage = getTerminalLanguage()
                )
                if (logonTransactionResponse is ResponseData.Success) {
                    ResponseData.Success(logonTransactionResponse.data!!.toResponseTransaction())
                } else {
                    ResponseData.Error(error = logonTransactionResponse.data?.responseMessage)
                }
            } else {
                ResponseData.Error(error = "تنظیمات ارتباطی وجود ندارد لطفا به قسمت تنظیمات اتصال بروید و  تنظیمات را تعیین نمایید")
            }
        }
    }
    override suspend fun init(): ResponseData<BaseTransactionResponse.InitTransactionResponse> {
        return  withContext(ioDispatcher){
            val terminalId=dataSource.getTerminalId()
            val merchantId=dataSource.getMerchant().merchantId
            val acquiringInstitutionIdentificationCode=dataSource.getAcquiringInstitutionIdentificationCode()
            val initTransactionResponse = dataSource.
            init(
                terminalId =terminalId,
                serial = device.getSerial(),
                appVersion = BuildConfig.VERSION_NAME,
                nii = connectionSettingsDataSource.getNii(), terminalLanguage = getTerminalLanguage(), merchantId = merchantId?:"",
                acquiringInstitutionIdentificationCode = acquiringInstitutionIdentificationCode?:""
            )
            if (initTransactionResponse is ResponseData.Success) {
                dataSource.storeMerchant(
                    (initTransactionResponse.data as BaseTransactionResponse.InitTransactionResponse).merchantId.trim(),
                    initTransactionResponse.data.merchantPhone?.trim() ?: "",
                    initTransactionResponse.data.merchantName?.trim() ?: ""
                )
                initTransactionResponse
            } else {
                initTransactionResponse
            }
        }
    }

    override suspend fun configuration(): ResponseData<BaseTransactionResponse.InitTransactionResponse> {
        return init()
    }

    override suspend fun balance(
        track2: String,
        pinBlock: String
    ): ResponseData<ResponseTransaction> {
        return withContext(ioDispatcher) {
            val terminalId = dataSource.getTerminalId()
            val merchantId = dataSource.getMerchant().merchantId
            val string: List<String> = track2.split("=")
            val pan = string[0]
            val result = dataSource.balance(
                pan = pan,
                terminalId =terminalId,// "12010164"
                serial = device.getSerial(),
                appVersion = BuildConfig.VERSION_NAME,
                nii = connectionSettingsDataSource.getNii(),
                terminalLanguage = getTerminalLanguage(),
                terminalConnectionType = dataSource.getTerminalConnectionType(),
                terminalType = dataSource.getTerminalType(),
                track2 = track2,
                pinBlock = pinBlock,
                merchantId = merchantId?:"",
                currency = dataSource.getCurrency(),
                POS = "021",
            )
            val merchant = dataSource.getMerchant()
            if (result is ResponseData.Success) {
                ResponseData.Success(
                    ResponseTransaction(
                        responseCode = result.data!!.responseCode.toString(),
                        responseMessage = result.data.responseMessage ?: "",
                        rrn = result.data.rrn ?: "",
                        trace = result.data.trace,
                        merchantName = merchant.merchantName ?: "",
                        merchantId = merchant.merchantId ?: "",
                        merchantPhone = merchant.merchantPhone ?: "",
                        terminalID = dataSource.getTerminalId(),
                        transactionType = TransactionType.BALANCE.title,
                        date = result.data.date,
                        time = result.data.time.formatTime(),
                        issuerName = result.data.issuerName,
                        amount = "", availableBalance = result.data.availableBalance,
                        realBalance = result.data.balance, maskedPan = pan
                    )
                )
            } else {
                ResponseData.Error(
                    data = result.data?.toResponseTransaction(
                        merchant,
                        dataSource.getTerminalId()
                    ), error = result.data?.responseMessage
                )
            }
        }
    }

    override suspend fun purchase(
        amount: String,
        track2: String,
        pinBlock: String
    ): ResponseData<ResponseTransaction> {
        return withContext(ioDispatcher) {
            val terminalId = dataSource.getTerminalId()
            val merchantId = dataSource.getMerchant().merchantId
            val string: List<String> = track2.split("=")
            val pan = string[0]
            val result = dataSource.purchase(
                amount = amount,
                pan = pan.mask(),
                terminalId = terminalId,
                serial = device.getSerial(),
                appVersion = BuildConfig.VERSION_NAME,
                nii = connectionSettingsDataSource.getNii(),
                terminalLanguage = getTerminalLanguage(),
                terminalConnectionType = "2",
                terminalType = "2",
                track2 = track2,
                pinBlock = pinBlock,
                merchantId = merchantId?:"",
                currency = "364",
                POS = "021",
            )
            val merchant = dataSource.getMerchant()
            if (result is ResponseData.Success) {
                ResponseData.Success(
                    ResponseTransaction(
                        responseCode = result.data!!.responseCode.toString(),
                        responseMessage = result.data.responseMessage ?: "",
                        rrn = result.data.rrn ?: "",
                        trace = result.data.trace,
                        merchantName = merchant.merchantName ?: "",
                        merchantId = merchant.merchantId ?: "",
                        merchantPhone = merchant.merchantPhone ?: "",
                        terminalID = dataSource.getTerminalId(),
                        transactionType = TransactionType.PURCHASE.title,
                        date = result.data.date,
                        time = result.data.time.formatTime(),
                        issuerName = result.data.issuerName,
                        amount = amount, maskedPan = pan.mask()
                    )
                )
            } else {
                ResponseData.Error(
                    data = result.data?.toResponseTransaction(
                        merchant,
                        dataSource.getTerminalId()
                    ), error = result.data?.responseMessage
                )
            }
        }
    }

    override suspend fun voucher(
        amount: String,
        pan: String,
        track2: String,
        pinBlock: String,operator: Operator
    ): ResponseData<ResponseTransaction> {
        return withContext(ioDispatcher){
            val terminalId = dataSource.getTerminalId()
            val merchantId = dataSource.getMerchant().merchantId
            val string: List<String> = track2.split("=")
            val p = string[0]
            val result = dataSource.voucher(amount = amount,
                pan = p,
                terminalId = terminalId,
                serial = device.getSerial(),
                appVersion = BuildConfig.VERSION_NAME,
                nii = connectionSettingsDataSource.getNii(),
                terminalLanguage = getTerminalLanguage(),
                terminalConnectionType = "2",
                terminalType = "2",
                track2 = track2,
                pinBlock = pinBlock,
                merchantId = merchantId?:"",
                currency = "364",
                productCode = operator.code.toString(),
                POS = "021", requestDecryptData = { device.requestDecryptData(data = it) }
            )
            val merchant = dataSource.getMerchant()
            if (result is ResponseData.Success) {
                ResponseData.Success(
                    ResponseTransaction(
                        responseCode = result.data!!.responseCode.toString(),
                        responseMessage = result.data.responseMessage ?: "",
                        rrn = result.data.rrn ?: "",
                        trace = result.data.trace,
                        merchantName = merchant.merchantName ?: "",
                        merchantId = merchant.merchantId ?: "",
                        merchantPhone = merchant.merchantPhone ?: "",
                        terminalID = dataSource.getTerminalId(),
                        transactionType = TransactionType.VOUCHER.title,
                        date = result.data.date,
                        time = result.data.time.formatTime(),
                        issuerName = result.data.issuerName,
                        amount = amount, voucherPin = result.data.voucherPin, maskedPan = p.mask(),
                        voucherSerial = result.data.voucherSerial, productCode = operator.code
                    )
                )
            } else {
                ResponseData.Error(
                    data = result.data?.toResponseTransaction(
                        merchant,
                        dataSource.getTerminalId()
                    ), error = result.data?.responseMessage
                )
            }
        }
    }

    override suspend fun topup(
        amount: String,
        mobile: String,
        productCode: String,
        track2: String,
        pinBlock: String
    ): ResponseData<ResponseTransaction> {
        val terminalId = dataSource.getTerminalId()
        val merchantId = dataSource.getMerchant().merchantId
        return  withContext(ioDispatcher) {
            val string: List<String> = track2.split("=")
            val p = string[0]
            val result = dataSource.topup(amount = amount,
                mobile = mobile,
                productCode = productCode,
                pan = p,
                terminalId = terminalId,
                serial = device.getSerial(),
                appVersion = BuildConfig.VERSION_NAME,
                nii = connectionSettingsDataSource.getNii(),
                terminalLanguage = getTerminalLanguage(),
                terminalConnectionType = "2",
                terminalType = "2",
                track2 = track2,
                pinBlock = pinBlock,
                merchantId = merchantId?:"",
                currency = "364",
                POS = "021", requestDecryptData = { device.requestDecryptData(data = it) }
            )
            val merchant = dataSource.getMerchant()
            if (result is ResponseData.Success) {
                ResponseData.Success(
                    ResponseTransaction(
                        responseCode = result.data!!.responseCode.toString(),
                        responseMessage = result.data.responseMessage ?: "",
                        rrn = result.data.rrn ?: "",
                        trace = result.data.trace,
                        merchantName = merchant.merchantName ?: "",
                        merchantId = merchant.merchantId ?: "",
                        merchantPhone = merchant.merchantPhone ?: "",
                        terminalID = dataSource.getTerminalId(),
                        transactionType = TransactionType.TOPUP.title,
                        date = result.data.date,
                        time = result.data.time.formatTime(),
                        issuerName = result.data.issuerName,
                        amount = amount, voucherPin = null, maskedPan = p.mask()
                    )
                )
            } else {
                ResponseData.Error(
                    data = result.data?.toResponseTransaction(
                        merchant,
                        dataSource.getTerminalId()
                    ), error = result.data?.responseMessage
                )
            }
        }
    }

    override suspend fun readCard(onSuccess: (String) -> Unit, onError: (String) -> Unit,onTimeOut: () -> Unit) {
        device.readCard(onSuccess, onError={onError(it)}, onTimeOut = onTimeOut)
    }

    override suspend fun getPinBlock(
        pan: String,
        onError: (String) -> Unit,
        onInput: (Int) -> Unit,
        onConfirm: (String) -> Unit,
        onCancel: () -> Unit,
        onTimeOut: () -> Unit
    ) {
        // TODO:
        //withContext(ioDispatcher) {
        device.getPinBlock(
            pan,
            onError = onError,
            onInput = onInput,
            onConfirm = onConfirm,
            onCancel = onCancel,
            onTimeOut = onTimeOut
        )
        //}
    }

//    override suspend fun billPay(
//        billID: String,
//        payId: String,
//        amount: String,
//        track2: String,
//        pinBlock: String
//    ): ResponseData<BaseTransactionResponse.BillPayTransactionResponse> {
//        return withContext(ioDispatcher) {
//            dataSource.billPay(
//                terminalId = "12010164",
//                serial = device.getSerial(),
//                appVersion = BuildConfig.VERSION_NAME,
//                nii = "211",
//                terminalLanguage = getTerminalLanguage(),
//                terminalConnectionType = "2",
//                terminalType = "2",
//                merchantId = "01150407",
//                billID = billID,
//                payId = payId,
//                amount = amount,
//                pinBlock = pinBlock,
//                track2 = track2,
//                pan = ""
//            )
//        }
//    }

//    override suspend fun billInquery(
//        billID: String,
//        payId: String
//    ): ResponseData<ResponseTransaction> {
//        return withContext(ioDispatcher) {
//            dataSource.billInquery(
//                terminalId = "12010164",
//                serial = device.getSerial(),
//                appVersion = BuildConfig.VERSION_NAME,
//                nii = "211",
//                terminalLanguage = getTerminalLanguage(),
//                terminalConnectionType = "2",
//                terminalType = "2",
//                merchantId = "01150407",
//                billID = billID, payId = payId
//            )
//        }
//    }
override suspend fun billInquery(
    billID: String,
    payId: String
): ResponseData<ResponseTransaction> {
    return withContext(ioDispatcher) {
        val terminalId = dataSource.getTerminalId()
        val merchantId = dataSource.getMerchant().merchantId
        val merchant = dataSource.getMerchant()
        val result = dataSource.billInquery(
            terminalId = terminalId,
            serial = device.getSerial(),
            appVersion = BuildConfig.VERSION_NAME,
            nii = connectionSettingsDataSource.getNii(),
            terminalLanguage = getTerminalLanguage(),
            terminalConnectionType = "2",
            terminalType = "2",
            merchantId = merchantId ?: "",
            billID = billID, payId = payId
        )
        if (result is ResponseData.Success) {
            ResponseData.Success(ResponseTransaction( responseCode = (result.data!! as BaseTransactionResponse.BillInqueryTransactionResponse).responseCode.toString(),
                responseMessage = result.data.responseMessage ?: "",
                rrn = result.data.rrn ?: "",
                trace = result.data.trace,
                merchantName = merchant.merchantName ?: "",
                merchantId = merchant.merchantId ?: "",
                merchantPhone = merchant.merchantPhone ?: "",
                terminalID = dataSource.getTerminalId(),
                transactionType = TransactionType.TOPUP.title,
                date = result.data.date,
                time = result.data.time.formatTime(),
                issuerName = "",
                amount = result.data.amount?:"", voucherPin = null, maskedPan = "",
                billId = billID, paymentId = payId, serviceDesc = ""))
        } else {
            ResponseData.Error(
                data =ResponseTransaction( responseCode=result.data?.responseCode.toString(),
                    responseMessage=result.data?.responseMessage?:"",
                    rrn=result.data?.rrn?:"",
                    trace=result.data?.trace?:"",
                    merchantName =merchant.merchantName?:"",
                    merchantId=merchant.merchantId?:"",
                    merchantPhone=merchant.merchantPhone?:"",
                    terminalID=terminalId,
                    transactionType=TransactionType.BILL_INQUERY.title,
                    date=result.data?.date?:"",
                    time=result.data?.time?:"",
                    issuerName="",
                    amount="",
                    billId =""
                    , maskedPan="",
                )
            )
        }
    }
}
    override suspend fun billPay(
        billID: String,
        payId: String,
        amount: String,
        track2: String,
        pinBlock: String,
        serviceDesc: String?
    ): ResponseData<ResponseTransaction> {
        TODO("Not yet implemented")
    }



    override fun confirmConnectionSettings(ip: String, port: String, nii: String) {
        dataSource.saveConnectionSettings(ip, port, nii)
    }


    override fun getIP(): String {
        return connectionSettingsDataSource.getIP()
    }

    override fun getNii(): String {
        return connectionSettingsDataSource.getNii()

    }

    override fun getPort(): String {
        return connectionSettingsDataSource.getPort()
    }

    override suspend fun isConfigured(): Boolean {
        return withContext(ioDispatcher) {
            var flag = true
            val merchant = dataSource.getMerchant()
            if (merchant == null)
                flag = false
            if (merchant.merchantName.isNullOrEmpty())
                flag = false
            flag
            true
        }
    }

    override suspend fun print(bitmap: Bitmap, context: Context) {
        coroutineScope.launch {
            device.print(bitmap, context, onSuccess = {}, onFailed = {})
            dataSource.updatePrintStatusOfTransactionInQueue(printStatus =true)
        }
    }

    override suspend fun settlementReverse(terminalLanguage: String) {
        coroutineScope.launch {
            dataSource.settlementReverse(terminalLanguage)
        }.join()
    }

    override suspend fun changePrintStatusOfTransactionInQueue(date_:String,time_:String) {
        coroutineScope.launch {
            val transactionInQueue=dataSource.getTransactionInQueue(date_,time_)
            dataSource.updatePrintStatusOfTransactionInQueue(transactionInQueue)
        }.join()
    }

    override suspend fun getMerchant(): Merchant? {
        return dataSource.getMerchant()
    }

    override suspend fun getTerminalId(): String? {
        return dataSource.getTerminalId()
    }
}