package com.example.totanpay.data.entity

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.totanpay.data.repository.datasource.transaction.TransactionInQueue

@Entity(
    tableName = "transaction_queue"
)
data class TransactionQueueEntity(
    @NonNull @PrimaryKey var dateTime: String,
    var date: String,
    var time: String,
    var status: Char,
    var printed: Boolean,
    var processingCode: String,
    var amount: String,
    var stan: String,
    var merchantId: String,
    var maskedPan: String?,
    var type: Int,
    var rrn: String?,
    var issuer: String?,
    var responseCode: Int?,
    var responseMsg: String?,
    var terminalId: String,
    var posConditionCode:String,
    var currency:String
//    var billId:String?=null,
//    var payId:String?=null,
//    var serviceDesc:String?=null,
//    var pinVoucher:String?=null,
//    var serialVoucher:String?=null,
//    var mobileNumber:String?=null,
//    var operatorCode:Int?=null
) {

}

/*
 var id: Long ,
    var processingCode: String ,
    var amount: String ,
    var stan: String ,
    var dateTransaction: String ,
    var timeTransaction: String ,
    var merchantId: String ,
    var maskedPan:String?,var type:Int,
    var rrn: String?,
    var issuer:String?,
    var responseCode:Int?,
    var responseMsg:String?,var billId:String?=null,var payId:String?=null,var serviceDesc:String?=null,var pinVoucher:String?=null,
    var serialVoucher:String?=null,var mobileNumber:String?=null,var operatorCode:Int?=null
 */
fun TransactionQueueEntity.toTransactionInQueue(): TransactionInQueue? {
    return TransactionInQueue(
        dateTime = this.dateTime,
        date = this.date,
        time = this.time,
        status = this.status,
        printed = this.printed,
        processingCode = this.processingCode,
        amount = this.amount,
        stan = this.stan,
        merchantId = this.merchantId,
        maskedPan = this.maskedPan,
        type = this.type,
        rrn = this.rrn,
        issuer = this.issuer,
        responseCode = this.responseCode,
        responseMsg = this.responseMsg
    )
}