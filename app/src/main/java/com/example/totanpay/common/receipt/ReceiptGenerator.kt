package com.example.totanpay.common.receipt

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.example.totanpay.data.util.formatAmount
import com.example.totanpay.data.util.mask

@Composable
fun RowReceipt(modifier: Modifier=Modifier,
    first: String,
    second: String,
    textColor: Color,
    isPaperReceipt: Boolean = false
) {
    ConstraintLayout(
        modifier = modifier
//            .padding(top = 5.dp)
//            .padding(horizontal = if (isPaperReceipt) 1.dp else 12.dp)
            .fillMaxWidth(),
        constraintSet = ConstraintSet {
            val first = createRefFor("first")
            val second=createRefFor("second")
            constrain(second) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)
            }
            constrain(first) {
                top.linkTo(second.top)
                bottom.linkTo(second.bottom)
                start.linkTo(parent.start)
            }
        }
    ) {
        Text(
            text = first,
            modifier = Modifier
                .wrapContentWidth()
                .padding(end = if (isPaperReceipt) 0.dp else 2.dp)
                .layoutId("first"),
            color = textColor,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = if (isPaperReceipt) 8.sp else 16.sp),
            textAlign = TextAlign.Start
        )
        Text(
            text = second,
            modifier = Modifier
                .wrapContentWidth()
                .padding(start = if (isPaperReceipt) 0.dp else 2.dp)
                .layoutId("second"),
            color = textColor,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = if (isPaperReceipt) 8.sp else 16.sp),
            textAlign = TextAlign.End
        )
    }
  //  CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
//        Row(modifier = Modifier.padding(horizontal = 1.dp).fillMaxWidth()) {
///*
// fontFamily = fontFamily,
//        fontWeight = FontWeight.Medium,
//        fontSize = 18.sp,
//        lineHeight = 26.sp,
//        letterSpacing = 0.5.sp
// */
//
//
//    //    }
//    }
}

@Composable
fun AddMerchantNamePhone(modifier: Modifier=Modifier,
    merchantName: String,
    merchantPhone: String,
    textColor: Color,
    isPaperReceipt: Boolean = false
) {
    RowReceipt(modifier=modifier,
        first = merchantName,
        second = merchantPhone,
        textColor = textColor, isPaperReceipt = isPaperReceipt
    )
}

@Composable
fun AddTypeDateTime(modifier: Modifier=Modifier,
    type: String,
    date: String,
    time: String,
    textColor: Color,
    isPaperReceipt: Boolean = false
) {
    RowReceipt(modifier,
        first = type,
        second = "$date $time",
        textColor = textColor, isPaperReceipt = isPaperReceipt
    )
}

@Composable
fun AddMerchantIdTerminalId(modifier: Modifier=Modifier,
    merchantId: String,
    terminalId: String,
    textColor: Color,
    isPaperReceipt: Boolean = false
) {
    RowReceipt(modifier=modifier,
        first = "پایانه/پذیرنده",
        second = "${merchantId}/${terminalId}",
        textColor = textColor, isPaperReceipt = isPaperReceipt
    )
}

@Composable
fun AddMaskedPanCardIssuer(modifier: Modifier=Modifier,
    maskedPan: String,
    cardIssuer: String,
    textColor: Color,
    isPaperReceipt: Boolean = false
) {
    RowReceipt(modifier=modifier,
        first = if (cardIssuer.isNullOrEmpty()) "شماره کارت"
        else cardIssuer,
        second = maskedPan,
        textColor = textColor, isPaperReceipt = isPaperReceipt
    )
}

@Composable
fun AddRRNStan(modifier: Modifier=Modifier,
    rrn: String?,
    stan: String,
    textColor: Color,
    isPaperReceipt: Boolean = false
) {
    RowReceipt(modifier=modifier,
        first = if (rrn.isNullOrEmpty()) "پیگیری"
        else "پیگیری/شماره ارجاع",
        second = if (rrn.isNullOrEmpty()) stan
        else "${stan}/${rrn}",
        textColor = textColor, isPaperReceipt = isPaperReceipt
    )
}

@Composable
fun AddAmount(modifier: Modifier=Modifier,
    amount: String, textColor: Color,
    isPaperReceipt: Boolean = false
) {
    Row(
        modifier = modifier
            .padding(top = 5.dp)
            .padding(horizontal = if (isPaperReceipt) 1.dp else 12.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "مبلغ",
            modifier = Modifier.padding(end = if (isPaperReceipt) 0.dp else 12.dp),
            color = textColor, fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = amount.formatAmount(),
            modifier = Modifier.padding(start = if (isPaperReceipt) 0.dp else 12.dp),
            color = textColor, fontWeight = FontWeight.Bold
        )

    }
}

@Composable
fun AddBalance(modifier: Modifier=Modifier,
    balance: String, textColor: Color,
    isPaperReceipt: Boolean = false
) {
    Row(
        modifier = modifier
            .padding(top = 5.dp)
            .padding(horizontal = if (isPaperReceipt) 1.dp else 12.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "موجودی",
            modifier = Modifier.padding(end = if (isPaperReceipt) 2.dp else 12.dp),
            color = textColor, fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = balance.formatAmount(),
            modifier = Modifier.padding(start = if (isPaperReceipt) 2.dp else 12.dp),
            color = textColor, fontWeight = FontWeight.Bold
        )

    }
}