package com.example.totanpay.feature.settings

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.totanpay.MainActivity
import com.example.totanpay.R
import com.example.totanpay.common.BackButtonModifier
import com.example.totanpay.common.isSmall
import com.example.totanpay.ui.component.button.BackButton
import com.example.totanpay.ui.component.compound.ExitItem
import com.example.totanpay.ui.component.compound.SettingsItem
import com.example.totanpay.ui.theme.Dimensions.TOTAN_ICON_SIZE

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBackClicked: () -> Unit,
    onMerchantSettingsClicked: () -> Unit,
    onSupervisorSettingsClicked: () -> Unit,
    onReportsClicked: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    BackHandler {
        onBackClicked()
    }
    LaunchedEffect(uiState.supervisorPasswordVerified) {
        if (uiState.supervisorPasswordVerified) {
            onSupervisorSettingsClicked()
        }
    }
    LaunchedEffect(uiState.merchantPasswordVerified) {
        if (uiState.merchantPasswordVerified)
            onMerchantSettingsClicked()
    }
    LaunchedEffect(uiState.reportPasswordVerified) {
        if (uiState.reportPasswordVerified)
            onReportsClicked()
    }
    LaunchedEffect(uiState.isExit) {
        if (uiState.isExit)
            (context as MainActivity).finish()
    }
    SettingsContent(uiState = uiState,
        onBackClicked = { onBackClicked() },
        onMerchantSettingsClicked = { viewModel.showGetMerchantPasswordDialog() },
        onSupervisorSettingsClicked = { viewModel.showGetSupervisorPasswordDialog() },
        onConfirmExitPassword = { viewModel.showGetExitPasswordDialog() },
        onReportsClicked = { viewModel.showGetReportPasswordDialog() },
        hideMerchantPasswordDialog = {
            viewModel.hideGetMerchantPasswordDialog()
        }, hideReportPasswordDialog = {
            viewModel.hideGetReportPasswordDialog()
        }, hideSupervisorPasswordDialog = {
            viewModel.hideGetSupervisorPasswordDialog()
        },
        hideExitPasswordDialog = { viewModel.hideGetExitPasswordDialog() },
        validateReportPassword = {
            viewModel.checkReportPassword(it)
        }, validateMerchantPassword = {
            viewModel.checkMerchantPassword(it)
        }, validateSupervisorPassword = {
            viewModel.checkSupervisorPassword(it)
        }, validateExitPassword = {
            viewModel.checkExistPassword(it)
        }
    )
}
