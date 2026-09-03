package com.example.totanpay.feature.settings.merchant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.example.totanpay.R
import com.example.totanpay.common.BackButtonModifier
import com.example.totanpay.common.mainButtonModifier
import com.example.totanpay.data.repository.datasource.transaction.request.Apportionment
import com.example.totanpay.ui.component.ApportionmentView
import com.example.totanpay.ui.component.ConfirmDialog
import com.example.totanpay.ui.component.ShowToast
import com.example.totanpay.ui.component.button.BackButton
import com.example.totanpay.ui.component.button.MainButton
import com.example.totanpay.ui.component.dialog.ApportiomentDialog

@Composable
fun ApportionmentContent(
    uiState: ApportionmentUiState,
    confirmApportionment: () -> Unit,
    addApportionment: (Apportionment, String) -> Unit,
    onBackClicked: () -> Unit,
) {
    var showConfirmDialog: Boolean by remember {
        mutableStateOf(false)
    }
    var selectedApportioment: Apportionment? by remember {
        mutableStateOf(null)
    }
    var showToast by remember { mutableStateOf(false) }
    var showAddAmountDialog by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ConstraintLayout(
            ConstraintSet {
                val toolBar = createRefFor("toolBar")
                val apportionment = createRefFor("apportionment")
                val confirm = createRefFor("confirm")
                constrain(toolBar) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    height = Dimension.wrapContent
                }
                constrain(apportionment) {
                    top.linkTo(toolBar.bottom)
                    end.linkTo(toolBar.end)
                    start.linkTo(toolBar.start)
                }
                constrain(confirm) {
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }

            }, modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            BackButton(
                title = stringResource(id = R.string.account_management),
                modifier = BackButtonModifier
                    .layoutId("toolBar")
            ) {
                onBackClicked()
            }
            LazyColumn(
                modifier = Modifier
                    .wrapContentHeight()
                    .layoutId("apportionment")
                    .padding(PaddingValues(top = 10.dp, bottom = 10.dp))
                    .align(Alignment.TopCenter)
            ) {
                items(uiState.apportionments) {
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
            MainButton(
                modifier = Modifier.mainButtonModifier(isSmall = false)
                    .layoutId("confirm")
            ) {
                showConfirmDialog = true
            }
        }
        if (showToast) {
            ShowToast(
                modifier = Modifier.align(Alignment.Center),
                message = if (showToast) stringResource(R.string.no_password_has_been_set)
                else ""
            ) {
                showToast = false
            }
        }
        if (showConfirmDialog) {
            ConfirmDialog(isSmall = false, onCancelButtonClicked = {
                showConfirmDialog = false
            }, onConfirmButtonClicked = {
                confirmApportionment()
                showConfirmDialog = false

            })
        }
        if (showAddAmountDialog) {
            ApportiomentDialog(onCancelButtonClicked = {
                showAddAmountDialog = false
            }, onConfirmButtonClicked = {
                if (selectedApportioment != null) {
                        addApportionment(selectedApportioment!!, it)
                    selectedApportioment = null
                }
                showAddAmountDialog = false

            })
        }
        if (uiState.showErrorInPercent) {
            ShowToast(
                modifier = Modifier.align(Alignment.Center),
                message = if (uiState.showErrorInPercent)stringResource(R.string.the_total_of_percentages_must_be_equal_to_on_houndred)
                else ""
            ) {
                // showToast = false
            }
        }

    }
}