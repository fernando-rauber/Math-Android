package uk.fernando.math.component.game

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk.fernando.math.ext.operatorIcon

@Composable
fun MyQuestion(value1: String, value2: String, operator: Int, result: String, resultColor: Color? = null, size: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = value1,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            fontSize = size.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        Icon(
            modifier = Modifier
                .padding(start = 3.dp, end = 1.dp, top = 2.dp)
                .size((size * if(operator == 6) 1.2 else 0.7).dp),
            painter = painterResource(operator.operatorIcon()),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = "$value2 = ",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            fontSize = size.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = result,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            fontSize = size.sp,
            color = resultColor ?: MaterialTheme.colorScheme.onBackground
        )
    }
}