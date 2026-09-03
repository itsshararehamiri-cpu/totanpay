package com.example.totanpay.feature.reports

import android.graphics.Bitmap
import android.os.Build.VERSION.SDK_INT
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.totanpay.R
import com.example.totanpay.ResultReceiptContainer
import com.example.totanpay.common.BackButtonModifier
import com.example.totanpay.common.ReceiptResultContainer
import com.example.totanpay.common.isSmall
import com.example.totanpay.feature.purchase.ReceiptContent
import com.example.totanpay.receipt.ReceiptType
import com.example.totanpay.receipt.ReceiptUi
import com.example.totanpay.ui.component.Loading
import com.example.totanpay.ui.component.ShowToast
import com.example.totanpay.ui.component.button.BackButton
import kotlin.text.ifEmpty

@Composable
fun LastTransactionReportScreen(
    viewModel: LastTransactionViewModel, onBackButtonClicked: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    BackHandler {
        onBackButtonClicked()
    }
    var receiptBitmap: Bitmap? by remember {
        mutableStateOf(null)
    }
    var startPrint by remember {
        mutableStateOf(false)
    }
    var errorInPrint by remember { mutableStateOf("") }
    if (uiState.result != null) {
        ReceiptUi(content = {
            ReceiptContent(true, uiState.result, ReceiptType.DUPLICATE_RECEIPT)
        }) {
            receiptBitmap = it
        }
    }
    LaunchedEffect(startPrint) {
        if (receiptBitmap != null && startPrint) {
            viewModel.print(
                bitmap = receiptBitmap!!,
                context = context,
                onSuccess = {},
                onFailed = {
                    errorInPrint=it
                })
            startPrint = false
        }
    }
    if (uiState.showProgress) {
        val imageLoader = ImageLoader.Builder(LocalContext.current)
            .components {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
        Loading(waitingTitle = stringResource(R.string.in_searching))
    }
    if (uiState.result != null && !uiState.showProgress) {
        BackButton(
            title = stringResource(id = R.string.last_transaction), modifier = BackButtonModifier
        ) {
            onBackButtonClicked()
        }
        ReceiptResultContainer(
            showMerchantPrintButton = false,
            showCustomerPrintButton = true,
            errorInPrint = errorInPrint,
            printTitle = stringResource(id = R.string.print),
            onCustomerPrintButtonClicked = { startPrint = true },
            onMerchantPrintButtonClicked = {},
            onBackButtonClicked = {
                onBackButtonClicked()
            }, clearPrintErrorMessage = {
                errorInPrint=""
            }) {
            ResultReceiptContainer(
                isPaperReceipt = false,
                modifier = Modifier.layoutId("receipt"), isSuccess = true
            ) {
                ReceiptContent(false, uiState.result, ReceiptType.DUPLICATE_RECEIPT)
            }
        }
    } else if (!uiState.showProgress) {
        NotFoundTransaction(title = stringResource(id = R.string.last_transaction)) {
            onBackButtonClicked()
        }

    }
}

@Composable
fun NotFoundTransaction(
    title: String,
    modifier: Modifier = Modifier,
    onBackButtonClicked: () -> Unit
) {
    val context = LocalContext.current
    ConstraintLayout(
        ConstraintSet {
            val toolBar = createRefFor("toolBar")
            val notFoundImage = createRefFor("notFoundImage")
            val notFoundTitle = createRefFor("notFoundTitle")
            constrain(toolBar) {
                top.linkTo(parent.top)
                end.linkTo(parent.end, 0.dp)
                start.linkTo(parent.start, 0.dp)
            }
            constrain(notFoundImage) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end, 0.dp)
                start.linkTo(parent.start, 0.dp)
            }
            constrain(notFoundTitle) {
                top.linkTo(notFoundImage.bottom)
                end.linkTo(parent.end, 0.dp)
                start.linkTo(parent.start, 0.dp)
            }
        }, modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        if (!isSmall(context))
            BackButton(
                title = title,
                modifier = BackButtonModifier.layoutId("toolBar")
            ) {
                onBackButtonClicked()
            }
        Image(
            painter = painterResource(id = R.drawable.not_fount_transaction),
            contentDescription = stringResource(
                id = R.string.not_found_transaction
            ),
            modifier = Modifier
                .size(if (isSmall(context)) 110.dp else 190.dp)
                .layoutId("notFoundImage"),
            contentScale = if (isSmall(context)) ContentScale.Fit else ContentScale.FillBounds,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.outlineVariant)
        )
        Text(
            text = stringResource(
                id = R.string.not_found_transaction
            ),
            Modifier
                .padding(top = if (isSmall(context)) 8.dp else 12.dp)
                .layoutId("notFoundTitle"),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}


