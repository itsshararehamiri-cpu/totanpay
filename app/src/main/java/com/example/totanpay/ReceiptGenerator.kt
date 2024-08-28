package com.example.totanpay

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun ReceiptUi(content: @Composable () -> Unit, onGenerateReceipt: (Bitmap) -> Unit) {
    val context = LocalContext.current
    var receiptView by remember {
        mutableStateOf(ReceiptView(context = context, content = content))
    }
    var isInit by remember {
        mutableStateOf(false)
    }
    AndroidView(
        modifier = Modifier.wrapContentHeight(unbounded = true),
        factory = {
            ReceiptView(context = it, content = content).apply {
                post {
                    receiptView = this
                    isInit = true
                }
            }
        })

    if (isInit) {
        onGenerateReceipt(generateBitmap(receiptView))
    }
}
private fun generateBitmap(view: View): Bitmap {
    val bitmap = Bitmap.createBitmap(
        view.width,
        view.height,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    view.layout(
        view.left,
        view.top,
        view.right,
        view.bottom
    )
    view.draw(canvas)
    return bitmap
}
@SuppressLint("ViewConstructor")
class ReceiptView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    private val content: @Composable () -> Unit,
    showView: Boolean = false
) : AbstractComposeView(context, attrs) {
    init {
        layoutDirection = LAYOUT_DIRECTION_RTL
        if (!showView) {
            visibility = INVISIBLE
        }
    }

    @Composable
    override fun Content() {
        content()
    }
}