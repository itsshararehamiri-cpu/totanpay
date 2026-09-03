package com.example.totanpay.ui.component.report

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.totanpay.R
import com.example.totanpay.common.SelectDateModifier
import com.example.totanpay.ui.theme.TotanPayTheme

@Composable
fun SelectDateTime(modifier: Modifier,
               title: String,
               value: String,
                   isTime:Boolean,
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
            modifier = Modifier .padding(top = 3.dp).clip(RoundedCornerShape(16.dp))
                .clickable { onClick() }
                .fillMaxWidth() .height(40.dp)
                .border(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.8f),
                            MaterialTheme.colorScheme.outline.copy(alpha = 1f)
                        )
                    ),
                    shape = RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface,RoundedCornerShape(16.dp))

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center).padding(horizontal = 4.dp)
                , verticalAlignment = Alignment.CenterVertically
            ) {
                Image(painter = painterResource(id = if(isTime)R.drawable.ic_clock else R.drawable.ic_calendar),
                    contentDescription = title, modifier = Modifier.padding(start = 4.dp).size(20.dp)
                ,colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground))
                Text(
                    text = value,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,style = MaterialTheme.typography.labelSmall.copy(fontSize = 15.sp),
                )
            }
        }


    }
}
