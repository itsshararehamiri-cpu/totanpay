package com.example.totanpay.feature.topup

import android.graphics.Bitmap
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.example.totanpay.LocalDeviceManager
import com.example.totanpay.R
import com.example.totanpay.ReceiptUi
import com.example.totanpay.common.receipt.AddMaskedPanCardIssuer
import com.example.totanpay.common.receipt.AddMerchantIdTerminalId
import com.example.totanpay.common.receipt.AddMerchantNamePhone
import com.example.totanpay.common.receipt.AddRRNStan
import com.example.totanpay.common.receipt.AddTypeDateTime
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.example.totanpay.data.repository.datasource.transaction.TransactionType
import com.example.totanpay.feature.purchase.RowReceipt
import com.example.totanpay.ui.component.MainButton
import com.example.totanpay.ui.theme.Background
import com.example.totanpay.ui.theme.Black100
import com.example.totanpay.ui.theme.TotanPayTheme
import com.example.totanpay.ui.theme.White100
import com.google.gson.Gson

@Composable
fun TopupUnSuccessResult(
    viewModel: TopupUnUnSuccessResultViewModel,
    response: String,
    onBackButtonClicked: () -> Unit
) {
    println("yyyyyyyyyyy->$response")
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val deviceManager = LocalDeviceManager.current
    val context = LocalContext.current
    var receiptBitmap: Bitmap? by remember {
        mutableStateOf(null)
    }
    val startPrint by remember {
        mutableStateOf(false)
    }
    BackHandler {
        println("hhhhhhhhhhhhiu")
        onBackButtonClicked()
    }
    ReceiptUi(content = {
        if (uiState.result != null)
            Box(
                modifier = Modifier
                    .layoutId("mainContentReceipt")
                    .padding(horizontal = 12.dp)
                    .padding(top = 30.dp)
                    .background(
                        color = White100,
                        shape = RoundedCornerShape(16.dp)
                    )
            )
            {
                Column(modifier = Modifier.fillMaxWidth()) {
                    UnSuccessReceiptContent(true, uiState.result)
                }
            }
    }) {
        receiptBitmap = it
    }
    if (receiptBitmap != null) {
        LaunchedEffect(startPrint) {
            deviceManager.print(bitmap = receiptBitmap!!, context = context)
        }
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
            VoucherUnSuccessReceipt(isPaperReceipt = false, uiState.result)
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
fun VoucherUnSuccessReceipt(isPaperReceipt: Boolean, result: ResponseTransaction?) {
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
            UnSuccessReceiptContent(false, result)
        }
    }
}

@Composable
fun UnSuccessReceiptContent(isPaperReceipt: Boolean, result: ResponseTransaction?) {
    Column(
        modifier = Modifier
            .padding(horizontal = if (isPaperReceipt) 0.dp else 12.dp)
            .fillMaxWidth()
            .padding(horizontal = if (isPaperReceipt) 0.dp else 12.dp)
            .background(if (isPaperReceipt) White100 else Black100)
    ) {
        val firstColor = if (isPaperReceipt) Black100 else White100
        AddMerchantNamePhone(merchantName = result!!.merchantName, merchantPhone = result.merchantPhone, textColor =firstColor )
        AddTypeDateTime(type = TransactionType.PURCHASE.title, date = result.date, time = result.time, textColor =firstColor )
        HorizontalDivider(
            modifier = Modifier.padding(top = 10.dp),
            color = Color.White.copy(alpha = 0.16f), thickness = 1.dp
        )// TODO: dotted
        AddMerchantIdTerminalId(merchantId = result.merchantId, terminalId = result.terminalID, textColor = firstColor)
        AddMaskedPanCardIssuer(maskedPan = result.maskedPan, cardIssuer = result.issuerName, textColor = firstColor)
        AddRRNStan(rrn = result.rrn, stan =result.trace , textColor = firstColor)
        if(result.responseCode!="-1")
        {
            Text(
                text = "${stringResource(id = R.string.unsuccess_transaction)} - ${result.responseCode}", textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium.copy(fontSize = if (isPaperReceipt) 10.sp else 16.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                color = firstColor.copy(alpha = 0.72f),
            )
        }
        else{
            Text(
                text = "${stringResource(id = R.string.amount)} ${result.amount}", textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium.copy(fontSize = if (isPaperReceipt) 10.sp else 16.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                color = firstColor.copy(alpha = 0.72f),
            )
            Text(
                text = "در صورت کسر وجه از حساب شما", textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium.copy(fontSize = if (isPaperReceipt) 10.sp else 16.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                color = firstColor.copy(alpha = 0.72f),
            )
            Text(
                text =" مبلغ  طی 72 ساعت به حساب شما باز خواهد گشت", textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium.copy(fontSize = if (isPaperReceipt) 10.sp else 16.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                color = firstColor.copy(alpha = 0.72f),
            )
        }
    }
}


@Composable
@Preview
fun UnSuccessReceiptContentPreveiw() {
    TotanPayTheme {
        UnSuccessReceiptContent(
            true, result = ResponseTransaction(
                responseCode = "-1",
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