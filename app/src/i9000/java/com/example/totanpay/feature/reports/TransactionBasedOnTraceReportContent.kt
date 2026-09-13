package com.example.totanpay.feature.reports

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.layoutId
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.example.totanpay.R
import com.example.totanpay.ResultReceiptContainer
import com.example.totanpay.common.BackButtonModifier
import com.example.totanpay.common.ReceiptResultContainer
import com.example.totanpay.common.mainButtonModifier
import com.example.totanpay.data.util.isNotNumber
import com.example.totanpay.data.util.toEnglishNumber
import com.example.totanpay.feature.purchase.ReceiptContent
import com.example.totanpay.receipt.ReceiptType
import com.example.totanpay.ui.TextInputModifier
import com.example.totanpay.ui.component.TextInput
import com.example.totanpay.ui.component.button.BackButton
import com.example.totanpay.ui.component.button.MainButton
import com.example.totanpay.ui.theme.Dimensions.LOADING_HEIGHT
import com.example.totanpay.ui.theme.END_PADDING
import com.example.totanpay.ui.theme.START_PADDING

@Composable
fun TransactionBasedOnTraceReportContent(errorInPrint: String,
    uiState: SearchTransactionUiState,
    onBackClicked: () -> Unit,
    onConfirm: (String, Boolean) -> Unit,
                                         clearPrintErrorMessage:()-> Unit,
    onPrintClicked: () -> Unit
) {
    val keyboard = LocalSoftwareKeyboardController.current
    var trace by remember {
        mutableStateOf("")
    }
    var hasError by remember {
        mutableStateOf(false)
    }
    var traceIsSelected by remember { mutableStateOf(true) }
    Box(
        modifier =  Modifier.fillMaxSize() ){
        ConstraintLayout(
        ConstraintSet {
            val toolBar = createRefFor("toolBar")
            val traceRadioButton=createRefFor("traceRadioButton")
            val rrnRadioButton=createRefFor("rrnRadioButton")
            val traceInputText = createRefFor("traceInputText")
            val confirm = createRefFor("confirm")
            val result = createRefFor("mainContentReceipt")
            val print = createRefFor("print")
            val loading = createRefFor("loading")
            val inProcessing = createRefFor("inProcessing")
            val notFound = createRefFor("notFound")
            constrain(toolBar) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }
            constrain(loading) {
                top.linkTo(toolBar.bottom)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }
            constrain(inProcessing) {
                top.linkTo(loading.bottom)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }
            constrain(traceRadioButton) {
                top.linkTo(toolBar.bottom, 20.dp)
                end.linkTo(parent.end)
                start.linkTo(rrnRadioButton.end)
                width= Dimension.fillToConstraints
            }
            constrain(rrnRadioButton) {
                top.linkTo(traceRadioButton.top)
                bottom.linkTo(traceRadioButton.bottom)
                end.linkTo(traceRadioButton.start)
                start.linkTo(parent.start)
                width= Dimension.fillToConstraints
                height=Dimension.fillToConstraints
            }
            constrain(traceInputText) {
                top.linkTo(traceRadioButton.bottom)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }
            constrain(confirm) {
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }
            constrain(result) {
                top.linkTo(toolBar.bottom)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }
            constrain(print) {
                top.linkTo(result.bottom, 20.dp)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }
            constrain(notFound) {
                top.linkTo(toolBar.bottom)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }
        }, modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        if (uiState.isInitState) {
            BackButton(
                title = stringResource(id = R.string.report_based_on_trace),
                modifier =BackButtonModifier
                    .layoutId("toolBar")
            ) {
                onBackClicked()
            }
            Box(
                modifier = Modifier
                    .padding(start = END_PADDING, end = START_PADDING, top = 3.dp)
                    .fillMaxWidth()
                    .border(
                        width = 1.dp, brush = Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                                MaterialTheme.colorScheme.onBackground.copy(alpha = 1f)
                            )
                        ), shape = RoundedCornerShape(16.dp)
                    )
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                    .layoutId("rrnRadioButton")

            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = traceIsSelected, onClick = {
                            traceIsSelected=true
                        }, colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.primary,
                            unselectedColor = androidx.compose.ui.graphics.Color.Gray
                        )
                    )
                    Text(
                        modifier = Modifier.wrapContentWidth(),
                        text = stringResource(R.string.trace),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.fillMaxWidth(1f))
                }
            }
            Box(
                modifier = Modifier
                    .padding(start = END_PADDING, end = START_PADDING, top = 3.dp)
                    .fillMaxWidth()
                    .border(
                        width = 1.dp, brush = Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                                MaterialTheme.colorScheme.onBackground.copy(alpha = 1f)
                            )
                        ), shape = RoundedCornerShape(16.dp)
                    )
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                    .layoutId("traceRadioButton")

            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = !traceIsSelected, onClick = {
                            traceIsSelected=false
                        }, colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.primary,
                            unselectedColor = androidx.compose.ui.graphics.Color.Gray
                        )
                    )
                    Text(
                        modifier = Modifier.wrapContentWidth(),
                        text = stringResource(R.string.rrn),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.fillMaxWidth(1f))
                }
            }
            TextInput(
                modifier = TextInputModifier
                    .layoutId("traceInputText"),
                errorMessage = stringResource(if(traceIsSelected)R.string.plz_enter_trace
                else R.string.plz_enter_rrn),
                title = "",
                placeholder = stringResource(id =if(traceIsSelected) R.string.trace else R.string.rrn),
                value = trace, hasError = hasError, onNextClicked = {
                    keyboard?.hide()
                }, isSmall =false,onValueChange = {
                    if (!it.trim().toEnglishNumber().isNotNumber())
                        trace = it.trim()
                })

            MainButton(
                modifier = Modifier.
                mainButtonModifier(isSmall = false )
                    .layoutId("confirm")
            ) {
                hasError = false
                if (trace.isNotEmpty())
                    onConfirm(trace.trim(),traceIsSelected)
                else {
                    hasError = true
                }
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
                }.build()

            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = R.drawable.loading)
                        .build(),
                    imageLoader = imageLoader
                ),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .layoutId("loading")
                    .fillMaxWidth()
                    .height(LOADING_HEIGHT),
                alignment = Alignment.Center
            )
            Text(
                text = stringResource(id = R.string.in_searching),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .layoutId("inProcessing"),
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.Center

            )
        }
        if (uiState.result != null && !uiState.showProgress) {
            BackButton(
                title = stringResource(id = R.string.report_based_on_trace),
                modifier =BackButtonModifier
                    .layoutId("toolBar")
            ) {
                onBackClicked()
            }
            ReceiptResultContainer(showCustomerPrintButton = true,
                showMerchantPrintButton = false,
                printTitle = stringResource(id = R.string.print),
                errorInPrint =errorInPrint,
                onCustomerPrintButtonClicked = { onPrintClicked() },
                onMerchantPrintButtonClicked = {},

                clearPrintErrorMessage = {
                    clearPrintErrorMessage()
                },
                onBackButtonClicked = {
                    onBackClicked()
                }) {
                ResultReceiptContainer(
                    isPaperReceipt = false,
                    modifier = Modifier.layoutId("receipt"), isSuccess = true
                ) {
                    ReceiptContent(false, uiState.result,ReceiptType.CUSTOMER_RECEIPT)
                }
            }
        }
        if (uiState.showNotFounding && !uiState.showProgress) {
            NotFoundTransaction(title = stringResource(id = R.string.report_based_on_trace),modifier = Modifier.layoutId("notFound")){
                onBackClicked()
            }
        }
    }
}
}