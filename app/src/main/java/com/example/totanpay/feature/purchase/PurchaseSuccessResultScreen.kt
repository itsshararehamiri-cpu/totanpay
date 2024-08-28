package com.example.totanpay.feature.purchase

import android.graphics.Bitmap
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
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
import com.example.totanpay.ReceiptUi
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
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PurchaseSuccessResult(
    viewModel: PurchaseSuccessResultViewModel,
    response: String,
    onBackButtonClicked: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var receiptBitmap: Bitmap? by remember {
        mutableStateOf(null)
    }
    var secondsToRemainInScreen: Int by remember {
        mutableStateOf(15000)
    }
    var startPrint by remember {
        mutableStateOf(false)
    }
    BackHandler {
        onBackButtonClicked()
    }
    LaunchedEffect(Unit) {
        viewModel.init(response)
    }
    LaunchedEffect(Unit) {
        while (secondsToRemainInScreen > 0) {
            delay(1.seconds)
            secondsToRemainInScreen -= 1
            if (secondsToRemainInScreen == 0) {
                onBackButtonClicked()
            }
        }
    }
    ReceiptUi(content = {
        if (uiState.result != null)
//            Box(
//                modifier = Modifier
//                    .layoutId("mainContentReceipt")
//                    .padding(horizontal = 12.dp)
//                    .padding(top = 30.dp)
//                    .background(
//                        color = White100,
//                        shape = RoundedCornerShape(16.dp)
//                    )
//            )
//            {
                Column(modifier = Modifier.fillMaxWidth() .background(
                    color = White100), horizontalAlignment = Alignment.CenterHorizontally) {
                    ReceiptContent(true, uiState.result)
                }
           // }
    }) {
        receiptBitmap = it
    }
    LaunchedEffect(startPrint) {
        if (receiptBitmap != null) {
            viewModel.print(bitmap = receiptBitmap!!, context = context)
            startPrint=false
        }
    }
    Column(
        Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        if (uiState.result != null)
            PurchaseSuccessReceipt(isPaperReceipt = false, uiState.result)
        MainButton(
            title = "چاپ رسید مشتری", modifier = Modifier
                .padding(top = 18.dp)
                .padding(bottom = 12.dp, start = 8.dp, end = 8.dp)
                .fillMaxWidth()

        ) {
            startPrint=true
        }
        val imageLoader = ImageLoader.Builder(LocalContext.current)
            .components {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data(data = receiptBitmap)
                    .apply(block = fun ImageRequest.Builder.() {
                        size(Size.ORIGINAL)
                    }).build(),
                imageLoader = imageLoader
            ),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth()
               // .padding(top = 20.dp)
                .align(Alignment.CenterHorizontally)
              //  .size(50.dp)
                .layoutId("successTickImage")
        )
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
fun PurchaseSuccessReceipt(isPaperReceipt: Boolean, result: ResponseTransaction?) {
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
        Column {
            if (!isPaperReceipt)
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
            ReceiptContent(false, result)
        }
    }
}

@Composable
fun ReceiptContent(isPaperReceipt: Boolean, result: ResponseTransaction?) {
    Column(
        modifier = Modifier
            .padding(horizontal = if (isPaperReceipt) 0.dp else 12.dp)
            .background(if (isPaperReceipt) White100 else Black100)
    ) {
        val firstColor = if (isPaperReceipt) Black100 else White100
        AddMerchantNamePhone(modifier = Modifier.padding(top = MARGIN_TOP_ROW),
            merchantName = result!!.merchantName,
            merchantPhone = result.merchantPhone,
            textColor = firstColor,isPaperReceipt
        )
        AddTypeDateTime(modifier = Modifier.padding(top = MARGIN_TOP_ROW),
            type = TransactionType.PURCHASE.title,
            date = result.date,
            time = result.time,
            textColor = firstColor,isPaperReceipt
        )
        HorizontalDivider(
            modifier = Modifier.padding(top = 10.dp),
            color = Color.White.copy(alpha = 0.16f), thickness = 1.dp
        )// TODO: dotted
        AddMerchantIdTerminalId(modifier = Modifier.padding(top = MARGIN_TOP_ROW),
            merchantId = result.merchantId,
            terminalId = result.terminalID,
            textColor = firstColor,isPaperReceipt
        )
        AddMaskedPanCardIssuer(modifier = Modifier.padding(top = MARGIN_TOP_ROW),
            maskedPan = result.maskedPan,
            cardIssuer = result.issuerName,
            textColor = firstColor,isPaperReceipt
        )
        AddRRNStan(modifier = Modifier.padding(top = MARGIN_TOP_ROW),rrn = result.rrn, stan = result.trace, textColor = firstColor,isPaperReceipt)
        AddAmount(modifier = Modifier.padding(top = MARGIN_TOP_ROW),result.amount, firstColor,isPaperReceipt)
    }
}

@Composable
fun RowReceipt(
    first: String,
    second: String,
    firstTextColor: Color,
    secondTextColor: Color,
    isPaperReceipt: Boolean = false
) {
    Row(
        modifier = Modifier
            .padding(top = 5.dp)
            .padding(horizontal = if (isPaperReceipt) 1.dp else 12.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = first,
            modifier = Modifier.padding(end = if (isPaperReceipt) 2.dp else 12.dp),
            color = firstTextColor
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = second,
            modifier = Modifier.padding(start = if (isPaperReceipt) 2.dp else 12.dp),
            color = secondTextColor
        )

    }
}

@Composable
@Preview
fun RowReceiptPreveiw() {
    TotanPayTheme {
        RowReceipt("خرید کالا", "رسید مشتری", White100, White100, true)
    }
}

@Composable
@Preview
fun ReceiptContentPreveiw() {
    TotanPayTheme {
        ReceiptContent(
            true, result = ResponseTransaction(
                responseCode = "00",
                responseMessage = "خطا",
                rrn = "1",
                trace = "2",
                merchantName = "تست",
                merchantId = "3",
                merchantPhone = "0214236598",
                terminalID = "2",
                transactionType = "خرید",
                date = "1403/10/08",
                time = "10:22",
                issuerName = "صادرات",
                amount = "10000",
                availableBalance = "10000", maskedPan = "6037********78",
                realBalance = "13", voucherPin = "12"
            )
        )
    }
}