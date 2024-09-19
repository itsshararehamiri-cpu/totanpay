package com.example.totanpay.feature.charge


import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import com.example.totanpay.R
import com.example.totanpay.data.Operator
import com.example.totanpay.data.util.getAllCharges
import com.example.totanpay.ui.ListModifier
import com.example.totanpay.ui.component.ShowToast
import com.example.totanpay.ui.component.button.OperatorButton
import com.example.totanpay.ui.theme.END_PADDING
import com.example.totanpay.ui.theme.START_PADDING
import com.google.gson.Gson

@Composable
fun SelectOperatorScreen(
    onConfirm: (operator: String) -> Unit, onBackButton: () -> Unit
) {
    var operatorHasError: Boolean by remember { mutableStateOf(true) }
    var selectedOperator: Operator? by remember {
        mutableStateOf(null)
    }
    var showToast by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    BackHandler {
        onBackButton()
    }
    Box(
        Modifier  .focusRequester(focusRequester)
            .focusable()
            .onKeyEvent { keyEvent ->
                if (keyEvent.type == KeyEventType.KeyDown) {
                    if (keyEvent.key == Key.Enter) {
                        showToast = false
                        if (selectedOperator != null) {
                            onConfirm(
                                Gson()
                                    .toJson(selectedOperator)
                                    .toString()
                            )

                        } else {
                            showToast = true
                        }
                        true // مصرف کردن رویداد
                    } else {
                        false
                    }
                } else {
                    false
                }
            }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 16.dp)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                ConstraintLayout(modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(MaterialTheme.colorScheme.background),
                    constraintSet = ConstraintSet {
                        val operatorTitle = createRefFor("operatorTitle")
                        val operators = createRefFor("operators")

                        constrain(operatorTitle) {
                            top.linkTo(parent.top,40.dp)
                            end.linkTo(parent.end)
                            //start.linkTo(parent.start)
                        }
                        constrain(operators) {
                            top.linkTo(operatorTitle.bottom)
                            end.linkTo(parent.end)
                            start.linkTo(parent.start)
                        }
                    }) {

                    Text(
                        text = stringResource(R.string.plz_select_operator),
                        modifier = Modifier
                            .padding(
                                start = END_PADDING, end = 5.dp, top = 0.dp
                            )
                            .wrapContentWidth()
                            .layoutId("operatorTitle"),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge

                    )

                    val operators: List<Operator>? = getAllCharges(context = LocalContext.current)
                    LazyColumn(
                        modifier =  Modifier
                            .padding(horizontal = END_PADDING)
                            .padding(top = 2.dp)
                            .fillMaxWidth().layoutId("operators")
                    ) {
                        items(operators!!) { operator ->
                            OperatorButton(isSmall = true,
                                isSelected = selectedOperator == operator,
                                operator,
                                modifier = Modifier.padding(top = 5.dp).fillMaxWidth()
                            ) {
                                selectedOperator = it
                                operatorHasError = false
                            }
                        }
                    }
                }
            }
        }
        if (showToast) {
            ShowToast(
                modifier = Modifier.align(Alignment.BottomCenter), message = if (operatorHasError) {
                    stringResource(R.string.operator_not_selected)
                } else ""
            ) {
                showToast = false
            }
        }
    }
}



