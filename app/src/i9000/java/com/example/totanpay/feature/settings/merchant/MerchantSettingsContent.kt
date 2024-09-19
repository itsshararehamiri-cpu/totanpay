package com.example.totanpay.feature.settings.merchant
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
import com.example.totanpay.ui.component.compound.SettingsItem
import com.example.totanpay.ui.theme.Dimensions.TOTAN_ICON_SIZE
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
    val context = LocalContext.current
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
                val toolBar = createRefFor("toolBar")
                val a = createRefFor("a")
                val printerSettings = createRefFor("printerSettings")
                val changeMerchantPassword = createRefFor("changeMerchantPassword")
                val enableAndDisableVoice = createRefFor("enableAndDisableVoice")
                val chooseTheme = createRefFor("chooseTheme")
                val sleepingSettingsDevice = createRefFor("sleepingSettingsDevice")
                constrain(toolBar) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(a) {
                    top.linkTo(toolBar.bottom,if(isSmall(context)) 0.dp else 30.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(printerSettings) {
                    top.linkTo(a.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(changeMerchantPassword) {
                    top.linkTo(printerSettings.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(enableAndDisableVoice) {
                    top.linkTo(changeMerchantPassword.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(sleepingSettingsDevice) {
                    top.linkTo(enableAndDisableVoice.bottom)
                    end.linkTo(enableAndDisableVoice.end)
                    start.linkTo(enableAndDisableVoice.start)
                    width = Dimension.fillToConstraints
                }
                constrain(chooseTheme) {
                    top.linkTo(sleepingSettingsDevice.bottom)
                    end.linkTo(enableAndDisableVoice.end)
                    start.linkTo(enableAndDisableVoice.start)
                    width = Dimension.fillToConstraints
                }
            }, modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                // .padding(bottom = PADDING_BOTTOM_SETTINGS)
                .verticalScroll(scrollState)

        ) {
            BackButton(
                title = stringResource(id = R.string.merchant_settings),
                modifier = BackButtonModifier
                    .layoutId("toolBar")
            ) {
                onBackClicked()
            }
            SettingsItem(
                modifier = Modifier
                    .layoutId("a"),
                iconImageId = R.drawable.user_octagon,
                title = stringResource(id = R.string.account_management),
                backgroundImageId = R.drawable.ic_item_settings1,
                isSmall = isSmall(context),
                backgroundIconId = R.drawable.user_octagon__,
            ) {
                onA()
            }
            SettingsItem(
                modifier = Modifier
                    .layoutId("printerSettings"),
                iconImageId = R.drawable.printer_,
                title = stringResource(id = R.string.printer_settings),
                backgroundImageId = R.drawable.ic_item_settings1,
                isSmall = isSmall(context),
                backgroundIconId = R.drawable.printer,
            ) {
                onPrinterSettings()
            }
            SettingsItem(
                modifier = Modifier
                    .layoutId("changeMerchantPassword"),
                iconImageId = R.drawable.ic_change_merchant_password,
                title = stringResource(id = R.string.change_merchant_password),
                backgroundImageId = R.drawable.ic_item_settings2,
                isSmall = isSmall(context),
                backgroundIconId = R.drawable.background_change_merchant_password,
            ) {
                onChangeMerchantPassword()

            }
            Box(
                modifier = Modifier
                    .layoutId("enableAndDisableVoice")
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(MaterialTheme.colorScheme.background)

            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_item_settings2),
                    contentDescription = "",
                    modifier = Modifier
                        .height(80.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.FillBounds,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surface)
                )
                Row(
                    Modifier
                        .fillMaxSize()
                        .align(Alignment.Center)
                ) {
                    Box(
                        modifier = Modifier
                            .padding(start = 18.dp)
                            .padding(top = 10.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.background_change_merchant_password),
                            contentDescription = "",
                            modifier = Modifier.size(28.dp),
                            contentScale = ContentScale.FillBounds,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.tertiary)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.ic_playback_sound),
                            contentDescription = "",
                            modifier = Modifier
                                .size(28.dp)
                                .align(Alignment.Center),
                            contentScale = ContentScale.FillBounds,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surface)
                        )
                    }
                    Text(
                        text = stringResource(id = R.string.enable_and_disable_voice),
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .padding(top = 10.dp),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.displayMedium

                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(
                        modifier = Modifier.padding(end = 30.dp, top = 0.dp),
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
                    .layoutId("chooseTheme")
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(MaterialTheme.colorScheme.background)

            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_item_settings_4),
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
                        text = stringResource(id = R.string.dark_mode),
                        modifier = Modifier.padding(start = 10.dp),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.displayMedium

                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(
                        modifier = Modifier.padding(end = 30.dp, top = 0.dp),
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
        if (!isSmall(context = context))
            Image(
                painter = painterResource(id = R.drawable.totan),
                contentDescription = "",
                modifier = Modifier
                    .size(TOTAN_ICON_SIZE)
                    .align(Alignment.BottomCenter)
            )
    }
}