package com.example.totanpay.common

import com.example.totanpay.data.repository.settings.merchant.PrintStatus
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction

data class ResultTransactionUiState(
    val result: ResponseTransaction? = null,
    val error: String = "",
    val customerPrintStatus: PrintStatus = PrintStatus.NO_PRINTING,
    val merchantPrintStatus: PrintStatus = PrintStatus.NO_PRINTING,
    val autoPrintCustomerReceipt: Boolean=false,
    val customerReceiptPrinted: Boolean = false,
    val merchantReceiptPrinted: Boolean = false,
    val responseForCallerApp: String? = null,
    val playbackSound: Boolean = false,
    val errorInPrint: String = "",
  val   showPrintForMerchant: Boolean=false,
    val   showPrintForCustomer: Boolean=false,

    )