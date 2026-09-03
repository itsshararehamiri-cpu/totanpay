package com.example.totanpay.common.receipt

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.multidex.BuildConfig.FLAVOR
import com.example.totanpay.LocalLanguageState
import com.example.totanpay.R
import com.example.totanpay.common.isSmall
import com.example.totanpay.data.OperatorContainer
import com.example.totanpay.data.util.formatAmount
import com.example.totanpay.receipt.ReceiptType
import com.example.totanpay.ui.theme.Blue30
import com.example.totanpay.ui.theme.Dimensions.PADDING_SIDE_ROW_RECEIPT
import com.example.totanpay.ui.theme.Dimensions.PSP_LOGO_hEIGHT_IS_PAPER_RECEPINT
import com.example.totanpay.ui.theme.Dimensions.PSP_LOGO_hEIGHT_RECEPINT

@Composable
fun RowReceipt(
    modifier: Modifier = Modifier,
    first: String,
    second: String,
    textColor: Color,
    isPaperReceipt: Boolean = false
) {
    val context = LocalContext.current
    Row(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Text(
            text = first,
            modifier = Modifier
                .wrapContentWidth()
                .padding(end = if (isPaperReceipt) 0.dp else PADDING_SIDE_ROW_RECEIPT)
                .layoutId("first"),
            color = textColor,
            style =
                MaterialTheme.typography.bodyMedium.copy(
                    fontSize = getFontSize(isPaperReceipt, context),
                    fontWeight = getFontWeight(isPaperReceipt, context)
                ),
            textAlign = TextAlign.End
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = second,
            modifier = Modifier
                .wrapContentWidth()
                .padding(start = if (isPaperReceipt) 0.dp else PADDING_SIDE_ROW_RECEIPT)
                .layoutId("second"),
            color = textColor,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = getFontSize(isPaperReceipt, context),
                fontWeight = getFontWeight(isPaperReceipt, context)
            ),
            textAlign = TextAlign.Start
        )
    }
}

@Composable
fun CenterRowReceipt(
    modifier: Modifier = Modifier,
    first: String,
    second: String,
    textColor: Color,
    isPaperReceipt: Boolean = false
) {
    val context = LocalContext.current
    Row(
        modifier = modifier
    ) {
        Text(
            text = first,
            modifier = Modifier
                .wrapContentWidth()
                .padding(end = if (isPaperReceipt) 0.dp else 2.dp)
                .layoutId("first"),
            color = textColor,
            style =
                MaterialTheme.typography.bodyMedium.copy(
                    fontSize = getFontSize(isPaperReceipt, context),
                    fontWeight = getFontWeight(isPaperReceipt, context)
                ),
            textAlign = TextAlign.Start
        )
        Text(
            text = second,
            modifier = Modifier
                .wrapContentWidth()
                .padding(start = if (isPaperReceipt) 0.dp else 2.dp)
                .layoutId("second"),
            color = textColor,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = getFontSize(isPaperReceipt, context),
                fontWeight = getFontWeight(isPaperReceipt, context)
            ),
            textAlign = TextAlign.End
        )
    }
}

@Composable
fun AddMerchantNamePhone(
    modifier: Modifier = Modifier,
    merchantName: String,
    englishMerchantName: String,
    merchantPhone: String,
    textColor: Color,
    isPaperReceipt: Boolean = false
) {
    val isFarsi = LocalLanguageState.current.isFarsiSelected.value
//    val context = LocalContext.current
//            val textStyle = MaterialTheme.typography.bodyMedium.copy(
//            fontSize = getFontSize(isPaperReceipt, context),
//            fontWeight = getFontWeight(isPaperReceipt, context)
//        )
//    Row(
//        modifier = modifier
//            .fillMaxWidth(),
//    ) {
//        Text(
//            text =  if (isFarsi) merchantName else englishMerchantName,
//            modifier = Modifier
//                .wrapContentWidth()
//                .padding(end = if (isPaperReceipt) 0.dp else PADDING_SIDE_ROW_RECEIPT)
//                .layoutId("first"),
//            color = textColor,
//
//                style =if (isPaperReceipt) textStyle.copy(lineHeight = (8 * 1.1).sp ,
//                    platformStyle = PlatformTextStyle(
//                        includeFontPadding = false
//                    )) else textStyle,
//            textAlign = TextAlign.End ,maxLines = 1,
//                overflow = TextOverflow.Ellipsis
//        )
//        Spacer(modifier = Modifier.weight(1f))
//        Text(
//            text = merchantPhone,
//            modifier = Modifier
//                .wrapContentWidth()
//                .padding(start = if (isPaperReceipt) 0.dp else PADDING_SIDE_ROW_RECEIPT)
//                .layoutId("second"),
//            color = textColor,
//            style = MaterialTheme.typography.bodyMedium.copy(
//                fontSize = getFontSize(isPaperReceipt, context),
//                fontWeight = getFontWeight(isPaperReceipt, context)
//            ),
//            textAlign = TextAlign.Start
//        )
//    }

//    if (!isPaperReceipt)
//    RowReceipt(
//        modifier = modifier,
//        first = if (isFarsi) merchantName else englishMerchantName,
//        second = merchantPhone,
//        textColor = textColor, isPaperReceipt = isPaperReceipt
//    )
//    else{
        val context = LocalContext.current
        val textStyle = MaterialTheme.typography.bodyMedium.copy(
            fontSize = getFontSize(isPaperReceipt, context),
            fontWeight = getFontWeight(isPaperReceipt, context)
        )

        Row(
            modifier = modifier.fillMaxWidth(),
            // استفاده از Alignment.Top برای اینکه اگر یکی بلندتر شد، دیگری از بالا شروع شود
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = if (isFarsi) merchantName else englishMerchantName,
                modifier = Modifier
                    .weight(1f) // به این متن اجازه می‌دهیم فضا بگیرد و اگر طولانی شد، بشکند
                    .padding(end = if (isPaperReceipt) 0.dp else PADDING_SIDE_ROW_RECEIPT),
                color = textColor,
                style =if (isPaperReceipt) textStyle.copy(lineHeight = (8 * 1.1).sp ,
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )) else textStyle,
                textAlign = TextAlign.End, maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // اگر می‌خواهید فاصله ثابتی بینشان باشد، یک Spacer کوچک بگذارید
            // Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = merchantPhone,
                modifier = Modifier
                    .weight(1f) // این هم به اندازه اولی فضا می‌گیرد
                    .padding(start = if (isPaperReceipt) 0.dp else PADDING_SIDE_ROW_RECEIPT),
                color = textColor,
                style = textStyle,
                textAlign = TextAlign.Start
            )
        }
    //}

}

@Composable
fun AddReceiptType(
    modifier: Modifier = Modifier,
    receiptType: ReceiptType,
    textColor: Color, isPaperReceipt: Boolean = true
) {
    val context = LocalContext.current
    Row(
        modifier = modifier
            .fillMaxWidth(), horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(
                when (receiptType) {
                    ReceiptType.CUSTOMER_RECEIPT -> R.string.customer_receipt
                    ReceiptType.MERCHANT_RECEIPT -> R.string.merchant_receipt
                    ReceiptType.DUPLICATE_RECEIPT -> R.string.duplicate_receipt
                }
            ),
            modifier = Modifier
                .wrapContentWidth()
                .padding(end = if (isPaperReceipt) 0.dp else PADDING_SIDE_ROW_RECEIPT),
            color = textColor,
            style =
                MaterialTheme.typography.bodyMedium.copy(
                    fontSize = getFontSize(isPaperReceipt, context),
                    fontWeight = FontWeight.ExtraBold
                ),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun AddTypeDateTime(
    modifier: Modifier = Modifier,
    type: String,
    date: String,
    time: String,
    textColor: Color,
    isPaperReceipt: Boolean = false
) {
    RowReceipt(
        modifier,
        first = type,
        second = "$time - $date",
        textColor = textColor, isPaperReceipt = isPaperReceipt
    )
}

@Composable
fun AddDateTime(
    modifier: Modifier = Modifier,

    date: String,
    time: String,
    textColor: Color,
    isPaperReceipt: Boolean = false
) {
    RowReceipt(
        modifier,
        first = date,
        second = time,
        textColor = textColor, isPaperReceipt = isPaperReceipt
    )
}

@Composable
fun AddNumberOfAllTransactions(
    modifier: Modifier = Modifier,
    number: String,
    textColor: Color,
    isPaperReceipt: Boolean = false
) {
    RowReceipt(
        modifier,
        first = stringResource(R.string.number_of_all_transactions),
        second = number,
        textColor = textColor, isPaperReceipt = isPaperReceipt
    )
}

@Composable
fun AddSumOfAllTransactions(
    modifier: Modifier = Modifier,
    sum: String,
    textColor: Color,
    isPaperReceipt: Boolean = false
) {
    RowReceipt(
        modifier,
        first = stringResource(R.string.sum_of_all_transactions),
        second = stringResource(R.string.amount_with_currency, sum.formatAmount()),
        textColor = textColor, isPaperReceipt = isPaperReceipt
    )
}

@Composable
fun AddBillId(
    modifier: Modifier = Modifier,
    billIdTitle: String,
    billId: String,
    textColor: Color,
    isPaperReceipt: Boolean = false
) {
    RowReceipt(
        modifier,
        first = billIdTitle,
        second = billId,
        textColor = textColor, isPaperReceipt = isPaperReceipt
    )
}

@Composable
fun AddPaymentId(
    modifier: Modifier = Modifier,
    paymentIdTitle: String,
    paymentId: String,
    textColor: Color,
    isPaperReceipt: Boolean = false
) {
    RowReceipt(
        modifier,
        first = paymentIdTitle,
        second = paymentId,
        textColor = textColor, isPaperReceipt = isPaperReceipt
    )
}

@Composable
fun AddMerchantIdTerminalId(
    modifier: Modifier = Modifier,
    merchantId: String,
    terminalId: String,
    textColor: Color,
    isPaperReceipt: Boolean = false
) {
    RowReceipt(
        modifier = modifier,
        first = stringResource(R.string.terminal_merchant),
        second = "${merchantId}/${terminalId}",
        textColor = textColor, isPaperReceipt = isPaperReceipt
    )
}

@Composable
fun AddMaskedPanCardIssuer(
    modifier: Modifier = Modifier,
    maskedPan: String,
    cardIssuer: String,
    textColor: Color,
    isPaperReceipt: Boolean = false
) {
    val context = LocalContext.current
    Row(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Text(
            text = cardIssuer.ifEmpty { context.getString(R.string.card_number_title) },
            modifier = Modifier
                .wrapContentWidth()
                .padding(end = if (isPaperReceipt) 0.dp else PADDING_SIDE_ROW_RECEIPT)
                .layoutId("first"),
            color = textColor,
            style =
                MaterialTheme.typography.bodyMedium.copy(
                    fontSize = getFontSize(isPaperReceipt, context),
                    fontWeight = getFontWeight(isPaperReceipt, context)
                ),
            textAlign = TextAlign.Start
        )
        Spacer(modifier = Modifier.weight(1f))
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
            Text(
                text = maskedPan,
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(start = if (isPaperReceipt) 0.dp else PADDING_SIDE_ROW_RECEIPT)
                    .layoutId("second"),
                color = textColor,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = getFontSize(isPaperReceipt, context),
                    fontWeight = getFontWeight(isPaperReceipt, context)
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun AddPurchaseId(
    modifier: Modifier = Modifier,
    purchaseId: String,
    textColor: Color,
    isPaperReceipt: Boolean = false
) {
    val context = LocalContext.current

    Row(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Text(
            text = stringResource(R.string.payment_id),
            modifier = Modifier
                .wrapContentWidth()
                .padding(end = if (isPaperReceipt) 0.dp else PADDING_SIDE_ROW_RECEIPT)
                .layoutId("first"),
            color = textColor,
            style =
                MaterialTheme.typography.bodyMedium.copy(
                    fontSize = getFontSize(isPaperReceipt, context = context),
                    fontWeight = getFontWeight(isPaperReceipt, context = context)
                ),
            textAlign = TextAlign.Start
        )
        Spacer(modifier = Modifier.weight(1f))
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
            Text(
                text = purchaseId,
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(start = if (isPaperReceipt) 0.dp else PADDING_SIDE_ROW_RECEIPT)
                    .layoutId("second"),
                color = textColor,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = getFontSize(isPaperReceipt, context),
                    fontWeight = getFontWeight(isPaperReceipt, context)
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun AddRRNStan(
    modifier: Modifier = Modifier,
    rrn: String?,
    stan: String,
    textColor: Color,
    isPaperReceipt: Boolean = false
) {
    RowReceipt(
        modifier = modifier,
        first = if (rrn.isNullOrEmpty()) stringResource(R.string.trace__)
        else stringResource(R.string.trace_rrn),
        second = if (rrn.isNullOrEmpty()) "${stan}/   "
        else "${stan}/${rrn}",
        textColor = textColor, isPaperReceipt = isPaperReceipt
    )
}

@Composable
fun AddAmount(
    modifier: Modifier = Modifier,
    amount: String, textColor: Color,
    isPaperReceipt: Boolean = false
) {

    val context = LocalContext.current
    var tempModifier = modifier
        .fillMaxWidth()
    if (isPaperReceipt)
        tempModifier =
            tempModifier
                .border(width = 1.dp, shape = RoundedCornerShape(3.dp), color = textColor)
                .padding(horizontal = 2.dp)
                .padding(top = 1.dp, bottom = 1.dp)
    Row(
        modifier = tempModifier,
    ) {
        Text(
            text = stringResource(R.string.amount),
            modifier = Modifier
                .wrapContentWidth()
                .padding(end = if (isPaperReceipt) 0.dp else PADDING_SIDE_ROW_RECEIPT)
                .layoutId("first"),
            color = textColor,
            style =
                MaterialTheme.typography.bodyMedium.copy(
                    fontSize = getFontSize(isPaperReceipt, context),
                    fontWeight = getFontWeight(isPaperReceipt, context)
                ),
            textAlign = TextAlign.End
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = stringResource(R.string.amount_with_currency, amount.formatAmount()),
            modifier = Modifier
                .wrapContentWidth()
                .padding(start = if (isPaperReceipt) 0.dp else PADDING_SIDE_ROW_RECEIPT)
                .layoutId("second"),
            color = textColor,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = getFontSize(isPaperReceipt, context),
                fontWeight = if (isSmall(context)) {
                    if (isPaperReceipt) FontWeight.Black else FontWeight.Bold
                } else {
                    if (isPaperReceipt) FontWeight.ExtraBold else FontWeight.Bold
                }
            ),
            textAlign = TextAlign.Start
        )
    }
}

@Composable
fun AddPosCode(
    modifier: Modifier = Modifier,
    posCode: String, textColor: Color,
    isPaperReceipt: Boolean = false
) {
    RowReceipt(
        modifier = modifier,
        first = stringResource(R.string.pos_code),
        second = posCode,
        textColor = textColor, isPaperReceipt = isPaperReceipt
    )
}

@Composable
fun AddMobile(
    modifier: Modifier = Modifier,
    mobile: String, textColor: Color,
    isPaperReceipt: Boolean = false
) {

    RowReceipt(
        modifier = modifier,
        first = stringResource(R.string.mobile_number),
        second = mobile,
        textColor = textColor, isPaperReceipt = isPaperReceipt
    )
}

@Composable
fun AddBalance(
    modifier: Modifier = Modifier,
    balance: String, textColor: Color,
    isPaperReceipt: Boolean = false
) {

    val context = LocalContext.current
    Row(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Text(
            text = stringResource(R.string.balance_),
            modifier = Modifier
                .wrapContentWidth()
                .padding(end = if (isPaperReceipt) 0.dp else PADDING_SIDE_ROW_RECEIPT)
                .layoutId("first"),
            color = textColor,
            style =
                MaterialTheme.typography.bodyMedium.copy(
                    fontSize = getFontSize(isPaperReceipt, context),
                    fontWeight = getFontWeight(isPaperReceipt, context)
                ),
            textAlign = TextAlign.End
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = stringResource(R.string.amount_with_currency, balance.formatAmount()),
            modifier = Modifier
                .wrapContentWidth()
                .padding(start = if (isPaperReceipt) 0.dp else PADDING_SIDE_ROW_RECEIPT)
                .layoutId("second"),
            color = textColor,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = getFontSize(isPaperReceipt, context),
                fontWeight = if (isSmall(context)) FontWeight.ExtraBold else {
                    if (isPaperReceipt) {
                        if (isSmall(context)) {
                            FontWeight.Bold
                        } else {
                            FontWeight.Medium
                        }
                    } else {
                        FontWeight.Bold
                    }
                }
            ),
            textAlign = TextAlign.Start
        )
    }
}

@Composable
fun AddFee(
    modifier: Modifier = Modifier,
    fee: String, textColor: Color,
    isPaperReceipt: Boolean = false
) {
    CenterRowReceipt(
        modifier = modifier,
        first = stringResource(R.string.balance_transaction_fee),
        second = stringResource(R.string.amount_with_currency, fee.formatAmount()),
        textColor = textColor, isPaperReceipt = isPaperReceipt
    )
}


@Composable
fun AddAvailableBalance(
    modifier: Modifier = Modifier,
    balance: String, textColor: Color,
    isPaperReceipt: Boolean = false
) {
    RowReceipt(
        modifier = modifier,
        first = stringResource(R.string.withdrawable_account_balance),
        second = stringResource(R.string.amount_with_currency, balance.formatAmount()),
        textColor = textColor, isPaperReceipt = isPaperReceipt
    )
}

@Composable
fun AddVoucherSerial(
    modifier: Modifier = Modifier,
    voucherSerial: String?,
    textColor: Color,
    isPaperReceipt: Boolean = false
) {
    RowReceipt(
        modifier = modifier,
        first = stringResource(R.string.voucher_serial),
        second = voucherSerial ?: "",
        textColor = textColor, isPaperReceipt = isPaperReceipt
    )
}

@Composable
fun AddVoucherPin(
    modifier: Modifier = Modifier,
    voucherPin: String?,
    textColor: Color,
    isPaperReceipt: Boolean = false
) {
    RowReceipt(
        modifier = modifier,
        first = stringResource(R.string.voucher_pin), second = voucherPin ?: "",
        textColor = textColor, isPaperReceipt = isPaperReceipt
    )
}

@Composable
fun AddVoucherChargeMSG(
    modifier: Modifier = Modifier,
    productCode: Int,
    textColor: Color,
    isPaperReceipt: Boolean = false
) {
    val context = LocalContext.current
    Row(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Text(
            text = stringResource(R.string.charging_method),
            modifier = Modifier
                .wrapContentWidth()
                .padding(end = if (isPaperReceipt) 0.dp else PADDING_SIDE_ROW_RECEIPT)
                .layoutId("first"),
            color = textColor,
            style =
                MaterialTheme.typography.bodyMedium.copy(
                    fontSize = getFontSize(isPaperReceipt, context),
                    fontWeight = getFontWeight(isPaperReceipt, context)
                ),
            textAlign = TextAlign.End
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = OperatorContainer.getVoucherChargeMSG(productCode),
            modifier = Modifier
                .wrapContentWidth()
                .padding(start = if (isPaperReceipt) 0.dp else PADDING_SIDE_ROW_RECEIPT)
                .layoutId("second"),
            color = textColor,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = getFontSize(isPaperReceipt, context),
                fontWeight = getFontWeight(isPaperReceipt, context)
            ),
            textAlign = TextAlign.Start
        )

    }
}

@Composable
fun AddPSPLog(modifier: Modifier, color: Color, isPaperReceipt: Boolean) {
    val context = LocalContext.current
    if (isPaperReceipt) Image(
        painter = painterResource(id = R.drawable.logos_receipt),//fa_logo
        contentDescription = "",
        modifier = Modifier
            .padding(start = 0.dp)
            .fillMaxWidth(),
        contentScale = ContentScale.FillWidth, alpha = 0.4f
    )
    else Row(
        modifier = modifier
            .padding(
                bottom = if (isPaperReceipt) 0.dp else {
                    if (isSmall(context)) 5.dp else 10.dp
                }, top = if (isPaperReceipt) 0.dp else 10.dp
            )
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.sha),
            contentDescription = "",
            modifier = Modifier
                .padding(start = 0.dp)
                .height(
                    if (isSmall(context)) 20.dp else PSP_LOGO_hEIGHT_RECEPINT
                )
                .width(90.dp)
                .align(Alignment.CenterVertically),
            contentScale = ContentScale.FillBounds,
            colorFilter = ColorFilter.tint(Blue30)
        )

        Spacer(modifier = Modifier.weight(1f))
        if (FLAVOR == "pn") {
            Image(
                painter = painterResource(
                    id = if (isPaperReceipt)
                        R.drawable.ic_black_pn_logo else R.drawable.ic_white_pn_logo
                ),
                contentDescription = "",
                modifier = Modifier
                    .height(if (isPaperReceipt) PSP_LOGO_hEIGHT_IS_PAPER_RECEPINT else PSP_LOGO_hEIGHT_RECEPINT)
                    .width(if (isPaperReceipt) 90.dp else 130.dp)
                    .align(Alignment.CenterVertically),
                colorFilter = if (isPaperReceipt) ColorFilter.tint(color) else null,
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.fanava_logo),
                contentDescription = "",
                modifier = Modifier
                    .padding(end = 0.dp)
                    .height(
                        if (isSmall(context)) 20.dp else
                            PSP_LOGO_hEIGHT_RECEPINT
                    )
                    .width(130.dp)
                    .align(Alignment.CenterVertically),
                contentScale = ContentScale.FillWidth
            )
        }
    }
}

@Composable
fun AddCustomerSignature(
    modifier: Modifier = Modifier,
    textColor: Color,
    isPaperReceipt: Boolean = false
) {
    RowReceipt(
        modifier = modifier,
        first = stringResource(R.string.customer_signature),
        second = "",
        textColor = textColor, isPaperReceipt = isPaperReceipt
    )

}

@Composable
fun ShowSuccessResult(modifier: Modifier, firstColor: Color) {
    Text(
        modifier = modifier,
        text = stringResource(id = R.string.success_operation),
        color = firstColor,
        style = MaterialTheme.typography.titleMedium.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold)
    )
}