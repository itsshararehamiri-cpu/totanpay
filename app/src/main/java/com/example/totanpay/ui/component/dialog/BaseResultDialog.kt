package com.example.totanpay.ui.component.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.example.totanpay.common.mainButtonModifier
import com.example.totanpay.ui.component.button.MainButton


@Composable
fun BaseResultDialog(
    message: String,
    logoId: Int,
    onDismiss: () -> Unit,
    onConfirmButtonClicked: () -> Unit
) {
    Dialog(
        onDismissRequest = {}, properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        ConstraintLayout(
            ConstraintSet {
                val logo = createRefFor("logo")
                val mainContainer = createRefFor("mainContainer")
                constrain(mainContainer) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                constrain(logo) {
                    top.linkTo(mainContainer.top)
                    bottom.linkTo(mainContainer.top)
                    end.linkTo(mainContainer.end, 0.dp)
                    start.linkTo(mainContainer.start, 0.dp)
                }
            }, modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
        ) {

            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                   .wrapContentHeight()
                    .padding(0.dp)
                    .layoutId("mainContainer"),
                colors = CardDefaults.cardColors(containerColor = Color.White)

            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .wrapContentHeight()
                        .background(Color.White)
                ) {
                    Text(
                        text = message,
                        modifier = Modifier.padding(horizontal = 12.dp)
                            .padding(top = 30.dp)
                            .wrapContentWidth(),
                        color = Color.Black,
                        textAlign = TextAlign.Center, style = MaterialTheme.typography.titleMedium, minLines = 1
                    )
                    MainButton(
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .fillMaxWidth()
                            .mainButtonModifier(isSmall = false), onClick = {
                            onConfirmButtonClicked()
                        })
                }
            }
            Image(
                painterResource(logoId), modifier =
                Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .layoutId("logo"),
                contentScale = ContentScale.Fit, contentDescription = ""
            )
        }
    }
}