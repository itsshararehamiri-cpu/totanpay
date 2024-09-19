package com.example.totanpay.feature.settings.merchant
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.example.totanpay.common.isSmall
import com.example.totanpay.data.repository.datasource.transaction.request.Apportionment
import com.example.totanpay.ui.component.ApportionmentView
import com.example.totanpay.ui.component.ConfirmDialog
import com.example.totanpay.ui.component.ShowToast
import com.example.totanpay.ui.component.dialog.KeyboardApportiomentDialog

@Composable
fun ApportionmentContent(uiState:ApportionmentUiState,
                         confirmApportionment:()->Unit,
                         addApportionment:( Apportionment, String)->Unit,
                         onBackClicked:()->Unit,
) {
    var showConfirmDialog: Boolean by remember {
        mutableStateOf(false)
    }
    var selectedApportioment: Apportionment? by remember {
        mutableStateOf(null)
    }
    var showToast by remember { mutableStateOf(false) }
    var showAddAmountDialog by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    Box(modifier = Modifier.fillMaxSize()
        .focusRequester(focusRequester)
        .focusable()
        .onKeyEvent { keyEvent ->
            if (keyEvent.type == KeyEventType.KeyDown) {
                if (keyEvent.key == Key.Enter) {
                    showConfirmDialog = true
                    true
                } else {
                    false
                }
            } else {
                false
            }
        }) {
        ConstraintLayout(
            ConstraintSet {
                val apportionment = createRefFor("apportionment")
                constrain(apportionment) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }

            }, modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {

            LazyColumn(
                modifier = Modifier
                    .wrapContentHeight()
                    .layoutId("apportionment")
                    .padding(PaddingValues(top = 10.dp, bottom = 10.dp))
                    .align(Alignment.TopCenter)
            ) {
                items(uiState.apportionments, key = {
                    it.IBAN
                }) {
                    ApportionmentView(it, onAddApportionment = {
                        showAddAmountDialog = true
                        selectedApportioment = it
                    },
                        onEditClicked = {
                            showAddAmountDialog = true
                            selectedApportioment = it
                        })
                }
            }
        }
        if (showToast) {
            ShowToast(
                modifier = Modifier.align(Alignment.Center),
                message = if (showToast) "کلمه عبور تعیین نشده است."
                else ""
            ) {
                showToast = false
            }
        }
        if (showConfirmDialog) {
            ConfirmDialog(isSmall = true, onCancelButtonClicked = {
                showConfirmDialog = false
            }, onConfirmButtonClicked = {
               confirmApportionment()
                showConfirmDialog = false

            })
        }
        if (showAddAmountDialog) {
           KeyboardApportiomentDialog(isSmall = isSmall(context = LocalContext.current), onCancelButtonClicked = {
                showAddAmountDialog = false
            }, onConfirmButtonClicked = {
               if (selectedApportioment != null) {
                    if (selectedApportioment != null)
                   addApportionment(selectedApportioment!!, it)
                    selectedApportioment = null
                }
                showAddAmountDialog = false

            })
        }
        if (uiState.showErrorInPercent) {
            ShowToast(
                modifier = Modifier.align(Alignment.Center),
                message = if (uiState.showErrorInPercent) "جمع درصدها باید برابر با 100 باشد."
                else ""
            ) {
            }
        }

    }
}