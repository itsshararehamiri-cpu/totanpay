package com.example.totanpay.feature.reports

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.example.totanpay.R
import com.example.totanpay.ui.component.BackButton
import com.example.totanpay.ui.component.DateRange
import com.example.totanpay.ui.component.MainButton
import com.example.totanpay.ui.theme.Background
import com.example.totanpay.ui.theme.Black100
import com.example.totanpay.ui.theme.END_PADDING
import com.example.totanpay.ui.theme.HEIGHT_TEXT_INPUT
import com.example.totanpay.ui.theme.MARGIN_BOTTOM_MAIN_CONFIRM
import com.example.totanpay.ui.theme.START_PADDING
import com.example.totanpay.ui.theme.TotanPayTheme
import com.example.totanpay.ui.theme.White100
import com.razaghimahdi.compose_persian_date.bottom_sheet.DatePickerLinearModalBottomSheet
import com.razaghimahdi.compose_persian_date.core.components.rememberDialogDatePicker
import kotlinx.coroutines.launch
import java.util.Date

@Composable
fun DetailsTransactionReportScreen(onBackClicked: () -> Unit) {
    BackHandler {
        onBackClicked()
    }
    DetailTransactionReportContent(onBackClicked = { onBackClicked() })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTransactionReportContent(
    onBackClicked: () -> Unit
) {
    var fromDate by remember {
        mutableStateOf("")
    }
    var toDate by remember {
        mutableStateOf("")
    }
    val coroutine = rememberCoroutineScope()
    val rememberPersianDialogFromDatePicker = rememberDialogDatePicker()
    val rememberPersianDialogToDatePicker = rememberDialogDatePicker()

    val rememberPersianBottomSheetFromDatePickerController = rememberDialogDatePicker()
    val rememberPersianBottomSheetToDatePickerController = rememberDialogDatePicker()

    val fromDateBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val toDateBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(key1 = Unit) {
        rememberPersianDialogFromDatePicker.updateDate(date = Date())
        rememberPersianDialogFromDatePicker.updateDate(timestamp = Date().time)
        rememberPersianBottomSheetFromDatePickerController.updateDate(date = Date())
        rememberPersianBottomSheetFromDatePickerController.updateDate(timestamp = Date().time)
        rememberPersianDialogFromDatePicker.updateMinYear(1403)
        rememberPersianDialogFromDatePicker.updateYearRange(10)
        rememberPersianDialogFromDatePicker.updateDisplayMonthNames(true)


        rememberPersianDialogToDatePicker.updateDate(date = Date())
        rememberPersianDialogToDatePicker.updateDate(timestamp = Date().time)
        rememberPersianBottomSheetToDatePickerController.updateDate(date = Date())
        rememberPersianBottomSheetToDatePickerController.updateDate(timestamp = Date().time)
        rememberPersianDialogToDatePicker.updateMinYear(1403)
        rememberPersianDialogToDatePicker.updateYearRange(10)
        rememberPersianDialogToDatePicker.updateDisplayMonthNames(true)
    }
    if (fromDateBottomSheetState.isVisible)
        DatePickerLinearModalBottomSheet(
            submitTitle = "انتخاب",
            modifier = Modifier
                .fillMaxSize().padding(bottom = 100.dp),
            font= R.font.danafanum_light,
            textButtonStyle= MaterialTheme.typography.displayMedium,
            unSelectedTextStyle =MaterialTheme.typography.displayMedium,
            selectedTextStyle=MaterialTheme.typography.displayMedium,
            sheetState = fromDateBottomSheetState,
            sheetMaxWidth = BottomSheetDefaults.SheetMaxWidth,
            shape = BottomSheetDefaults.ExpandedShape,
            controller = rememberPersianBottomSheetFromDatePickerController,
            containerColor =Black100,
            contentColor = White100,
            onDismissRequest = {
                coroutine.launch {
                    fromDateBottomSheetState.hide()
                }
            },
            onDateChanged = { year, month, day ->
                fromDate="$year/$month/$day"
                Log.d(
                    "TAG",
                    "DetailTransactionReportContent() called with: year = $year, month = $month, day = $day"
                )
            }
        )
    if (toDateBottomSheetState.isVisible)
        DatePickerLinearModalBottomSheet(
            submitTitle = "انتخاب",
            modifier = Modifier
                .fillMaxSize()
            ,
            font= R.font.danafanum_light,
            textButtonStyle= MaterialTheme.typography.displayMedium,
            unSelectedTextStyle =MaterialTheme.typography.displayMedium,
            selectedTextStyle=MaterialTheme.typography.displayMedium,
            sheetState = toDateBottomSheetState,
            controller = rememberPersianBottomSheetToDatePickerController,
            containerColor =Black100,
            contentColor = White100,
            onDismissRequest = {
                coroutine.launch {
                    toDateBottomSheetState.hide()
                }
            },
            onDateChanged = { year, month, day ->
                toDate="$year/$month/$day"
                Log.d(
                    "TAG",
                    "DetailTransactionReportContent() called with: year = $year, month = $month, day = $day"
                )
            }
        )
    BackHandler {
        onBackClicked()
    }
    ConstraintLayout(
        ConstraintSet {
            val toolBar = createRefFor("toolBar")
            val fromDate = createRefFor("fromDate")
            val toDate = createRefFor("toDate")
            val confirm = createRefFor("confirm")
            constrain(toolBar) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }
            constrain(fromDate) {
                top.linkTo(toolBar.bottom)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }
            constrain(toDate) {
                end.linkTo(parent.end)
                start.linkTo(parent.start)
                top.linkTo(fromDate.bottom)
                width = Dimension.fillToConstraints
            }
            constrain(confirm) {
                top.linkTo(toDate.bottom)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }

        }, modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        BackButton(
            title = stringResource(id = R.string.details_of_transactions), modifier = Modifier
                .fillMaxWidth()
                .layoutId("toolBar")
        ) {
            onBackClicked()
        }
        DateRange(
            modifier = Modifier
                .layoutId("fromDate")
                .padding(start = END_PADDING, end = START_PADDING, top = 16.dp)
                .fillMaxWidth()
                .height(HEIGHT_TEXT_INPUT)
                .layoutId("fromDate"),
            title = stringResource(id = R.string.from_date),
            value = fromDate
        ) {
            //   billIdValue = it
            coroutine.launch { fromDateBottomSheetState.show() }
        }
        DateRange(
            modifier = Modifier
                .layoutId("toDate")
                .padding(start = END_PADDING, end = START_PADDING, top = 16.dp)
                .fillMaxWidth()
                .height(HEIGHT_TEXT_INPUT)
                .layoutId("toDate"),
            title = stringResource(id = R.string.to_date),
            value = toDate
        ) {
            //   billIdValue = it
            coroutine.launch { toDateBottomSheetState.show() }
        }
        MainButton(
            title = "تایید",
            modifier = Modifier
                .padding(
                    bottom = MARGIN_BOTTOM_MAIN_CONFIRM,
                    start = END_PADDING, end = START_PADDING, top = 16.dp
                )

                .layoutId("confirm")
                .clickable {

                }
                .padding(top = 18.dp)
                .fillMaxWidth()

        )
    }


}

@Composable
@Preview
fun a() {
    TotanPayTheme {
        DetailTransactionReportContent(onBackClicked = {})
    }
}