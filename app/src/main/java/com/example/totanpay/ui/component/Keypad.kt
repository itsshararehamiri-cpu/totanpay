package com.example.totanpay.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.totanpay.R
import com.example.totanpay.ui.theme.Green50
import com.example.totanpay.ui.theme.TotanPayTheme

@Composable
fun Keypad(
    modifier: Modifier,
    onKeyClicked: (String) -> Unit,
    onTikKeyClicked: () -> Unit,
    onClearKeyClicked: () -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            DigitKey(
                number = "3", modifier = Modifier
                    .padding(horizontal = 7.dp)
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                onKeyClicked(it)
            }

            DigitKey(
                number = "2", modifier = Modifier
                    .padding(horizontal = 7.dp)
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                onKeyClicked(it)
            }
            DigitKey(
                number = "1", modifier = Modifier
                    .padding(horizontal = 7.dp)
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                onKeyClicked(it)
            }

        }
        Row(
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth()
        ) {
            DigitKey(
                number = "6", modifier = Modifier
                    .padding(horizontal = 7.dp)
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                onKeyClicked(it)
            }
            DigitKey(
                number = "5", modifier = Modifier
                    .padding(horizontal = 7.dp)
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                onKeyClicked(it)
            }
            DigitKey(
                number = "4", modifier = Modifier
                    .padding(horizontal = 7.dp)
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                onKeyClicked(it)
            }
        }
        Row(
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth()
        ) {

            DigitKey(
                number = "9", modifier = Modifier
                    .padding(horizontal = 7.dp)
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                onKeyClicked(it)
            }
            DigitKey(
                number = "8", modifier = Modifier
                    .padding(horizontal = 7.dp)
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                onKeyClicked(it)
            }
            DigitKey(
                number = "7", modifier = Modifier
                    .padding(horizontal = 7.dp)
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                onKeyClicked(it)
            }

        }
        Row(
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth()
        ) {

            TickKey(
                modifier = Modifier
                    .padding(horizontal = 7.dp)
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                onTikKeyClicked()
            }
            DigitKey(
                number = "0", modifier = Modifier
                    .padding(horizontal = 7.dp)
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                onKeyClicked(it)
            }
            ClearKey(
                modifier = Modifier
                    .padding(horizontal = 7.dp)
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                onClearKeyClicked()
            }

        }
    }

}

@Composable
fun DigitKey(number: String, modifier: Modifier, onClick: (String) -> Unit) {
    Box(
        modifier = modifier
            .height(60.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.background)
            .clickable {
                onClick(number)
            }
            .fillMaxWidth(1f)
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .padding(top = 6.dp, bottom = 6.dp)
                .align(Alignment.Center),
            text = number,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold, fontSize = 22.sp)
        )
    }
}

@Composable
fun TickKey(modifier: Modifier, onKeyClicked: () -> Unit) {
    Box(
        modifier = modifier
            .height(60.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Green50)
            .clickable {
                onKeyClicked()
            }
            .fillMaxWidth(1f)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_tick), "",
            modifier = Modifier
                .size(15.dp)
                .align(Alignment.Center),
        )
    }
}

@Composable
fun ClearKey(modifier: Modifier, onKeyClicked: () -> Unit) {
    Box(
        modifier = modifier
            .height(60.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.background)
            .clickable {
                onKeyClicked()
            }
            .fillMaxWidth(1f)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_clear), "",
            modifier = Modifier
                .size(15.dp)
                .align(Alignment.Center), colorFilter = ColorFilter.tint(
                MaterialTheme.colorScheme.onBackground,
            )
        )
    }
}

@Composable
@Preview
fun KeyPreview() {
    TotanPayTheme {
        // DigitKey(number = "1", Modifier.fillMaxWidth())
    }
}

@Composable
@Preview
fun KeypadPreview() {
    TotanPayTheme {
        // Keypad(Modifier)
    }
}