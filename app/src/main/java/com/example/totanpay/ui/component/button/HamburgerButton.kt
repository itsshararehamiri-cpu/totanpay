package com.example.totanpay.ui.component.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.totanpay.R
import com.example.totanpay.common.isSmall
import com.example.totanpay.ui.theme.TotanPayTheme

@Composable
fun HamburgerButton(modifier: Modifier, onClick: () -> Unit) {
    val context= LocalContext.current
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ), colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
        ), shape = RoundedCornerShape(size = 5.dp),
        modifier = modifier.clickable {
            onClick()
        }
    ) {
        Row(
            modifier = Modifier.padding( all = if(isSmall(context)) 3.dp else 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier.size(if(isSmall(context)) 21.dp else 26.dp),
                painter = painterResource(id = R.drawable.ic_hamberger),
                contentDescription = "",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surface)
            )
        }
    }

}
@Composable
@Preview
fun HamburgerButtonPreview() {
    TotanPayTheme {
        HamburgerButton(
            modifier = Modifier
                .padding(top = 16.dp)
                .layoutId("hamburgerButton")
        ) {

        }
    }
}