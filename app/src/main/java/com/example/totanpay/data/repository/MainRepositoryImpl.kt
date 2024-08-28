package com.example.totanpay.data.repository

import android.content.Context
import android.device.sdk.BuildConfig
import android.graphics.Bitmap
import android.util.Log
import com.example.totanpay.data.repository.datasource.MainDataSource
import com.example.totanpay.data.repository.datasource.ResponseData
import com.example.totanpay.data.repository.datasource.getSHA256
import com.example.totanpay.data.repository.datasource.mask
import com.example.totanpay.data.repository.datasource.model.Merchant
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.example.totanpay.data.repository.datasource.rki.ECCOperations
import com.example.totanpay.data.repository.datasource.rki.ECCParallel
import com.example.totanpay.data.repository.datasource.rki.ECCPoint
import com.example.totanpay.data.repository.datasource.toHex
import com.example.totanpay.data.repository.datasource.transaction.TransactionType
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.toResponseTransaction
import com.example.totanpay.data.util.formatTime
import com.google.gson.Gson
import com.urovo.sdk.utils.BytesUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigInteger
import java.util.concurrent.ExecutionException
import java.util.concurrent.Future
import javax.inject.Inject


class MainRepositoryImpl @Inject constructor(
    protected val coroutineScope: CoroutineScope,
    private val dataSource: MainDataSource,
    private val device: IDevice
) : MainRepository {
    override suspend fun getKeyTransaction(otp: String) {
        val hashCode = getSHA256(otp)
        var privateKey: BigInteger
        var publicKey: ECCPoint
        val decryptedToken = ""
        val eccParallel = ECCParallel(Runtime.getRuntime().availableProcessors())
        val keyPair: Array<BigInteger>
        try {
            val futureKeyPair: Future<Array<BigInteger>> = eccParallel.generateKeyPairAsync()
            keyPair = futureKeyPair.get() // This blocks until the computation is done
            // The private key is the first element in the array
            privateKey = keyPair[0]
            // The public key is the second and third elements in the array
            publicKey = ECCPoint(keyPair[1], keyPair[2])
            // Compress the public key
            val compressedPublicKey: String = toHex(ECCOperations.compressPoint(publicKey))
            dataSource.getKeyTransaction(
                serial = device.getSerial(),
                appVersion = BuildConfig.VERSION_NAME,
                nii = dataSource.getNii(), compressedPublicKey, hashCode
            )

            // create message for getting master key
        } catch (e: InterruptedException) {
            e.printStackTrace()
            return
        } catch (e: ExecutionException) {
            e.printStackTrace()
            return
        } finally {
            eccParallel.shutdown()
        }


    }

    override suspend fun keyInjection() {
////getKeyTransaction("274479")
        device.clearMasterKey()
        device.clearMacKey()
        device.writeMasterKey(BytesUtil.hexString2Bytes("3272A9DB5E07D848337D00C275CCC58A"))
        device.writeMacKey(BytesUtil.hexString2Bytes("3272A9DB5E07D848337D00C275CCC58A"))

//        device.clearMacKey()
//        device.clearMasterKey()
//        device.writeTekKey(BytesUtil.hexString2Bytes("3272A9DB5E07D848337D00C275CCC58A"))
//        device.writeMacKey(
//            BytesUtil.hexString2Bytes("3272A9DB5E07D848337D00C275CCC58A"),
//            isLogon = true
//        )
    }

    override suspend fun logon(): ResponseData<ResponseTransaction> {
        if (dataSource.hasConnectionSettings()) {
            val logonTransactionResponse = dataSource.logon(
                serial = device.getSerial(),
                appVersion = BuildConfig.VERSION_NAME,
                nii = dataSource.getNii(),
            )
            if (logonTransactionResponse is ResponseData.Success) {
                dataSource.storeTerminalId((logonTransactionResponse.data as BaseTransactionResponse.LogonTransactionResponse).terminalId.trim())
                device.writePinKey(logonTransactionResponse.data.pinKey)
                device.writeDataKey(logonTransactionResponse.data.dataKey)
                device.writeMacKey(logonTransactionResponse.data.macKey)
                return ResponseData.Success(logonTransactionResponse.data.toResponseTransaction())
            } else {
                return ResponseData.Error(error = logonTransactionResponse.data?.responseMessage)
            }
        } else {
            return ResponseData.Error(error = "تنظیمات ارتباطی وجود ندارد لطفا به قسمت تنظیمات اتصال بروید و  تنظیمات را تعیین کنید")
        }

    }

    override suspend fun init(): ResponseData<BaseTransactionResponse.InitTransactionResponse> {
        val initTransactionResponse = dataSource.init(
            terminalId = "01396009",
            serial = device.getSerial(),
            appVersion = BuildConfig.VERSION_NAME,
            nii = "211"
        )
//        println("aaaaaaaaa->${initTransactionResponse.data!!.merchantId}")//012008521
//        println("aaaaaaaaa->${initTransactionResponse.data.merchantPhone}")//89122202
//        println("aaaaaaaaa->${initTransactionResponse.data.merchantName}")//توسعه توتا
        if (initTransactionResponse is ResponseData.Success) {
            dataSource.storeMerchant(
                (initTransactionResponse.data as BaseTransactionResponse.InitTransactionResponse).merchantId.trim(),
                initTransactionResponse.data.merchantPhone?.trim() ?: "",
                initTransactionResponse.data.merchantName?.trim() ?: ""
            )
            return initTransactionResponse
        } else {
            return initTransactionResponse
        }


    }

    override suspend fun balance(
        track2: String,
        pinBlock: String
    ): ResponseData<ResponseTransaction> {
        return withContext(Dispatchers.IO) {
            val string: List<String> = track2.split("=")
            val pan = string[0]
            val result = dataSource.balance(
                pan = pan,
                terminalId = "12010164",
                serial = device.getSerial(),
                appVersion = BuildConfig.VERSION_NAME,
                nii = dataSource.getNii(),
                terminalLanguage = "0",
                terminalConnectionType = dataSource.getTerminalConnectionType(),
                terminalType = dataSource.getTerminalType(),
                track2 = track2,
                pinBlock = pinBlock,
                merchantId = "01150407",
                currency = dataSource.getCurrency(),
                POS = "021",
            )
            val merchant = dataSource.getMerchant()
            println("hhhhhhhhhhhhhhh${merchant.toString()}")
            println("hhhhhhhhhashhhhhhh${Gson().toJson(result)}")
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
       return withContext(Dispatchers.IO){
           val string: List<String> = track2.split("=")
           val pan = string[0]
           val result = dataSource.purchase(
               amount = amount,
               pan = pan,
               terminalId = "12010164",
               serial = device.getSerial(),
               appVersion = BuildConfig.VERSION_NAME,
               nii = "211",
               terminalLanguage = "0",
               terminalConnectionType = "2",
               terminalType = "2",
               track2 = track2,
               pinBlock = pinBlock,
               merchantId = "01150407",
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
        pinBlock: String,
    ): ResponseData<ResponseTransaction> {
        val string: List<String> = track2.split("=")
        val p = string[0]
        val result = dataSource.voucher(amount = amount,
            pan = p,
            terminalId = "12010164",
            serial = device.getSerial(),
            appVersion = BuildConfig.VERSION_NAME,
            nii = "211",
            terminalLanguage = "0",
            terminalConnectionType = "2",
            terminalType = "2",
            track2 = track2,
            pinBlock = pinBlock,
            merchantId = "01150407",
            currency = "364",
            POS = "021", requestDecryptData = { device.requestDecryptData(data = it) }
        )
        val merchant = dataSource.getMerchant()
        println("hhhhhhhhhhhhhhh${merchant.toString()}")
        return if (result is ResponseData.Success) {
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
                    transactionType = "خرید کد شارژ",
                    date = result.data.date,
                    time = result.data.time.formatTime(),
                    issuerName = result.data.issuerName,
                    amount = amount, voucherPin = result.data.voucherPin, maskedPan = p.mask()
                    //  voucherPin=result.data.,
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

    override suspend fun topup(
        amount: String,
        mobile: String,
        productCode: String,
        track2: String,
        pinBlock: String
    ): ResponseData<ResponseTransaction> {
        val string: List<String> = track2.split("=")
        val p = string[0]
        val result = dataSource.topup(amount = amount,
            mobile = mobile,
            productCode = productCode,
            pan = p,
            terminalId = "12010164",
            serial = device.getSerial(),
            appVersion = BuildConfig.VERSION_NAME,
            nii = "211",
            terminalLanguage = "0",
            terminalConnectionType = "2",
            terminalType = "2",
            track2 = track2,
            pinBlock = pinBlock,
            merchantId = "01150407",
            currency = "364",
            POS = "021", requestDecryptData = { device.requestDecryptData(data = it) }
        )
        val merchant = dataSource.getMerchant()
        println("hhhhhhhhhhhhhhh${merchant.toString()}")
        return if (result is ResponseData.Success) {
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
                    //  voucherPin=result.data.,
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

    override suspend fun readCard(onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        Log.d("TAG", "readCard() called with: onSuccess = $onSuccess, onError = $onError")
        device.readCard(onSuccess, onError)
    }

    override suspend fun getPinBlock(
        pan: String,
        onError: (String) -> Unit,
        onInput: (Int) -> Unit,
        onConfirm: (String) -> Unit,
        onCanecl: () -> Unit,
        onTimeOut: () -> Unit
    ) {
        device.getPinBlock(
            pan,
            onError = onError,
            onInput = onInput,
            onConfirm = onConfirm,
            onCanecl = onCanecl,
            onTimeOut = onTimeOut
        )
    }

    override suspend fun billPay(
        billID: String,
        payId: String
    ) {
        dataSource.billInquery(
            terminalId = "12010164",
            serial = device.getSerial(),
            appVersion = BuildConfig.VERSION_NAME,
            nii = "211",
            terminalLanguage = "0",
            terminalConnectionType = "2",
            terminalType = "2",
            merchantId = "01150407",
            billID = billID, payId = payId
        )
    }

    override fun confirmConnectionSettings(ip: String, port: String, nii: String) {
        dataSource.saveConnectionSettings(ip, port, nii)
    }

    override suspend fun getLastTransaction(): ResponseTransaction? {
        return withContext(Dispatchers.IO) {
            val lastTransaction = dataSource.getLastTransaction()
            if (lastTransaction == null) null
            else ResponseTransaction(
                responseCode = lastTransaction.processingCode,
                responseMessage = lastTransaction.responseMsg ?: "",
                rrn = lastTransaction.rrn ?: "",
                trace = lastTransaction.stan,
                merchantName = lastTransaction.merchantId,// TODO:
                merchantId = lastTransaction.merchantId,// TODO:
                merchantPhone = lastTransaction.merchantId,// TODO:
                terminalID = lastTransaction.merchantId,// TODO:
                transactionType = lastTransaction.type.toString(),// TODO:
                date = lastTransaction.dateTransaction,// TODO: ,
                time = lastTransaction.timeTransaction,// TODO:
                issuerName = lastTransaction.issuer ?: "",// TODO:
                amount = lastTransaction.amount,// TODO:
                availableBalance = lastTransaction.merchantId,// TODO:
                maskedPan = lastTransaction.maskedPan ?: "",
                realBalance = null, voucherPin = null
            )
        }
    }

    override suspend fun geTransactionBasedStan(stan: String): ResponseTransaction? {
        return withContext(Dispatchers.IO) {
            val lastTransaction = dataSource.geTransactionBasedStan(stan)
            if (lastTransaction == null) null
            else ResponseTransaction(
                responseCode = lastTransaction.processingCode,
                responseMessage = lastTransaction.responseMsg ?: "",
                rrn = lastTransaction.rrn ?: "",
                trace = lastTransaction.stan,
                merchantName = lastTransaction.merchantId,// TODO:
                merchantId = lastTransaction.merchantId,// TODO:
                merchantPhone = lastTransaction.merchantId,// TODO:
                terminalID = lastTransaction.merchantId,// TODO:
                transactionType = lastTransaction.type.toString(),// TODO:
                date = lastTransaction.dateTransaction,// TODO: ,
                time = lastTransaction.timeTransaction,// TODO:
                issuerName = lastTransaction.issuer ?: "",// TODO:
                amount = lastTransaction.amount,// TODO:
                availableBalance = lastTransaction.merchantId,// TODO:
                maskedPan = lastTransaction.maskedPan ?: "",
                realBalance = null, voucherPin = null
            )
        }

    }

    override fun getIP(): String {
        return dataSource.getIP()
    }

    override fun getNii(): String {
        return dataSource.getNii()

    }

    override fun getPort(): String {
        return dataSource.getPort()
    }

    override fun isConfigured(): Boolean {
        return true
        var flag = true
        val merchant = dataSource.getMerchant()
        if (merchant == null)
            flag = false
        if (merchant.merchantName.isNullOrEmpty())
            flag = false
        return flag
    }

    override suspend fun print(bitmap: Bitmap, cotext: Context) {
        coroutineScope.launch {
            device.print(bitmap, cotext)
        }
    }

    override suspend fun settlementReverse() {
        dataSource.settlementReverse()
    }
}

private fun BaseTransactionResponse.PurchaseTransactionResponse.toResponseTransaction(
    merchant: Merchant,
    terminalId: String
): ResponseTransaction? {
    return ResponseTransaction(
        responseCode = this.responseCode.toString(),
        responseMessage = this.responseMessage ?: "",
        rrn = this.rrn ?: "",
        trace = this.trace,
        merchantName = merchant.merchantName ?: "",
        merchantId = merchant.merchantId ?: "",
        merchantPhone = merchant.merchantPhone ?: "",
        terminalID = terminalId,
        transactionType = "خرید",
        date = this.date,
        time = this.time.formatTime(),
        issuerName = this.issuerName,
        amount = this.amount, maskedPan = this.maskedPan
    )
}
