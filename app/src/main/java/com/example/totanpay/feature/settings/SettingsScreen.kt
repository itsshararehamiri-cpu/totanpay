package com.example.totanpay.feature.settings



import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.example.totanpay.R
import com.example.totanpay.ui.component.BackButton
import com.example.totanpay.ui.theme.Background
import com.example.totanpay.ui.theme.Black100
import com.example.totanpay.ui.theme.START_PADDING
import com.example.totanpay.ui.theme.TotanPayTheme
import com.example.totanpay.ui.theme.White100

@Composable
fun SettingsScreen(
    onBackClicked: () -> Unit, onMerchantSettingsClicked: () -> Unit,
    onSupervisorSettingsClicked: () -> Unit
) {
    BackHandler {
        onBackClicked()
    }
    SettingsContent(
        onBackClicked = { onBackClicked() },
        onMerchantSettingsClicked = {onMerchantSettingsClicked()},
        onSupervisorSettingsClicked = {onSupervisorSettingsClicked()})
}

@Composable
fun SettingsContent(
    onBackClicked: () -> Unit, onMerchantSettingsClicked: () -> Unit,
    onSupervisorSettingsClicked: () -> Unit
) {
    ConstraintLayout(
        ConstraintSet {
            val toolBar = createRefFor("toolBar")
            val supervisorSettings = createRefFor("supervisorSettings")
            val merchantSettings = createRefFor("merchantSettings")
            constrain(toolBar) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }
            constrain(supervisorSettings) {
                top.linkTo(toolBar.bottom, 30.dp)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            }
            constrain(merchantSettings) {
                top.linkTo(supervisorSettings.bottom,30.dp)
                end.linkTo(supervisorSettings.end)
                start.linkTo(supervisorSettings.start)
                width = Dimension.fillToConstraints
            }
        }, modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        BackButton(
            title = stringResource(id = R.string.settings), modifier = Modifier
                .fillMaxWidth()
                .layoutId("toolBar")
        ) {
            onBackClicked()
        }
        SettingsItem(
            modifier = Modifier
                .layoutId("supervisorSettings"),
            iconImageId = R.drawable.ic_supervisor_settings,
            title = stringResource(id = R.string.supervisor_settings)
        ) {
            onSupervisorSettingsClicked()
        }
        SettingsItem(
            modifier = Modifier
                .layoutId("merchantSettings"),
            iconImageId = R.drawable.ic_merchant_settings,
            title = stringResource(id = R.string.merchant_settings)
        ) {
            onMerchantSettingsClicked()
        }


    }
}

@Composable
fun SettingsItem(modifier: Modifier, iconImageId:Int,title: String, onItemClicked: () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable { onItemClicked() }
            .background(Black100)
    ) {

        Row(
            Modifier
                .padding(top = 20.dp)
                .fillMaxSize()
                .align(Alignment.Center)
        ) {
            Image(painter = painterResource(id = iconImageId), contentDescription = "",
                modifier = modifier
                    .padding(start = 9.dp)
                    .size(24.dp))
            Text(
                text = title,
                modifier = Modifier.padding(start = 8.dp)
                ,
                color = White100,
                style = MaterialTheme.typography.displayMedium

            )
            Spacer(modifier = Modifier.weight(1f))
            Image(painter = painterResource(id = R.drawable.ic_arrow_left_gray), contentDescription = "",
                modifier = modifier
                    .padding(end = 12.dp)
                    .size(24.dp))

        }
    }
}

@Composable
@Preview
fun SettingsItemPreview() {
    TotanPayTheme {
        SettingsItem(
            modifier = Modifier
            ,
            iconImageId = R.drawable.ic_merchant_settings,
            title = stringResource(id = R.string.last_transaction)
        ) {
        }
    }
}

@Composable
@Preview
fun SettingsContentPreview() {
    TotanPayTheme {


    }
}