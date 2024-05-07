package pl.wsei.pam.lab06

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ListItem(item: TodoTask, modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(120.dp)
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            Column {
                Text(text = "Title", style = MaterialTheme.typography.titleSmall)
                Text(text = item.title, style = MaterialTheme.typography.titleLarge)
            }
            Spacer(modifier = Modifier.weight(1f))
            Column {
                Text(
                    text = "Deadline",
                    textAlign = TextAlign.Right,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = item.deadline.toString(),
                    textAlign = TextAlign.Right,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
        Row(modifier = Modifier.padding(8.dp)) {
            Column {
                Text(text = "Priority")
                Text(
                    text = item.priority.toString(),
                    textAlign = TextAlign.Right,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.weight(1f))
            if (item.isDone) {
                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "")
            } else {
                Icon(imageVector = Icons.Default.Clear, contentDescription = "")
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}