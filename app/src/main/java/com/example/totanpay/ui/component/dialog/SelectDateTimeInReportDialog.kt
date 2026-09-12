package com.example.totanpay.ui.component.dialog

import SelectWheelTimePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.layoutId
import com.example.totanpay.R
import com.example.totanpay.feature.reports.DateContainer
import com.example.totanpay.ui.component.button.MainButton
import com.example.totanpay.ui.component.report.SelectDateTime
import com.example.totanpay.ui.getPersianDateFrom
import com.example.totanpay.ui.selectedDateIsSmallerOrEqualThanCurrenDate
import com.example.totanpay.ui.theme.END_PADDING
import com.example.totanpay.ui.theme.HEIGHT_SELECT_DATE_TIME_I5000
import com.example.totanpay.ui.theme.START_PADDING
import com.google.gson.Gson
import com.razaghimahdi.compose_persian_date.core.components.rememberDialogDatePicker
import com.razaghimahdi.compose_persian_date.dialog.PersianLinearDatePickerDialog
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectDateTimeInReportDialog(
    onCancelButtonClicked: () -> Unit, confirmDateTime: (String,String) -> Unit
) {
    val rememberDialogFromDatePicker = rememberDialogDatePicker()
    val rememberDialogToDatePicker = rememberDialogDatePicker()
    var showFromDateDialog by remember { mutableStateOf(false) }
    var showToDateDialog by remember { mutableStateOf(false) }
    val coroutine = rememberCoroutineScope()
    val fromDateBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val toDateBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    var fromDateValue by remember {
        mutableStateOf(
            "${rememberDialogFromDatePicker.getPersianDay()}" + " ${rememberDialogFromDatePicker.getPersianMonthName()}" + "${rememberDialogFromDatePicker.getPersianYear()}"
        )
    }
    var toDateValue by remember {
        mutableStateOf(
            "${rememberDialogToDatePicker.getPersianDay()}" + " ${rememberDialogToDatePicker.getPersianMonthName()}" + "${rememberDialogToDatePicker.getPersianYear()}"
        )
    }
    var fromTimeValue by remember {
        mutableStateOf(LocalTime.MIN.toString())
    }
    var toTimeValue by remember {
        mutableStateOf("%02d:%02d".format(LocalTime.now().hour, LocalTime.now().minute))
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
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    fun confirmSelection() {
        val temp = DateContainer(
            rememberDialogFromDatePicker.getPersianYear().toString(),
            rememberDialogFromDatePicker.getPersianMonth().toString(),
            rememberDialogFromDatePicker.getPersianDay().toString(),
            hour = fromTimeValue.dropLast(3),
            minute = fromTimeValue.drop(3)
        )
        val from = Gson().toJson(temp)
        val tt = DateContainer(
            rememberDialogToDatePicker.getPersianYear().toString(),
            rememberDialogToDatePicker.getPersianMonth().toString(),
            rememberDialogToDatePicker.getPersianDay().toString(),
            hour = toTimeValue.dropLast(3),
            minute = toTimeValue.drop(3)
        )
        val to = Gson().toJson(tt)
        confirmDateTime(from, to)
    }
    LaunchedEffect(Unit) {
        rememberDialogFromDatePicker.updateDate(date = Date())
        rememberDialogFromDatePicker.updateDate(timestamp = Date().time)
        rememberDialogToDatePicker.updateDate(date = Date())
        rememberDialogToDatePicker.updateDate(timestamp = Date().time)
        currentYear = rememberDialogFromDatePicker.getPersianYear()
        currentMonth = rememberDialogFromDatePicker.getPersianMonth()
        currentDay = rememberDialogFromDatePicker.getPersianDay()
    }
    if (showFromDateDialog) {
        PersianLinearDatePickerDialog(rememberDialogFromDatePicker,
            Modifier.fillMaxWidth(),
            unSelectedTextStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 15.sp,
                color = Color.Blue
            ),
            selectedTextStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 15.sp,
                color = Color.Blue
            ),
            onDismissRequest = {
                showFromDateDialog = false
            },
            onDateChanged = { year, month, day ->
                if (selectedDateIsSmallerOrEqualThanCurrenDate(
                        year, month, day, currentYear, currentMonth, currentDay
                    )
                ) {
                    fromDateValue = getPersianDateFrom(year, month, day)
                }

            })
    }
    if (showToDateDialog) {
        PersianLinearDatePickerDialog(rememberDialogToDatePicker,
            Modifier.fillMaxWidth(),
            unSelectedTextStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 15.sp,
                color = Color.Blue
            ),
            selectedTextStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 15.sp,
                color = Color.Blue
            ),
            onDismissRequest = {
                showToDateDialog = false
            },
            onDateChanged = { year, month, day ->
                if (selectedDateIsSmallerOrEqualThanCurrenDate(
                        year, month, day, currentYear, currentMonth, currentDay
                    )
                ) toDateValue = getPersianDateFrom(year, month, day)
            })
    }
    ModalBottomSheet(
        onDismissRequest = { onCancelButtonClicked() },
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
                .focusRequester(focusRequester)
                .focusable()
                .onKeyEvent { keyEvent ->
                    if (keyEvent.type == KeyEventType.KeyDown) {
                        if (keyEvent.key == Key.Enter) {
                            confirmSelection()
                            true
                        } else {
                            false
                        }
                    } else {
                        false
                    }
                }
        ) {
            ConstraintLayout(
                ConstraintSet {

                    val fromDate = createRefFor("fromDate")
                    val toDate = createRefFor("toDate")
                    val fromTime = createRefFor("fromTime")
                    val toTime = createRefFor("toTime")
                    val confirm = createRefFor("confirm")
                    constrain(fromDate) {
                        top.linkTo(parent.top, 10.dp)
                        end.linkTo(fromTime.start)
                        start.linkTo(parent.start)
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
                        start.linkTo(parent.start)
                        width = Dimension.percent(0.60f)
                    }
                    constrain(toTime) {
                        top.linkTo(toDate.top)
                        bottom.linkTo(toDate.bottom)
                        end.linkTo(fromTime.end)
                        start.linkTo(fromTime.start)
                        width = Dimension.percent(0.40f)
                    }
                    constrain(confirm) {
                        top.linkTo(toDate.bottom, 24.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                }, modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .fillMaxWidth()
                    .height(450.dp)
            ) {
                SelectDateTime(
                    modifier = Modifier
                        .layoutId("fromDate")
                        .padding(start = END_PADDING, end = START_PADDING, top = 6.dp)
                        .height(HEIGHT_SELECT_DATE_TIME_I5000)
                        .layoutId("fromDate"),
                    title = stringResource(id = R.string.from_date),
                    isTime = false,
                    value = fromDateValue
                ) {
                    coroutine.launch { fromDateBottomSheetState.show() }
                    showFromDateDialog=true
                }
                SelectDateTime(
                    modifier = Modifier
                        .layoutId("fromTime")
                        .padding(start = END_PADDING, end = START_PADDING, top = 6.dp)
                        .height(HEIGHT_SELECT_DATE_TIME_I5000)
                        .layoutId("fromTime"),
                    title = stringResource(id = R.string.from_time),
                    isTime = true,
                    value = fromTimeValue
                ) {
                    showSelectFromTime = true
                }
                SelectDateTime (
                    modifier = Modifier
                        .layoutId("toDate")
                        .padding(start = END_PADDING, end = START_PADDING, top = 6.dp)
                        .fillMaxWidth()
                        .height(HEIGHT_SELECT_DATE_TIME_I5000)
                        .layoutId("toDate"),
                    title = stringResource(id = R.string.to_date),
                    isTime = false,
                    value = toDateValue
                ) {
                    coroutine.launch { toDateBottomSheetState.show() }
                    showToDateDialog=true
                }
                SelectDateTime(
                    modifier = Modifier
                        .layoutId("toTime")
                        .padding(start = END_PADDING, end = START_PADDING, top = 6.dp)
                        .fillMaxWidth()
                        .height(HEIGHT_SELECT_DATE_TIME_I5000)
                        .layoutId("toTime"),
                    title = stringResource(id = R.string.to_time),
                    isTime = true,
                    value = toTimeValue
                ) {
                    showSelectToTime = true
                }
                MainButton(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .height(50.dp)
                        .layoutId("confirm")
                ) {
                    confirmSelection()
                }
            }

            if (showSelectFromTime || showSelectToTime) {
                Dialog(
                    onDismissRequest = {
                        if (showSelectToTime) {
                            showSelectToTime = false
                        }
                        if (showSelectFromTime) {
                            showSelectFromTime = false
                        }
                    }, properties = DialogProperties(usePlatformDefaultWidth = false)
                ) {
                    SelectWheelTimePicker(isSmall = true, onTimeSelected = { time ->
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
        }

    }
}