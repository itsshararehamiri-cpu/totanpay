package com.example.totanpay.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.totanpay.ui.theme.Green50

@Composable
fun TransactionTypeCheckbox(
    title: String, modifier: Modifier, isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(1.dp)
    ) {
        Checkbox(modifier = Modifier.size(40.dp),
            colors = CheckboxDefaults.colors(checkedColor = Green50,
                uncheckedColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                disabledUncheckedColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                ),
            checked = isChecked,
            onCheckedChange = {
                onCheckedChange(!isChecked) }
        )
        Text(
            text = title, style = MaterialTheme.typography.labelSmall.copy(fontSize = 16.sp),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSimpleCheckbox() {
    Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        TransactionTypeCheckbox("خرید", modifier
        = Modifier.wrapContentSize(), isChecked = false){

        }

    }
}
