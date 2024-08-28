package com.example.totanpay.data.repository.datasource.transaction.response

import com.example.totanpay.data.repository.datasource.model.Merchant
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.example.totanpay.data.repository.datasource.transaction.TransactionType


fun BaseTransactionResponse.LogonTransactionResponse.toResponseTransaction(): ResponseTransaction {
    return ResponseTransaction( responseCode=this.responseCode.toString(),
     responseMessage=this.responseMessage?:"",
     rrn=this.rrn?:"",
     trace=this.trace,
     merchantName ="",
     merchantId="",
     merchantPhone="",
     terminalID=this.terminalId,
     transactionType=TransactionType.LOGON.title,
     date=this.date,
     time=this.time,
     issuerName="",
     amount="",
     availableBalance=null
    , maskedPan="",
     realBalance=null, voucherPin=null)
}
fun BaseTransactionResponse.BalanceTransactionResponse.toResponseTransaction(merchant:Merchant,terminalId: String): ResponseTransaction {
 return ResponseTransaction( responseCode=this.responseCode.toString(),
  responseMessage=this.responseMessage?:"",
  rrn=this.rrn?:"",
  trace=this.trace,
  merchantName =merchant.merchantName?:"",
  merchantId=merchant.merchantId?:"",
  merchantPhone=merchant.merchantPhone?:"",
  terminalID=terminalId,
  transactionType=TransactionType.LOGON.title,
  date=this.date,
  time=this.time,
  issuerName=this.issuerName,
  amount="",
  availableBalance=this.availableBalance
  , maskedPan=this.maskedPan,
  realBalance=null, voucherPin=null)
}



fun BaseTransactionResponse.VoucherTransactionResponse.toResponseTransaction(merchant:Merchant,terminalId: String): ResponseTransaction {
 return ResponseTransaction( responseCode=this.responseCode.toString(),
  responseMessage=this.responseMessage?:"",
  rrn=this.rrn?:"",
  trace=this.trace,
  merchantName =merchant.merchantName?:"",
  merchantId=merchant.merchantId?:"",
  merchantPhone=merchant.merchantPhone?:"",
  terminalID=terminalId,
  transactionType=TransactionType.LOGON.title,
  date=this.date,
  time=this.time,
  issuerName=this.issuerName,
  amount=this.amount,
  availableBalance=""
  , maskedPan=this.maskedPan,
  realBalance=null, voucherPin=this.voucherPin,voucherSerial=this.voucherSerial)
}
fun BaseTransactionResponse.TopupTransactionResponse.toResponseTransaction(merchant:Merchant,terminalId:String): ResponseTransaction {
 return ResponseTransaction( responseCode=this.responseCode.toString(),
  responseMessage=this.responseMessage?:"",
  rrn=this.rrn?:"",
  trace=this.trace,
  merchantName =merchant.merchantName?:"",
  merchantId=merchant.merchantId?:"",
  merchantPhone=merchant.merchantPhone?:"",
  terminalID=terminalId,
  transactionType=TransactionType.TOPUP.title,
  date=this.date,
  time=this.time,
  issuerName=this.issuerName,
  amount=this.amount,
  availableBalance=""
  , maskedPan=this.maskedPan,
  realBalance=null, voucherPin=null,voucherSerial=null)
}