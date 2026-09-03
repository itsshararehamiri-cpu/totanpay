package com.example.totanpay.data.repository.datasource.transaction.response


/*val respCode: Int,
val respMessage: String?,
val reasonCode: Int? = null,
var date: String,
var time: String,
var trace: String,
val cardIssuer: String? = null,
var maskedPan: String? = null,
var amount : String? = null,
var rrn : String? = null*/
//
class FailedTransactionResponse(
    stan: String,
    responseCode: Int,
    responseMessage: Int?,
    reasonCode: Int? = null,
    date: String,
    time: String,
    val maskedPan: String? = null,
    val amount: String? = null,
    rrn: String? = null,
    val cardIssuer: String? = null,
    posCode:String?
    , dateTimeOfServer:String?=null
) : BaseTransactionResponse(
    responseCode = responseCode,
    responseMessage = responseMessage,
    reasonCode = reasonCode,
    date = date,
    time = time,
    trace = stan.toString(),
    rrn = rrn,
    posCode=posCode,
    dateTimeOfServer = dateTimeOfServer

) {

}

fun FailedTransactionResponse.toPurchaseTransactionResponse(responseMessage:Int): BaseTransactionResponse.PurchaseTransactionResponse? {
    return BaseTransactionResponse.PurchaseTransactionResponse(
        responseCode = this.responseCode,
        responseMessage = responseMessage,
        reasonCode = this.responseCode,
        date = this.date,
        time = this.time,
        trace = this.trace,
        rrn = this.rrn,
        amount = this.amount ?: "",
        maskedPan = this.maskedPan ?: "",
        issuerName = this.cardIssuer ?: "", posCode = this.posCode ?: "",
        dateTimeOfServer = this.dateTimeOfServer
    )
}

fun FailedTransactionResponse.toLogonTransactionResponse(responseMessage:Int): BaseTransactionResponse.LogonTransactionResponse? {
    return BaseTransactionResponse.LogonTransactionResponse(
        responseCode = this.responseCode,
        responseMessage = responseMessage,
        reasonCode = this.reasonCode,
        date = this.date,
        time = this.time,
        trace = this.trace,
        rrn = this.rrn,
        dataKey = ByteArray(0),
        pinKey = ByteArray(0),
        macKey = ByteArray(0),
        terminalId = "", dateTimeOfServer = this.dateTimeOfServer
    )
}

fun FailedTransactionResponse.toInitTransactionResponse(responseMessage:Int): BaseTransactionResponse.InitTransactionResponse? {
    return BaseTransactionResponse.InitTransactionResponse(
        responseCode = this.responseCode,
        responseMessage =responseMessage,
        reasonCode = this.reasonCode,

        date = this.date,
        time = this.time,
        trace = this.trace,
        rrn = this.rrn,
        merchantId = "",
        merchantPhone = "",
        posCode = this.posCode ?: "",
        dateTimeOfServer = this.dateTimeOfServer ?: "",
        merchantName = "", accountMerchants = null,
        englishMerchantName = ""
    )
}

fun FailedTransactionResponse.toBalanceTransactionResponse(responseMessage: Int): BaseTransactionResponse.BalanceTransactionResponse? {
    return BaseTransactionResponse.BalanceTransactionResponse(
        responseCode = this.responseCode,
        responseMessage =responseMessage,
        reasonCode = this.reasonCode,
        date = this.date,
        time = this.time,
        trace = this.trace,
        rrn = this.rrn,
        balance = "",
        maskedPan = this.maskedPan ?: "",
        issuerName = "",
        availableBalance = "",
        currency = "", posCode = this.posCode, dateTimeOfServer = this.dateTimeOfServer
    )
}

fun FailedTransactionResponse.toVoucherTransactionResponse(responseMessage:Int): BaseTransactionResponse.VoucherTransactionResponse? {
    return BaseTransactionResponse.VoucherTransactionResponse(
        responseCode = this.responseCode,
        responseMessage = responseMessage,
        reasonCode = this.responseCode,
        date = this.date,
        time = this.time,
        trace = this.trace,
        rrn = this.rrn,
        maskedPan = this.maskedPan ?: "",
        issuerName = this.cardIssuer ?: "",
        amount = this.amount ?: "",
        groupVoucherData = HashMap(),
        voucherSerial = "",
        voucherPin = "",
        voucherPINEncrypted = "",
        groupVoucherDataEncrypted = HashMap(), posCode = this.posCode,
        dateTimeOfServer = this.dateTimeOfServer
    )
}

fun FailedTransactionResponse.toTopupTransactionResponse(responseMessage:Int): BaseTransactionResponse.TopUpTransactionResponse? {
    return BaseTransactionResponse.TopUpTransactionResponse(
        responseCode = this.responseCode,
        responseMessage = responseMessage,
        reasonCode = this.responseCode,
        date = this.date,
        time = this.time,
        trace = this.trace,
        rrn = this.rrn,
        maskedPan = this.maskedPan ?: "",
        issuerName = this.cardIssuer ?: "",
        amount = this.amount ?: "", posCode = this.posCode, dateTimeOfServer = this.dateTimeOfServer
    )
}

/*
    var responseCode: Int=-1,
    var responseMessage: String?=null,
    var reasonCode: Int? = null,
    var date: String="",
    var time: String="",
    var trace: String="",
    var rrn : String? = null
 */
fun FailedTransactionResponse.toBillPayTransactionResponse(responseMessage:Int): BaseTransactionResponse.BillPayTransactionResponse? {
    return BaseTransactionResponse.BillPayTransactionResponse(
        responseCode = this.responseCode,
        responseMessage = responseMessage,
        reasonCode = this.reasonCode,
        date = this.date,
        time = this.time,
        trace = this.trace,
        rrn = this.rrn,
        maskedPan = "",
        issuerName = "",
        amount = "", dateTimeOfServer = this.dateTimeOfServer
    )
}

fun FailedTransactionResponse.toBillInquiryTransactionResponse(responseMessage:Int): BaseTransactionResponse.BillInquiryTransactionResponse? {
    return BaseTransactionResponse.BillInquiryTransactionResponse(
        responseCode = this.responseCode,
        responseMessage = responseMessage,
        reasonCode = this.reasonCode,
        date = this.date,
        time = this.time,
        trace = this.trace,
        rrn = "",
        billType = "",
        serviceDesc = "", amount = "", posCode = this.posCode,
        englishServiceDesc = "",
        dateTimeOfServer = this.dateTimeOfServer
    )
}
