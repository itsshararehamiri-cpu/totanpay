package com.example.totanpay.data.repository.util

import com.example.totanpay.data.repository.datasource.TotanPayException
import com.example.totanpay.data.repository.datasource.transaction.ResponseMessageContainer

 fun getResponseMessageFromResponseCode(
    responseCode: Int,
    responseMessage: Int?
): Int {
    return responseMessage
        ?: ResponseMessageContainer.valueOfLabel(
            responseCode.toString(),
        ).messageId
}
 fun getExceptionFromResponseCode(
    responseCode: Int,
    responseMessage: Int?
): TotanPayException {
    return if (responseMessage==null) {
        TotanPayException(
            messageError = ResponseMessageContainer.valueOfLabel(
                responseCode.toString(),
            ).messageId, TotanPayException.Type.NORMAL
        )
    } else {
        TotanPayException(messageError = responseMessage, TotanPayException.Type.NORMAL)
    }
}