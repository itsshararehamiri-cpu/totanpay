package com.example.totanpay.common.receipt

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.multidex.BuildConfig.FLAVOR
import com.example.totanpay.R
import com.example.totanpay.common.isSmall
import com.example.totanpay.data.OperatorContainer
import com.example.totanpay.data.util.formatAmount
import com.example.totanpay.ui.theme.Dimensions.PADDING_SIDE_ROW_RECEIPT
import com.example.totanpay.ui.theme.Dimensions.PSP_LOGO_hEIGHT_IS_PAPER_RECEPINT
import com.example.totanpay.ui.theme.Dimensions.PSP_LOGO_hEIGHT_RECEPINT
import com.example.totanpay.ui.theme.TotanPayTheme

@Composable
fun RowReceipt(
    modifier: Modifier = Modifier,
    first: String,
    second: String,
    textColor: Color,
    isPaperReceipt: Boolean = false
) {
    val context= LocalContext.current
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
                fontSize = getFontSize(isPaperReceipt,context),
                fontWeight = getFontWeight(isPaperReceipt,context)
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
                fontSize = getFontSize(isPaperReceipt,context),
                fontWeight = getFontWeight(isPaperReceipt,context)
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
    val context= LocalContext.current
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
                fontSize = getFontSize(isPaperReceipt,context),
                fontWeight = getFontWeight(isPaperReceipt,context)
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
                fontSize = getFontSize(isPaperReceipt,context),
                fontWeight = getFontWeight(isPaperReceipt,context)
            ),
            textAlign = TextAlign.End
        )
    }
}

@Composable
fun AddMerchantNamePhone(
    modifier: Modifier = Modifier,
    merchantName: String,
    merchantPhone: String,
    textColor: Color,
    isPaperReceipt: Boolean = false
) {
    RowReceipt(
        modifier = modifier,
        first = merchantName,
        second = merchantPhone,
        textColor = textColor, isPaperReceipt = isPaperReceipt
    )
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
        second = "$number عدد ",
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
        second = "${sum.formatAmount()} ریال ",
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
        first = "پایانه/پذیرنده",
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
    val context= LocalContext.current
    Row(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Text(
            text = cardIssuer.ifEmpty { "شماره کارت" },
            modifier = Modifier
                .wrapContentWidth()
                .padding(end = if (isPaperReceipt) 0.dp else PADDING_SIDE_ROW_RECEIPT)
                .layoutId("first"),
            color = textColor,
            style =
            MaterialTheme.typography.bodyMedium.copy(
                fontSize = getFontSize(isPaperReceipt,context),
                fontWeight = getFontWeight(isPaperReceipt,context)
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
                    fontSize = getFontSize(isPaperReceipt,context),
                    fontWeight = getFontWeight(isPaperReceipt,context)
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
    val context= LocalContext.current

    Row(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Text(
            text = "شناسه پرداخت",
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
                    fontSize = getFontSize(isPaperReceipt,context),
                    fontWeight = getFontWeight(isPaperReceipt,context)
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
        first = if (rrn.isNullOrEmpty()) "شماره پیگیری/       "
        else "شماره پیگیری/ارجاع",
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

    val context= LocalContext.current
    Row(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Text(
            text = "مبلغ",
            modifier = Modifier
                .wrapContentWidth()
                .padding(end = if (isPaperReceipt) 0.dp else PADDING_SIDE_ROW_RECEIPT)
                .layoutId("first"),
            color = textColor,
            style =
            MaterialTheme.typography.bodyMedium.copy(
                fontSize = getFontSize(isPaperReceipt,context),
                fontWeight = getFontWeight(isPaperReceipt,context)
            ),
            textAlign = TextAlign.End
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text =  "${amount.formatAmount()} ریال ",
            modifier = Modifier
                .wrapContentWidth()
                .padding(start = if (isPaperReceipt) 0.dp else PADDING_SIDE_ROW_RECEIPT)
                .layoutId("second"),
            color = textColor,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = getFontSize(isPaperReceipt,context),
                fontWeight = if(isSmall(context))FontWeight.Bold else {
                    if (isPaperReceipt)
                    {
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
        first = "شماره موبایل",
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

    val context= LocalContext.current
    Row(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Text(
            text = "موجودی",
            modifier = Modifier
                .wrapContentWidth()
                .padding(end = if (isPaperReceipt) 0.dp else PADDING_SIDE_ROW_RECEIPT)
                .layoutId("first"),
            color = textColor,
            style =
            MaterialTheme.typography.bodyMedium.copy(
                fontSize = getFontSize(isPaperReceipt,context),
                fontWeight = getFontWeight(isPaperReceipt,context)
            ),
            textAlign = TextAlign.End
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "${balance.formatAmount()} ریال ",
            modifier = Modifier
                .wrapContentWidth()
                .padding(start = if (isPaperReceipt) 0.dp else PADDING_SIDE_ROW_RECEIPT)
                .layoutId("second"),
            color = textColor,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = getFontSize(isPaperReceipt,context),
                fontWeight =if(isSmall(context)) FontWeight.ExtraBold else {
                    if (isPaperReceipt)
                    {
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
        first = "کارمزد عملیات مانده موجودی",
        second = "${fee.formatAmount()} ریال ",
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
        first = "مانده حساب قابل برداشت",
        second = "${balance.formatAmount()} ریال ",
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
        first = "سریال شارژ",
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
        first = "رمز شارژ", second = voucherPin ?: "",
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
    val context= LocalContext.current
    Row(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Text(
            text = "روش شارژ",
            modifier = Modifier
                .wrapContentWidth()
                .padding(end = if (isPaperReceipt) 0.dp else PADDING_SIDE_ROW_RECEIPT)
                .layoutId("first"),
            color = textColor,
            style =
            MaterialTheme.typography.bodyMedium.copy(
                fontSize = getFontSize(isPaperReceipt,context),
                fontWeight = getFontWeight(isPaperReceipt,context)
            ),
            textAlign = TextAlign.End
        )
        Spacer(modifier = Modifier.weight(1f))
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
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
}

@Composable
fun AddPSPLog(modifier: Modifier, color: Color, isPaperReceipt: Boolean) {
    val context= LocalContext.current
    Row(
        modifier = modifier
            .padding(bottom =if(isPaperReceipt) 0.dp else {
                if(isSmall(context))5.dp else 10.dp
            }, top = if(isPaperReceipt) 0.dp else 10.dp)
            .fillMaxWidth()
    ) {
       if(isPaperReceipt){
           Image(
               painter = painterResource(id = R.drawable.ic_shaparak),
               contentDescription = "",
               modifier = Modifier
                   .padding(start = 0.dp)
                   .height(if(isSmall(context))100.dp else 45.dp)
                   .width(if(isSmall(context)) 80.dp else 60.dp )
                   .align(Alignment.CenterVertically),
               contentScale = ContentScale.Fit
           )
       }
        else{
           Image(
               painter = painterResource(id = R.drawable.ic_shaparak),
               contentDescription = "",
               modifier = Modifier
                   .padding(start = 0.dp)
                   .height(
                       if(isSmall(context))20.dp else      PSP_LOGO_hEIGHT_RECEPINT
                   )
                   .width( 90.dp)
                   .align(Alignment.CenterVertically),
               contentScale = ContentScale.FillBounds
           )
       }
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
                painter = painterResource(id = if (isPaperReceipt) R.drawable.fanava_logo else R.drawable.fanava_logo),
                contentDescription = "",
                modifier = Modifier
                    .padding(end = 0.dp)
                    .height(if (isPaperReceipt){
                        if(isSmall(context)){
                            80.dp
                        }
                      else  PSP_LOGO_hEIGHT_IS_PAPER_RECEPINT
                    }
                    else{
                        if(isSmall(context))20.dp else
                        PSP_LOGO_hEIGHT_RECEPINT
                    })
                    .width(if (isPaperReceipt) {
                        if(isSmall(context)) 100.dp else 80.dp
                    } else 130.dp)
                    .align(Alignment.CenterVertically),
                colorFilter = if (isPaperReceipt) ColorFilter.tint(Color.Black) else null,
                contentScale = ContentScale.FillWidth
            )
        }
    }
}

@Composable
@Preview
fun AddPSPLogPreview() {
    TotanPayTheme {
        AddPSPLog(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            color = Color.White, true
        )
    }
}