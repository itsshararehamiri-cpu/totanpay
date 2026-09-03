package com.example.totanpay.feature.reports

import SelectWheelTimePicker
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.example.totanpay.R
import com.example.totanpay.common.BackButtonModifier
import com.example.totanpay.common.SelectDateModifier
import com.example.totanpay.common.mainButtonModifier
import com.example.totanpay.data.repository.datasource.transaction.TransactionType
import com.example.totanpay.ui.TextInputModifier
import com.example.totanpay.ui.component.PriceTextInput
import com.example.totanpay.ui.component.ShowToast
import com.example.totanpay.ui.component.TransactionTypeCheckbox
import com.example.totanpay.ui.component.button.BackButton
import com.example.totanpay.ui.component.button.MainButton
import com.example.totanpay.ui.component.report.SelectDateTime
import com.example.totanpay.ui.getPersianDateFrom
import com.example.totanpay.ui.selectedDateIsSmallerOrEqualThanCurrenDate
import com.example.totanpay.ui.theme.TotanPayTheme
import com.google.gson.Gson
import com.razaghimahdi.compose_persian_date.bottom_sheet.DatePickerLinearModalBottomSheet
import com.razaghimahdi.compose_persian_date.core.components.rememberDialogDatePicker
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.time.LocalTime
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTransactionReportContent(
    onBackButtonClicked: () -> Unit,
    searchTransaction: (from: String, to: String, fromAmount: String, toAmount: String, selectedTransactions: String) -> Unit
) {
    var purchaseIsSelected by remember { mutableStateOf(true) }
    var billPayIsSelected by remember { mutableStateOf(false) }
    var voucherIsSelected by remember { mutableStateOf(false) }
    var topUpIsSelected by remember { mutableStateOf(false) }

    val coroutine = rememberCoroutineScope()
    val rememberPersianBottomSheetFromDatePickerController = rememberDialogDatePicker()
    val rememberPersianBottomSheetToDatePickerController = rememberDialogDatePicker()
    val fromDateBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val toDateBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val timeBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current

    val context= LocalContext.current
    BackHandler {
        onBackButtonClicked()
    }
    var fromDateValue by remember {
        mutableStateOf(
            "${rememberPersianBottomSheetFromDatePickerController.getPersianDay()}" +
                    " ${rememberPersianBottomSheetFromDatePickerController.getPersianMonthName()}" +
                    "${rememberPersianBottomSheetFromDatePickerController.getPersianYear()}"
        )
    }
    var toDateValue by remember {
        mutableStateOf(
            "${rememberPersianBottomSheetToDatePickerController.getPersianDay()}" +
                    " ${rememberPersianBottomSheetToDatePickerController.getPersianMonthName()}" +
                    "${rememberPersianBottomSheetToDatePickerController.getPersianYear()}"
        )
    }
    val fromAmountFocusRequester = remember { FocusRequester() }
    val toAmountFocusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        fromAmountFocusRequester.freeFocus()
        toAmountFocusRequester.freeFocus()
    }
    var fromTimeValue by remember {
        mutableStateOf(LocalTime.MIN.toString())
    }
    var toTimeValue by remember {
        mutableStateOf("%02d:%02d". format(LocalTime.now().hour,LocalTime.now().minute))
    }
    var fromAmountValue by remember {
        mutableStateOf("")
    }
    var toAmountValue by remember {
        mutableStateOf("")
    }
    var currentYear by remember {
        mutableIntStateOf(0)
    }
    var currentMonth by remember {
        mutableIntStateOf(0)
    }
    var currentDay by remember {
        mutableIntStateOf(0)
    }
    var showSelectFromTime by remember {
        mutableStateOf(false)
    }
    var showSelectToTime by remember {
        mutableStateOf(false)
    }
    var showToast by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        rememberPersianBottomSheetFromDatePickerController.updateDate(date = Date())
        rememberPersianBottomSheetFromDatePickerController.updateDate(timestamp = Date().time)
        rememberPersianBottomSheetToDatePickerController.updateDate(date = Date())
        rememberPersianBottomSheetToDatePickerController.updateDate(timestamp = Date().time)
        currentYear = rememberPersianBottomSheetFromDatePickerController.getPersianYear()
        currentMonth = rememberPersianBottomSheetFromDatePickerController.getPersianMonth()
        currentDay = rememberPersianBottomSheetFromDatePickerController.getPersianDay()
        keyboard?.hide()
    }
    if (fromDateBottomSheetState.isVisible) {
        DatePickerLinearModalBottomSheet(
            submitTitle = stringResource(id = R.string.confirm),
            modifier = Modifier.fillMaxSize(),
            font = R.font.danafanum_light,
            textButtonStyle = MaterialTheme.typography.displayMedium,
            unSelectedTextStyle = MaterialTheme.typography.displayMedium,
            selectedTextStyle = MaterialTheme.typography.displayMedium,
            sheetState = fromDateBottomSheetState,
            sheetMaxWidth = BottomSheetDefaults.SheetMaxWidth,
            shape = BottomSheetDefaults.ExpandedShape,
            controller = rememberPersianBottomSheetFromDatePickerController,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            onDismissRequest = {
                coroutine.launch {
                    fromDateBottomSheetState.hide()
                }
            },
            onDateChanged = { year, month, day ->
                if (selectedDateIsSmallerOrEqualThanCurrenDate(
                        year,
                        month,
                        day,
                        currentYear,
                        currentMonth,
                        currentDay
                    )
                )
                    fromDateValue = getPersianDateFrom(year, month, day)
            }
        )
    }
    if (toDateBottomSheetState.isVisible)
        DatePickerLinearModalBottomSheet(
            submitTitle = stringResource(id = R.string.confirm),
            modifier = Modifier.fillMaxSize(),
            font = R.font.danafanum_light,
            textButtonStyle = MaterialTheme.typography.displayMedium,
            unSelectedTextStyle = MaterialTheme.typography.displayMedium,
            selectedTextStyle = MaterialTheme.typography.displayMedium,
            sheetState = toDateBottomSheetState,
            controller = rememberPersianBottomSheetToDatePickerController,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            onDismissRequest = {
                coroutine.launch {
                    toDateBottomSheetState.hide()
                }
            },
            onDateChanged = { year, month, day ->
                if (selectedDateIsSmallerOrEqualThanCurrenDate(
                        year,
                        month,
                        day,
                        currentYear,
                        currentMonth,
                        currentDay
                    )
                )
                    toDateValue = getPersianDateFrom(year, month, day) },
        )
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            BackButton(
                title = stringResource(id = R.string.details_of_transactions),
                modifier = BackButtonModifier
                    .layoutId("toolBar")
            ) {
                onBackButtonClicked()
            }
            ConstraintLayout(
                ConstraintSet {
                    val transactionTypeTitle = createRefFor("transactionTypeTitle")
                    val purchase = createRefFor("purchase")
                    val billPay = createRefFor("billPay")
                    val voucher = createRefFor("voucher")
                    val topup = createRefFor("topup")
                    val fromDate = createRefFor("fromDate")
                    val toDate = createRefFor("toDate")
                    val fromTime = createRefFor("fromTime")
                    val toTime = createRefFor("toTime")
                    val fromAmount = createRefFor("fromAmount")
                    val toAmount = createRefFor("toAmount")
                    val confirm = createRefFor("confirm")
                    val loading = createRefFor("loading")
                    val inProcessing = createRefFor("in_processing")
                    constrain(loading) {
                        top.linkTo(parent.top, 90.dp)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                    }
                    constrain(inProcessing) {
                        top.linkTo(loading.bottom, 20.dp)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                    }
                    constrain(transactionTypeTitle) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                    constrain(purchase) {
                        top.linkTo(transactionTypeTitle.bottom)
                        start.linkTo(transactionTypeTitle.start)
                    }
                    constrain(topup) {
                        top.linkTo(purchase.top)
                        bottom.linkTo(purchase.bottom)
                        end.linkTo(parent.end, 16.dp)
                    }
                    constrain(voucher) {
                        top.linkTo(purchase.bottom)
                        start.linkTo(purchase.start)
                    }
                    constrain(billPay) {
                        top.linkTo(voucher.top)
                        bottom.linkTo(voucher.bottom)
                        start.linkTo(topup.start)
                    }
                    constrain(fromDate) {
                        top.linkTo(billPay.bottom, 10.dp)
                        end.linkTo(fromTime.start)
                        start.linkTo(transactionTypeTitle.start)
                        width = Dimension.percent(0.60f)
                    }
                    constrain(fromTime) {
                        top.linkTo(fromDate.top)
                        bottom.linkTo(fromDate.bottom)
                        end.linkTo(parent.end)
                        start.linkTo(fromDate.end)
                        width = Dimension.percent(0.40f)
                    }
                    constrain(toDate) {
                        top.linkTo(fromDate.bottom)
                        end.linkTo(fromTime.start)
                        start.linkTo(transactionTypeTitle.start)
                        width = Dimension.percent(0.60f)
                    }
                    constrain(toTime) {
                        top.linkTo(toDate.top)
                        bottom.linkTo(toDate.bottom)
                        end.linkTo(fromTime.end)
                        start.linkTo(fromTime.start)
                        width = Dimension.percent(0.40f)
                    }
                    constrain(fromAmount) {
                        top.linkTo(toDate.bottom, 10.dp)
                        end.linkTo(parent.end)
                        start.linkTo(transactionTypeTitle.start)
                    }
                    constrain(toAmount) {
                        top.linkTo(fromAmount.bottom)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                    }
                    constrain(confirm) {
                        top.linkTo(toAmount.bottom)
                        end.linkTo(parent.end)
                        start.linkTo(transactionTypeTitle.start)
                    }
                }, modifier = Modifier
                    .verticalScroll(scrollState)
                    .padding(16.dp)
                    .padding(bottom = 70.dp)
            ) {
                Text(
                    text = stringResource(R.string.transaction_type),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .layoutId("transactionTypeTitle"),
                    style = MaterialTheme.typography.bodyLarge
                )
                TransactionTypeCheckbox(modifier = Modifier.layoutId("purchase"),
                    title =context.getString( TransactionType.PURCHASE.title),
                    isChecked = purchaseIsSelected,
                    onCheckedChange = {
                        purchaseIsSelected = it
                    })
                TransactionTypeCheckbox(modifier = Modifier.layoutId("voucher"),
                    title = context.getString(TransactionType.VOUCHER.title),
                    isChecked = voucherIsSelected,
                    onCheckedChange = {
                        voucherIsSelected = it
                    })
                TransactionTypeCheckbox(modifier = Modifier.layoutId("topup"),
                    title = context.getString(TransactionType.TOPUP.title),
                    isChecked = topUpIsSelected,
                    onCheckedChange = {
                        topUpIsSelected = it
                    })
                TransactionTypeCheckbox(modifier = Modifier.layoutId("billPay"),
                    title = context.getString(TransactionType.BILL_PAY.title),
                    isChecked = billPayIsSelected,
                    onCheckedChange = {
                        billPayIsSelected = it
                    })
                SelectDateTime(
                    modifier = SelectDateModifier
                        .layoutId("fromDate"),
                    title = stringResource(id = R.string.from_date),
                    isTime = false,
                    value = fromDateValue
                ) {
                    coroutine.launch { fromDateBottomSheetState.show() }
                }
                SelectDateTime(
                    modifier = SelectDateModifier
                        .layoutId("fromTime"), isTime = true,

                    title = stringResource(id = R.string.from_time),
                    value = fromTimeValue
                ) {
                    showSelectFromTime = true
                }
                SelectDateTime(
                    modifier = SelectDateModifier
                        .layoutId("toDate"),
                    title = stringResource(id = R.string.to_date), isTime = false,

                    value = toDateValue
                ) {
                    coroutine.launch { toDateBottomSheetState.show() }
                }
                SelectDateTime(
                    modifier = SelectDateModifier
                        .layoutId("toTime"),
                    title = stringResource(id = R.string.to_time),
                    isTime = true,
                    value = toTimeValue
                ) {
                    showSelectToTime = true
                }
                PriceTextInput(
                    modifier = TextInputModifier
                        .focusRequester(fromAmountFocusRequester)
                        .layoutId("fromAmount"),
                    hasError = false,
                    errorMessage = "",
                    title = stringResource(R.string.from_amount),
                    trailerTitle = stringResource(id = R.string.currency),
                    value = fromAmountValue,
                    onNextClicked = {
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                ) {
                    fromAmountValue = it
                }
                PriceTextInput(
                    modifier = TextInputModifier
                        .focusRequester(toAmountFocusRequester)
                        .layoutId("toAmount"),
                    hasError = false,
                    errorMessage = "",
                    title = stringResource(R.string.to_amount),
                    trailerTitle = stringResource(id = R.string.currency),
                    value = toAmountValue,
                    onNextClicked = {
                        keyboard?.hide()
                    }
                ) {
                    toAmountValue = it
                }
            }
        }
        MainButton(
            modifier = Modifier
                .mainButtonModifier(isSmall = false)
                .align(Alignment.BottomCenter)
        ) {
            showToast=false
            if(!purchaseIsSelected && !billPayIsSelected && !voucherIsSelected && !topUpIsSelected)
            {
                showToast=true
            }
            else{
                val temp = DateContainer(
                    rememberPersianBottomSheetFromDatePickerController.getPersianYear().toString(),
                    rememberPersianBottomSheetFromDatePickerController.getPersianMonth().toString(),
                    rememberPersianBottomSheetFromDatePickerController.getPersianDay().toString(),
                    hour = fromTimeValue.dropLast(3),
                    minute =fromTimeValue.drop(3)
                )
                val from = Gson().toJson(temp)
                val tt = DateContainer(
                    rememberPersianBottomSheetToDatePickerController.getPersianYear().toString(),
                    rememberPersianBottomSheetToDatePickerController.getPersianMonth().toString(),
                    rememberPersianBottomSheetToDatePickerController.getPersianDay().toString(),
                    hour = "${if(toTimeValue.drop(3).toInt()+1<60)toTimeValue.dropLast(3) else toTimeValue.dropLast(3)+1}",
                    minute = "${if(toTimeValue.drop(3).toInt()+1<60)toTimeValue.drop(3).toInt()+1 else 0}"
                )
                val to = Gson().toJson(tt)
                val transactionType = JSONObject()
                transactionType.apply {
                    put("purchaseType", if (purchaseIsSelected) "has" else "dontHas")
                    put("billPayType", if (billPayIsSelected) "has" else "dontHas")
                    put("voucherType", if (voucherIsSelected) "has" else "dontHas")
                    put("topupType", if (topUpIsSelected) "has" else "dontHas")
                }
                searchTransaction(
                    from, to, fromAmountValue.ifEmpty { "-1" },
                    toAmountValue.ifEmpty { "-1" }, transactionType.toString()

                )
            }
        }
        if (showSelectFromTime || showSelectToTime) {
            ModalBottomSheet(sheetState = timeBottomSheetState,
                containerColor = MaterialTheme.colorScheme.surface,
                onDismissRequest = {
                    if (showSelectToTime) {
                        showSelectToTime = false
                    }
                    if (showSelectFromTime) {
                        showSelectFromTime = false
                    }
                }, modifier = Modifier.fillMaxSize())
            {
                SelectWheelTimePicker(isSmall = false, onTimeSelected = { time ->
                    if (showSelectToTime) {
                        toTimeValue = time
                        showSelectToTime = false
                    }
                    if (showSelectFromTime) {
                        fromTimeValue = time
                        showSelectFromTime = false
                    }
                })
            }
        }
        if (showToast) {
            ShowToast(
                modifier = Modifier.align(Alignment.BottomCenter),
                message = stringResource(R.string.no_transactions_selected)
            ) {
                showToast = false
            }
        }
    }

}
