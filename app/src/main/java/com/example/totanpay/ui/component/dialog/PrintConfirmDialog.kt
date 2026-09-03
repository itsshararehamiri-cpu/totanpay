package com.example.totanpay.ui.component.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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


@Composable
fun PrintConfirmDialog(isVisible: Boolean, message: String,
                       onDismiss: () -> Unit,
                       onConfirmButtonClicked: () -> Unit
) {
    val context= LocalContext.current
    if (isVisible)
    {
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

                        constrain(imageTitle) {
                            top.linkTo(parent.top,20.dp)
                            start.linkTo(parent.start, 10.dp)
                            end.linkTo(parent.end,10.dp)
                        }
                        constrain(title) {
                            top.linkTo(imageTitle.bottom, 20.dp)
                            start.linkTo(parent.start,20.dp)
                            end.linkTo(parent.end,20.dp)
                        }
                        constrain(cancelButton) {
                            top.linkTo(title.bottom, 20.dp)
                            end.linkTo(parent.end,8.dp)
                            start.linkTo(confirmButton.end,  10.dp)
                            width = Dimension.fillToConstraints
                        }
                        constrain(confirmButton) {
                            bottom.linkTo(cancelButton.bottom)
                            top.linkTo(cancelButton.top)
                            start.linkTo(parent.start,8.dp)
                            end.linkTo(cancelButton.start,   10.dp)
                            width = Dimension.fillToConstraints
                        }
                    }, modifier = Modifier
                        .padding(horizontal = 0.dp)
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 10.dp)

                ) {
                    Image(
                        modifier = Modifier
                            .size(28.dp)
                            .layoutId("imageTitle")
                          ,
                        painter = painterResource(id = R.drawable.ic_warning),
                        contentDescription = "",
                        alignment = Alignment.Center,
                    )
                    Text(
                        text = "${message} ${context.getString(R.string.error_in_print_message)}",
                        modifier = Modifier
                            .wrapContentWidth()
                            .layoutId("title"),
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 15.sp),
                    )
                    Box(
                        modifier =
                            Modifier
                                .padding(bottom = 20.dp)
                                .padding(horizontal = 8.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(
                                    1.dp,
                                    MaterialTheme.colorScheme.onSurface,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { onDismiss() }
                                .padding(horizontal = 16.dp)
                                .padding(top = 10.dp, bottom = 10.dp)
                                .fillMaxWidth(1f)
                                .layoutId("cancelButton")
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = stringResource(R.string.cancel),
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.titleMedium.copy(fontSize =  15.sp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .padding(bottom = 20.dp)
                            .padding(horizontal = 8.dp)
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
                            text = stringResource(R.string.continue_),
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.titleMedium.copy(fontSize =  15.sp)
                        )
                    }
                }
            }
        }
    }
    }
@Preview
@Composable
fun PrintConfirmDialogPreview(
) {
    PrintConfirmDialog(isVisible=true, message="تتتتتتتتتتتتتتتتتتتتتتتتتتتتتتتتتتتتتتتت",
        onDismiss={},
    onConfirmButtonClicked={})
}