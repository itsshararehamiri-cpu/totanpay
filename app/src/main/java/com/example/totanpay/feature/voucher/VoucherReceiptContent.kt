package com.example.totanpay.feature.voucher

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.totanpay.common.containerReceiptModifier
import com.example.totanpay.common.isSmall
import com.example.totanpay.common.rowReceiptModifier
import com.example.totanpay.common.receipt.AddAmount
import com.example.totanpay.common.receipt.AddMaskedPanCardIssuer
import com.example.totanpay.common.receipt.AddMerchantIdTerminalId
import com.example.totanpay.common.receipt.AddMerchantNamePhone
import com.example.totanpay.common.receipt.AddPSPLog
import com.example.totanpay.common.receipt.AddPosCode
import com.example.totanpay.common.receipt.AddRRNStan
import com.example.totanpay.common.receipt.AddTypeDateTime
import com.example.totanpay.common.receipt.AddVoucherChargeMSG
import com.example.totanpay.common.receipt.AddVoucherPin
import com.example.totanpay.common.receipt.AddVoucherSerial
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.example.totanpay.ui.component.HorizontalDivider
import com.example.totanpay.ui.theme.Black
import com.example.totanpay.ui.theme.Green60

@Composable
fun VoucherReceiptContent(
    isPaperReceipt: Boolean,
    result: ResponseTransaction?,
    printForCustomer: Boolean
) {
    val context= LocalContext.current

    Column(
        modifier = Modifier
        .containerReceiptModifier(isPaperReceipt,context)

    ) {
        val firstColor = if (isPaperReceipt) Color.Black else  MaterialTheme.colorScheme.onSurface
        if (!isPaperReceipt)
        {
            AddPSPLog(
                modifier = Modifier.fillMaxWidth(),
                color = firstColor,
                isPaperReceipt = false
            )
        }
        if (!result!!.voucherSerial.isNullOrEmpty() && isSmall(context)) {
            AddVoucherSerial(
                modifier = Modifier.rowReceiptModifier(isPaperReceipt),
                voucherSerial = result.voucherSerial,
                textColor = firstColor,isPaperReceipt
            )
            AddVoucherPin(
                modifier =Modifier.rowReceiptModifier(isPaperReceipt),
                voucherPin = result.voucherPin,
                textColor = firstColor,isPaperReceipt
            )
            AddVoucherChargeMSG(
                modifier =Modifier.rowReceiptModifier(isPaperReceipt),
                productCode = result.productCode ?: -1,
                textColor = firstColor,isPaperReceipt
            )
        }
        AddMerchantNamePhone(
            modifier = Modifier.rowReceiptModifier(isPaperReceipt),
            merchantName = result!!.merchantName,
            merchantPhone = result.merchantPhone,
            textColor = firstColor, isPaperReceipt
        )
        AddTypeDateTime(
            modifier = Modifier.rowReceiptModifier(isPaperReceipt),
            type = result.transactionType,
            date = result.date,
            time = result.time,
            textColor = firstColor, isPaperReceipt
        )
        HorizontalDivider(
            isPaperReceipt=isPaperReceipt
        )
        AddMerchantIdTerminalId(
            modifier = Modifier.rowReceiptModifier(isPaperReceipt),
            merchantId = result.merchantId,
            terminalId = result.terminalID,
            textColor = firstColor, isPaperReceipt
        )
        if (!result.posCode.isNullOrEmpty())
            AddPosCode(
                modifier = Modifier.rowReceiptModifier(isPaperReceipt),
                result.posCode,
                firstColor,
                isPaperReceipt
            )
        if (!result.voucherSerial.isNullOrEmpty() && printForCustomer && !isSmall(context)) {
            AddVoucherSerial(
                modifier = Modifier.rowReceiptModifier(isPaperReceipt),
                voucherSerial = result.voucherSerial,
                textColor = firstColor,isPaperReceipt
            )
            AddVoucherPin(
                modifier =Modifier.rowReceiptModifier(isPaperReceipt),
                voucherPin = result.voucherPin,
                textColor = firstColor,isPaperReceipt
            )
            AddVoucherChargeMSG(
                modifier =Modifier.rowReceiptModifier(isPaperReceipt),
                productCode = result.productCode ?: -1,
                textColor = firstColor,isPaperReceipt
            )
        }
        AddMaskedPanCardIssuer(
            modifier = Modifier.rowReceiptModifier(isPaperReceipt),
            maskedPan = result.maskedPan,
            cardIssuer = result.issuerName,
            textColor = firstColor, isPaperReceipt
        )
        AddRRNStan(
            modifier = Modifier.rowReceiptModifier(isPaperReceipt),
            rrn = result.rrn,
            stan = result.trace,
            textColor = firstColor,
            isPaperReceipt
        )
        AddAmount(
            modifier = Modifier.rowReceiptModifier(isPaperReceipt),
            result.amount
            ,textColor=if(isPaperReceipt) Black else Green60,
            isPaperReceipt
        )
        if (isPaperReceipt)
        {
            AddPSPLog(
                modifier = Modifier.fillMaxWidth(),
                color = firstColor,
                isPaperReceipt = true
            )
        }

    }
}