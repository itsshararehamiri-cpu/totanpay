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
class FaildTransactionResponse( stan:Int, responseCode:Int, responseMessage:String?,reasonCode:Int?=null,date:String,time:String,
    val maskedPan:String?=null,val amount:String?=null,rrn:String?=null,val cardIssuer: String? = null
                               ):BaseTransactionResponse(
     responseCode=responseCode,
 responseMessage=responseMessage,
 reasonCode=reasonCode,
 date=date,
 time=time,
 trace=stan.toString(),
 rrn =rrn

) {

}
fun FaildTransactionResponse.toPurchaseTransactionResponse(): BaseTransactionResponse.PurchaseTransactionResponse? {
    return BaseTransactionResponse.PurchaseTransactionResponse(
        responseCode = this.responseCode,
        responseMessage = this.responseMessage,
        reasonCode = this.responseCode,
        date = this.date,
        time = this.time,
        trace = this.trace,
        rrn = this.rrn,
        amount = this.amount?:"",
        maskedPan = this.maskedPan?:"",
        issuerName = this.cardIssuer?:""
    )
}
fun FaildTransactionResponse.toBalanceTransactionResponse(): BaseTransactionResponse.BalanceTransactionResponse? {
    return BaseTransactionResponse.BalanceTransactionResponse(
        responseCode = this.responseCode,
        responseMessage = this.responseMessage,
        reasonCode = this.responseCode,
        date = this.date,
        time = this.time,
        trace = this.trace,
        rrn = this.rrn,
        balance = "",
        maskedPan = this.maskedPan?:"",
        issuerName = "",
        availableBalance = "",
        currency = ""
    )
}
fun FaildTransactionResponse.toVoucherTransactionResponse(): BaseTransactionResponse.VoucherTransactionResponse? {
    return BaseTransactionResponse.VoucherTransactionResponse(
        responseCode = this.responseCode,
        responseMessage = this.responseMessage,
        reasonCode = this.responseCode,
        date = this.date,
        time = this.time,
        trace = this.trace,
        rrn = this.rrn,
        maskedPan = this.maskedPan?:"",
        issuerName = this.cardIssuer?:"",
        amount = this.amount?:"",
        groupVoucherData = HashMap(),
        voucherSerial = "",
        voucherPin = "",
        voucherPINEncrypted = "",
        groupVoucherDataEncrypted = HashMap()
    )
}
fun FaildTransactionResponse.toTopupTransactionResponse(): BaseTransactionResponse.TopupTransactionResponse? {
    return BaseTransactionResponse.TopupTransactionResponse(
        responseCode = this.responseCode,
        responseMessage = this.responseMessage,
        reasonCode = this.responseCode,
        date = this.date,
        time = this.time,
        trace = this.trace,
        rrn = this.rrn,
        maskedPan = this.maskedPan?:"",
        issuerName = this.cardIssuer?:"",
        amount = this.amount?:""
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
fun FaildTransactionResponse.toBillPayTransactionResponse(): BaseTransactionResponse.BillPayTransactionResponse? {
    return BaseTransactionResponse.BillPayTransactionResponse(
        responseCode = this.responseCode,
        responseMessage = this.responseMessage,
        reasonCode = this.reasonCode,
        date = this.date,
        time = this.time,
        trace = this.trace,
        rrn = this.rrn,
        maskedPan = "",
        issuerName = "",
        amount = "",
    )
}
