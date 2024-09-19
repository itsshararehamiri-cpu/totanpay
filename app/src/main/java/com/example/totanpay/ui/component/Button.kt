package com.example.totanpay.ui.component


import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.totanpay.R
import com.example.totanpay.ui.theme.TotanPayTheme


@Composable
fun CancelButton(
    modifier: Modifier = Modifier,  onClick: () -> Unit = {}
) {

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .border(1.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(16.dp))
            .padding(horizontal = 24.dp)

    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = stringResource(R.string.cancel),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

//
//@Composable
//@Preview
//fun CancelButtonPreview() {
//    TotanPayTheme {
//        CancelButton(
//            title = "تایید", modifier = Modifier
//                .fillMaxWidth()
//                .height(50.dp)
//        ) {
//
//        }
//    }
//}


//@Composable
//fun CancelButton(
//    modifier: Modifier = Modifier,  onClick: () -> Unit = {}
//) {
//
//    Button(
//        onClick = onClick,
//        modifier = modifier
//        ,
//        colors = ButtonDefaults.buttonColors(
//            containerColor = White, contentColor = Color.Black
//        ), shape = RoundedCornerShape(4.dp)
//    ) {
//        Text(text = stringResource(R.string.cancel), color = Color.Black, style = MaterialTheme.typography.titleSmall)
//    }
//}
//





@Composable
@Preview
fun MenuItemPreview() {

}





@Composable
@Preview
fun DateRangePreview() {
    TotanPayTheme {
    }
}


