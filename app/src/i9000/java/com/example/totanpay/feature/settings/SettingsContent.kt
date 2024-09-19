package com.example.totanpay.feature.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.example.totanpay.R
import com.example.totanpay.common.BackButtonModifier
import com.example.totanpay.common.isSmall
import com.example.totanpay.ui.component.button.BackButton
import com.example.totanpay.ui.component.compound.ExitItem
import com.example.totanpay.ui.component.compound.SettingsItem
import com.example.totanpay.ui.theme.Dimensions.TOTAN_ICON_SIZE


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
    val context= LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .blur(60.dp, edgeTreatment = BlurredEdgeTreatment.Rectangle)
    ) {
        ConstraintLayout(
            ConstraintSet {
                val toolBar = createRefFor("toolBar")
                val supervisorSettings = createRefFor("supervisorSettings")
                val merchantSettings = createRefFor("merchantSettings")
                val reports = createRefFor("reports")
                val exit = createRefFor("exit")
                constrain(toolBar) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(supervisorSettings) {
                    top.linkTo(toolBar.bottom,if(isSmall(context)) 20.dp else 30.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(merchantSettings) {
                    top.linkTo(supervisorSettings.bottom)
                    end.linkTo(supervisorSettings.end)
                    start.linkTo(supervisorSettings.start)
                    width = Dimension.fillToConstraints
                }
                constrain(reports) {
                    top.linkTo(merchantSettings.bottom)
                    end.linkTo(supervisorSettings.end)
                    start.linkTo(supervisorSettings.start)
                    width = Dimension.fillToConstraints
                }
                constrain(exit) {
                    top.linkTo(reports.bottom)
                    end.linkTo(supervisorSettings.end)
                    start.linkTo(supervisorSettings.start)
                    width = Dimension.fillToConstraints
                }
            }, modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(scrollState)
        ) {
            BackButton(
                title = stringResource(id = R.string.settings), modifier = BackButtonModifier
                    .layoutId("toolBar")
            ) {
                onBackClicked()
            }
            SettingsItem(
                modifier = Modifier
                    .layoutId("supervisorSettings"),
                backgroundImageId = R.drawable.ic_item_settings1,
                backgroundIconId = R.drawable.background_supervisor_settings,
                iconImageId = R.drawable.ic_supervisor_settings,
                isSmall = isSmall(context = context),
                title = stringResource(id = R.string.supervisor_settings)
            ) {
                onSupervisorSettingsClicked()
            }
            SettingsItem(
                modifier = Modifier
                    .layoutId("merchantSettings"),
                backgroundImageId = R.drawable.ic_item_settings2,
                iconImageId = R.drawable.ic_merchant_settings,
                backgroundIconId = R.drawable.backgound_merchant_settings,
                isSmall = isSmall(context = context),
                title = stringResource(id = R.string.merchant_settings)
            ) {
                onMerchantSettingsClicked()
            }
            SettingsItem(
                modifier = Modifier
                    .layoutId("reports"),
                backgroundImageId = R.drawable.ic_item_settings2,
                backgroundIconId = R.drawable.background_reports,
                iconImageId = R.drawable.ic_reports,
                isSmall = isSmall(context = context),
                title = stringResource(id = R.string.reports)
            ) {
                onReportsClicked()
            }
            ExitItem(
                modifier = Modifier
                    .layoutId("exit"),
                backgroundImageId = R.drawable.ic_item_settings_4,
                backgroundIconId = R.drawable.ic_exit,
                isSmall = isSmall(context = context),
                title = stringResource(id = R.string.exit)
            ) {
                onConfirmExitPassword()
            }
        }
            Image(
                painter = painterResource(id = R.drawable.totan),
                contentDescription = "",
                modifier = Modifier
                    .size(TOTAN_ICON_SIZE)
                    .align(Alignment.BottomCenter)
            )
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






