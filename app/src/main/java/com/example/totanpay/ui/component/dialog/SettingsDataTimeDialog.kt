package com.example.totanpay.ui.component.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.totanpay.R
import com.example.totanpay.ui.theme.TotanPayTheme

@Composable
fun SettingsDataTimeDialog(titleMessage:String,onDismiss: () -> Unit, onConfirmButtonClicked: () -> Unit) {
    BaseResultDialog(
        titleMessage,
        logoId = R.drawable.ic_warning,
        onDismiss = { onDismiss() }) {
        onConfirmButtonClicked()
    }
}

@Composable
@Preview
fun SettingsDataTimeDialogPreview(){
TotanPayTheme {
    SettingsDataTimeDialog(titleMessage =  "لطفا محدوده زمانی دستگاه را روی تهران تنظیم نمایید", onDismiss = {
    }, onConfirmButtonClicked = {

    })
}
}