package com.example.totanpay.feature.topup

import android.os.Build.VERSION.SDK_INT
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.example.totanpay.R
import com.example.totanpay.common.receipt.AddAmount
import com.example.totanpay.common.receipt.AddMaskedPanCardIssuer
import com.example.totanpay.common.receipt.AddMerchantIdTerminalId
import com.example.totanpay.common.receipt.AddMerchantNamePhone
import com.example.totanpay.common.receipt.AddRRNStan
import com.example.totanpay.common.receipt.AddTypeDateTime
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.example.totanpay.data.repository.datasource.transaction.TransactionType
import com.example.totanpay.ui.component.MainButton
import com.example.totanpay.ui.theme.Background
import com.example.totanpay.ui.theme.Black100
import com.example.totanpay.ui.theme.MARGIN_TOP_ROW
import com.example.totanpay.ui.theme.TotanPayTheme
import com.example.totanpay.ui.theme.White100
import com.google.gson.Gson

@Composable
fun TopupSuccessResult(viewModel: TopupSuccessResultViewModel, response: String,
                         onBackButtonClicked:()->Unit) {
    println("yyyyyyyyyyy->$response")
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    BackHandler {
        println("hhhhhhhhhhhhiu")
        onBackButtonClicked()
    }
    LaunchedEffect(Unit) {
        viewModel.init(response)
    }
    Column(
        Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        if (uiState.result != null)
            VoucherSuccessReceipt(isPaperReceipt = false, uiState.result)
        MainButton(
            title = "چاپ رسید مشتری", modifier = Modifier
                .padding(top = 18.dp)
                .padding(bottom = 12.dp, start = 8.dp, end = 8.dp)
                .fillMaxWidth()

        ) {
            println("yyyyyyyyyyyyyyyy")

        }
    }
}

@Composable
@Preview
fun PurchaseSuccessResultPreveiw() {
    TotanPayTheme {
        // PurchaseSuccessResult("")
    }
}

@Composable
fun VoucherSuccessReceipt(isPaperReceipt: Boolean, result: ResponseTransaction?) {
    println("aaaaPurchaseSuccessReceiptaaaaaaaa->${Gson().toJson(result)}")
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()


    Box(
        modifier = Modifier
            .layoutId("mainContentReceipt")
            .padding(horizontal = 12.dp)
            .padding(top = 30.dp)
            .background(
                color = if (isPaperReceipt) White100 else Black100,
                shape = RoundedCornerShape(16.dp)
            )
    )
    {
        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .background(if (isPaperReceipt) White100 else Black100)
        ) {
            val firstColor = if (isPaperReceipt) Black100 else White100
            val secondColor = if (isPaperReceipt) Black100 else White100
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = R.drawable.aa)
                        .apply(block = fun ImageRequest.Builder.() {
                            size(Size.ORIGINAL)
                        }).build(),
                    imageLoader = imageLoader
                ),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .align(Alignment.CenterHorizontally)
                    .size(50.dp)
                    .layoutId("successTickImage")
            )
            AddMerchantNamePhone(merchantName = result!!.merchantName, merchantPhone = result.merchantPhone, textColor =firstColor )
            AddTypeDateTime(type = TransactionType.PURCHASE.title, date = result.date, time = result.time, textColor =firstColor )
            HorizontalDivider(
                modifier = Modifier.padding(top = 10.dp),
                color = Color.White.copy(alpha = 0.16f), thickness = 1.dp
            )// TODO: dotted
            AddMerchantIdTerminalId(merchantId = result.merchantId, terminalId = result.terminalID, textColor = firstColor)
            AddMaskedPanCardIssuer(maskedPan = result.maskedPan, cardIssuer = result.issuerName, textColor = firstColor)
            AddRRNStan(rrn = result.rrn, stan =result.trace , textColor = firstColor)
            AddAmount(modifier = Modifier.padding(top = MARGIN_TOP_ROW),result.amount,firstColor)

        }
    }
}

@Composable
fun RowReceipt(first: String, second: String, firstTextColor: Color, secondTextColor: Color) {
    Row(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth()
    ) {
        Text(text = first, modifier = Modifier.padding(end = 12.dp), color = firstTextColor)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = second, modifier = Modifier.padding(start = 12.dp), color = secondTextColor)

    }
}

@Composable
@Preview
fun RowReceiptPreveiw() {
    TotanPayTheme {
        RowReceipt("خرید کالا", "رسید مشتری", White100, White100)
    }
}