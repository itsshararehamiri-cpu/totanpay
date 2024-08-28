package com.example.totanpay.feature.balance

import android.graphics.Bitmap
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.example.totanpay.common.receipt.AddBalance
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BalanceSuccessResultScreen(viewModel: BalanceSuccessResultViewModel,
                               response:String,
                               onBackButtonClicked:()->Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var receiptBitmap: Bitmap? by remember {
        mutableStateOf(null)
    }
    var startPrint by remember {
        mutableStateOf(true)// TODO:
    }
    var print by remember {
        mutableStateOf(false)
    }
    val deviceManager = LocalDeviceManager.current
    BackHandler {
        onBackButtonClicked()
    }
    LaunchedEffect(Unit) {
        viewModel.init(response)
    }
    ReceiptUi(content = {
       aa()
    }) {
        receiptBitmap = it
    }
    if (receiptBitmap != null ) {
        LaunchedEffect(startPrint) {
            deviceManager.print(bitmap = receiptBitmap!!, context = context)
        }
    }
    Column(
        Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        if (uiState.result != null)
            BalanceSuccessReceipt(isPaperReceipt = false, uiState.result)
        MainButton(
            title = "چاپ رسید مشتری", modifier = Modifier
                .padding(top = 8.dp)
                .padding(bottom = 12.dp, start = 8.dp, end = 8.dp)
                .fillMaxWidth()
        ) {
        }
    }
}
@Composable
fun BalanceSuccessReceipt(isPaperReceipt: Boolean, result: ResponseTransaction?) {
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
    Box(modifier = Modifier
                .layoutId("mainContentReceipt")
                .padding(horizontal = 12.dp)
                .padding(top = 30.dp)
                .background(color = if (isPaperReceipt) White100 else Black100, shape = RoundedCornerShape(16.dp))) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .background(if (isPaperReceipt) White100 else Black100)) {
                val firstColor = if (isPaperReceipt) Black100 else White100
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
                AddTypeDateTime(modifier = Modifier.padding(top = MARGIN_TOP_ROW),type = TransactionType.BALANCE.title, date = result.date, time = result.time, textColor =firstColor )
                HorizontalDivider(
                    modifier = Modifier.padding(top = 10.dp),
                    color = Color.White.copy(alpha = 0.16f), thickness = 1.dp
                )// TODO: dotted
                AddMerchantIdTerminalId(modifier = Modifier.padding(top = MARGIN_TOP_ROW),merchantId = result.merchantId, terminalId = result.terminalID, textColor = firstColor)
                AddMaskedPanCardIssuer(modifier = Modifier.padding(top = MARGIN_TOP_ROW),maskedPan = result.maskedPan, cardIssuer = result.issuerName, textColor = firstColor)
                AddRRNStan(modifier = Modifier.padding(top = MARGIN_TOP_ROW),rrn = result.rrn, stan =result.trace , textColor = firstColor)
              //  AddAmount(result.amount,firstColor)
                AddBalance(modifier = Modifier.padding(top = MARGIN_TOP_ROW),result.realBalance!!,firstColor)
            }
        }
    }
@Composable
fun aa(){
    Column(modifier = Modifier.background(Color.White)) {
//        RowReceipt(first ="dddd" , second = "gggg", textColor = Black600 , isPaperReceipt = true )
//        RowReceipt(first ="dddd" , second = "gggg", textColor = Black600 , isPaperReceipt = true )

        Text(text = "gggggggggggggggggggggg111111111111111111333333333", fontSize = 11.sp)
        Text(text = "gggggggggggggggggggggg111111111111111111333333333", fontSize = 11.sp)
        Text(text = "gggggggggggggggggggggg")
        Text(text = "gggggggggggggggggggggg")
        Text(text = "gggggggggggggggggggggg")
        Text (text = "gggggggggggggggggggggg")
    }
}
@Composable
@Preview
fun aaP(){
    TotanPayTheme {
        aa()
    }
}