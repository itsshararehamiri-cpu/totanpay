package com.example.totanpay.feature.charge

import androidx.activity.compose.BackHandler
import androidx.collection.emptyLongSet
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.layoutId
import com.example.totanpay.R
import com.example.totanpay.common.isSmall
import com.example.totanpay.data.util.isNotNumber
import com.example.totanpay.ui.TextInputModifier
import com.example.totanpay.ui.component.MobileTextInput
import com.example.totanpay.ui.component.compound.ChargeTypeContainer
import com.example.totanpay.ui.theme.MARGIN_SIDE
import com.example.totanpay.util.isValidIranianMobileNumber

@Composable
fun SelectChargeTypeScreen(
    onConfirm: (Boolean, String?) -> Unit,
    onBackButton: () -> Unit
) {
    val context = LocalContext.current
    var isTopUpSelected by remember {
        mutableStateOf(true)
    }
    var mobileHasError: Boolean by remember { mutableStateOf(false) }
    var mobileError by remember { mutableStateOf("") }
    var phoneNumberValue: String by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val mobileFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        mobileFocusRequester.requestFocus()
    }
    LaunchedEffect(isTopUpSelected) {
        if(isTopUpSelected)
        mobileFocusRequester.requestFocus()
    }
    BackHandler {
        onBackButton()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .focusRequester(focusRequester)
            .focusable()
            .onKeyEvent { keyEvent ->
                if (keyEvent.type == KeyEventType.KeyDown) {
                    if (keyEvent.key == Key.Enter) {
                        mobileHasError = false
                        mobileError = ""
                        if (isTopUpSelected) {
                            if (phoneNumberValue.isNotEmpty()) {

                                if (isValidIranianMobileNumber(phoneNumberValue)) {
                                    onConfirm(
                                        isTopUpSelected,
                                        phoneNumberValue
                                    )
                                } else {
                                    mobileHasError = true
                                    mobileError = context.getString(R.string.mobile_is_not_valid)
                                }
                            } else {
                                mobileError = ""
                                if (phoneNumberValue.isEmpty()) {
                                    mobileHasError = true
                                    mobileError = context.getString(R.string.mobile_not_entered)
                                }
                            }
                        } else {
                            onConfirm(
                                false,
                                null
                            )
                        }
                        true
                    } else {
                        false
                    }
                } else {
                    false
                }
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(MaterialTheme.colorScheme.background),
                constraintSet = ConstraintSet {
                    val chargeTypeContainer = createRefFor("chargeTypeContainer")
                    val phoneNumber = createRefFor("phoneNumber")

                    constrain(chargeTypeContainer) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                    }
                    constrain(phoneNumber) {
                        top.linkTo(chargeTypeContainer.bottom, 4.dp)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        height =
                            if (isTopUpSelected) Dimension.wrapContent else Dimension.value(0.dp)
                    }
                }
            ) {

                ChargeTypeContainer(
                    modifier = Modifier
                        .padding(horizontal = MARGIN_SIDE)
                        .layoutId("chargeTypeContainer")
                        .padding(top = 40.dp),
                    isTopUpSelected = isTopUpSelected,
                    onTopUpSelected = {
                        isTopUpSelected = true
                    },
                    onVoucherSelected = {
                        isTopUpSelected = false
                    })
                if (isTopUpSelected)
                    MobileTextInput(
                        modifier = TextInputModifier
                            .layoutId("phoneNumber").focusRequester(mobileFocusRequester),
                        title = stringResource(R.string.enter_your_mobile),
                        isSmall = isSmall(LocalContext.current),
                        errorMessage = mobileError,
                        value = phoneNumberValue, hasError = mobileHasError, onValueChange = {
                            if (it.length <= 11 && !it.trim().isNotNumber()) {
                                phoneNumberValue = it
                            }
                        }, onNextClicked = {
                            mobileHasError = false
                            mobileError = ""
                            if (isTopUpSelected) {
                                if (phoneNumberValue.isNotEmpty()) {

                                    if (isValidIranianMobileNumber(phoneNumberValue)) {
                                        onConfirm(
                                            isTopUpSelected,
                                            phoneNumberValue
                                        )
                                    } else {
                                        mobileHasError = true
                                        mobileError = context.getString(R.string.mobile_is_not_valid)
                                    }
                                } else {
                                    mobileError = ""
                                    if (phoneNumberValue.isEmpty()) {
                                        mobileHasError = true
                                        mobileError = context.getString(R.string.mobile_not_entered)
                                    }
                                }
                            } else {
                                onConfirm(
                                    false,
                                    null
                                )
                            }
                        })

            }
        }
    }
}