package com.example.totanpay.feature.bill

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.totanpay.R
import com.example.totanpay.common.containerReceiptModifier
import com.example.totanpay.common.receipt.AddAmount
import com.example.totanpay.common.receipt.AddBillId
import com.example.totanpay.common.receipt.AddCustomerSignature
import com.example.totanpay.common.receipt.AddMaskedPanCardIssuer
import com.example.totanpay.common.receipt.AddMerchantIdTerminalId
import com.example.totanpay.common.receipt.AddMerchantNamePhone
import com.example.totanpay.common.receipt.AddPSPLog
import com.example.totanpay.common.receipt.AddPaymentId
import com.example.totanpay.common.receipt.AddPosCode
import com.example.totanpay.common.receipt.AddRRNStan
import com.example.totanpay.common.receipt.AddReceiptType
import com.example.totanpay.common.receipt.AddTypeDateTime
import com.example.totanpay.common.receipt.ShowSuccessResult
import com.example.totanpay.common.rowReceiptModifier
import com.example.totanpay.common.rowReceiptWithPSPLogoModifier
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.example.totanpay.data.repository.datasource.transaction.TransactionType
import com.example.totanpay.receipt.ReceiptType
import com.example.totanpay.ui.component.HorizontalDivider
import com.example.totanpay.ui.theme.Green60

@Composable
fun BillPaymentReceiptContent(
    isPaperReceipt: Boolean,
    result: ResponseTransaction?, receiptType: ReceiptType
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
        if (isPaperReceipt)
            AddReceiptType(
                modifier = Modifier.rowReceiptModifier(isPaperReceipt),
             receiptType = receiptType,
                textColor = firstColor
            )
        AddMerchantNamePhone(
            modifier = Modifier.rowReceiptWithPSPLogoModifier(isPaperReceipt),
            merchantName = result!!.merchantName,
            merchantPhone = result.merchantPhone,
            isPaperReceipt = isPaperReceipt,
            englishMerchantName = result.englishMerchantName,
            textColor = firstColor
        )
        AddTypeDateTime(
            modifier = modifierRowReceipt,
            type = context.getString(TransactionType.BILL_PAY.title),
            date = result.date,
            time = result.time,
            textColor = firstColor, isPaperReceipt = isPaperReceipt,
        )
        HorizontalDivider(
            isPaperReceipt = isPaperReceipt
        )
        if (!result.billId.isNullOrEmpty())
            AddBillId(
                modifier = modifierRowReceipt,
                billId = result.billId,
                billIdTitle = stringResource(id = R.string.bill_id),
                textColor = firstColor, isPaperReceipt = isPaperReceipt,
            )
        if (!result.paymentId.isNullOrEmpty())
            AddPaymentId(
                modifier = modifierRowReceipt,
                paymentId = result.paymentId,
                paymentIdTitle = stringResource(id = R.string.payment_id),
                textColor = firstColor, isPaperReceipt = isPaperReceipt,
            )

        AddMerchantIdTerminalId(
            modifier = modifierRowReceipt,
            merchantId = result.merchantId,
            terminalId = result.terminalID,
            textColor = firstColor, isPaperReceipt = isPaperReceipt,
        )
        if (!result.posCode.isNullOrEmpty())
            AddPosCode(
                modifier = Modifier.rowReceiptModifier(isPaperReceipt),
                result.posCode,
                firstColor,
                isPaperReceipt
            )
        AddAmount(
            modifier = modifierRowReceipt,
            result.amount,
            if (isPaperReceipt) Color.Black else Green60,
            isPaperReceipt = isPaperReceipt
        )
        AddMaskedPanCardIssuer(
            modifier = modifierRowReceipt,
            maskedPan = result.maskedPan,
            cardIssuer = result.issuerName,
            textColor = firstColor, isPaperReceipt = isPaperReceipt,
        )
        AddRRNStan(
            modifier = modifierRowReceipt,
            rrn = result.rrn,
            stan = result.trace,
            textColor = firstColor,
            isPaperReceipt = isPaperReceipt
        )
        if (isPaperReceipt) {
            ShowSuccessResult(
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.CenterHorizontally), firstColor = firstColor
            )
            if (receiptType == ReceiptType.MERCHANT_RECEIPT)
          AddCustomerSignature(
                    modifier = Modifier.rowReceiptModifier(isPaperReceipt),
                    textColor = firstColor,
                    isPaperReceipt = true
                )
            AddPSPLog(
                modifier = Modifier.fillMaxWidth(),
                color = firstColor,
                isPaperReceipt = true
            )
        }
    }
}