package com.example.totanpay.common

import com.example.totanpay.data.repository.datasource.transaction.TransactionType
import org.json.JSONObject

fun selectedTransactionTypesOf(json: String): List<TransactionType> {
    if (json.isEmpty()) return emptyList()
    return try {
        val obj = JSONObject(json)
        buildList {
            if (obj.optString("purchaseType") == "has") add(TransactionType.PURCHASE)
            if (obj.optString("billPayType") == "has") add(TransactionType.BILL_PAY)
            if (obj.optString("voucherType") == "has") add(TransactionType.VOUCHER)
            if (obj.optString("topupType") == "has") add(TransactionType.TOPUP)
        }
    } catch (e: Exception) {
        emptyList()
    }
}
