package com.example.totanpay.data.repository.datasource.transaction


import android.util.Log
import com.example.totanpay.data.repository.datasource.transaction.connection.IConnection
import com.example.totanpay.data.repository.datasource.transaction.request.PurchaseTransactionRequest
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.FaildTransactionResponse
import com.google.gson.Gson
import org.jpos.iso.ISOUtil

class PurchaseTransaction(
    request: PurchaseTransactionRequest,
    private val iMacGenerator: IMacGenerator,
    iConnection: IConnection,
    saveReverseData: suspend (msg: IsoMessage) -> Unit,
    saveTransactionLog: suspend (msg: IsoMessage) -> Unit,
    sendTransactionInQueue: suspend () -> Boolean,
    updateTransaction: suspend (date: String, time: String,pan:String,cardIssuer:String,responseCode:String,amount:String,rrn:String?,
                                serviceDesc:String?, pinVoucher:String?, serialVoucher:String?,mobileNumber:String?,operatorCode:Int?) -> Unit,
    private val setStatusToSettle: suspend (date: String, time: String) -> Unit,
    private val setStatusToReverse: suspend (date: String, time: String) -> Unit,

    ) : BaseTransaction(
    request,
    iConnection,
    saveReverseData,
    saveTransactionLog,
    sendTransactionInQueue,
    updateTransaction = updateTransaction
) {
    override val isReversible: Boolean
        get() = true
    override val type: Int
        get() = TransactionType.PURCHASE.tag
    override val needReport: Boolean
        get() = true

    override suspend fun buildMessage() {
        with(sendMessage) {
            mti = "0200"
            processCode = "000000"
            stan = request.stan.toString()
            pan = (request as PurchaseTransactionRequest).pan
            amount = request.amount
            setTrack2(request.track2)
            setCurrency(request.currency)
            setPOS(request.POS)
            setPinBlock(request.pinBlock)
            setMerchantId(request.merchantId)
            setDateTime(request.date, request.time)
            setNii(request.nii)
            setTerminalId(request.terminalId)
            setField48 {
                setSerial(request.serial)
                setVersion(request.appVersion)
            }
            tranTime = getString(12)
            tranDate = getString(13)
            iMacGenerator?.getMac(this)
        }
    }

    override suspend fun onSuccess(receivedIsoMessage: IsoMessage): BaseTransactionResponse {
        val cardIssuer = receivedIsoMessage.getField48Tag(0x38)?.split("\\")?.get(0) ?: ""
        setStatusToSettle(sendMessage.tranDate, sendMessage.tranTime)
        // TODO:
        receivedIsoMessage.dump(System.out, ">>")
        println("kkkkkkkkkkkkk->$cardIssuer")
        var s = ""
        if (receivedIsoMessage.hasField(47)) {
            if (receivedIsoMessage.getBytes(47) != null) s =
                ISOUtil.hexString(
                    receivedIsoMessage.getBytes(
                        47
                    )
                )
        }
//        updateTransaction(
//            sendMessage.tranDate,
//            sendMessage.tranTime,
//            resp.rrn ?: "",
//            cardIssuer,
//            resp.respCode,
//            s
//        )
        // TODO:
        updateTransaction(
            sendMessage.tranDate,
            sendMessage.tranTime,
            (request as PurchaseTransactionRequest).pan,
            cardIssuer,
            receivedIsoMessage.respCode.toString(),
            request.amount,
            receivedIsoMessage.rrn ?: "",
            null,
            null,null,
            null,
            null
        )
        return BaseTransactionResponse.PurchaseTransactionResponse(
            amount = sendMessage.getString(4),
            issuerName = cardIssuer,
            responseCode = receivedIsoMessage.respCode,
            responseMessage = null,
            reasonCode = null,
            date = sendMessage.tranDate,
            time = sendMessage.tranTime,
            maskedPan = sendMessage.pan,
            trace = sendMessage.stan, rrn = receivedIsoMessage.rrn
        )
    }

    override suspend fun onFail(receivedIsoMessage: IsoMessage?): FaildTransactionResponse {
        Log.d("TAG", "onFail() called with: receivedIsoMessage = $receivedIsoMessage")
       sendMessage.dump(System.out,"><")
        if (receivedIsoMessage == null) {
            updateTransaction(
             request.date,
                request.time,
                (request as PurchaseTransactionRequest).pan,
                "",
                "-1",
                (request as PurchaseTransactionRequest).amount,
                null,
                null,
                null,
                null,
                null,
                null,
            )
            println("ooooooooopo")
            return FaildTransactionResponse(
                stan = sendMessage.stan.toInt(),
                responseCode = -1,
                responseMessage = "خطا در دریافت اطلاعات",
                reasonCode = null,
                date = sendMessage.tranDate,
                time = sendMessage.tranTime,
                maskedPan = sendMessage.pan,
                amount = request.amount
            )
        } else {
//            if (receivedIsoMessage.respCode != 80)
//                clearTransactionFromQueue(sendMessage.tranDate, sendMessage.tranTime)
            // TODO:
            val cardIssuer = receivedIsoMessage.getField48Tag(0x38)?.split("\\")?.get(0) ?: ""
            updateTransaction(
                request.date,
                request.time,
                cardIssuer,cardIssuer,
                receivedIsoMessage.respCode.toString(),
                (request as PurchaseTransactionRequest).amount,receivedIsoMessage.rrn,null,null,null,null,null
            )
            val v= FaildTransactionResponse(
                responseCode = receivedIsoMessage.respCode,
                reasonCode = null,
                rrn = receivedIsoMessage.rrn ?: "",
                stan = receivedIsoMessage.stan.toInt(),
                date = request.date,
                time = request.time,
                maskedPan = request.pan,
                amount = request.amount,
                responseMessage = ""

            )
            println("uyyyyyyyyyyyyyyyyiu->${Gson().toJson(request)}")

            println("uyyyyyyyyyyyyyyyyiu->${Gson().toJson(v)}")
return v
        }
    }


}

