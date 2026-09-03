package com.example.totanpay

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.totanpay.ui.theme.DarkBlue
import com.example.totanpay.ui.theme.DeepBlue

@Composable
fun SettlementBanner(
    isSyncing: Boolean,
    onActionClicked: () -> Unit
) {
//    val warningContainer = Color(0xFFFFF3CD)
//    val warningContent = Color(0xFF7A4E00)
//    val warningBorder = Color(0xFFFFD76A)
//    val actionColor = Color(0xFFE09F00)
    val warningContainer = Color(0xFFFFF6E0)
    val warningContent = DarkBlue
    val warningBorder = Color(0xFFFFD36B)
    val actionColor = DeepBlue

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = warningContainer),
        border = BorderStroke(1.dp, warningBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
//            Box(
//                modifier = Modifier
//                    .size(46.dp)
//                    .background(
//                        color = warningContent.copy(alpha = 0.12f),
//                        shape = RoundedCornerShape(14.dp)
//                    ),
//                contentAlignment = Alignment.Center
//            ) {
//                Image(
//                    painterResource(R.drawable.ic_send),
//                    contentDescription = null,
//                )
//            }

            Spacer(modifier = Modifier.width(4.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.transaction_settlement_reversal_has_not_been_completed),
                    style = MaterialTheme.typography.titleMedium,
                    color = warningContent,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stringResource(R.string.plz_send_it_to_complete_transaction_process),
                    style = MaterialTheme.typography.bodySmall,
                    color = warningContent.copy(alpha = 0.92f)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            if (isSyncing) {
                CircularProgressIndicator(
                    modifier = Modifier.size(26.dp),
                    strokeWidth = 3.dp,
                    color = warningContent
                )
            } else {
                Button(
                    onClick = onActionClicked,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = actionColor,
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = "ارسال",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }
    }
}
