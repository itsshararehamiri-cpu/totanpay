package com.example.totanpay.ui.component


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.example.totanpay.R
import com.example.totanpay.data.Operator
import com.example.totanpay.ui.theme.Black100
import com.example.totanpay.ui.theme.Black400
import com.example.totanpay.ui.theme.Black500
import com.example.totanpay.ui.theme.END_PADDING
import com.example.totanpay.ui.theme.Orange
import com.example.totanpay.ui.theme.Pink
import com.example.totanpay.ui.theme.TotanPayTheme
import com.example.totanpay.ui.theme.White100
import com.example.totanpay.ui.theme.White200

@Composable
fun HambergerButton(modifier: Modifier, onClick: () -> Unit) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ), colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ), shape = RoundedCornerShape(size = 5.dp), modifier = modifier.clickable {
            onClick()
        }
    ) {
        Row(
            modifier = Modifier.padding(all = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier.size(32.dp),
                painter = painterResource(id = R.drawable.ic_hamberger),
                contentDescription = ""
            )
        }
    }

}


@Composable
@Preview
fun HambergerButtonPreview() {
    TotanPayTheme {
//        HambergerButton(Modifier) {
//
//        }
    }
}

@Composable
fun MainButton(
    modifier: Modifier = Modifier, title: String, onClick: () -> Unit = {}
) {
    val brush = Brush.radialGradient(//verticalGradient
        colors = listOf(
//            Color.White.copy(0.70f),
//            Color.White.copy(0.70f),
//            Color.White.copy(0.70f),
            Orange, Orange
        )
    )
//
//    Button(
//        onClick = onClick,
//        modifier = modifier
//            .shadow(
//                elevation = 8.dp,
//                shape = RoundedCornerShape(8.dp),
//                clip = true
//            )
//            .clip(RoundedCornerShape(8.dp))
//            .background(brush = brush),
//        colors = ButtonDefaults.buttonColors(
//            containerColor = Color.Transparent, contentColor = Color.White
//        )
//    ) {
//        Text(text = title, color = Color.White, style = MaterialTheme.typography.titleMedium)
//    }
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Orange)
            //   .shadow(
//                elevation = 8.dp,
//                shape = RoundedCornerShape(8.dp),
//                clip = true
//            )
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp)
            .padding(
                top = 5.dp, bottom = 5.dp
            )

    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = title,
            color = Color.White,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
@Preview
fun ColorBrushedButtonPreview() {
//    TotanPayTheme {
//        MainButton(
//            title = "تایید", modifier = Modifier
//                .fillMaxWidth()
//                .height(50.dp)
//        ) {
//
//        }
//    }
}

@Composable
@Preview
fun CancelButtonPreview() {
    TotanPayTheme {
        CancelButton(
            title = "تایید", modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {

        }
    }
}

@Composable
fun CancelButton(
    modifier: Modifier = Modifier, title: String, onClick: () -> Unit = {}
) {

    Button(
        onClick = onClick,
        modifier = modifier
        //  .clip(RoundedCornerShape(4.dp))
        ,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White, contentColor = Color.Black
        ), shape = RoundedCornerShape(4.dp)
    ) {
        Text(text = title, color = Color.Black, style = MaterialTheme.typography.titleSmall)
    }
}

@Composable
fun BackButton(title: String, modifier: Modifier, onBackButtonClicked: () -> Unit) {
    Row(modifier = modifier
        .height(74.dp)
        .clickable { onBackButtonClicked() }) {
        Image(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = END_PADDING)
                .size(28.dp),
            painter = painterResource(id = R.drawable.ic_arrow_back),
            contentDescription = "back_button"
        )
        Text(
            text = title, color = White, modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 3.dp)
        )
    }
}

@Composable
@Preview
fun BackButtonPreview() {
//    TotanPayTheme {
//        BackButton(
//            title = "پرداخت قبض", modifier = Modifier
//                .fillMaxWidth()
//                .height(50.dp)
//        )
//    }
}

@Composable
fun OperatorButton(operator: Operator, modifier: Modifier,onClick: (Operator) -> Unit) {
    Row(
        modifier = modifier
            .padding(horizontal = 4.dp)
            .wrapContentWidth()
            .border(1.dp,operator. borderColor, shape = RoundedCornerShape(8.dp))
            .clickable { onClick(operator) }
    ) {
        Image(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .size(28.dp)
                .align(Alignment.CenterVertically),
            painter = painterResource(id = operator.imaged),
            contentDescription = "back_button"
        )
        Text(
            modifier = Modifier
                .padding(end = 8.dp)
                .align(Alignment.CenterVertically), text =operator. persianName, color = White
        )
    }
}

@Composable
@Preview
fun OperatorButtonPreview() {
    TotanPayTheme {
        //val operator = Operator("ایرانسل", R.drawable.ic_irancel, TurquoiseBlue,"11")
//        OperatorButton(
//            title = operator.title,
//            imageId = operator.imaged,
//            modifier = Modifier.height(40.dp), borderColor = operator.borderColor
//        )
    }
}


@Composable
fun PriceButton(
    selected: Boolean = false,
    title: String,
    modifier: Modifier,
    onSelected: (String) -> Unit
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(
                1.dp, shape = RoundedCornerShape(12.dp), color =
                if (selected) Pink else White100
            )
            .clickable { onSelected(title) }
            .background(Black100)
            .padding(horizontal = 5.dp)
            .padding(top = 2.dp, bottom = 2.dp)
    ) {
        Image(
            modifier = Modifier
                .padding(start = 10.dp)
                .size(28.dp)
                .align(Alignment.CenterVertically),
            painter = painterResource(id = if (selected) R.drawable.ic_selected_amount else R.drawable.ic_unselected_amount),
            contentDescription = "back_button"
        )
        Text(
            text = title, color = White, modifier = Modifier
                .padding(start = 10.dp)
                .align(Alignment.CenterVertically)
        )
    }
}

@Composable
@Preview
fun PriceButtonPreview() {
    TotanPayTheme {
        PriceButton(
            title = "ایرانسل", modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {}
    }
}

@Composable
fun MenuItem(modifier: Modifier, backgroundImageId: Int, iconId: Int, title: String) {
    Box(
        modifier = modifier
            .height(170.dp)
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = backgroundImageId),
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Column(
            Modifier
                .padding(top = 20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = iconId),
                contentDescription = "",
                modifier = Modifier
                    .padding(top = 22.dp)

                    .size(32.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = title,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .align(Alignment.CenterHorizontally),
                color = White100,
                style = MaterialTheme.typography.bodyLarge

            )

        }
    }
}

@Composable
@Preview
fun MenuItemPreview() {
//    TotanPayTheme {
//        MenuItem( Modifier
//            .fillMaxWidth(), backgroundImageId = R.drawable.balance_container,
//            iconId =  R.drawable.ic_bill_pay,
//            title =  "مانده حساب"
//        )
//    }
}

@Composable
fun SquareRoundedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(30.dp)
            .clickable { onClick() }
            .clip(RoundedCornerShape(8.dp))
            .background(Black500),
    ) {
        Image(
            modifier = Modifier
                .size(14.dp)
                .align(Alignment.Center),
            painter = painterResource(id = R.drawable.ic_arrow_to_left),
            contentDescription = ""
        )
    }
}

@Composable
@Preview
fun SquareRoundedButtonPreview() {
    TotanPayTheme {
        SquareRoundedButton(onClick = {}, modifier = Modifier)
    }
}

@Composable
fun DateRange(
    modifier: Modifier,
    title: String,
    value: String,
     onClick: () -> Unit
) {

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
                .clickable { onClick() }
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
                    // val trailer = createRefFor("trailer")
                    val textfield = createRefFor("textfield")
//                    constrain(trailer) {
//                        top.linkTo(parent.top)
//                        bottom.linkTo(parent.bottom)
//                        end.linkTo(parent.end)
//                    }
                    constrain(textfield) {
                        top.linkTo(parent.top)
                        //   bottom.linkTo(trailer.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(1f)
            ) {
                Text(
                    text = value,
                    modifier = Modifier.align(Alignment.Center)
                        .fillMaxWidth()
                        .layoutId("textfield")
                        .wrapContentHeight()
                        .padding(horizontal = 8.dp), color = White, textAlign = TextAlign.End
                )
            }
        }


    }
}

@Composable
@Preview
fun DateRangePreview() {
    TotanPayTheme {
        DateRange(onClick = {}, modifier = Modifier.fillMaxWidth(), title = "kkk", value = "99999999999")
    }
}