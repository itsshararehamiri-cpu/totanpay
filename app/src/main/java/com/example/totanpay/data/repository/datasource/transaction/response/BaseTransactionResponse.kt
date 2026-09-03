package com.example.totanpay.data.repository.datasource.transaction.response

import com.example.totanpay.data.repository.datasource.model.Merchant
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.example.totanpay.data.repository.datasource.transaction.ResponseMessageContainer
import com.example.totanpay.data.repository.datasource.transaction.TransactionType
import com.example.totanpay.data.util.formatTime

sealed class BaseTransactionResponse (
    var responseCode: Int=-1,
    var responseMessage: Int?=null,
    var reasonCode: Int? = null,
    var date: String="p",
    var time: String="q",
    var trace: String="",
    var rrn : String? = null,val dateTimeOfServer: String?=null,val posCode:String?=null)
{
    class BalanceTransactionResponse(val balance:String,
                                     val availableBalance:String,
                                     val currency:String,
                                     val issuerName:String,
                                     val maskedPan:String,
                                     responseCode: Int,
                                     responseMessage: Int?,
                                     reasonCode: Int? = null,
                                     date: String,
                                     time: String,
                                     trace: String,
                                     rrn: String? = null,posCode:String?=null,dateTimeOfServer:String?=null,
    ) : BaseTransactionResponse(
        responseCode=responseCode,
        responseMessage=responseMessage,
        reasonCode=reasonCode,
        date=date,
        time=time,
        trace=trace,
        rrn=rrn, posCode = posCode, dateTimeOfServer = dateTimeOfServer
    )
    class BillInquiryTransactionResponse(val billType:String,
                                         val serviceDesc:String,
                                       val  englishServiceDesc: String,
                                         val amount:String?= null,
                                         responseCode: Int,
                                         responseMessage: Int?,
                                         reasonCode: Int? = null,
                                         date: String,
                                         time: String,
                                         trace: String,
                                         rrn: String? = null, posCode:String?=null,dateTimeOfServer:String?=null,
    ) : BaseTransactionResponse(
        responseCode=responseCode,
        responseMessage=responseMessage,
        reasonCode=reasonCode,
        date=date,
        time=time,
        trace=trace,
        rrn=rrn,posCode=posCode, dateTimeOfServer = dateTimeOfServer
    )
    class VoucherTransactionResponse(val amount:String,
                                     val issuerName:String,
                                     val maskedPan:String,
                                     val voucherSerial:String,
                                     val voucherPin:String,
                                     val voucherPINEncrypted:String,
                                     val groupVoucherData:HashMap<String,String>,
                                     val groupVoucherDataEncrypted:HashMap<String,String>,                                  responseCode: Int,
                                     responseMessage: Int?,
                                     reasonCode: Int? = null,
                                     date: String,
                                     time: String,
                                     trace: String,
                                     rrn: String? = null,posCode:String?=null,dateTimeOfServer:String?=null,

    ) : BaseTransactionResponse(
        responseCode=responseCode,
        responseMessage=responseMessage,
        reasonCode=reasonCode,
        date=date,
        time=time,
        trace=trace,
        rrn=rrn, posCode = posCode,dateTimeOfServer = dateTimeOfServer
    ) {

    }
    class TopUpTransactionResponse(val amount:String,
                                   val issuerName:String,
                                   val maskedPan:String,
                                   responseCode: Int,
                                   responseMessage: Int?,
                                   reasonCode: Int? = null,
                                   date: String,
                                   time: String,
                                   trace: String,
                                   rrn: String? = null, posCode:String?=null,dateTimeOfServer:String?=null,
    ) : BaseTransactionResponse(
        responseCode=responseCode,
        responseMessage=responseMessage,
        reasonCode=reasonCode,
        date=date,
        time=time,
        trace=trace,
        rrn=rrn,posCode=posCode,dateTimeOfServer = dateTimeOfServer
    ) {

    }

    class BillPayTransactionResponse(amount:String,
                                     val issuerName:String,
                                     val maskedPan:String,
                                     responseCode: Int,
                                     responseMessage: Int?,
                                     reasonCode: Int? = null,
                                     date: String,
                                     time: String,
                                     trace: String,
                                     rrn: String? = null,posCode:String?=null,dateTimeOfServer:String?=null,
    ) : BaseTransactionResponse(
        responseCode=responseCode,
        responseMessage=responseMessage,
        reasonCode=reasonCode,
        date=date,
        time=time,
        trace=trace,
        rrn=rrn,posCode=posCode,dateTimeOfServer = dateTimeOfServer
    )
    class InitTransactionResponse(val merchantPhone:String?,
                                  val merchantId:String,
                                  val merchantName:String?,
                                  val englishMerchantName:String?,
                                  responseCode: Int,
                                  responseMessage: Int?,
                                  reasonCode: Int? = null,
                                  date: String,
                                  time: String,
                                  trace: String,
                                  rrn: String? = null, dateTimeOfServer:String?=null,posCode:String?=null,
                                val accountMerchants:  List<AccountMerchant>?=null
    ) : BaseTransactionResponse(
        responseCode=responseCode,
        responseMessage=responseMessage,
        reasonCode=reasonCode,
        date=date,
        time=time,
        trace=trace,
        rrn=rrn,dateTimeOfServer=dateTimeOfServer,posCode
    ) {
    }
    class LogonTransactionResponse(
        val pinKey: ByteArray,
        val dataKey: ByteArray,
        val macKey: ByteArray,
        val terminalId: String,
        responseCode: Int,
        responseMessage: Int?,
        reasonCode: Int? = null,
        date: String,
        time: String,
        trace: String,
        rrn: String? = null,dateTimeOfServer: String?,posCode:String?=null
    ) : BaseTransactionResponse(
        responseCode=responseCode,
        responseMessage=responseMessage,
        reasonCode=reasonCode,
        date=date,
        time=time,
        trace=trace,
        rrn=rrn,dateTimeOfServer=dateTimeOfServer, posCode = posCode
    )
    class PurchaseTransactionResponse(val amount:String,
                                      val issuerName:String,
                                      val maskedPan:String,
                                      responseCode: Int,
                                      responseMessage: Int?,
                                      reasonCode: Int? = null,
                                      date: String,
                                      time: String,
                                      trace: String,
                                      rrn: String? = null,posCode:String?=null,val purchaseId:String?=null,dateTimeOfServer:String?=null
    ) : BaseTransactionResponse(
        responseCode=responseCode,
        responseMessage=responseMessage,
        reasonCode=reasonCode,
        date=date,
        time=time,
        trace=trace,
        rrn=rrn,posCode=posCode,dateTimeOfServer=dateTimeOfServer
    )
    class SettlementReverseTransactionResponse(
        responseCode: Int,
        responseMessage: Int?,
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
    class GetKeyTransactionResponse(
        responseCode: Int,
        responseMessage: Int?,
        reasonCode: Int? = null,
        date: String,
        time: String,
        trace: String,
        rrn: String? = null,
        val decryptedToken:String?=null,
        val macKeySwitch:String?=null,
        val masterKeySwitch:String?=null,
val acquiringInstitutionIdentificationCode:String?=null,
        val terminalId: String?=null,
        val merchantId: String?=null
    ) : BaseTransactionResponse(
        responseCode=responseCode,
        responseMessage=responseMessage,
        reasonCode=reasonCode,
        date=date,
        time=time,
        trace=trace,
        rrn=rrn
    )
    class MasterKeyConfirmationTransactionResponse(
        responseCode: Int,
        responseMessage: Int?,
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
    )
    class WorkingKeyExchangeTransactionResponse(
        responseCode: Int,
        responseMessage: Int?,
        reasonCode: Int? = null,
        date: String,
        time: String,
        trace: String,
        rrn: String? = null,
        val macKey:String?=null,
        val pinKey:String?=null,
        val dataKey:String?=null,
        val terminalId: String?=null,
        val merchantId: String?=null,
        val acquiringInstitutionIdentificationCode:String?=null
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
data class AccountMerchant(var bankName:String?=null,var farsiBankName:String?=null,var number:String?=null,var isActive:Boolean=true,
    var englishBankName:String?=null,)
fun BaseTransactionResponse.PurchaseTransactionResponse.toResponseTransaction(purchaseId: String?,
                                                                              merchant: Merchant,
                                                                              terminalId: String
): ResponseTransaction? {
    return ResponseTransaction(
        responseCode = this.responseCode.toString(),
        responseMessage = if (this.responseMessage==null) {
            ResponseMessageContainer.valueOfLabel(
                this.responseCode.toString()
            ).messageId
        } else {
            this.responseMessage
        },
        rrn = this.rrn ?: "",
        trace = this.trace,
        merchantName = merchant.merchantName ?: "",
        merchantId = merchant.merchantId ?: "",
        merchantPhone = merchant.merchantPhone ?: "",
        terminalID = terminalId,
        transactionType = if (purchaseId.isNullOrEmpty()) TransactionType.PURCHASE.title else TransactionType.PURCHASEWITHID.title,
        date = this.date,
        time = this.time.formatTime(),
        issuerName = this.issuerName,
        amount = this.amount, maskedPan = this.maskedPan, posCode = this.posCode, englishMerchantName = merchant.englishMerchantName?:""
    )
}