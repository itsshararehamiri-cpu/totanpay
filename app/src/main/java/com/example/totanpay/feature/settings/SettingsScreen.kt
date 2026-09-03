package com.example.totanpay.feature.settings

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.totanpay.LanguageState
import com.example.totanpay.LocalLanguageState
import com.example.totanpay.MainActivity
import com.example.totanpay.util.LocaleHelper

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
   val isFarsiSelected = LocalLanguageState.current.isFarsiSelected.value
//    LaunchedEffect(isFarsiSelected) {
//        LocaleHelper.setLocale(context, if (!isFarsiSelected) "en" else "fa")
//        globalLanguageState.isFarsiSelected.value = isFarsiSelected
//        //  onLanguageChanged(currentLanguage.isFarsi)
//    }
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
    SettingsContent(isFarsiSelected=isFarsiSelected,
        uiState = uiState,
        onBackClicked = { onBackClicked() },
        validateReportPassword = {
            viewModel.checkReportPassword(it)
        }, validateMerchantPassword = {
            viewModel.checkMerchantPassword(it)
        }, validateSupervisorPassword = {
            viewModel.checkSupervisorPassword(it)
        }, validateExitPassword = {
            viewModel.checkExistPassword(it)
        },
        onIsFarsiLanguageSelected = {
            viewModel.setCurrentLanguage(it)
        }
    )
}
