package com.example.totanpay.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.totanpay.data.repository.datasource.transaction.TransactionLog

@Entity(
    tableName = "transaction_log"
)
data class TransactionLogEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    val timestamp:Long,
    var processingCode: String,
    var amount: Long,
    var stan: String,
    var dateTransaction: String,
    var timeTransaction: String,
    var merchantId: String,
    var maskedPan: String?,
    var type: Int,
    var rrn: String?,
    var issuer: String?,
    var responseCode: Int?,
    var responseMsg: Int?,
    var billId: String? = null,
    var payId: String? = null,
    var serviceDesc: String? = null,
    var pinVoucher: String? = null,
    var serialVoucher: String? = null,
    var mobileNumber: String? = null,
    var operatorCode: Int? = null,
    var terminalId: String,
    val merchantName: String = "",
    val merchantPhone: String? = null
) {

}

fun TransactionLogEntity.toTransaction(): TransactionLog? {
    return TransactionLog(
        id = this.id,
        processingCode = this.processingCode,
        amount = this.amount,
        stan = this.stan,
        dateTransaction = this.dateTransaction,
        timeTransaction = this.timeTransaction,
        merchantId = this.merchantId,
        maskedPan = this.maskedPan,
        type = this.type,
        rrn = this.rrn,
        issuer = this.issuer,
        responseCode = this.responseCode,
        responseMsg = this.responseMsg,
        billId = this.billId,
        payId = this.payId,
        serviceDesc = this.serviceDesc,
        pinVoucher = this.pinVoucher,
        serialVoucher = this.serialVoucher,
        mobileNumber = this.mobileNumber,
        operatorCode = this.operatorCode,
        terminalId=this.terminalId,
        merchantName=this.merchantName,
        merchantPhone=this.merchantPhone,
        timestamp=this.timestamp
    )
}

