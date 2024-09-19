package com.example.totanpay.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.layoutId
import com.example.totanpay.R
import com.example.totanpay.ui.DialogContainerModifier
import com.example.totanpay.ui.theme.Green50
import com.example.totanpay.ui.theme.TotanPayTheme

@Composable
fun ConfirmDialog(isSmall:Boolean,
    onCancelButtonClicked: () -> Unit,
    onConfirmButtonClicked: () -> Unit
) {
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(DialogContainerModifier()) {
            ConstraintLayout(
                ConstraintSet {
                    val imageTitle = createRefFor("imageTitle")
                    val title = createRefFor("title")
                    val cancelButton = createRefFor("cancelButton")
                    val confirmButton = createRefFor("confirmButton")
                    constrain(title) {
                        top.linkTo(parent.top, 20.dp)
                        start.linkTo(parent.start,20.dp)
                        end.linkTo(parent.end)
                    }
                    constrain(imageTitle) {
                        top.linkTo(title.top)
                        bottom.linkTo(title.bottom)
                        start.linkTo(parent.start, 1.dp)
                        end.linkTo(title.start)
                    }
                    constrain(cancelButton) {
                        top.linkTo(title.bottom, 20.dp)
                        end.linkTo(parent.end,8.dp)
                        start.linkTo(confirmButton.end, if(isSmall)4.dp else 10.dp)
                        width = Dimension.fillToConstraints
                    }
                    constrain(confirmButton) {
                        bottom.linkTo(cancelButton.bottom)
                        top.linkTo(cancelButton.top)
                        start.linkTo(parent.start,8.dp)
                        end.linkTo(cancelButton.start,  if(isSmall)4.dp else 10.dp)
                        width = Dimension.fillToConstraints
                    }
                }, modifier = Modifier.padding(horizontal = 0.dp)
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 10.dp)

            ) {
                Image(
                    modifier = Modifier
                        .size(28.dp)
                        .layoutId("imageTitle"),
                    painter = painterResource(id = R.drawable.ic_tick_circle),
                    contentDescription = "",
                )
                Text(
                    text = stringResource(R.string.are_you_sure_you_want_to_make_changes),
                    modifier = Modifier
                        .wrapContentWidth()
                        .layoutId("title"),
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = if (isSmall) 20.sp else 15.sp),
                    )
                Box(
                    modifier =
                    Modifier
                        .padding(bottom = 20.dp)
                        .padding(horizontal = if (isSmall) 0.dp else 8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, MaterialTheme.colorScheme.onSurface, RoundedCornerShape(8.dp))
                        .clickable { onCancelButtonClicked() }
                        .padding(horizontal = 16.dp)
                        .padding(top = 10.dp, bottom = 10.dp)
                        .fillMaxWidth(1f)
                        .layoutId("cancelButton")
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = stringResource(R.string.cancel),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = if(isSmall)18.sp else 15.sp)
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                        .padding(horizontal = if (isSmall) 0.dp else 8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Green50)
                        .clickable {
                            onConfirmButtonClicked()
                        }
                        .padding(horizontal = 16.dp)
                        .padding(
                            top = 10.dp, bottom = 10.dp
                        )
                        .fillMaxWidth(1f)
                        .layoutId("confirmButton")
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = stringResource(R.string.confirm),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = if(isSmall)18.sp else 15.sp)
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun ConfirmDialogPreview() {
    TotanPayTheme {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            ConfirmDialog(isSmall = true, onConfirmButtonClicked = {}, onCancelButtonClicked = {})
        }
    }
}