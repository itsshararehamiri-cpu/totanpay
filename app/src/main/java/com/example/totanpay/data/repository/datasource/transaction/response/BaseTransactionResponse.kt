package com.example.totanpay.data.repository.datasource.transaction.response
sealed class BaseTransactionResponse (
    var responseCode: Int=-1,
    var responseMessage: String?=null,
    var reasonCode: Int? = null,
    var date: String="p",
    var time: String="q",
    var trace: String="",
    var rrn : String? = null)
{
    class BalanceTransactionResponse(val balance:String,
                                     val availableBalance:String,
                                     val currency:String,
                                     val issuerName:String,
                                     val maskedPan:String,
                                     responseCode: Int,
                                     responseMessage: String?,
                                     reasonCode: Int? = null,
                                     date: String,
                                     time: String,
                                     trace: String,
                                     rrn: String? = null
    ) : BaseTransactionResponse(
        responseCode=responseCode,
        responseMessage=responseMessage,
        reasonCode=reasonCode,
        date=date,
        time=time,
        trace=trace,
        rrn=rrn
    )
    class BillInqueryTransactionResponse(val billType:String,
                                         val serviceDesc:String,
                                         amount:String,
                                         responseCode: Int,
                                         responseMessage: String?,
                                         reasonCode: Int? = null,
                                         date: String,
                                         time: String,
                                         trace: String,
                                         rrn: String? = null
    ) : BaseTransactionResponse(
        responseCode=responseCode,
        responseMessage=responseMessage,
        reasonCode=reasonCode,
        date=date,
        time=time,
        trace=trace,
        rrn=rrn
    )
    class VoucherTransactionResponse(val amount:String,
                                     val issuerName:String,
                                     val maskedPan:String,
                                     val voucherSerial:String,
                                     val voucherPin:String,
                                     val voucherPINEncrypted:String,
                                     val groupVoucherData:HashMap<String,String>,
                                     val groupVoucherDataEncrypted:HashMap<String,String>,                                  responseCode: Int,
                                     responseMessage: String?,
                                     reasonCode: Int? = null,
                                     date: String,
                                     time: String,
                                     trace: String,
                                     rrn: String? = null
    ) : BaseTransactionResponse(
        responseCode=responseCode,
        responseMessage=responseMessage,
        reasonCode=reasonCode,
        date=date,
        time=time,
        trace=trace,
        rrn=rrn
    ) {

    }
    class TopupTransactionResponse(val amount:String,
                                   val issuerName:String,
                                   val maskedPan:String,
                                   responseCode: Int,
                                   responseMessage: String?,
                                   reasonCode: Int? = null,
                                   date: String,
                                   time: String,
                                   trace: String,
                                   rrn: String? = null,
    ) : BaseTransactionResponse(
        responseCode=responseCode,
        responseMessage=responseMessage,
        reasonCode=reasonCode,
        date=date,
        time=time,
        trace=trace,
        rrn=rrn
    ) {

    }

    class BillPayTransactionResponse(amount:String,
                                     val issuerName:String,
                                     val maskedPan:String,
                                     responseCode: Int,
                                     responseMessage: String?,
                                     reasonCode: Int? = null,
                                     date: String,
                                     time: String,
                                     trace: String,
                                     rrn: String? = null
    ) : BaseTransactionResponse(
        responseCode=responseCode,
        responseMessage=responseMessage,
        reasonCode=reasonCode,
        date=date,
        time=time,
        trace=trace,
        rrn=rrn
    )
    class InitTransactionResponse(val merchantPhone:String?,
                                  val merchantId:String,
                                  val merchantName:String?,
                                  responseCode: Int,
                                  responseMessage: String?,
                                  reasonCode: Int? = null,
                                  date: String,
                                  time: String,
                                  trace: String,
                                  rrn: String? = null
    ) : BaseTransactionResponse(
        responseCode=responseCode,
        responseMessage=responseMessage,
        reasonCode=reasonCode,
        date=date,
        time=time,
        trace=trace,
        rrn=rrn
    ) {
    }
    class LogonTransactionResponse(
        val pinKey: ByteArray,
        val dataKey: ByteArray,
        val macKey: ByteArray,
        val terminalId: String,// TODO:is nullable>?
        responseCode: Int,
        responseMessage: String?,
        reasonCode: Int? = null,
        date: String,
        time: String,
        trace: String,
        rrn: String? = null
    ) : BaseTransactionResponse(
        responseCode=responseCode,
        responseMessage=responseMessage,
        reasonCode=reasonCode,
        date=date,
        time=time,
        trace=trace,
        rrn=rrn
    )
    class PurchaseTransactionResponse(val amount:String,
                                      val issuerName:String,
                                      val maskedPan:String,
                                      responseCode: Int,
                                      responseMessage: String?,
                                      reasonCode: Int? = null,
                                      date: String,
                                      time: String,
                                      trace: String,
                                      rrn: String? = null
    ) : BaseTransactionResponse(
        responseCode=responseCode,
        responseMessage=responseMessage,
        reasonCode=reasonCode,
        date=date,
        time=time,
        trace=trace,
        rrn=rrn
    )
    class SettlementReverseTransactionResponse(
        responseCode: Int,
        responseMessage: String?,
        reasonCode: Int? = null,
        date: String,
        time: String,
        trace: String,
        rrn: String? = null
    ) : BaseTransactionResponse(
        responseCode=responseCode,
        responseMessage=responseMessage,
        reasonCode=reasonCode,
        date=date,
        time=time,
        trace=trace,
        rrn=rrn
    )
}