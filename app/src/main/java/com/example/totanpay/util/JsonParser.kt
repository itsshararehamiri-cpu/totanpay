package com.example.totanpay.util

import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.google.gson.Gson
import org.json.JSONObject

fun convertPurchaseResultToJsonObject(response: String) :String{
    val result = Gson().fromJson(response, ResponseTransaction::class.java)
    val responseForCallerApp = JSONObject()
    responseForCallerApp.put("amount", result.amount)
    responseForCallerApp.put("rrn", result.rrn)
    responseForCallerApp.put("trace", result.trace)
    responseForCallerApp.put("issuerName", result.issuerName)
    responseForCallerApp.put("responseCode", result.responseCode)
    responseForCallerApp.put(
        "posCode",
        result.posCode
    )
    responseForCallerApp.put("maskedPan", result.maskedPan)
    responseForCallerApp.put("responseMessage", result.responseMessage)
    responseForCallerApp.put("merchantId", result.merchantId)
    responseForCallerApp.put("terminalID", result.terminalID)
    responseForCallerApp.put("merchantPhone", result.merchantPhone)
    responseForCallerApp.put("time", result.time)
    responseForCallerApp.put("date", result.date)
    responseForCallerApp.put("merchantName", result.merchantName)
    return responseForCallerApp.toString()
}