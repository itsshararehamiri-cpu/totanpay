import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.commandiron.wheel_picker_compose.WheelTimePicker
import com.commandiron.wheel_picker_compose.core.TimeFormat
import com.commandiron.wheel_picker_compose.core.WheelPickerDefaults
import com.example.totanpay.common.mainButtonModifier
import com.example.totanpay.ui.component.button.MainButton
import com.example.totanpay.ui.theme.TotanPayTheme
import java.time.LocalTime

@Composable
fun SelectWheelTimePicker(isSmall:Boolean,onTimeSelected: (String) -> Unit) {
    val context = LocalContext.current
    var time by remember {
        mutableStateOf(LocalTime.MIN)
    }
    var rangeError by remember {
        mutableStateOf(false)
    }

    if (rangeError) {
        Toast.makeText(context, "بازه انتخابی درست نمی باشد", Toast.LENGTH_SHORT).show()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
            ConstraintLayout(
                ConstraintSet {
                    val timePicker = createRefFor("timePicker")
                    val confirm = createRefFor("confirm")
                    constrain(timePicker) {
                        top.linkTo(parent.top, 40.dp)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        bottom.linkTo(confirm.top)
                        height= androidx.constraintlayout.compose.Dimension.fillToConstraints

                    }
                    constrain(confirm) {
                        bottom.linkTo(parent.bottom, 20.dp)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                    }
                }, modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                WheelTimePicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .layoutId("timePicker")
                        .align(Alignment.TopCenter),
                    startTime = LocalTime.now(),
                    timeFormat = TimeFormat.HOUR_24,
                    size = DpSize(400.dp, 100.dp),
                    rowCount = 3,
                    textStyle = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
                    textColor = MaterialTheme.colorScheme.onSurface,
                    selectorProperties = WheelPickerDefaults.selectorProperties(
                        color = MaterialTheme.colorScheme.surface,
                        enabled = false,
                        shape = RoundedCornerShape(0.dp),
                    )
                ) {
                    time = it
                }
                MainButton(
                     modifier =Modifier .mainButtonModifier(isSmall = isSmall)
                        .layoutId("confirm")
                ) {
                    onTimeSelected(time.toString())
                }
            }
        }
    }
}

@Composable
@Preview
fun SelectWheelTimePickerPreview() {
    TotanPayTheme {
         SelectWheelTimePicker(isSmall = false, onTimeSelected = {})
    }
}