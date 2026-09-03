package com.example.totanpay.ui.component.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.totanpay.R
import com.example.totanpay.ui.theme.TotanPayTheme

@Composable
fun SettingsDataTimeDialog(titleMessage:String,onDismiss: () -> Unit,
                           onConfirmButtonClicked: () -> Unit) {
    BaseResultDialog(
        titleMessage,
        logoId = R.drawable.ic_warning,
        onDismiss = { onDismiss() }) {
        onConfirmButtonClicked()
    }
}
@Preview
@Composable
fun SettingsDataTimeDialogPreview(){
    SettingsDataTimeDialog(titleMessage="تست", onDismiss = {}){

    }
}