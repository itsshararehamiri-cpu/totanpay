package com.example.totanpay.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.example.totanpay.ui.priceFilter
import com.example.totanpay.ui.theme.Black400
import com.example.totanpay.ui.theme.TotanPayTheme
import com.example.totanpay.ui.theme.White100
import com.example.totanpay.ui.theme.White200

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextInput(
    modifier: Modifier,
    title: String,
    trailerTitle: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    val textFieldValue = TextFieldValue(text = value, selection = TextRange(value.length))

    Column(
        modifier = modifier.wrapContentHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            modifier = Modifier,
            text = title,
            color = White,
            style = MaterialTheme.typography.titleMedium
        )
        Box(
            modifier = Modifier
                .padding(top = 5.dp)
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            White100.copy(alpha = 0.8f),
                            White200.copy(alpha = 1f)
                        )
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .clip(RoundedCornerShape(8.dp))
                // .background(color = White100.copy(alpha = 0.5f))
                .background(Black400)

        ) {
            ConstraintLayout(
                ConstraintSet {
                   val trailer = createRefFor("trailer")
                    val textfield = createRefFor("textfield")
                    constrain(trailer) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
                    constrain(textfield) {
                        top.linkTo(trailer.top)
                        bottom.linkTo(trailer.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(1f)
            ) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .layoutId("textfield")
                        .wrapContentHeight()
                        .padding(horizontal = 20.dp),
                    value = textFieldValue,
                    onValueChange = {
                        onValueChange(it.text)
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Black400,
                        disabledTextColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.titleSmall.copy(
                        textDirection = TextDirection.Ltr,
                        color = White
                    ),
//                    visualTransformation = { annotatedString ->
//                      //  priceFilter(annotatedString.text)
//                    },
                )
                Text(
                    modifier = Modifier
                        .padding(top=10.dp,end = 15.dp, start = 1.dp)
                        .layoutId("trailer")
                        .wrapContentWidth(),
                    text = trailerTitle,
                    style = MaterialTheme.typography.labelMedium, color = White100
                )
            }
        }


    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceTextInput(
    modifier: Modifier,
    title: String,
    trailerTitle: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    val textFieldValue = TextFieldValue(text = value, selection = TextRange(value.length))

    Column(
        modifier = modifier.wrapContentHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            modifier = Modifier,
            text = title,
            color = White,
            style = MaterialTheme.typography.titleMedium
        )
        Box(
            modifier = Modifier
                .padding(top = 5.dp)
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            White100.copy(alpha = 0.8f),
                            White200.copy(alpha = 1f)
                        )
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .clip(RoundedCornerShape(8.dp))
                // .background(color = White100.copy(alpha = 0.5f))
                .background(Black400)

        ) {
            ConstraintLayout(
                ConstraintSet {
                    val trailer = createRefFor("trailer")
                    val textfield = createRefFor("textfield")
                    constrain(trailer) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
                    constrain(textfield) {
                        top.linkTo(trailer.top)
                        bottom.linkTo(trailer.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(trailer.start)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(1f)
            ) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .layoutId("textfield")
                        .wrapContentHeight()
                        .padding(horizontal = 8.dp),
                    value = textFieldValue,
                    onValueChange = {
                        onValueChange(it.text)
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Black400,
                        disabledTextColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.titleSmall.copy(
                        textDirection = TextDirection.Ltr,
                        color = White
                    ),
                    visualTransformation = { annotatedString ->
                        priceFilter(annotatedString.text)
                    },
                )
                Text(
                    modifier = Modifier
                        .padding(end = 15.dp, start = 1.dp)
                        .layoutId("trailer")
                        .wrapContentWidth(),
                    text = trailerTitle,
                    style = MaterialTheme.typography.labelMedium, color = White100
                )

            }
        }


    }
}

@Composable
@Preview
fun TextInputPreview() {
    TotanPayTheme {
        TextInput(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .fillMaxSize()
                .background(
                    Black
                ), title = "تایید", trailerTitle = "09", value = "183698798"
        ) {

        }
    }
}