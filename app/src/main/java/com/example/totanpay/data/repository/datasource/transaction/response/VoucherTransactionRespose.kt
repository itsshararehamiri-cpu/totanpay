package com.example.totanpay.data.repository.datasource.transaction.response


/*
class VoucherTranResp(
    respCode: Int,
    respMessage: String?,
    reasonCode: Int?,
    rrn: String,
    trace: String,
    date: String,
    time: String,
    cardIssuer: String,
    maskedPan: String,
    val voucherSerial:String,
    val voucherPin:String,
    val voucherPINEncrypted:String,
    val groupVoucherData:HashMap<String,String>,
    val groupVoucherDataEncrypted:HashMap<String,String>,
    amount :String?
) : TranResp(respCode, respMessage, reasonCode, date, time, trace, cardIssuer, maskedPan, amount, rrn)

VoucherTranResp(
            0,
            null,
            null,
            resp.rrn ?: "",
            sendMessage.stan,
            sendMessage.tranDate,
            sendMessage.tranTime,
            cardIssuer,
            sendMessage.pan, voucherSerial,
            indentBytes4Decrypt(decData), "", HashMap(), HashMap(),
            sendMessage.getString(4)
        )
 */