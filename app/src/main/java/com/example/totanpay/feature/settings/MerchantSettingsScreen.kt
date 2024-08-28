package com.example.totanpay.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.example.totanpay.R
import com.example.totanpay.ui.component.BackButton
import com.example.totanpay.ui.theme.Background

@Composable
fun MerchantSettingsScreen(
    onBackClicked: () -> Unit,onReportClicked:()->Unit
) {
    MerchantSettingsContent(
        onBackClicked = { onBackClicked() },onReportClicked={onReportClicked()})
}

@Composable
fun MerchantSettingsContent(
    onBackClicked: () -> Unit,onReportClicked:()->Unit
) {
    ConstraintLayout(
        ConstraintSet {
            val toolBar = createRefFor("toolBar")
            val reports=createRefFor("reports")
            val enableAndDisableVoice = createRefFor("enableAndDisableVoice")
            val chooseTheme = createRefFor("chooseTheme")
            constrain(toolBar) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }
            constrain(reports) {
                top.linkTo(toolBar.bottom, 30.dp)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }
            constrain(enableAndDisableVoice) {
                top.linkTo(reports.bottom, 30.dp)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }
            constrain(chooseTheme) {
                top.linkTo(enableAndDisableVoice.bottom,30.dp)
                end.linkTo(enableAndDisableVoice.end)
                start.linkTo(enableAndDisableVoice.start)
                width = Dimension.fillToConstraints
            }
        }, modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        BackButton(
            title = stringResource(id = R.string.supervisor_settings), modifier = Modifier
                .fillMaxWidth()
                .layoutId("toolBar")
        ) {
            onBackClicked()
        }
        SettingsItem(
            modifier = Modifier
                .layoutId("reports"),
            iconImageId = R.drawable.enable_and_disable_voice,
            title = stringResource(id = R.string.reports)
        ) {
            onReportClicked()
        }
        SettingsItem(
            modifier = Modifier
                .layoutId("enableAndDisableVoice"),
            iconImageId = R.drawable.enable_and_disable_voice,
            title = stringResource(id = R.string.enable_and_disable_voice)
        ) {
        }
        SettingsItem(
            modifier = Modifier
                .layoutId("chooseTheme"),
            iconImageId = R.drawable.ic_key_injection_settings,
            title = stringResource(id = R.string.choose_theme)
        ) {
        }
    }
}
