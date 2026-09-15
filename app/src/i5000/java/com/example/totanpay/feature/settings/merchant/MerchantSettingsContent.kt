package com.example.totanpay.feature.settings.merchant

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.example.totanpay.R
import com.example.totanpay.ui.component.compound.I5000SettingsItem
import com.example.totanpay.ui.theme.FONT_SIZE_16
import com.example.totanpay.ui.theme.Green50

@Composable
fun MerchantSettingsContent(
    uiState: MerchantSettingsUiState,
    onBackClicked: () -> Unit,
    onA: () -> Unit,
    onPrinterSettings: () -> Unit,
    onChangeMerchantPassword: () -> Unit,
    onEnableAndDisableVoice: (Boolean) -> Unit,
    onSelectTheme: (Boolean) -> Unit
) {
    var playSoundIsEnabled by remember { mutableStateOf(false) }
    var themeIsDarkStatus: Boolean by remember { mutableStateOf(false) }
    LaunchedEffect(uiState.themeIsDark) {
        themeIsDarkStatus = uiState.themeIsDark
    }
    val scrollState = rememberScrollState()
    LaunchedEffect(uiState.playSoundIsEnabled) {
        playSoundIsEnabled = uiState.playSoundIsEnabled
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ConstraintLayout(
            ConstraintSet {
                val a = createRefFor("a")
                val printerSettings = createRefFor("printerSettings")
                val changeMerchantPassword = createRefFor("changeMerchantPassword")
                val enableAndDisableVoice = createRefFor("enableAndDisableVoice")
                val chooseTheme = createRefFor("chooseTheme")
               // val enableAndDisableVoice = createRefFor("enableAndDisableVoice")
                constrain(a) {
                    top.linkTo(parent.top, 28.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(printerSettings) {
                    top.linkTo(a.bottom, 10.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(changeMerchantPassword) {
                    top.linkTo(printerSettings.bottom, 10.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(enableAndDisableVoice) {
                    top.linkTo(changeMerchantPassword.bottom, 10.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
//                constrain(sleepingSettingsDevice) {
//                    top.linkTo(enableAndDisableVoice.bottom, 10.dp)
//                    end.linkTo(enableAndDisableVoice.end)
//                    start.linkTo(enableAndDisableVoice.start)
//                    width = Dimension.fillToConstraints
//                }
                constrain(chooseTheme) {
                    top.linkTo(enableAndDisableVoice.bottom, 10.dp)
                    end.linkTo(enableAndDisableVoice.end)
                    start.linkTo(enableAndDisableVoice.start)
                    width = Dimension.fillToConstraints
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(scrollState)

        ) {

            I5000SettingsItem(
                modifier = Modifier.layoutId("a"),
                iconImageId = R.drawable.ic_i5000_account_management,
                title = stringResource(id = R.string.account_management),
                height = 52.dp
            ) {
                onA()
            }
            I5000SettingsItem(
                modifier = Modifier.layoutId("printerSettings"),
                iconImageId = R.drawable.ic_i5000_print_settings,
                title = stringResource(id = R.string.printer_settings),
                height = 52.dp
            ) {
                onPrinterSettings()
            }
            I5000SettingsItem(
                modifier = Modifier.layoutId("changeMerchantPassword"),
                iconImageId = R.drawable.ic_i5000_chane_password,
                title = stringResource(id = R.string.change_merchant_password),
                height = 52.dp
            ) {
                onChangeMerchantPassword()

            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .padding(horizontal = 12.dp)
                    .layoutId("enableAndDisableVoice"), contentAlignment = Alignment.Center
            ) {
                Row(
                    Modifier
                        .fillMaxSize()
                        .align(Alignment.Center),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_i5000_enable_voice),
                        contentDescription = "",
                        modifier = Modifier.size(24.dp),
                        contentScale = ContentScale.Fit
                    )

                    Text(
                        text = stringResource(id = R.string.enable_and_disable_voice),
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .weight(1f),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = FONT_SIZE_16,
                            fontWeight = FontWeight.SemiBold
                        )

                    )
                    Switch(
                        checked = playSoundIsEnabled,
                        onCheckedChange = {
                            playSoundIsEnabled = it
                            onEnableAndDisableVoice(it)

                        },
                        colors = SwitchDefaults.colors(
                            checkedTrackColor = Green50
                        )
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .padding(horizontal = 12.dp)
                    .layoutId("chooseTheme"), contentAlignment = Alignment.Center
            ) {
                Row(
                    Modifier
                        .fillMaxSize()
                        .align(Alignment.Center),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_i5000_light_mode),
                        contentDescription = "",
                        modifier = Modifier.size(24.dp),
                        contentScale = ContentScale.Fit)

                    Text(
                        text = stringResource(id = R.string.dark_mode),
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .weight(1f),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = FONT_SIZE_16,
                            fontWeight = FontWeight.SemiBold
                        )

                    )
                    Switch(
                        checked = themeIsDarkStatus,
                        onCheckedChange = {
                            themeIsDarkStatus = it
                            onSelectTheme(it)

                        },
                        colors = SwitchDefaults.colors(
                            checkedTrackColor = Green50
                        )
                    )
                }
            }
        }
    }
}