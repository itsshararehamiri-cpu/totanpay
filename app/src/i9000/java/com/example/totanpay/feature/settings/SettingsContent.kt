package com.example.totanpay.feature.settings

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
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
import com.example.totanpay.ui.theme.Green50


@Composable
fun SettingsContent(isFarsiSelected: Boolean,
    uiState: SettingsUiState,
    onBackClicked: () -> Unit,
    validateSupervisorPassword: (String) -> Unit,
    validateMerchantPassword: (String) -> Unit,
    validateReportPassword: (String) -> Unit,
    validateExitPassword: (String) -> Unit,onIsFarsiLanguageSelected:(Boolean)-> Unit
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    var showExitPasswordDialog: Boolean by remember { mutableStateOf(false) }
    var showSupervisorPasswordDialog: Boolean by remember { mutableStateOf(false) }
    var showMerchantPasswordDialog: Boolean by remember { mutableStateOf(false) }
    var showReportPasswordDialog: Boolean by remember { mutableStateOf(false) }
    var isEnglishLanguageSelected: Boolean by remember { mutableStateOf(true) }
    LaunchedEffect(isFarsiSelected){
        isEnglishLanguageSelected=!isFarsiSelected
    }
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
                val  connectToNet=createRefFor("connectToNet")
                val selectCurrentLanguage=createRefFor("selectCurrentLanguage")
                val exit = createRefFor("exit")
                constrain(toolBar) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(supervisorSettings) {
                    top.linkTo(toolBar.bottom, if (isSmall(context)) 20.dp else 30.dp)
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
                constrain(connectToNet) {
                    top.linkTo(reports.bottom)
                    end.linkTo(supervisorSettings.end)
                    start.linkTo(supervisorSettings.start)
                    width = Dimension.fillToConstraints
                }
                constrain(selectCurrentLanguage) {
                    top.linkTo(connectToNet.bottom)
                    end.linkTo(supervisorSettings.end)
                    start.linkTo(supervisorSettings.start)
                    width = Dimension.fillToConstraints
                }
                constrain(exit) {
                    top.linkTo(selectCurrentLanguage.bottom)
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
                showSupervisorPasswordDialog=true
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
                showMerchantPasswordDialog=true
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
              showReportPasswordDialog=true
            }
            SettingsItem(
                modifier = Modifier
                    .layoutId("connectToNet"),
                backgroundImageId = R.drawable.ic_item_settings2,
                backgroundIconId = R.drawable.background_reports,
                iconImageId = R.drawable.ic_setting_connection,
                isSmall = isSmall(context = context),
                title = stringResource(id = R.string.connection_to_net)
            ) {


                val intent =  Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
            Box(
                modifier = Modifier
                    .layoutId("selectCurrentLanguage")
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(MaterialTheme.colorScheme.background)

            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_item_settings2),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.FillBounds,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surface)
                )
                Row(
                    Modifier
                        .padding(top = 20.dp)
                        .fillMaxSize()
                        .align(Alignment.Center)
                ) {
                    Box(modifier = Modifier.padding(start = 18.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.background_moon),
                            contentDescription = "",
                            modifier = Modifier.size(28.dp),
                            contentScale = ContentScale.FillBounds,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.tertiary)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.ic_moon),
                            contentDescription = "",
                            modifier = Modifier
                                .size(28.dp)
                                .align(Alignment.Center),
                            contentScale = ContentScale.FillBounds,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondary)
                        )
                    }
                    Text(
                        text = stringResource(id = R.string.english_language),
                        modifier = Modifier.padding(start = 10.dp),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.displayMedium

                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(
                        modifier = Modifier.padding(end = 30.dp, top = 0.dp),
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
            }
            ExitItem(
                modifier = Modifier
                    .layoutId("exit"),
                backgroundImageId = R.drawable.ic_item_settings_4,
                backgroundIconId = R.drawable.ic_exit,
                isSmall = isSmall(context = context),
                title = stringResource(id = R.string.exit)
            ) {
                showExitPasswordDialog=true
            }
        }
        Image(
            painter = painterResource(id = R.drawable.totan),
            contentDescription = "",
            modifier = Modifier
                .size(TOTAN_ICON_SIZE)
                .align(Alignment.BottomCenter)
        )
        if (showExitPasswordDialog) {
            EnterPasswordBottomDialog(modifier = Modifier.align(Alignment.BottomCenter),
                errorMessage = stringResource(if(uiState.existPasswordError!=-1)
                    uiState.existPasswordError else R.string.empty_message),
                onConfirmButtonClicked = {
                    validateExitPassword(it)
                },
                onCancelButtonClicked = {
                    showExitPasswordDialog = false
                })
        }
        if (showSupervisorPasswordDialog) {
            EnterPasswordBottomDialog(modifier = Modifier.align(Alignment.BottomCenter),
                errorMessage = stringResource(if(uiState.supervisorPasswordError!=-1)
                    uiState.supervisorPasswordError else R.string.empty_message),
                onCancelButtonClicked = {
                    showSupervisorPasswordDialog=false
                }) {
                validateSupervisorPassword(it)
            }
        }
        if (showMerchantPasswordDialog) {
            EnterPasswordBottomDialog(modifier = Modifier.align(Alignment.BottomCenter),
                errorMessage =stringResource(if(uiState.merchantPasswordError!=-1)
                    uiState.merchantPasswordError else R.string.empty_message),
                onCancelButtonClicked = {
                    showMerchantPasswordDialog=false
                }) {
                validateMerchantPassword(it)
            }
        }
        if (showReportPasswordDialog) {
            EnterPasswordBottomDialog(modifier = Modifier.align(Alignment.BottomCenter),
                errorMessage = stringResource(if(uiState.reportPasswordError!=-1)
                    uiState.reportPasswordError else
                R.string.empty_message),
                onCancelButtonClicked = {
                    showReportPasswordDialog=false
                }) {
                validateReportPassword(it)
            }
        }
    }
}






