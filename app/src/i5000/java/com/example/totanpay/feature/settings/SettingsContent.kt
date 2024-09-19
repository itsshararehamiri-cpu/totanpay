package com.example.totanpay.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.example.totanpay.R
import com.example.totanpay.ui.component.compound.I5000SettingsItem


@Composable
fun SettingsContent(
    uiState: SettingsUiState,
    onBackClicked: () -> Unit,
    onMerchantSettingsClicked: () -> Unit,
    onSupervisorSettingsClicked: () -> Unit,
    onConfirmExitPassword: () -> Unit,
    onReportsClicked: () -> Unit,
    hideMerchantPasswordDialog: () -> Unit,
    hideSupervisorPasswordDialog: () -> Unit,
    hideReportPasswordDialog: () -> Unit,
    hideExitPasswordDialog: () -> Unit,
    validateSupervisorPassword: (String) -> Unit,
    validateMerchantPassword: (String) -> Unit,
    validateReportPassword: (String) -> Unit,
    validateExitPassword: (String) -> Unit
) {
    val scrollState = rememberScrollState()
    Box(
        modifier = Modifier
            .fillMaxSize()  .verticalScroll(scrollState)

    ) {
        ConstraintLayout(
            ConstraintSet {
                val supervisorSettings = createRefFor("supervisorSettings")
                val merchantSettings = createRefFor("merchantSettings")
                val reports = createRefFor("reports")
                val exit = createRefFor("exit")

                constrain(supervisorSettings) {
                    top.linkTo(parent.top,28.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(merchantSettings) {
                    top.linkTo(supervisorSettings.bottom,10.dp)
                    end.linkTo(supervisorSettings.end)
                    start.linkTo(supervisorSettings.start)
                    width = Dimension.fillToConstraints
                }
                constrain(reports) {
                    top.linkTo(merchantSettings.bottom,10.dp)
                    end.linkTo(supervisorSettings.end)
                    start.linkTo(supervisorSettings.start)
                    width = Dimension.fillToConstraints
                }
                constrain(exit) {
                    top.linkTo(reports.bottom,10.dp)
                    end.linkTo(supervisorSettings.end)
                    start.linkTo(supervisorSettings.start)
                    width = Dimension.fillToConstraints
                }
            }, modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background).padding(bottom = 20.dp)
        ) {
            I5000SettingsItem(
                modifier = Modifier.background(Color.White)
                    .layoutId("supervisorSettings"),
                iconImageId = R.drawable.ic_i5000_supervisor_settings,
                title = stringResource(id = R.string.supervisor_settings)
            ) {
                onSupervisorSettingsClicked()
            }
            I5000SettingsItem(
                modifier = Modifier.background(Color.White)
                    .layoutId("merchantSettings"),
                iconImageId = R.drawable.ic_i5000_merchant_settings,
                title = stringResource(id = R.string.merchant_settings)
            ) {
                onMerchantSettingsClicked()
            }
            I5000SettingsItem(
                modifier = Modifier.background(Color.White)
                    .layoutId("reports"),
                iconImageId = R.drawable.ic_i5000_reports,
                title = stringResource(id = R.string.reports)
            ) {
                onReportsClicked()
            }
            I5000SettingsItem(
                modifier = Modifier.background(Color.White)
                    .layoutId("exit"),
                iconImageId = R.drawable.ic_i5000_exit,
                title = stringResource(id = R.string.exit), textColor = Color(0XFFED1C22)
            ) {
                onConfirmExitPassword()
            }
        }
        if (uiState.showExitPasswordDialog) {
            EnterPasswordBottomDialog(modifier = Modifier.align(Alignment.BottomCenter),
                errorMessage = uiState.existPasswordError,
                onConfirmButtonClicked = {
                    validateExitPassword(it)
                },
                onCancelButtonClicked = {
                    hideExitPasswordDialog()
                })
        }
        if (uiState.showSupervisorPasswordDialog) {
            EnterPasswordBottomDialog(modifier = Modifier.align(Alignment.BottomCenter),
                errorMessage = uiState.supervisorPasswordError,
                onCancelButtonClicked = {
                    hideSupervisorPasswordDialog()
                }) {
                validateSupervisorPassword(it)
            }
        }
        if (uiState.showMerchantPasswordDialog) {
            EnterPasswordBottomDialog(modifier = Modifier.align(Alignment.BottomCenter),
                errorMessage = uiState.merchantPasswordError,
                onCancelButtonClicked = {
                    hideMerchantPasswordDialog()
                }) {
                validateMerchantPassword(it)
            }
        }
        if (uiState.showReportPasswordDialog) {
            EnterPasswordBottomDialog(modifier = Modifier.align(Alignment.BottomCenter),
                errorMessage = uiState.reportPasswordError,
                onCancelButtonClicked = {
                    hideReportPasswordDialog()
                }) {
                validateReportPassword(it)
            }
        }
    }
}





