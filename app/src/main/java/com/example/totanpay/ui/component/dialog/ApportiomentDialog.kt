package com.example.totanpay.ui.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import com.example.totanpay.R
import com.example.totanpay.ui.component.Keypad
import com.example.totanpay.ui.theme.TotanPayTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApportiomentDialog(
    onCancelButtonClicked: () -> Unit, onConfirmButtonClicked: (String) -> Unit
) {
    var firstPinKey by remember {
        mutableStateOf("")
    }
    ModalBottomSheet(
        onDismissRequest = { onCancelButtonClicked() },
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            ConstraintLayout(
                ConstraintSet {
                    val value = createRefFor("value")
                    val title = createRefFor("title")

                    constrain(title) {
                        top.linkTo(parent.top, 3.dp)
                        start.linkTo(parent.start, 12.dp)
                        end.linkTo(parent.end)
                    }
                    constrain(value) {
                        top.linkTo(title.bottom, 3.dp)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                    }
                }, modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .fillMaxWidth()
                    .height(450.dp)
            ) {
                Text(
                    text = stringResource(R.string.plz_enter_percent),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.layoutId("title"),
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
                Row(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .padding(horizontal = 12.dp)
                        .fillMaxWidth()
                        .border(
                            1.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(8.dp)
                        )
                        .layoutId("value"), Arrangement.End
                ) {
                    Text(
                        text = firstPinKey,
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .fillMaxWidth()
                            .padding(top = 10.dp, bottom = 10.dp, end = 5.dp),
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.End
                    )
                }
            }

            Keypad(
                Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                onKeyClicked = {
                    val temp="$firstPinKey$it"
                    if (temp.isNotEmpty()) {
                        if (temp.toInt() in 1..100) {
                            firstPinKey = temp
                        } else {
                            firstPinKey
                        }
                    } else ""
                },
                onClearKeyClicked = {
                    val temp = firstPinKey.dropLast(1)
                    if (temp.isNotEmpty()) {
                        if (temp.toInt() in 0..100) {
                            firstPinKey = temp
                        } else {
                            firstPinKey
                        }
                    } else ""

                },
                onTikKeyClicked = {
                    onConfirmButtonClicked(firstPinKey)
                })
        }

    }
}

@Composable
@Preview
fun ApportiomentDialogPreview() {
    TotanPayTheme {
        ApportiomentDialog(onConfirmButtonClicked = {}, onCancelButtonClicked = {})
    }
}