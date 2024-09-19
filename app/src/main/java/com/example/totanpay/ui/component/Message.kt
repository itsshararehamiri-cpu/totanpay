package com.example.totanpay.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.totanpay.ui.theme.Red100
import com.example.totanpay.ui.theme.fontFamily
import kotlinx.coroutines.delay

@Composable
fun CustomToast(
    message: String, modifier: Modifier,
    durationMillis: Long=0 ,
    backgroundColor: Color =Red100,
    textColor: Color =  Color.White,
    onDismiss: () -> Unit
) {
    var isVisible by remember { mutableStateOf(true) }
    LaunchedEffect(key1 = isVisible) {
        if(durationMillis!=0L)
        if (isVisible) {
            delay(durationMillis)
            isVisible = false
            onDismiss()
        }
    }
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + slideInVertically { it / 2 },
        exit = fadeOut() + slideOutVertically { it / 2 },
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth().clip(RoundedCornerShape(16.dp))
                .padding(16.dp).clip(RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.BottomCenter
        ) {
            Surface(
                modifier = Modifier
                    .padding(8.dp)
                    .background(backgroundColor, RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = MaterialTheme.shapes.small,
                color = backgroundColor,
                contentColor = textColor
            ) {
                Text(
                    text = message, modifier = Modifier.padding(horizontal = 16.dp).padding(top = 5.dp, bottom = 5.dp),
                    style = TextStyle(color = textColor, fontSize = 18.sp, fontFamily = fontFamily)
                )
            }
        }
    }
}
@Composable
fun ShowToast(modifier: Modifier, message:String, onDismiss:()->Unit){
    CustomToast(modifier=modifier,
        message = message,
        durationMillis = 3000L,
        onDismiss = { onDismiss() }
    )
}
@Composable
fun ShowErrorMessage(modifier: Modifier, message:String, onDismiss:()->Unit){
    CustomToast(modifier=modifier,
        message = message,
        onDismiss = { onDismiss() }
    )
}


