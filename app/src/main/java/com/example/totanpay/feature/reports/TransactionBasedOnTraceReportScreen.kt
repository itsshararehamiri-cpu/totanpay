package com.example.totanpay.feature.reports

import android.graphics.Bitmap
import android.os.Build.VERSION.SDK_INT
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.example.totanpay.R
import com.example.totanpay.ResultReceiptContainer
import com.example.totanpay.common.BackButtonModifier
import com.example.totanpay.common.ReceiptResultContainer
import com.example.totanpay.common.isSmall
import com.example.totanpay.common.mainButtonModifier
import com.example.totanpay.data.util.isNotNumber
import com.example.totanpay.feature.purchase.ReceiptContent
import com.example.totanpay.receipt.ReceiptUi
import com.example.totanpay.ui.TextInputModifier
import com.example.totanpay.ui.component.TextInput
import com.example.totanpay.ui.component.button.BackButton
import com.example.totanpay.ui.component.button.MainButton
import com.example.totanpay.ui.theme.Dimensions.LOADING_HEIGHT
import com.example.totanpay.ui.theme.TotanPayTheme

@Composable
fun TransactionBasedOnTraceReportScreen(
    viewModel: SearchTransactionViewModel,
    onBackButtonClicked: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val keyboard = LocalSoftwareKeyboardController.current
    BackHandler {
        onBackButtonClicked()
    }
    var receiptBitmap: Bitmap? by remember {
        mutableStateOf(null)
    }
    var startPrint by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(startPrint) {
        if (receiptBitmap != null && startPrint) {
            viewModel.print(bitmap = receiptBitmap!!, context = context, onSuccess = {}, onFailed = {})
            startPrint = false
        }
    }
    if (uiState.result != null) {
        ReceiptUi(content = {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                ReceiptContent(true, uiState.result)
            }
        }) {
            receiptBitmap = it
        }
    }
    TransactionBasedOnTraceReportContent(
        uiState = uiState,
        onBackClicked = { onBackButtonClicked() },
        onConfirm = {
            keyboard?.hide()
            viewModel.search(it)
        }, onPrintClicked = {
            startPrint = true
        })
}

