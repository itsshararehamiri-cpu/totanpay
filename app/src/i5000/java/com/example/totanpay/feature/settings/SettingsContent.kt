package com.example.totanpay.feature.settings

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.totanpay.R
import com.example.totanpay.ui.component.HorizontalDivider
import com.example.totanpay.ui.component.compound.I5000SettingsItem
import com.example.totanpay.ui.theme.FONT_SIZE_14
import com.example.totanpay.ui.theme.Green50

@Composable
fun SettingsContent(isFarsiSelected: Boolean,
    uiState: SettingsUiState,
    onBackClicked: () -> Unit,
    validateSupervisorPassword: (String) -> Unit,
    validateMerchantPassword: (String) -> Unit,
    validateReportPassword: (String) -> Unit,
    validateExitPassword: (String) -> Unit, onIsFarsiLanguageSelected: (Boolean) -> Unit
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    var showExitPasswordDialog: Boolean by remember { mutableStateOf(false) }
    var showSupervisorPasswordDialog: Boolean by remember { mutableStateOf(false) }
    var showMerchantPasswordDialog: Boolean by remember { mutableStateOf(false) }
    var showReportPasswordDialog: Boolean by remember { mutableStateOf(false) }
    var isEnglishLanguageSelected: Boolean by remember { mutableStateOf(true) }
    LaunchedEffect(isFarsiSelected) {
        isEnglishLanguageSelected = !isFarsiSelected
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(scrollState)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 20.dp)
            ) {
                I5000SettingsItem(
                    modifier = Modifier.background(Color.White),
                    iconImageId = R.drawable.ic_i5000_supervisor_settings,
                    title = stringResource(id = R.string.supervisor_settings)
                ) {
                    showSupervisorPasswordDialog = true
                }
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    isPaperReceipt = false
                )
                I5000SettingsItem(
                    modifier = Modifier.background(Color.White),
                    iconImageId = R.drawable.ic_i5000_merchant_settings,
                    title = stringResource(id = R.string.merchant_settings)
                ) {
                    showMerchantPasswordDialog = true
                }
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    isPaperReceipt = false
                )
                I5000SettingsItem(
                    modifier = Modifier.background(Color.White),
                    iconImageId = R.drawable.ic_i5000_reports,
                    title = stringResource(id = R.string.reports)
                ) {
                    showReportPasswordDialog = true
                }
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    isPaperReceipt = false
                )
                I5000SettingsItem(
                    modifier = Modifier.background(Color.White),
                    iconImageId = R.drawable.ic_setting_connection,
                    title = stringResource(id = R.string.connection_to_net)
                ) {
                    val intent = Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                }
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    isPaperReceipt = false
                )
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .background(Color.White)
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_moon),
                        contentDescription = "",
                        modifier = Modifier.size(20.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondary)
                    )
                    Text(
                        text = stringResource(id = R.string.english_language),
                        modifier = Modifier.padding(start = 10.dp).weight(1f),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = FONT_SIZE_14,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Switch(
                        checked = isEnglishLanguageSelected,
                        onCheckedChange = {
                            isEnglishLanguageSelected = it
                            onIsFarsiLanguageSelected(!it)
                        },
                        colors = SwitchDefaults.colors(
                            checkedTrackColor = Green50
                        )
                    )
                }
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    isPaperReceipt = false
                )
                I5000SettingsItem(
                    modifier = Modifier.background(Color.White),
                    iconImageId = R.drawable.ic_i5000_exit,
                    title = stringResource(id = R.string.exit), textColor = Color(0XFFED1C22)
                ) {
                    showExitPasswordDialog = true
                }
            }
        }
        if (showExitPasswordDialog) {
            EnterPasswordBottomDialog(modifier = Modifier.align(Alignment.BottomCenter),
                errorMessage = stringResource(
                    if (uiState.existPasswordError != -1)
                        uiState.existPasswordError else R.string.empty_message
                ),
                onConfirmButtonClicked = {
                    validateExitPassword(it)
                },
                onCancelButtonClicked = {
                    showExitPasswordDialog = false
                })
        }
        if (showSupervisorPasswordDialog) {
            EnterPasswordBottomDialog(modifier = Modifier.align(Alignment.BottomCenter),
                errorMessage = stringResource(
                    if (uiState.supervisorPasswordError != -1)
                        uiState.supervisorPasswordError else R.string.empty_message
                ),
                onCancelButtonClicked = {
                    showSupervisorPasswordDialog = false
                }) {
                validateSupervisorPassword(it)
            }
        }
        if (showMerchantPasswordDialog) {
            EnterPasswordBottomDialog(modifier = Modifier.align(Alignment.BottomCenter),
                errorMessage = stringResource(
                    if (uiState.merchantPasswordError != -1)
                        uiState.merchantPasswordError else R.string.empty_message
                ),
                onCancelButtonClicked = {
                    showMerchantPasswordDialog = false
                }) {
                validateMerchantPassword(it)
            }
        }
        if (showReportPasswordDialog) {
            EnterPasswordBottomDialog(modifier = Modifier.align(Alignment.BottomCenter),
                errorMessage = stringResource(
                    if (uiState.reportPasswordError != -1)
                        uiState.reportPasswordError else
                            R.string.empty_message
                ),
                onCancelButtonClicked = {
                    showReportPasswordDialog = false
                }) {
                validateReportPassword(it)
            }
        }
    }
}
