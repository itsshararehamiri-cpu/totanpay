package com.example.totanpay.feature.topup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.totanpay.common.containerReceiptModifier
import com.example.totanpay.common.receipt.AddAmount
import com.example.totanpay.common.receipt.AddMaskedPanCardIssuer
import com.example.totanpay.common.receipt.AddMerchantIdTerminalId
import com.example.totanpay.common.receipt.AddMerchantNamePhone
import com.example.totanpay.common.receipt.AddMobile
import com.example.totanpay.common.receipt.AddPSPLog
import com.example.totanpay.common.receipt.AddPosCode
import com.example.totanpay.common.receipt.AddRRNStan
import com.example.totanpay.common.receipt.AddTypeDateTime
import com.example.totanpay.common.rowReceiptModifier
import com.example.totanpay.common.rowReceiptWithPSPLogoModifier
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.example.totanpay.data.repository.datasource.transaction.TransactionType
import com.example.totanpay.ui.component.HorizontalDivider
import com.example.totanpay.ui.theme.Green60

@Composable
fun TopUpReceiptContent(
    isPaperReceipt: Boolean,
    result: ResponseTransaction?, printForCustomer: Boolean
) {
    val context = LocalContext.current

    Column(
        modifier =
        Modifier.containerReceiptModifier(isPaperReceipt, context)
    ) {
        val firstColor = if (isPaperReceipt) Color.Black else MaterialTheme.colorScheme.onSurface
        val modifierRowReceipt = Modifier.rowReceiptModifier(isPaperReceipt)
        if (!isPaperReceipt) {
            AddPSPLog(
                modifier = Modifier.fillMaxWidth(),
                color = firstColor,
                isPaperReceipt = false
            )
        }
        AddMerchantNamePhone(
            modifier = modifierRowReceipt,
            merchantName = result!!.merchantName,
            merchantPhone = result.merchantPhone,
            textColor = firstColor,
            isPaperReceipt = isPaperReceipt
        )
        AddTypeDateTime(
            modifier = modifierRowReceipt,
            type = TransactionType.TOPUP.title,
            date = result.date,
            time = result.time,
            textColor = firstColor,
            isPaperReceipt = isPaperReceipt
        )
        HorizontalDivider(
            isPaperReceipt = isPaperReceipt
        )
        if (!result.mobile.isNullOrEmpty())
            if (printForCustomer)
                AddMobile(
                    modifier = Modifier.rowReceiptWithPSPLogoModifier(isPaperReceipt),
                    result.mobile,
                    isPaperReceipt = isPaperReceipt,
                    textColor = firstColor,
                )
        AddMerchantIdTerminalId(
            modifier = modifierRowReceipt,
            merchantId = result.merchantId,
            terminalId = result.terminalID,
            textColor = firstColor,
            isPaperReceipt = isPaperReceipt
        )
        if (!result.posCode.isNullOrEmpty())
            AddPosCode(
                modifier = Modifier.rowReceiptModifier(isPaperReceipt),
                result.posCode,
                firstColor,
                isPaperReceipt
            )
        AddMaskedPanCardIssuer(
            modifier = modifierRowReceipt,
            maskedPan = result.maskedPan,
            cardIssuer = result.issuerName,
            textColor = firstColor,
            isPaperReceipt = isPaperReceipt
        )
        AddRRNStan(
            modifier = modifierRowReceipt,
            rrn = result.rrn,
            stan = result.trace,
            textColor = firstColor,
            isPaperReceipt = isPaperReceipt
        )
        AddAmount(
            modifier = modifierRowReceipt,
            result.amount,
            if (isPaperReceipt) Color.Black else Green60,
            isPaperReceipt = isPaperReceipt
        )
        if (isPaperReceipt) {
            AddPSPLog(
                modifier = Modifier.fillMaxWidth(),
                color = firstColor,
                isPaperReceipt = true
            )
        }

    }
}