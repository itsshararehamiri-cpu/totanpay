package com.example.totanpay.data.repository.datasource.transaction

import com.example.totanpay.data.repository.datasource.transaction.connection.IConnection
import com.example.totanpay.data.repository.datasource.transaction.request.BillPayTransactionRequest
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse
import com.example.totanpay.data.repository.datasource.transaction.response.FaildTransactionResponse
import org.jpos.iso.ISOUtil

class BillPayTransaction(
    request: BillPayTransactionRequest, private val iMacGenerator: IMacGenerator,
    iConnection: IConnection,
    saveReverseData: suspend (msg: IsoMessage) -> Unit,
    saveTransactionLog: suspend (msg: IsoMessage) -> Unit,
    updateTransaction: suspend (
        date: String, time: String, pan: String, cardIssuer: String, responseCode: String, amount: String, rrn: String?,
        serviceDesc: String?, pinVoucher: String?, serialVoucher: String?, mobileNumber: String?, operatorCode: Int?
    ) -> Unit, sendTransactionInQueue: suspend () -> Boolean,
    private val setStatusToSettle: suspend (date: String, time: String) -> Unit,

    ) : BaseTransaction(request, iConnection, saveReverseData, saveTransactionLog,sendTransactionInQueue,updateTransaction) {
    override val isReversible: Boolean
        get() = true
    override val type: Int
        get() = TransactionType.PURCHASE.tag
    override val needReport: Boolean
        get() = true

    override suspend fun buildMessage() {
        with(sendMessage) {
            mti = "0200"
            processCode = "170000"
            stan = request.stan.toString()
            pan = (request as BillPayTransactionRequest).pan
            amount = request.amount
            setTrack2(request.track2)
            setCurrency(request.currency)
            setPOS(request.POS)
            setPinBlock(request.pinBlock)
            setMerchantId(request.merchantId)
            setDateTime(request.date, request.time)
            setNii(request.nii)
            setTerminalId(request.terminalId!!)
            setField48 {
                setSerial(request.serial)
                setVersion(request.appVersion)
                setBillId(request.billId)
                setPayId(request.payId)
            }
            val serviceDesc = request.serviceDesc
            iMacGenerator?.getMac(this)
        }
    }

    override suspend fun  onSuccess(receivedIsoMessage: IsoMessage): BaseTransactionResponse {
        val cardIssuer = receivedIsoMessage.getField48Tag(0x38)?.split("\\")?.get(0) ?: ""
        setStatusToSettle(sendMessage.tranDate, sendMessage.tranTime)
        var s = ""
        if (receivedIsoMessage.hasField(47)) {
            if (receivedIsoMessage.getBytes(47) != null) s =
                ISOUtil.hexString(
                    receivedIsoMessage.getBytes(
                        47
                    )
                )
        }
        var ss = receivedIsoMessage.getField48Tag(0x44)?.split("\\")?.get(0) ?: ""
        updateTransaction(
            sendMessage.tranDate,
            sendMessage.tranTime,
            sendMessage.pan,
            cardIssuer,
            receivedIsoMessage.getString(39),
            sendMessage.amount,
            receivedIsoMessage.rrn ?: "",
            s,
            null,null,null,null
        )

        return BaseTransactionResponse.BillPayTransactionResponse(
            responseCode = 0,
            responseMessage = null,
            reasonCode = null,
            rrn = receivedIsoMessage.rrn ?: "",
            trace = sendMessage.stan,
            date = sendMessage.tranDate,
            time = sendMessage.tranTime,
            issuerName = cardIssuer,
            maskedPan = sendMessage.pan, amount = sendMessage.getString(4),
        )
    }

    override suspend fun onFail(receivedIsoMessage: IsoMessage?): FaildTransactionResponse {
        if (receivedIsoMessage == null)
        {
            /*
            date: String, time: String,pan:String,cardIssuer:String,responseCode:String,amount:String,rrn:String?,
                                               serviceDesc:String?, pinVoucher:String?, serialVoucher:String?,mobileNumber:String?,operatorCode:Int?
             */
            updateTransaction(sendMessage.tranDate, sendMessage.tranTime, request.appVersion,"","-1",
                (request as BillPayTransactionRequest).amount
                ,null,null,null,null,null,null)
//            return  BaseTransactionResponse.BillPayTransactionResponse(                (request as BillPayTransactionRequest).amount
//                    ,"",request.pan,
//                -1, "خطا در دریافت اطلاعات", null, sendMessage.tranDate, sendMessage.tranTime, sendMessage.stan,
//                null)
        }
        else {
            if (receivedIsoMessage.respCode != 80)
                clearTransactionFromQueue(sendMessage.tranDate, sendMessage.tranTime)
            val cardIssuer = receivedIsoMessage.getField48Tag(0x38)?.split("\\")?.get(0) ?: ""
            updateTransaction(sendMessage.tranDate,sendMessage.tranTime, (request as BillPayTransactionRequest).pan,
                cardIssuer,receivedIsoMessage.respCode.toString(),request.amount,receivedIsoMessage.rrn?:"",
                "","","","",0)
//            return   BaseTransactionResponse.BillPayTransactionResponse(request.amount, cardIssuer, request.pan,
//                receivedIsoMessage.respCode.toInt(),null, null, sendMessage.tranDate,sendMessage.tranTime,
//                sendMessage.stan, receivedIsoMessage.rrn)
        }
        return FaildTransactionResponse(// TODO:
            responseCode=-2, responseMessage="خطا در ارسال تراکنش تسویه-بازگشت", reasonCode=null,
            date=sendMessage.tranDate, time=sendMessage.tranTime, stan =sendMessage.stan.toInt()
        )
        /*
        amount:String,
                                     val issuerName:String,
                                     val maskedPan:String,
                                     responseCode: Int,
                                     responseMessage: String?,
                                     reasonCode: Int? = null,
                                     date: String,
                                     time: String,
                                     trace: String,
                                     rrn: String? = null
         */
//        return FaildTransactionResponse(
//            responseCode=-2, responseMessage="خطا در ارسال تراکنش تسویه/بازگشت", reasonCode=null,
//            date=sendMessage.tranDate, time=sendMessage.tranTime, stan =sendMessage.stan.toInt()
//        )
    }
}
