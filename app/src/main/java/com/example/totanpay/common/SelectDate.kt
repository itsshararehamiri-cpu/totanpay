package com.example.totanpay.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.totanpay.R
import com.example.totanpay.ui.theme.White100
import com.example.totanpay.ui.theme.White200

@Composable
fun SelectDate(modifier: Modifier,
               title: String,
               value: String,
               onClick: () -> Unit){
    Column(
        modifier = modifier.wrapContentHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            modifier = Modifier,
            text = title,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyLarge
        )
        Box(
            modifier = Modifier
                .clickable { onClick() }
                .padding(top = 3.dp)
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            White100.copy(alpha = 0.8f),
                            White200.copy(alpha = 1f)
                        )
                    ),
                    shape = RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface,RoundedCornerShape(8.dp))

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    //.fillMaxHeight()
                    .align(Alignment.Center).padding(top=7.dp, bottom = 7.dp).padding(horizontal = 4.dp)
                , verticalAlignment = Alignment.CenterVertically
            ) {
                Image(painter = painterResource(id = R.drawable.ic_calendar), contentDescription = title, modifier = Modifier.padding(start = 4.dp).size(20.dp)
                ,colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground))
                Text(
                    text = value,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.End
                )
            }
        }


    }
}